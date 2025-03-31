package com.example.myapplication.ExpenseManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.apiservice.UserApiService;
import com.example.myapplication.model.res.ExpensesHistoryResponse;
import com.example.myapplication.model.res.Top5Income;
import com.example.myapplication.model.res.UserProfileResponse;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView txtHello, txtMonthValue, txtIncomeValue, txtExpenseValue, txtRemainingValue;
    private RecyclerView viewExpenseRecycle, viewIncomeRecycle;
    private TopExpenseAdapter expenseAdapter;
    private TopIncomeAdapter incomeAdapter;
    private List<ExpensesHistoryResponse> expenseList = new ArrayList<>();
    private List<Top5Income> incomeList = new ArrayList<>();

    private double income = 0,expense = 0,remaining = 0;
    private UserApiService userapi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences preferences = requireActivity().getSharedPreferences("userID", Context.MODE_PRIVATE);
        String userID = preferences.getString("UserID", "");
        int userId = Integer.parseInt(userID);

        txtHello = view.findViewById(R.id.txtHello);
        txtMonthValue  = view.findViewById(R.id.txtMonthValue);
        txtIncomeValue  = view.findViewById(R.id.txtIncomeValue);
        txtExpenseValue  = view.findViewById(R.id.txtExpenseValue);
        txtRemainingValue  = view.findViewById(R.id.txtRemainingValue);

        getName(userId);
         Calendar calendar = Calendar.getInstance();
          int currentMonth = calendar.get(Calendar.MONTH) + 1;
          txtMonthValue.setText(String.valueOf(currentMonth));
          getTotalIncome(userId);
          getTotalExpense(userId);
          getRemaining();

        viewExpenseRecycle = view.findViewById(R.id.viewExpenseRecycle);
        viewIncomeRecycle = view.findViewById(R.id.viewIncomeRecycle);

        viewExpenseRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
       viewIncomeRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

        expenseAdapter = new TopExpenseAdapter(expenseList);
        incomeAdapter = new TopIncomeAdapter(incomeList);

       viewExpenseRecycle.setAdapter(expenseAdapter);
       viewIncomeRecycle.setAdapter(incomeAdapter);

       loadTopExpensesOfMonth(userId);
       loadTopIncomesOfYear(userId);

        return view;
    }

    public void getName(int userId){
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:7046/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            userapi = retrofit.create(UserApiService.class);
            Call<UserProfileResponse> call = userapi.getUserProfile(userId);

            call.enqueue(new Callback<UserProfileResponse>() {

                @Override
                public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserProfileResponse user = response.body();
                        txtHello.setText(user.getFullName());
                    }
                }

                @Override
                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    Log.e("ProfileActivity", "Lỗi gọi API: " + t.getMessage());
                }
            });
        }catch(Exception ex){
            Log.e("ProfileActivity", "Lỗi gọi API: " + ex.getMessage());
        }
    }
    public void getTotalIncome(int userId){
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:7046/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            userapi = retrofit.create(UserApiService.class);
            Call<String> call = userapi.getTotalIncome(userId);
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        income = Double.parseDouble(response.body().toString());
                        String value = String.format("%,.2f VND", income);
                        txtIncomeValue.setText(value);
                        getRemaining();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("ProfileActivity", "Lỗi gọi API: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("ProfileActivity", "Lỗi gọi API: " + e.getMessage());
        }
    }

    public void getTotalExpense(int userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userapi = retrofit.create(UserApiService.class);
        Call<String> call = userapi.getTotalExpense(userId);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String expenseValue = response.body();
                    expense = Double.parseDouble(expenseValue);
                    getRemaining();
                    txtExpenseValue.setText(String.format("%,.2f VND",expense));
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ProfileActivity", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }
    public void getRemaining(){
        remaining = income - expense;
        txtRemainingValue.setText(String.format("%,.2f VND",remaining));
    }

    private void loadTopExpensesOfMonth(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userapi = retrofit.create(UserApiService.class);
        Call<List<ExpensesHistoryResponse>> call = userapi.getTop5ExpenseInMonth(userId);

        call.enqueue(new Callback<List<ExpensesHistoryResponse>>() {
            @Override
            public void onResponse(Call<List<ExpensesHistoryResponse>> call, Response<List<ExpensesHistoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    expenseList.clear();
                    expenseList.addAll(response.body());
                    expenseAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<ExpensesHistoryResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi tải dữ liệu chi tiêu", t);
            }
        });
    }

    private void loadTopIncomesOfYear(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userapi = retrofit.create(UserApiService.class);
        Call<List<Top5Income>> call = userapi.getTop5IncomeInYear(userId);

        call.enqueue(new Callback<List<Top5Income>>() {
            @Override
            public void onResponse(Call<List<Top5Income>> call, Response<List<Top5Income>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    incomeList.clear();
                    incomeList.addAll(response.body());
                    incomeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Top5Income>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi tải dữ liệu thu nhập", t);
            }
        });
    }
}