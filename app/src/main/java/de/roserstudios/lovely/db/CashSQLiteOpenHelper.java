package de.roserstudios.lovely.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Vector;

import de.roserstudios.lovely.budgetPlanning.Entry;
import de.roserstudios.lovely.budgetPlanning.EntryCategory;
import de.roserstudios.lovely.budgetPlanning.ReoccurringEntry;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class CashSQLiteOpenHelper extends SQLiteOpenHelper {

    //db info
    private static final String DATABASE_NAME = "lovelydatabase.sqlite";
    private static final int DATABASE_VERSION = 1;

    //Table names
    private static final String TABLE_ENTRIES = "Entries";
    private static final String TABLE_REOCCURRING_ENTRIES = "Reoccurring";

    //Cols entries
    private static final String COL_ENTRIES_ID = "id";
    private static final String COL_ENTRIES_CATEGORY = "category";
    private static final String COL_ENTRIES_TIME = "time";
    private static final String COL_ENTRIES_DESCRIPTION = "description";
    private static final String COL_ENTRIES_AMOUNT = "amount";
    private static final String[] COLS_ENTRIES = { COL_ENTRIES_ID,
            COL_ENTRIES_CATEGORY,
            COL_ENTRIES_TIME,
            COL_ENTRIES_DESCRIPTION,
            COL_ENTRIES_AMOUNT};

    //Cols income
    private static final String COL_REOCCURRING_ENTRIES_ID = "id";
    private static final String COL_REOCCURRING_ENTRIES_TIME = "time";
    private static final String COL_REOCCURRING_ENTRIES_CATEGORY = "category";
    private static final String COL_REOCCURRING_ENTRIES_DAY_OF_MONTH = "dayOfMonth";
    private static final String COL_REOCCURRING_ENTRIES_DESCRIPTION = "description";
    private static final String COL_REOCCURRING_ENTRIES_AMOUNT = "amount";
    private static final String[] COLS_REOCCURRING_ENTRIES = {COL_REOCCURRING_ENTRIES_ID,
            COL_REOCCURRING_ENTRIES_TIME,
            COL_REOCCURRING_ENTRIES_CATEGORY,
            COL_REOCCURRING_ENTRIES_DAY_OF_MONTH,
            COL_REOCCURRING_ENTRIES_DESCRIPTION,
            COL_REOCCURRING_ENTRIES_AMOUNT};

    private static CashSQLiteOpenHelper _instance;

    private CashSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_ENTRIES_TABLE = "CREATE TABLE " + TABLE_ENTRIES + "(" +
                COL_ENTRIES_ID  + " INTEGER PRIMARY KEY," +
                COL_ENTRIES_CATEGORY + " TEXT," +
                COL_ENTRIES_TIME + " INTEGER," +
                COL_ENTRIES_DESCRIPTION + " TEXT," +
                COL_ENTRIES_AMOUNT + " INTEGER)";

        String CREATE_REOCCURRING_ENTRIES_TABLE = "CREATE TABLE " + TABLE_REOCCURRING_ENTRIES + "(" +
                COL_REOCCURRING_ENTRIES_ID  + " INTEGER PRIMARY KEY," +
                COL_REOCCURRING_ENTRIES_TIME + " INTEGER," +
                COL_REOCCURRING_ENTRIES_CATEGORY + " TEXT," +
                COL_REOCCURRING_ENTRIES_DAY_OF_MONTH + " INTEGER," +
                COL_REOCCURRING_ENTRIES_DESCRIPTION + " TEXT," +
                COL_REOCCURRING_ENTRIES_AMOUNT + " INTEGER)";

        sqLiteDatabase.execSQL(CREATE_ENTRIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_REOCCURRING_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static synchronized CashSQLiteOpenHelper get_instance(Context context){
        if(_instance ==  null)
            _instance = new CashSQLiteOpenHelper(context);
        return _instance;
    }

    public Entry insertNewEntry(EntryCategory category, long time, String description, double amount){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ENTRIES_CATEGORY, category.toString());
        values.put(COL_ENTRIES_TIME, time);
        values.put(COL_ENTRIES_DESCRIPTION, description);
        values.put(COL_ENTRIES_AMOUNT, amount);
        long rowID = db.insert(TABLE_ENTRIES, null, values);

        db.close();

        return new Entry(rowID, time, description, amount, category);
    }

    public ReoccurringEntry insertNewReoccuringEntry(EntryCategory category, long time, String description, double amount, int dayOfMonth){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_REOCCURRING_ENTRIES_TIME, time);
        values.put(COL_REOCCURRING_ENTRIES_CATEGORY, category.toString());
        values.put(COL_REOCCURRING_ENTRIES_DAY_OF_MONTH, dayOfMonth);
        values.put(COL_REOCCURRING_ENTRIES_DESCRIPTION, description);
        values.put(COL_REOCCURRING_ENTRIES_AMOUNT, amount);
        long rowID = db.insert(TABLE_REOCCURRING_ENTRIES, null, values);

        db.close();

        return new ReoccurringEntry(rowID, time, description, amount, category, dayOfMonth);
    }

    public boolean deleteEntry(long id, long time){
        return 1 == getWritableDatabase().delete(TABLE_ENTRIES, id + "=" + COL_ENTRIES_ID + " AND "
                + time + "=" + COL_ENTRIES_TIME, null);
    }
    public List<Entry> getEntriesBetween(long from, long to) {
        SQLiteDatabase db = getReadableDatabase();
        List<Entry> list = new Vector<>();

        Cursor c = db.query(TABLE_ENTRIES, COLS_ENTRIES,
                COL_ENTRIES_TIME + ">=" + from + " AND " + COL_ENTRIES_TIME + "<=" + to,
                null, null, null, COL_ENTRIES_TIME + " DESC");

        c.moveToFirst();
        while (!c.isAfterLast()) {
            list.add(convertToEntry(c));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }

    private Entry convertToEntry(Cursor c){
        long id = c.getLong(c.getColumnIndex(COL_ENTRIES_ID));
        EntryCategory category = EntryCategory.valueOf(c.getString(c.getColumnIndex(COL_ENTRIES_CATEGORY)));
        long time = c.getLong(c.getColumnIndex(COL_ENTRIES_TIME));
        String description = c.getString(c.getColumnIndex(COL_ENTRIES_DESCRIPTION));
        double amount = c.getDouble(c.getColumnIndex(COL_ENTRIES_AMOUNT));

        return new Entry(id, time, description, amount, category);
    }

    public List<Entry> allReoccuringEntries(){
        SQLiteDatabase db = getReadableDatabase();
        List<Entry> list = new Vector<>();
        Cursor c = db.query(TABLE_REOCCURRING_ENTRIES, COLS_REOCCURRING_ENTRIES,
                null, null, null, null, null);

        c.moveToFirst();
        while(!c.isAfterLast()){
            list.add(convertToReoccurringEntry(c));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }
    private Entry convertToReoccurringEntry(Cursor c){
        long id = c.getLong(c.getColumnIndex(COL_ENTRIES_ID));
        int dayOfMonth = c.getInt(c.getColumnIndex(COL_REOCCURRING_ENTRIES_DAY_OF_MONTH));
        EntryCategory category = EntryCategory.valueOf(c.getString(c.getColumnIndex(COL_ENTRIES_CATEGORY)));
        long time = c.getLong(c.getColumnIndex(COL_ENTRIES_TIME));
        String description = c.getString(c.getColumnIndex(COL_ENTRIES_DESCRIPTION));
        double amount = c.getDouble(c.getColumnIndex(COL_ENTRIES_AMOUNT));

        return new ReoccurringEntry(id, time, description, amount, category, dayOfMonth);
    }

    public int countTableEntries(){
        SQLiteDatabase db = getReadableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_ENTRIES;
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        return c.getInt(0);
    }

    public int countTableReoccurringEntries(){
        SQLiteDatabase db = getReadableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_REOCCURRING_ENTRIES;
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        return c.getInt(0);
    }
}
