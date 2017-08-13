package de.roserstudios.lovely.budgetPlanning;

/**
 * Created by danie_000 on 13.08.2017.
 */

public class ReoccurringEntry extends Entry {

    private int monthlyReoccurringDay;

    public ReoccurringEntry(long _id, long timeOfOccurrence, String description, double amount, EntryCategory category, int monthlyReoccurringDay) {
        super(_id, timeOfOccurrence, description, amount, category);
        this.monthlyReoccurringDay = monthlyReoccurringDay;
    }

    public int getMonthlyReoccurringDay() {
        return monthlyReoccurringDay;
    }

    public void setMonthlyReoccurringDay(int monthlyReoccurringDay) {
        this.monthlyReoccurringDay = monthlyReoccurringDay;
    }
}
