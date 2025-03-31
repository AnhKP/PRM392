package com.example.myapplication.ExpenseManager;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.apiservice.ExpensesApiService;
import com.example.myapplication.apiservice.IncomeApiService;
import com.example.myapplication.model.res.ExpenseDetailResponse;
import com.example.myapplication.model.res.ExpensesHistoryResponse;
import com.example.myapplication.model.res.IncomesResponse;

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
 * Use the {@link ExpensesFragment} factory method to
 * create an instance of this fragment.
 */
public class ExpensesFragment extends Fragment implements ExpenseAdapter.OnDetailClickListener {
    Spinner spnMonthSelector, spnYearSelector;
    ImageView imgSearch;
    RecyclerView recyclerView;
    ExpenseAdapter adapter;
    List<ExpensesHistoryResponse> exList = new ArrayList<>();

    private TextView tvAmount, tvMonth;

    private int currentMonth = 0;

    private int currentYear = 0;
    private ExpensesApiService exApiService;

    public ExpensesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_expenses, container, false);

        SharedPreferences preferences = requireActivity().getSharedPreferences("userID", Context.MODE_PRIVATE);
        String userID = preferences.getString("UserID", "");
        int userId = Integer.parseInt(userID);

        spnMonthSelector = view.findViewById(R.id.spnMonthSelector);
        spnYearSelector = view.findViewById(R.id.spnYearSelector);
        imgSearch = view.findViewById(R.id.imgSearch);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvMonth = view.findViewById(R.id.tvMonth);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        exList = new ArrayList<>();
        adapter = new ExpenseAdapter(exList, categoryId -> showExpenseDetailDialog(categoryId, userId));
        recyclerView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentYear = calendar.get(Calendar.YEAR);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.months,
                android.R.layout.simple_spinner_item
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonthSelector.setAdapter(monthAdapter);
        spnMonthSelector.setSelection(currentMonth - 1);


        List<String> years = new ArrayList<>();
        for (int i = currentYear - 10; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                years
        );
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYearSelector.setAdapter(yearAdapter);
        spnYearSelector.setSelection(years.indexOf(String.valueOf(currentYear))); // Chọn năm hiện tại


        imgSearch.setOnClickListener(v -> {
            int selectedMonthIndex = spnMonthSelector.getSelectedItemPosition() + 1; // Tháng 1 -> index 0, nên +1
            int selectedYear = Integer.parseInt(spnYearSelector.getSelectedItem().toString());
            fetchExpenseHistory(userId, selectedMonthIndex, selectedYear);
            currentMonth = selectedMonthIndex;
            currentYear = selectedYear;
        });

        fetchExpenseHistory(userId,currentMonth, currentYear);
        return view;
    }

    private void fetchExpenseHistory(int userId,int month, int year) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        exApiService =  retrofit.create(ExpensesApiService.class);
        Call<List<ExpensesHistoryResponse>> call = exApiService.getExpenseHistory(userId, month, year);
        call.enqueue(new Callback<List<ExpensesHistoryResponse>>() {
            @Override
            public void onResponse(Call<List<ExpensesHistoryResponse>> call, Response<List<ExpensesHistoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    exList.clear();
                    exList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    displayMonthAndAmount(exList, month,year);
                }
            }
            @Override
            public void onFailure(Call<List<ExpensesHistoryResponse>> call, Throwable t) {
                Log.e("IncomeFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }
    private void displayMonthAndAmount(List<ExpensesHistoryResponse> list, int month, int year){
        double sum = 0;
        for(int i = 0; i < list.size(); i++){
            sum += list.get(i).getTotalAmount();
        }
        String date = String.valueOf(month) + "/" + String.valueOf(year);
        tvAmount.setText("Tổng chi tiêu: " + String.valueOf(sum));
        tvMonth.setText("Tháng: " + date);

    }

    private void showExpenseDetailDialog(int categoryId, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Lịch sử chi tiết");

        RecyclerView recyclerView = new RecyclerView(requireActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ExpensesApiService apiService = retrofit.create(ExpensesApiService.class);
        Call<List<ExpenseDetailResponse>> call = apiService.getExpenseDetail(userId, categoryId);

        call.enqueue(new Callback<List<ExpenseDetailResponse>>() {
            @Override
            public void onResponse(Call<List<ExpenseDetailResponse>> call, Response<List<ExpenseDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ExpenseDetailResponse> details = response.body();
                    Log.d("ExpenseDetail", "Số lượng chi tiết: " + response.body().size()); // Kiểm tra số lượng dữ liệu
                    ExpenseHistoryAdapter detailAdapter = new ExpenseHistoryAdapter(details);
                    Log.d("ExpenseDialog", "Adapter item count: " + detailAdapter.getItemCount());
                    if (recyclerView == null) {
                        Log.e("ExpenseDetail", "RecyclerView is null");
                    } else {
                        Log.d("ExpenseDetail", "RecyclerView is ready");
                    }
                    recyclerView.setAdapter(detailAdapter);
                    detailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ExpenseDetailResponse>> call, Throwable t) {
                Log.e("ExpenseDetail", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
        if(recyclerView == null){
            Log.e("ExpenseDetail", "RecyclerView is null");
        }else{
            Log.e("ExpenseDetail", "RecyclerView is not null");
        }
        builder.setView(recyclerView);
        builder.setNegativeButton("Đóng", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDetailClick(int incomeId) {

    }
}