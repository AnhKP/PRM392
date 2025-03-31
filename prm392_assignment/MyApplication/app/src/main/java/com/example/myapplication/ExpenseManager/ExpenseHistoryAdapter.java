package com.example.myapplication.ExpenseManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.apiservice.ExpensesApiService;
import com.example.myapplication.model.res.ExpenseDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpenseHistoryAdapter extends RecyclerView.Adapter<ExpenseHistoryAdapter.ViewHolder> {
    private List<ExpenseDetailResponse> historyList;
    public ExpenseHistoryAdapter(List<ExpenseDetailResponse> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseDetailResponse expense = historyList.get(position);
        Log.d("ExpenseHistoryAdapter", "Binding item: " + expense.getDescription());
        if (holder.txtDateDetail == null) {
            Log.e("ExpenseAdapter", "txtDate is null, setting default value.");
            holder.txtDateDetail.setText("N/A");
        }
        if (holder.txtDescriptionDetail == null) {
            Log.e("ExpenseAdapter", "txtDescription is null, setting default value.");
            holder.txtDescriptionDetail.setText("Không có mô tả");
        }
        if (holder.txtAmountDetail == null) {
            Log.e("ExpenseAdapter", "txtAmount is null, setting default value.");
            holder.txtAmountDetail.setText("0 đ");
        }
        holder.txtAmountDetail.setText("Số tiền đã tiêu: "+ String.valueOf(expense.getAmount()));
        holder.txtDateDetail.setText("Ngày: " + expense.getDate());
        holder.txtDescriptionDetail.setText(expense.getDescription());


    }

    private void deleteExpense(int expenseId, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7046/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ExpensesApiService apiService = retrofit.create(ExpensesApiService.class);
        Call<Void> call = apiService.deleteExpense(expenseId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    historyList.remove(position);
                    notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ExpenseDetail", "Lỗi khi xóa: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmountDetail, txtDateDetail, txtDescriptionDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmountDetail = itemView.findViewById(R.id.txtAmountDetail);
            txtDateDetail = itemView.findViewById(R.id.txtDateDetal);
            txtDescriptionDetail = itemView.findViewById(R.id.txtDescriptionDetail);

        }
    }
}