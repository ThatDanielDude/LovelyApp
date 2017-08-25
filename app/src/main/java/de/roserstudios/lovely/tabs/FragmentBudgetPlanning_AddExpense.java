package de.roserstudios.lovely.tabs;

import android.renderscript.Double2;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import de.roserstudios.lovely.R;
import de.roserstudios.lovely.budgetPlanning.Entry;
import de.roserstudios.lovely.budgetPlanning.EntryCategory;
import de.roserstudios.lovely.budgetPlanning.MoneyManager;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class FragmentBudgetPlanning_AddExpense extends Fragment{

    private static View rootView;
    private View vw_entry_preview_view;

    private TextView tv_category_title;
    private TextView tv_description_title;
    private TextView tv_amount_title;
    private TextView tv_entry_preview_category_title;
    private TextView tv_entry_preview_description;
    private TextView tv_entry_preview_amount;

    private static Spinner spn_category;

    private static EditText edt_description;
    private static EditText edt_amount;

    private Button btn_add;

    private static MoneyManager moneyManager;

    private static String errorMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_budget_planning_add_expense, container, false);
        vw_entry_preview_view = rootView.findViewById(R.id.f_add_expense_entry_preview);

        moneyManager = MoneyManager.get_instance(rootView.getContext());

        //Get references to views
        tv_category_title = rootView.findViewById(R.id.f_add_expense_tv_category_title);
        tv_description_title = rootView.findViewById(R.id.f_add_expense_tv_description_title);
        tv_amount_title = rootView.findViewById(R.id.f_add_expense_tv_amount_title);

        tv_entry_preview_category_title = vw_entry_preview_view.findViewById(R.id.f_spendings_list_item_tv_categorie);
        tv_entry_preview_description = vw_entry_preview_view.findViewById(R.id.f_spendings_list_item_tv_description);
        tv_entry_preview_amount = vw_entry_preview_view.findViewById(R.id.f_spendings_list_item_tv_amount);

        spn_category = rootView.findViewById(R.id.f_add_expense_spn_category);
        spn_category.setAdapter(new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, EntryCategory.values()));

        edt_description = rootView.findViewById(R.id.f_add_expense_edt_description);
        edt_amount = rootView.findViewById(R.id.f_add_expense_edt_amount);

        btn_add = rootView.findViewById(R.id.f_add_expense_btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validEntry()){
                    Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_LONG).show();
                    return;
                }

                moneyManager.addNewEntry(new Entry(edt_description.getText().toString(),
                        Double.parseDouble(edt_amount.getText().toString()),
                        spn_category.getSelectedItem().toString()));

                Snackbar.make(rootView, "Your expenses have successfully been updated.",
                        Snackbar.LENGTH_LONG).show();

                clearInputFields();
            }
        });
        //On input change, update the preview entry according to the new data
        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tv_entry_preview_category_title.setText(spn_category.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_entry_preview_description.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_entry_preview_amount.setText("-" + charSequence + " €");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;
    }

    public static boolean validEntry(){

        if(edt_description.getText().equals("")){
            errorMessage = "Please give a short description so you don't get lost in bills :$";
            return false;
        }
        else if(edt_amount.getText().equals("") ||
                Double.parseDouble(edt_amount.getText().toString()) <= 0){
            errorMessage = "Ooohhh wooaw, congrats! You payed nothing -_-";
            return false;
        }
        else
            return true;
    }

    private static void clearInputFields(){
        edt_amount.setText("");
        edt_description.setText("");
        spn_category.setSelection(0);
    }
}
