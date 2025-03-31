package com.example.myapplication.ExpenseManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.res.IncomesResponse;

import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    private List<IncomesResponse> incomeList;
    private OnDeleteClickListener deleteClickListener;
    public interface OnDeleteClickListener {
        void onDeleteClick(int incomeId);
    }
    public IncomeAdapter(List<IncomesResponse> incomeList, OnDeleteClickListener listener) {
        this.incomeList = incomeList;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncomesResponse income = incomeList.get(position);
        holder.txtAmount.setText( "Thu nhập: " + String.format("%,.2f VND", income.getAmount()));
        holder.txtDate.setText("Ngày thêm: " + income.getCreatedAt());
        holder.imgDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(income.getIncomeId()); // Gửi ID khoản thu nhập lên Fragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount, txtDate;
        ImageView imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
