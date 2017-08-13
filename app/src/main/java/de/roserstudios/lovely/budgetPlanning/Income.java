package de.roserstudios.lovely.budgetPlanning;

/**
 * Created by danie_000 on 12.08.2017.
 */

public class Income {

    private long _id;
    private IncomeCategory category;
    private double amount;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private String amountString;

    public Income(IncomeCategory category, double amount, String amountString) {
        this.category = category;
        this.amount = amount;
        this.amountString = amountString;
    }

    public Income(long _id, IncomeCategory category, double amount, long time) {
        this._id = _id;
        this.category = category;
        this.amount = amount;
        this.time = time;
    }

    public long get_id(){ return _id; }

    public IncomeCategory getCategory() {
        return category;
    }

    public void setCategory(IncomeCategory category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountString() { return String.format("%.2f€", amount); }


    @Override
    public String toString() {
        return "Income{" +
                "category=" + category +
                ", amount=" + amount +
                ", amountString='" + amountString + '\'' +
                '}';
    }
}
