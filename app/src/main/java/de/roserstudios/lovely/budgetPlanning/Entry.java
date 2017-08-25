package de.roserstudios.lovely.budgetPlanning;

import android.icu.util.ULocale;

import java.util.Calendar;

/**
 * Created by danie_000 on 13.08.2017.
 */

public class Entry {

    private long _id;
    private long timeOfOccurrence;
    private String description;
    private double amount;
    private String amountString;
    private EntryCategory category;

    public Entry(long _id, long timeOfOccurrence, String description, double amount,  EntryCategory category) {
        this._id = _id;
        this.timeOfOccurrence = timeOfOccurrence;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public Entry(String description, double amount, String category){
        this._id = -1;
        this.timeOfOccurrence = Calendar.getInstance().getTimeInMillis();
        this.description = description;
        this.amount = amount;
        this.category = EntryCategory.valueOf(category);
    }

    public long get_id() {
        return _id;
    }

    public long getTimeOfOccurrence() {
        return timeOfOccurrence;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountString() {
        return String.format("%.2f€", getAmount());
    }

    public EntryCategory getCategory() {
        return category;
    }
}
