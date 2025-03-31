package com.example.myapplication.apiservice;

import com.example.myapplication.model.req.LoginRequest;
import com.example.myapplication.model.req.RegisterRequest;
import com.example.myapplication.model.req.UpdateProfileRequest;
import com.example.myapplication.model.res.ExpensesHistoryResponse;
import com.example.myapplication.model.res.IncomesResponse;
import com.example.myapplication.model.res.LoginResponse;
import com.example.myapplication.model.res.RegisterResponse;
import com.example.myapplication.model.res.Top5Income;
import com.example.myapplication.model.res.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiService {
    @POST("api/User/CheckLoginUser")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/User/RegisterAccount")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("api/User/emails")
    Call<List<String>> getRegisteredEmails();

    @GET("api/User/user-profile/{id}")
    Call<UserProfileResponse> getUserProfile(@Path("id") int userId);

    @PUT("api/User/update-profile/{id}")
    Call<UserProfileResponse> updateProfile(@Path("id") int userId, @Body UpdateProfileRequest request);

    @GET("api/Expenses/top-expenses/{userId}")
    Call<List<ExpensesHistoryResponse>> getTop5ExpenseInMonth(@Path("userId") int userId);

    @GET("api/Income/top-5-income/{userId}")
    Call<List<Top5Income>> getTop5IncomeInYear(@Path("userId") int userId);

    @GET("api/Income/total-current-month/{userId}")
    Call<String> getTotalIncome(@Path("userId") int userId);

    @GET(value = "api/Expenses/total-expense-current-month/{userId}")
    Call<String> getTotalExpense(@Path("userId") int userId);



}
