package de.roserstudios.lovely.budgetPlanning;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import de.roserstudios.lovely.db.CashSQLiteOpenHelper;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class MoneyManager {

    public List<Expense> getCurrentMonthExpenses() {
        return currentMonthExpenses;
    }

    private List<Income> currentMonthIncome;
    private List<Expense> currentMonthExpenses;
    private CashSQLiteOpenHelper helper;
    private long nowInMs = Calendar.getInstance().getTimeInMillis();
    private final long DAY_IN_MS = 1000 * 60 * 60 * 24;
    private int daysFrom = 10;
    private long tenDaysAgoInMs =  nowInMs - (daysFrom * DAY_IN_MS);

    private static MoneyManager _instance;

    public MoneyManager(Context context) {
        helper = CashSQLiteOpenHelper.get_instance(context);
        generateTestData();
        currentMonthExpenses = helper.getExpensesBetween(nowInMs, tenDaysAgoInMs);
        currentMonthIncome = helper.getIncomeBetween(nowInMs, tenDaysAgoInMs);
    }

    public static MoneyManager get_instance(Context context){
        if (_instance == null)
            _instance = new MoneyManager(context);

        return _instance;
    }

    private void generateTestData(){
        helper.insertNewExpense(ExpenseCategory.FOOD, Calendar.getInstance().getTimeInMillis(), "DB Weekly shopping", 39.47);
        helper.insertNewExpense(ExpenseCategory.RENT, nowInMs - (12 * DAY_IN_MS), "DB Apartment rent", 570);
        helper.insertNewExpense(ExpenseCategory.CELLPHONE, nowInMs - (6 * DAY_IN_MS), "DVB Daniel's cellphon", 24.99);
        helper.insertNewExpense(ExpenseCategory.CELLPHONE, nowInMs - (3 * DAY_IN_MS), "DB Melda's pre-paid cellphone top-up", 15);
        helper.insertNewExpense(ExpenseCategory.INTERNET_AND_PHONE, nowInMs - (1 * DAY_IN_MS), "DB Telecom bill", 29.99);
        helper.insertNewExpense(ExpenseCategory.MONTHLY_EXPENSE, nowInMs - (4 * DAY_IN_MS), "DB This is a test to see if " +
                "longer description texts look as good as we want them to or if the dispasdasdlayed " +
                "fashion is not to our liking", 39.47);
    }
}
