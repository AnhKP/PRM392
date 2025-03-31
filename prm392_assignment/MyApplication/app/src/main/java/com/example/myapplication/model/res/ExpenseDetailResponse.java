package com.example.myapplication.model.res;

public class ExpenseDetailResponse {

    private int expenseId;

    private String date;

    private double amount;

    private String description;

    public ExpenseDetailResponse() {
    }

    public ExpenseDetailResponse(int expenseId, String date, double amount, String description) {
        this.expenseId = expenseId;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
