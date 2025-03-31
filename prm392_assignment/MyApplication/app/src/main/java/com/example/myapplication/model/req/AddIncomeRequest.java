package com.example.myapplication.model.req;

public class AddIncomeRequest {
    private double amount;
    private int month;
    private int year;

    public AddIncomeRequest(double amount, int month, int year) {
        this.amount = amount;
        this.month = month;
        this.year = year;
    }
}
