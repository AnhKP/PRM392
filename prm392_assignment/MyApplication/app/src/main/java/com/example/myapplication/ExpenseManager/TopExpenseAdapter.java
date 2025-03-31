package com.example.myapplication.ExpenseManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.res.ExpensesHistoryResponse;
import com.example.myapplication.model.res.Top5Income;

import java.util.List;

public class TopExpenseAdapter extends RecyclerView.Adapter<TopExpenseAdapter.ViewHolder> {
    private List<ExpensesHistoryResponse> expensesList;

    public TopExpenseAdapter(List<ExpensesHistoryResponse> expensesList) {
        this.expensesList = expensesList;
    }
    @NonNull
    @Override
    public TopExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense_top, parent, false);
        return new TopExpenseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopExpenseAdapter.ViewHolder holder, int position) {
        ExpensesHistoryResponse expense = expensesList.get(position);
        holder.txtItem.setText( "Khoản chi: " + expense.getCategoryName());
        holder.txtAmount.setText("Số tiền đã chi: " + String.format("%,d VND", (int) expense.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount, txtItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtItem = itemView.findViewById(R.id.txtItem);
        }
    }
}
