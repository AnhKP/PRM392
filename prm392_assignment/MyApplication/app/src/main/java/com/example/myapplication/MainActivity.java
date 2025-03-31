package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.ExpenseManager.AddFragment;
import com.example.myapplication.ExpenseManager.ExpensesFragment;
import com.example.myapplication.ExpenseManager.HomeFragment;
import com.example.myapplication.ExpenseManager.IncomeFragment;
import com.example.myapplication.ExpenseManager.ProfileFragment;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        String userID = getIntent().getStringExtra("UserID");

        // Nếu userID null, lấy từ SharedPreferences
        if (userID == null) {
            SharedPreferences preferences = getSharedPreferences("userID", MODE_PRIVATE);
            userID = preferences.getString("UserID", "");
        }

        Log.i("MainActivity", "UserID: " + userID);
       binding.bottomNavigationView.setOnItemSelectedListener(item -> {
           if (item.getItemId() == R.id.navHome) {
               replaceFragment(new HomeFragment());
           } else if (item.getItemId() == R.id.navProfile) {
               replaceFragment(new ProfileFragment());
           } else if(item.getItemId() == R.id.navAdd){
               replaceFragment(new AddFragment());
           } else if(item.getItemId() == R.id.navIncome){
               replaceFragment(new IncomeFragment());
           } else if(item.getItemId() == R.id.navExpense){
               replaceFragment(new ExpensesFragment());
           }
           return true;
       });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager frag = getSupportFragmentManager();
        FragmentTransaction fragTrans = frag.beginTransaction();
        fragTrans.replace(R.id.fragmentContainer, fragment);
        fragTrans.commit();
    }
}