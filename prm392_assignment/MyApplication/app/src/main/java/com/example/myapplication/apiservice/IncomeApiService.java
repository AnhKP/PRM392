package com.example.myapplication.apiservice;

import com.example.myapplication.model.req.AddIncomeRequest;
import com.example.myapplication.model.res.IncomesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IncomeApiService {
    @GET("api/Income/income-history/{userId}/month/{month}/year/{year}")
    Call<List<IncomesResponse>> getIncomeHistory(@Path("userId") int userId, @Path("month") int month, @Path("year") int year);

    @DELETE("api/Income/income-delete/{incomeId}")
    Call<Void> deleteIncome(@Path("incomeId") int incomeId);

    @POST("api/Income/income/{userId}/add")
    Call<Void> addIncome(@Path("userId") int userId, @Body AddIncomeRequest addIncomeRequest);
}
