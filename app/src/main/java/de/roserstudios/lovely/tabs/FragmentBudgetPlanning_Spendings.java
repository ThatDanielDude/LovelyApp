package de.roserstudios.lovely.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import de.roserstudios.lovely.R;
import de.roserstudios.lovely.budgetPlanning.Entry;
import de.roserstudios.lovely.budgetPlanning.MoneyManager;
import de.roserstudios.lovely.budgetPlanning.SpendingListAdapter;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class FragmentBudgetPlanning_Spendings extends Fragment{

    private ListView lv_spendings;
    private SpendingListAdapter adapter;
    private List<Entry> expensesList;
    private MoneyManager moneyManager;
    private View v_currentBalanceRootView;
    private TextView tv_currentBalanceTitle;
    private TextView tv_currentBalanceDescription;
    private TextView tv_currentBalanceAmount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_budget_planning_spendings, container, false);
        v_currentBalanceRootView = (View)rootView.findViewById(R.id.fragment_budget_planning_spendings_itm_currentBalance);
        tv_currentBalanceTitle = v_currentBalanceRootView.findViewById(R.id.f_spendings_list_item_tv_categorie);
        tv_currentBalanceDescription = v_currentBalanceRootView.findViewById(R.id.f_spendings_list_item_tv_description);
        tv_currentBalanceAmount = v_currentBalanceRootView.findViewById(R.id.f_spendings_list_item_tv_amount);

        moneyManager = MoneyManager.get_instance(rootView.getContext());
        moneyManager.updateData();
        expensesList = moneyManager.getCurrentTimeFrameEntries();
        adapter = new SpendingListAdapter(rootView.getContext(), expensesList);
        lv_spendings = (ListView)rootView.findViewById(R.id.fragment_budget_planning_spendings_lv_spendings);

        lv_spendings.setAdapter(adapter);

        initCurrentBalance();
        return rootView;
    }

    private void initCurrentBalance(){
        tv_currentBalanceTitle.setText("Current Balance");
        tv_currentBalanceDescription.setText("From the " + moneyManager.getFormattedDate("dd.MM.yyyy") + " until today");
        tv_currentBalanceAmount.setText(String.format("%.2f€", moneyManager.currentAccountBalance()));
    }
}
