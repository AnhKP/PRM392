package com.example.myapplication.ExpenseManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiservice.ExpensesApiService;
import com.example.myapplication.apiservice.IncomeApiService;
import com.example.myapplication.apiservice.UserApiService;
import com.example.myapplication.model.req.AddExpenseRequest;
import com.example.myapplication.model.req.AddIncomeRequest;
import com.example.myapplication.model.res.ListCategory;
import com.example.myapplication.model.res.UserProfileResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    private RadioGroup radioGroup;
    private LinearLayout incomeForm, expenseForm;
    private Spinner spinnerCategory, spnMonthSelector, spnYearSelector;
    private EditText editTextIncomeAmount, editTextExpenseAmount, editTextExpenseDate, editTextExpenseDescription;
    private Button btnSelectDate, btnAddIncome, btnAddExpense;
    private List<ListCategory> categoryList = new ArrayList<>();
    private ExpensesApiService exApiService;
    private IncomeApiService incomeApiService;

    private int userId = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add, container, false);
        radioGroup = view.findViewById(R.id.radioGroup);
        incomeForm = view.findViewById(R.id.incomeForm);
        expenseForm = view.findViewById(R.id.expenseForm);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spnMonthSelector = view.findViewById(R.id.spnMonthSelector);
        spnYearSelector = view.findViewById(R.id.spnYearSelector);
        editTextIncomeAmount = view.findViewById(R.id.editTextIncomeAmount);
        editTextExpenseAmount = view.findViewById(R.id.editTextExpenseAmount);
        editTextExpenseDate = view.findViewById(R.id.editTextExpenseDate);
        editTextExpenseDescription = view.findViewById(R.id.editTextExpenseDescription);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);

        SharedPreferences preferences = requireActivity().getSharedPreferences("userID", Context.MODE_PRIVATE);
        String userID = preferences.getString("UserID", "");
        userId = Integer.parseInt(userID);
        //Radio Group
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioIncome) {
                incomeForm.setVisibility(View.VISIBLE);
                expenseForm.setVisibility(View.GONE);
            } else {
                incomeForm.setVisibility(View.GONE);
                expenseForm.setVisibility(View.VISIBLE);
            }
        });

        //Them date time picker
        btnSelectDate.setOnClickListener(v -> showDatePicker());

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

       // Thiết lập Spinner chọn tháng
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.months,
                android.R.layout.simple_spinner_item
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonthSelector.setAdapter(monthAdapter);
        spnMonthSelector.setSelection(currentMonth - 1); // Đặt tháng hiện tại

       // Thiết lập Spinner chọn năm
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
        spnYearSelector.setSelection(years.indexOf(String.valueOf(currentYear))); // Đặt năm hiện tại
        // Load danh mục chi tiêu từ API
        loadCategories();

        // Xử lý thêm thu nhập
        btnAddIncome.setOnClickListener(v -> addIncome());

        // Xử lý thêm khoản chi tiêu
        btnAddExpense.setOnClickListener(v -> addExpense());
        return view;
    }

    private void addIncome() {
        try {
            String amountStr = editTextIncomeAmount.getText().toString().trim();
            if (!validateAmount(amountStr)) {
                Toast.makeText(requireContext(), "Vui lòng nhập số tiền hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }
            double amount = Double.parseDouble(amountStr.trim());
            String monthStr = spnMonthSelector.getSelectedItem().toString().replaceAll("[^0-9]", "");
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(spnYearSelector.getSelectedItem().toString());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:7046/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            incomeApiService = retrofit.create(IncomeApiService.class);
            AddIncomeRequest income = new AddIncomeRequest(amount, month, year);

            Call<Void> call = incomeApiService.addIncome(userId, income);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Thêm thu nhập thành công!", Toast.LENGTH_SHORT).show();
                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setSelectedItemId(R.id.navIncome);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(requireContext(), "Lỗi khi thêm thu nhập", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("AddIncomeError", "Lỗi khi thêm thu nhập: " + e.getMessage());
        }
    }

    private void addExpense() {

        int selectedPosition = spinnerCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(selectedPosition).getCategoryId(); // Lấy ID của danh mục đã chọn
        String amountStr = editTextExpenseAmount.getText().toString().trim();
        if(!validateAmount(amountStr)){
            Toast.makeText(requireContext(), "Vui lòng nhập số tiền hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountStr.trim());
        String dateStr = editTextExpenseDate.getText().toString();
        String dateAdd = convertToISO8601(dateStr);
        String description = editTextExpenseDescription.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        exApiService = retrofit.create(ExpensesApiService.class);
        AddExpenseRequest expense = new AddExpenseRequest(categoryId, amount, dateAdd, description);
        Call<Void> call = exApiService.addExpense(userId, expense);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Thêm khoản chi thành công!", Toast.LENGTH_SHORT).show();
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                    bottomNavigationView.setSelectedItemId(R.id.navExpense);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi khi thêm khoản chi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        exApiService = retrofit.create(ExpensesApiService.class);
        Call<List<ListCategory>> call = exApiService.getListCategory();

        call.enqueue(new Callback<List<ListCategory>>() {
            @Override
            public void onResponse(Call<List<ListCategory>> call, Response<List<ListCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    // Lấy danh sách tên danh mục
                    List<String> categoryNames = new ArrayList<>();
                    for (ListCategory category : categoryList) {
                        categoryNames.add(category.getCategoryName());                }

                    // Gán dữ liệu vào Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ListCategory>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, day1) -> {
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + day1;
            editTextExpenseDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private String convertToISO8601(String dateString) {
        try {
            // Định dạng ban đầu của ngày nhập từ EditText (giả sử là dd/MM/yyyy)
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateString);

            // Định dạng sang ISO 8601 (UTC)
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Đặt múi giờ UTC

            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }

    private boolean validateAmount(String editText) {
        if (!editText.isEmpty()) {
            try {
                double amount = Double.parseDouble(editText);
                if (amount <= 0) {
                   return false;
                } else {
                   return true; // Xóa lỗi nếu hợp lệ
                }
            } catch (NumberFormatException e) {
               return false;
            }
        }
        return false;
    }
}