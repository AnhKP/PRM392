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

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.apiservice.RetrofitInstance;
import com.example.myapplication.apiservice.UserApiService;
import com.example.myapplication.model.req.LoginRequest;
import com.example.myapplication.model.res.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {
   private EditText edtEmail, edtPassword;
   private Button btnSignIn;

   private TextView tvSignUp;
   private UserApiService userapi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        edtEmail = findViewById(R.id.txtEmail);
        edtPassword = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        tvSignUp = findViewById(R.id.tvSignUp);

        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnSignIn.setOnClickListener(v -> {
            login();
        });
    }

    public void login(){
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:7046/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            userapi = retrofit.create(UserApiService.class);

            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            LoginRequest request = new LoginRequest(email, password);
            Call<LoginResponse> call = userapi.login(request);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    int code = response.code();
                    Log.i("Is: ", String.valueOf(code));
                    if (response.isSuccessful() && response.body() != null) {
                        int userId = response.body().getUserId();
                        String email = response.body().getEmail();
                        String uId = String.valueOf(userId);
                        SharedPreferences preferences = getSharedPreferences("userID", MODE_PRIVATE);
                        preferences.edit().putString("UserID", uId).apply();

                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Log.i("Log in successfull!", email);
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("UserID", uId); // Truyền userID qua intent
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Email hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                        Log.i("Log in fail!", email);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("API_ERROR", "Request failed: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e("LoginError", "Lỗi khi gọi API: " + e.getMessage());
        }
    }
}