package com.example.myapplication.ExpenseManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.res.Top5Income;

import java.util.List;

public class TopIncomeAdapter  extends RecyclerView.Adapter<TopIncomeAdapter.ViewHolder> {
    private final List<Top5Income> incomeList;

    public TopIncomeAdapter(List<Top5Income> incomeList) {
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income_top, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Top5Income income = incomeList.get(position);
        holder.txtAmount.setText(String.format("Thu nhập: %,.2f VND", income.getTotalIncome()));
        holder.txtDate.setText("Tháng: " + String.valueOf(income.getMonth()));
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount,txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
