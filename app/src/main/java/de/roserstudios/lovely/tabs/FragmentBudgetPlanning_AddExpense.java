package de.roserstudios.lovely.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.roserstudios.lovely.R;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class FragmentBudgetPlanning_AddExpense extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget_planning_add_expense, container, false);
    }
}
