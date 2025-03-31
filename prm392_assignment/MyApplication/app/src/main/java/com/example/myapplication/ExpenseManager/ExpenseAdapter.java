package com.example.myapplication.ExpenseManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.res.ExpensesHistoryResponse;
import com.example.myapplication.model.res.IncomesResponse;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>{
    private List<ExpensesHistoryResponse> expensesList;
    private OnDetailClickListener detailClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpensesHistoryResponse expense = expensesList.get(position);
        holder.txtItem.setText( "Khoản chi: " + expense.getCategoryName());
        holder.txtAmount.setText("Số tiền đã chi: " + String.format("%,.2f VND",expense.getTotalAmount()));
        holder.imgDetail.setOnClickListener(v -> {
            if (detailClickListener != null) {
                detailClickListener.onDetailClick(expense.getCategoryId()); // Gọi sự kiện khi click
            }
        });
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public interface OnDetailClickListener {
        void onDetailClick(int incomeId);
    }
    public ExpenseAdapter(List<ExpensesHistoryResponse> expensesList, OnDetailClickListener listener) {
        this.expensesList = expensesList;
        this.detailClickListener = listener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount, txtItem;
        ImageView imgDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtItem = itemView.findViewById(R.id.txtItem);
            imgDetail = itemView.findViewById(R.id.imgDetail);
        }
    }
}
