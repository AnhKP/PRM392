package com.example.myapplication.ExpenseManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiservice.IncomeApiService;
import com.example.myapplication.apiservice.UserApiService;
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
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment implements IncomeAdapter.OnDeleteClickListener {

    Spinner spnMonthSelector, spnYearSelector;
    ImageView imgSearch;
    RecyclerView recyclerView;
    IncomeAdapter adapter;
    List<IncomesResponse> incomeList = new ArrayList<>();

    private TextView tvAmount, tvMonth;

    private int currentMonth = 0;

    private int currentYear = 0;
    private IncomeApiService incomeApiService;

    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_income, container, false);

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

        incomeList = new ArrayList<>();
        adapter = new IncomeAdapter(incomeList,this);
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
            fetchIncomeHistory(userId, selectedMonthIndex, selectedYear);
            currentMonth = selectedMonthIndex;
            currentYear = selectedYear;
        });

        fetchIncomeHistory(userId,currentMonth, currentYear);
        return view;
    }

    @Override
    public void onDeleteClick(int incomeId) {
        deleteIncome(incomeId);
    }
    private void fetchIncomeHistory(int userId,int month, int year) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        incomeApiService =  retrofit.create(IncomeApiService.class);
        Call<List<IncomesResponse>> call = incomeApiService.getIncomeHistory(userId, month, year);
        call.enqueue(new Callback<List<IncomesResponse>>() {
            @Override
            public void onResponse(Call<List<IncomesResponse>> call, Response<List<IncomesResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    incomeList.clear();
                    incomeList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    displayMonthAndAmount(incomeList, month,year);
                }
            }
            @Override
            public void onFailure(Call<List<IncomesResponse>> call, Throwable t) {
                Log.e("IncomeFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }
    private void deleteIncome(int incomeId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.i("Incomce Id :{0}", String.valueOf(incomeId));
        incomeApiService =  retrofit.create(IncomeApiService.class);
        Call<Void> call = incomeApiService.deleteIncome(incomeId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < incomeList.size(); i++) {
                        if (incomeList.get(i).getIncomeId() == incomeId) {
                            incomeList.remove(i);
                            adapter.notifyItemRemoved(i);
                            adapter.notifyItemRangeChanged(i, incomeList.size());
                            break;
                        }
                    }
                    Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayMonthAndAmount(List<IncomesResponse> list, int month, int year){
          double sum = 0;
          for(int i = 0; i < list.size(); i++){
              sum += list.get(i).getAmount();
          }
          String date = String.valueOf(month) + "/" + String.valueOf(year);
          tvAmount.setText("Tổng thu nhập: " + String.valueOf(sum));
          tvMonth.setText("Tháng: " + date);

    }
}
