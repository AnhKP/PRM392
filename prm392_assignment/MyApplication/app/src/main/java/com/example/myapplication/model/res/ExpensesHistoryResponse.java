package com.example.myapplication.model.res;

public class ExpensesHistoryResponse {
    private int categoryId;
    private double totalAmount;
    private String categoryName;


    public ExpensesHistoryResponse() {
    }

    public ExpensesHistoryResponse(int categoryId, double totalAmount, String categoryName) {
        this.categoryId = categoryId;
        this.totalAmount = totalAmount;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
