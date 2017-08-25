package de.roserstudios.lovely.tabs;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.roserstudios.lovely.R;
import de.roserstudios.lovely.budgetPlanning.Entry;
import de.roserstudios.lovely.budgetPlanning.MoneyManager;
import de.roserstudios.lovely.budgetPlanning.SpendingListAdapter;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class FragmentBudgetPlanning_Spendings extends Fragment{

    private View rootView;

    private final String MSG_DELETE_ENTRY = "Delete Entry?";
    private final String ERR_DATE_FROM_MISSING = "Please first select a date from.";

    private ListView lv_spendings;
    private MoneyManager moneyManager;
    private View v_currentBalanceRootView;
    private TextView tv_currentBalanceTitle;
    private TextView tv_currentBalanceDescription;
    private TextView tv_currentBalanceAmount;
    private EditText edt_dateFrom;
    private EditText edt_dateTo;
    private Button btn_refreshList;

    private long dateFromInMilliseconds;
    private long dateUntilInMilliseconds;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        dateFromInMilliseconds = 0;
        dateUntilInMilliseconds = 0;

        rootView = inflater.inflate(R.layout.fragment_budget_planning_spendings, container, false);
        getViewRefs();

        lv_spendings = (ListView)rootView.findViewById(R.id.fragment_budget_planning_spendings_lv_spendings);
        moneyManager = MoneyManager.get_instance(rootView.getContext());
        refreshList();

        lv_spendings.setAdapter(new SpendingListAdapter(rootView.getContext(), moneyManager.getCurrentTimeFrameEntries()));

        setOnClickListeners();
        updateCurrentBalanceView();
        return rootView;
    }

    public void refreshList(){
        if(dateUntilInMilliseconds == 0 || dateFromInMilliseconds == 0)
            moneyManager.updateData(0, Calendar.getInstance().getTimeInMillis());
        else
            moneyManager.updateData(dateFromInMilliseconds, dateUntilInMilliseconds);

        updateCurrentBalanceView();
        lv_spendings.setAdapter(new SpendingListAdapter(rootView.getContext(), moneyManager.getCurrentTimeFrameEntries()));
    }

    public void deleteEntryDialog(final Entry entryToDelete){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Delete Entry?");
        builder.setTitle("Title");
        final AlertDialog dialog;



        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(moneyManager.deleteEntry(entryToDelete)) {
                    Snackbar.make(getView(), "Successfully deleted entry", Snackbar.LENGTH_SHORT).show();
                    refreshList();
                }
                else
                    Snackbar.make(getView(), "Unable to deleted entry", Snackbar.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Cancel", null);

        dialog = builder.create();
        dialog.show();
    }

    private void getViewRefs(){
        v_currentBalanceRootView = rootView.findViewById(R.id.fragment_budget_planning_spendings_itm_currentBalance);
        tv_currentBalanceTitle = v_currentBalanceRootView.findViewById(R.id.f_spendings_list_item_tv_categorie);
        tv_currentBalanceDescription = v_currentBalanceRootView.findViewById(R.id.f_spendings_list_item_tv_description);
        tv_currentBalanceAmount = v_currentBalanceRootView.findViewById(R.id.f_spendings_list_item_tv_amount);

        edt_dateFrom = rootView.findViewById(R.id.fragment_budget_planning_spendings_edt_dateFrom);
        edt_dateTo = rootView.findViewById(R.id.fragment_budget_planning_spendings_edt_dateTo);
        edt_dateFrom.setText("");
        edt_dateTo.setText("");
        btn_refreshList = rootView.findViewById(R.id.fragment_budget_planning_spendings_btn_refreshList);
    }

    private void setOnClickListeners(){
        lv_spendings.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteEntryDialog(moneyManager.getCurrentTimeFrameEntries().get(i));
                return true;
            }
        });

        edt_dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long maxDate = Calendar.getInstance().getTimeInMillis() - moneyManager.getDAY_IN_MS();

                if(!edtIsEmpty(edt_dateTo)) {
                    String[] split = edt_dateTo.getText().toString().split("[.]");
                    int day = Integer.parseInt(split[0]);
                    int month = Integer.parseInt(split[1]);
                    int year = Integer.parseInt(split[2]);

                    Calendar c = Calendar.getInstance();
                    //January = 0
                    c.set(year, month-1, day);

                    maxDate = c.getTimeInMillis() - moneyManager.getDAY_IN_MS();
                }
                dateUntilInMilliseconds = maxDate;
                initDatePicker(edt_dateFrom, 0, maxDate);
            }
        });

        edt_dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtIsEmpty(edt_dateFrom)) {
                    Snackbar.make(getView(), ERR_DATE_FROM_MISSING, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                String[] split = edt_dateFrom.getText().toString().split("[.]");
                int day = Integer.parseInt(split[0]);
                int month = Integer.parseInt(split[1]);
                int year = Integer.parseInt(split[2]);

                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                dateFromInMilliseconds = c.getTimeInMillis();
                initDatePicker(edt_dateTo, dateFromInMilliseconds, Calendar.getInstance().getTimeInMillis());

            }
        });

        btn_refreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               refreshList();
            }
        });

    }

    private void initDatePicker(final EditText ed, long minDate, long maxDate){

        final Calendar c = Calendar.getInstance();



        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DATE, dayOfMonth);
                updateEditDates(ed, c);
                switch(ed.getId()){
                    case R.id.fragment_budget_planning_spendings_edt_dateFrom:
                        dateFromInMilliseconds = c.getTimeInMillis();
                        break;
                    case R.id.fragment_budget_planning_spendings_edt_dateTo:
                        dateUntilInMilliseconds = c.getTimeInMillis();
                        break;
                }
            }
        };

        c.setTimeInMillis(maxDate);
        DatePickerDialog dialog = new DatePickerDialog(getContext(), date, c
                .get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DATE));



        dialog.getDatePicker().setMinDate(minDate);
        dialog.getDatePicker().setMaxDate(maxDate);

        dialog.show();
    }

    private void updateEditDates(EditText e, Calendar c){
        String format = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.GERMANY);
        e.setText(simpleDateFormat.format(c.getTime()));
    }

    private void updateCurrentBalanceView(){
        tv_currentBalanceTitle.setText("Current Balance");
        tv_currentBalanceDescription.setText("From the " +
                moneyManager.getFormattedDate("dd.MM.yyyy", dateFromInMilliseconds) + " until " +
                moneyManager.getFormattedDate("dd.MM.yyy", dateUntilInMilliseconds));
        tv_currentBalanceAmount.setText(String.format("%.2f€",
                moneyManager.accountBalanceInCurrentTimeFrame()));
    }

    private boolean edtIsEmpty(EditText e){
        return e.getText().toString().trim().length() == 0;
    }

}
