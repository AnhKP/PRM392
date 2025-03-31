package com.example.myapplication.model.res;

public class IncomesResponse {
    private int incomeId;
    private int userId;
    private double amount;
    private int month;
    private int year;
    private String date;
    public IncomesResponse() {
    }

    public IncomesResponse(int incomeId, int userId, double amount, int month, int year, String date) {
        this.incomeId = incomeId;
        this.userId = userId;
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.date = date;
    }

    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCreatedAt() {
        return date;
    }

    public void setCreatedAt(String date) {
        this.date = date;
    }
}
