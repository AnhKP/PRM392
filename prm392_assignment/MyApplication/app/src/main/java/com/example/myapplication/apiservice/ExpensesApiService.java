package com.example.myapplication.apiservice;

import com.example.myapplication.model.req.AddExpenseRequest;
import com.example.myapplication.model.req.AddIncomeRequest;
import com.example.myapplication.model.res.ExpenseDetailResponse;
import com.example.myapplication.model.res.ExpensesHistoryResponse;
import com.example.myapplication.model.res.IncomesResponse;
import com.example.myapplication.model.res.ListCategory;
import com.example.myapplication.model.res.UserProfileResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ExpensesApiService {
    @GET(value = "api/Expenses/expense-history/{userId}/month/{month}/year/{year}")
    Call<List<ExpensesHistoryResponse>> getExpenseHistory(@Path("userId") int userId, @Path("month") int month, @Path("year") int year);

    @POST("api/Expenses/add-expense/{userId}")
    Call<Void> addExpense(@Path("userId") int userId, @Body AddExpenseRequest request);

    @GET("api/Expenses/list-expense-category")
    Call<List<ListCategory>> getListCategory();

    @GET("/api/Expenses/history-expense/{userId}/{categoryId}")
    Call<List<ExpenseDetailResponse>> getExpenseDetail(@Path("userId") int userId, @Path("categoryId") int categoryId);

    @DELETE("/api/Expenses/delete-expense/{expenseId}")
    Call<Void> deleteExpense(@Path("expenseId") int expenseId);

}
