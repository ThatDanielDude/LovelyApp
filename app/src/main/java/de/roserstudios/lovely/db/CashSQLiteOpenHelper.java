package de.roserstudios.lovely.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Vector;

import de.roserstudios.lovely.budgetPlanning.Expense;
import de.roserstudios.lovely.budgetPlanning.ExpenseCategory;
import de.roserstudios.lovely.budgetPlanning.Income;
import de.roserstudios.lovely.budgetPlanning.IncomeCategory;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class CashSQLiteOpenHelper extends SQLiteOpenHelper {

    //db info
    private static final String DATABASE_NAME = "S";
    private static final int DATABASE_VERSION = 1;

    //Table names
    private static final String TABLE_SPENDINGS = "Spendings";
    private static final String TABLE_INCOME = "Income";

    //Cols spendings
    private static final String COL_SPENDINGS_ID = "id";
    private static final String COL_SPENDINGS_CATEGORY = "category";
    private static final String COL_SPENDINGS_DATE = "time";
    private static final String COL_SPENDING_DESCRIPTION = "description";
    private static final String COL_SPENDING_AMOUNT = "amount";
    private static final String[] COLS_SPENDING = { COL_SPENDINGS_ID,
            COL_SPENDINGS_CATEGORY,
            COL_SPENDINGS_DATE,
            COL_SPENDING_DESCRIPTION,
            COL_SPENDING_AMOUNT};

    //Cols income
    private static final String COL_INCOME_ID = "id";
    private static final String COL_INCOME_CATEGORY = "category";
    private static final String COL_INCOME_DATE = "time";
    private static final String COL_INCOME_AMOUNT = "amount";
    private static final String[] COLS_INCOME = {COL_INCOME_ID,
            COL_INCOME_CATEGORY,
            COL_INCOME_DATE,
            COL_INCOME_AMOUNT};

    private static CashSQLiteOpenHelper _instance;


    private CashSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_INCOME_TABLE = "CREATE TABLE " + TABLE_INCOME + "(" +
                COL_INCOME_ID  + " INTEGER PRIMARY KEY," +
                COL_INCOME_CATEGORY + " TEXT," +
                COL_INCOME_DATE + " INTEGER," +
                COL_INCOME_AMOUNT + " INTEGER)";

        String CREATE_SPENDINGS_TABLE = "CREATE TABLE " + TABLE_SPENDINGS + "(" +
                COL_SPENDINGS_ID  + " INTEGER PRIMARY KEY," +
                COL_SPENDINGS_CATEGORY + " TEXT," +
                COL_SPENDINGS_DATE + " INTEGER," +
                COL_SPENDING_DESCRIPTION + " TEXT," +
                COL_INCOME_AMOUNT + " INTEGER)";

        sqLiteDatabase.execSQL(CREATE_INCOME_TABLE);
        sqLiteDatabase.execSQL(CREATE_SPENDINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static synchronized CashSQLiteOpenHelper get_instance(Context context){
        if(_instance ==  null)
            _instance = new CashSQLiteOpenHelper(context);
        return _instance;
    }

    public Income insertNewIncome(IncomeCategory category, long time, double amount){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_INCOME_CATEGORY, category.toString());
        values.put(COL_INCOME_DATE, time);
        values.put(COL_INCOME_AMOUNT, amount);

        long rowID = db.insert(TABLE_INCOME, null, values);

        db.close();
        return new Income(rowID, category, amount, time);
    }

    public Expense insertNewExpense(ExpenseCategory category, long time, String description, double amount){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_SPENDINGS_CATEGORY, category.toString());
        values.put(COL_SPENDINGS_DATE, time);
        values.put(COL_SPENDING_DESCRIPTION, description);
        values.put(COL_SPENDING_AMOUNT, amount);

        long rowID = db.insert(TABLE_SPENDINGS, null, values);

        db.close();
        return new Expense(rowID, category, time, description, amount);
    }

    public List<Income> getIncomeBetween(long from, long to) {
        SQLiteDatabase db = getReadableDatabase();
        List<Income> list = new Vector<Income>();

        Cursor c = db.query(TABLE_INCOME, COLS_INCOME,
                COL_INCOME_DATE + ">=" + from + " AND " + COL_INCOME_DATE + "<=" + to,
                null, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            list.add(convertToIncome(c));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }

    private Income convertToIncome(Cursor c){
        long id = c.getLong(c.getColumnIndex(COL_INCOME_ID));
        IncomeCategory category = IncomeCategory.valueOf(c.getString(c.getColumnIndex(COL_INCOME_CATEGORY)));
        long time = c.getLong(c.getColumnIndex(COL_INCOME_DATE));
        double amount = c.getDouble(c.getColumnIndex(COL_INCOME_AMOUNT));

        return new Income(id, category, amount, time);
    }

    public List<Expense> getExpensesBetween(long from, long to){
        List<Expense> list = new Vector<Expense>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_SPENDINGS, COLS_SPENDING,
                COL_SPENDINGS_DATE + ">=" + from + " AND " + COL_SPENDINGS_DATE + "<=" + to,
                null, null, null, null);

        c.moveToFirst();
        while(!c.isAfterLast()){
            list.add(convertToExpense(c));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }

    private Expense convertToExpense(Cursor c){

        long id = c.getLong(c.getColumnIndex(COL_SPENDINGS_ID));
        ExpenseCategory category = ExpenseCategory.valueOf(c.getString(c.getColumnIndex(COL_SPENDINGS_CATEGORY)));
        long time = c.getLong(c.getColumnIndex(COL_SPENDINGS_DATE));
        String description = c.getString(c.getColumnIndex(COL_SPENDING_DESCRIPTION));
        double amount = c.getDouble(c.getColumnIndex(COL_SPENDING_AMOUNT));

        return new Expense(id, category, time, description, amount);
    }
}
