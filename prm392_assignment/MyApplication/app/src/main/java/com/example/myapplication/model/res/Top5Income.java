package com.example.myapplication.model.res;

public class Top5Income {
    private double totalIncome;
    private int month;

    public Top5Income(int month, double totalIncome) {
        this.month = month;
        this.totalIncome = totalIncome;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotalIncome() {
        return totalIncome;
    }
    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
