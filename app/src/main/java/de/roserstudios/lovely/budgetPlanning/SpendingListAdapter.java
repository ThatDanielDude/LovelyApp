package de.roserstudios.lovely.budgetPlanning;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.roserstudios.lovely.R;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class SpendingListAdapter extends BaseAdapter {

    Context context;
    List<Entry> list;

    public SpendingListAdapter(Context context, List<Entry> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.fragment_budget_planning_spendings_list_item, viewGroup, false);
            holder = new ViewHolder();

            holder.category = (TextView) view.findViewById(R.id.f_spendings_list_item_tv_categorie);
            holder.description = (TextView) view.findViewById(R.id.f_spendings_list_item_tv_description);
            holder.amount = (TextView) view.findViewById(R.id.f_spendings_list_item_tv_amount);

            view.setTag(holder);
        }

        holder = (ViewHolder) view.getTag();

        Entry e = (Entry)getItem(i);
        holder.category.setText(e.getCategory().toString());
        holder.description.setText(e.getDescription());
        //Set color to green if the amount is larger then 0
        holder.amount.setTextColor((e.getAmount() < 0) ? Color.RED : Color.GREEN);
        holder.amount.setText(e.getAmountString());

        return view;
    }

    class ViewHolder{
        private TextView category;
        private TextView description;
        private TextView amount;
    }
}
