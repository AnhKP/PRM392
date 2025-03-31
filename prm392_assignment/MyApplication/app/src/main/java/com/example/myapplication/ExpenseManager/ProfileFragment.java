package com.example.myapplication.ExpenseManager;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.apiservice.UserApiService;
import com.example.myapplication.model.req.RegisterRequest;
import com.example.myapplication.model.req.UpdateProfileRequest;
import com.example.myapplication.model.res.RegisterResponse;
import com.example.myapplication.model.res.UserProfileResponse;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private EditText edtName, edtPhone, edtEmail, edtPwd, edtCfPwd, edtStatus;
    private Button btnEdit, btnSave, btnBack;

    private TextView tvCfPassword;
    private UserApiService userapi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Lay Id duoc luu
        SharedPreferences preferences = requireActivity().getSharedPreferences("userID", Context.MODE_PRIVATE);
        String userID = preferences.getString("UserID", "");
        int number = 0;
        try {
            number = Integer.parseInt(userID);
        } catch (NumberFormatException e) {
            System.out.println("Chuỗi không hợp lệ!");
        }

        edtName = view.findViewById(R.id.edtFullName);
        edtPhone = view.findViewById(R.id.edtPhoneNumber);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPwd = view.findViewById(R.id.edtPassword);
        edtCfPwd = view.findViewById(R.id.edtCfPassword);
        edtStatus = view.findViewById(R.id.edtStatus);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnBack = view.findViewById(R.id.btnBack);
        btnSave = view.findViewById(R.id.btnSave);

        tvCfPassword =  view.findViewById(R.id.tvCfPassword);

        displayprofle(number);
        edtCfPwd.setVisibility(GONE);
        tvCfPassword.setVisibility(GONE);
        btnEdit.setVisibility(View.VISIBLE);  // Hiển thị nút Edit
        btnBack.setVisibility(View.VISIBLE);  // Hiển thị nút Back
        btnSave.setVisibility(View.GONE);


        btnBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.navProfile, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnEdit.setOnClickListener(v -> {
            enableEditing(true);
            btnEdit.setVisibility(View.GONE);  // Hiển thị nút Edit
            btnBack.setVisibility(View.VISIBLE);  // Hiển thị nút Back
            btnSave.setVisibility(View.VISIBLE);
            edtCfPwd.setVisibility(View.VISIBLE);
            tvCfPassword.setVisibility(View.VISIBLE);
        });

        int finalNumber = number;
        btnSave.setOnClickListener(v -> {
            updateProfile(finalNumber);
            enableEditing(false);
            btnEdit.setVisibility(View.VISIBLE);  // Hiển thị nút Edit
            btnBack.setVisibility(View.VISIBLE);  // Hiển thị nút Back
            btnSave.setVisibility(View.GONE);
            edtCfPwd.setVisibility(View.GONE);
            tvCfPassword.setVisibility(View.GONE);
        });

        return view;
    }
    public void displayprofle(int userId){
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
                    edtName.setText(user.getFullName());
                    edtPhone.setText(user.getPhoneNumber());
                    edtEmail.setText(user.getEmail());
                    edtStatus.setText(user.getStatus());
                    edtPwd.setText(user.getPassword());
                }
            }
            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e("ProfileActivity", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }
    private void enableEditing(boolean isEnabled) {
        edtName.setEnabled(isEnabled);
        edtPhone.setEnabled(isEnabled);
        edtPwd.setEnabled(isEnabled);
    }

    private void updateProfile(int userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userapi = retrofit.create(UserApiService.class);

        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPwd.getText().toString().trim();
        String confirmPassword = edtCfPwd.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPhone(phone)){
            Toast.makeText(getActivity(), "Nhập số điện thoại bắt đầu từ 0 và có 10 số", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPassword(password)){
            Toast.makeText(getActivity(), "Vui lòng nhập mật khẩu có độ dài ít nhất 6 kí tự và có chứa cả chữ và số", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        //Lấy danh sách email
        UpdateProfileRequest update = new UpdateProfileRequest(userId,password,phone,name);
        Call<UserProfileResponse> call = userapi.updateProfile(userId, update);

        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show();
                    Log.i("Update profile: ",String.valueOf(userId));
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e("API_ERROR", "Request failed: " + t.getMessage());
                Toast.makeText(getActivity(), "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isValidPhone(String phone) {
        return Pattern.matches("^0\\d{9}$", phone);
    }

    public boolean isValidEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$", email);
    }

    public boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$", password);
    }
}