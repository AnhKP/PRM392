package com.example.myapplication.model.req;

public class AddExpenseRequest {
    private int categoryId;
    private double amount;
    private String date;
    private String description;

    public AddExpenseRequest(int categoryId, double amount, String date, String description) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
}
