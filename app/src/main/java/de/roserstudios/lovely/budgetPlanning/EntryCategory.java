package de.roserstudios.lovely.budgetPlanning;

/**
 * Created by danie_000 on 12.08.2017.
 */

public enum EntryCategory {
    FOOD("food"),
    HOUSE_HOLD("something for the apartment"),
    SAVING("saving"),
    GIFT("gift"),
    UNUSUAL("unusual"),
    INCOME("extra income"),
    CELLPHONE("CELLPHONE"),
    RENT("");

    private String text;

    EntryCategory(String text){
        this.text = text;
    }
}
