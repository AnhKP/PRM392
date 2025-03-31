package com.example.myapplication.ExpenseManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.apiservice.UserApiService;
import com.example.myapplication.model.req.LoginRequest;
import com.example.myapplication.model.req.RegisterRequest;
import com.example.myapplication.model.res.LoginResponse;
import com.example.myapplication.model.res.RegisterResponse;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtEmail, edtPwd, edtCfPwd;
    private Button btnSignUp;

    private TextView tvSignIn;
    private UserApiService userapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        edtName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhoneNumber);
        edtEmail = findViewById(R.id.edtEmail);
        edtPwd = findViewById(R.id.edtPassword);
        edtCfPwd = findViewById(R.id.edtCfPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        TextView tvSignUp = findViewById(R.id.tvSignIn);
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(v -> {
            registerAcc();
        });
    }

    public void registerAcc() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userapi = retrofit.create(UserApiService.class);

        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPwd.getText().toString().trim();
        String confirmPassword = edtCfPwd.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPhone(phone)){
            Toast.makeText(this, "Nhập số điện thoại bắt đầu từ 0 và có 10 số", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidEmail(email)){
            Toast.makeText(this, "Vui lòng nhập email có định dạng @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPassword(password)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu có độ dài ít nhất 6 kí tự và có chứa cả chữ và số", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        //Lấy danh sách email
        Call<List<String>> call = userapi.getRegisteredEmails();

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<String> listemail = response.body();
                    //checkemail đã tồn tại hay chưa
                    if(listemail.contains(email)){
                        Toast.makeText(SignUpActivity.this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        RegisterRequest request = new RegisterRequest(name, phone, email, password);
                        Call<RegisterResponse> callRegister = userapi.register(request);
                        callRegister.enqueue(new Callback<RegisterResponse>() {
                            @Override
                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                Log.e("API_ERROR", "Request failed: " + t.getMessage());
                                Toast.makeText(SignUpActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Lỗi khi kiểm tra email!", Toast.LENGTH_SHORT).show();
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