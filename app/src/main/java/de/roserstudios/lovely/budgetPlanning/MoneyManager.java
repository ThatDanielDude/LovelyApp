package de.roserstudios.lovely.budgetPlanning;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import de.roserstudios.lovely.db.CashSQLiteOpenHelper;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class MoneyManager {

    private final long DAY_IN_MS = 1000 * 60 * 60 * 24;
    private int daysBack = 10;

    public List<Entry> getCurrentTimeFrameEntries() {
        return currentTimeFrameEntries;
    }
    private List<Entry> currentTimeFrameEntries;

    private CashSQLiteOpenHelper helper;
    private static MoneyManager _instance;

    public MoneyManager(Context context) {
        //Create the database
        helper = CashSQLiteOpenHelper.get_instance(context);
        //Fill database with test entries
        generateTestData();
        currentTimeFrameEntries = new Vector<>();
    }

    public void updateData(){
        long millisFromToday = Calendar.getInstance().getTimeInMillis() - (daysBack * DAY_IN_MS);
        currentTimeFrameEntries = helper.getEntriesBetween(millisFromToday , Calendar.getInstance().getTimeInMillis());
    }

    public static MoneyManager get_instance(Context context){
        if (_instance == null)
            _instance = new MoneyManager(context);

        return _instance;
    }

    public void setDaysBack(int daysBack){
        this.daysBack = daysBack;
    }

    public int getDaysBack() { return this.daysBack; }

    public double currentAccountBalance(){
        double amount = 0;
        for (Entry e: helper.getEntriesBetween(0,Calendar.getInstance().getTimeInMillis())
             ) {
            amount+=e.getAmount();
        }
        return amount;
    }

    private void generateTestData(){

        if(helper.countTableEntries() > 0) return;
        long l  = Calendar.getInstance().getTimeInMillis();

        helper.insertNewEntry(EntryCategory.FOOD, l, "DB Weekly shopping", -39.47);
        helper.insertNewEntry(EntryCategory.RENT, l - (12 * DAY_IN_MS), "DB Apartment rent", -570);
        helper.insertNewEntry(EntryCategory.CELLPHONE, l - (6 * DAY_IN_MS), "DVB Daniel's cellphon", -24.99);
        helper.insertNewEntry(EntryCategory.CELLPHONE, l - (3 * DAY_IN_MS), "DB Melda's pre-paid cellphone top-up", -15);
        helper.insertNewEntry(EntryCategory.INTERNET_AND_PHONE, l - (1 * DAY_IN_MS), "DB Telecom bill", -29.99);
        helper.insertNewEntry(EntryCategory.MONTHLY_EXPENSE, l - (4 * DAY_IN_MS), "DB This is a test to see if " +
                "longer description texts look as good as we want them to or if the displayed " +
                "fashion is not to our liking", -39.47);

        if(helper.countTableReoccurringEntries() > 0) return;

        helper.insertNewReoccuringEntry(EntryCategory.SALARY, l, "Monthly salary Daniel", 1780, 3);
        helper.insertNewReoccuringEntry(EntryCategory.SALARY, l, "Monthly salary Melda", 880, 1);
        helper.insertNewEntry(EntryCategory.EXTRA, l, "Birthday present ", 25);

    }

    public String getFormattedDate(String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(daysBack * DAY_IN_MS);
        return formatter.format(calendar.getTime());
    }

}
