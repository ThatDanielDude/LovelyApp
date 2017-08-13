package de.roserstudios.lovely.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Vector;

import de.roserstudios.lovely.R;
import de.roserstudios.lovely.budgetPlanning.Expense;
import de.roserstudios.lovely.budgetPlanning.ExpenseCategory;
import de.roserstudios.lovely.budgetPlanning.MoneyManager;
import de.roserstudios.lovely.budgetPlanning.SpendingListAdapter;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class FragmentBudgetPlanning_Spendings extends Fragment{

    private ListView lv_spendings;
    private SpendingListAdapter adapter;
    private List<Expense> expensesList;
    private MoneyManager moneyManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_budget_planning_spendings, container, false);
        moneyManager = MoneyManager.get_instance(rootView.getContext());
        expensesList = moneyManager.getCurrentMonthExpenses();

        expensesList.add(new Expense(ExpenseCategory.FOOD, "Weekly shopping", 39.47));
        expensesList.add(new Expense(ExpenseCategory.RENT, "Apartment rent", 570));
        expensesList.add(new Expense(ExpenseCategory.CELLPHONE, "Daniel's cellphon", 24.99));
        expensesList.add(new Expense(ExpenseCategory.CELLPHONE, "Melda's pre-paid cellphone top-up", 15));
        expensesList.add(new Expense(ExpenseCategory.INTERNET_AND_PHONE, "Telecom bill", 29.99));
        expensesList.add(new Expense(ExpenseCategory.MONTHLY_EXPENSE, "Miguel and Sam debt midigation", 100));
        expensesList.add(new Expense(ExpenseCategory.MONTHLY_EXPENSE, "b.i.b debt midigtion", 220));
        expensesList.add(new Expense(ExpenseCategory.MONTHLY_EXPENSE, "This is a test to see if " +
                "longer description texts look as good as we want them to or if the displayed " +
                "fashion is not to our liking", 39.47));
        expensesList.add(new Expense(ExpenseCategory.MONTHLY_EXPENSE, "Payment for the beautiful bedroom", 59.67));


        lv_spendings = (ListView)rootView.findViewById(R.id.fragment_budget_planning_spendings_lv_spendings);
        adapter = new SpendingListAdapter(rootView.getContext(), expensesList);
        lv_spendings.setAdapter(adapter);

        return rootView;
    }
}
