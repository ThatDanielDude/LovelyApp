package de.roserstudios.lovely.budgetPlanning;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class Expense {

    private long _id;
    private ExpenseCategory category;
    private long time;
    private String description;
    private String stringAmount;
    private double amount;

    public Expense(ExpenseCategory category, String description, double amount) {
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public Expense(long _id, ExpenseCategory category, long time, String description, double amount) {
        this._id = _id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.time = time;
    }

    public long get_id() { return _id; }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStringAmount() {
        return String.format("%.2f€", amount);
    }

    @Override
    public String toString() {
        return String.format(" Category: %s \n Description: %s \n Amount: %s", category, description, getStringAmount());
    }
}
