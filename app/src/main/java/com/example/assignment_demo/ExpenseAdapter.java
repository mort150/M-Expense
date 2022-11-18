package com.example.assignment_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_demo.Database.Entity.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    public interface OnClickListener {
        public void OnClick(View view, int position);
    }

    public ExpenseAdapter(List<Expense> expense) {
        this.expense = expense;
    }

    private List<Expense> expense;

    public List<Expense> getExpense() {
        return expense;
    }

    public void setExpense(List<Expense> expense) {
        this.expense = expense;
    }

    public ExpenseAdapter.OnClickListener onClickListener;

    public void setOnClickListener(ExpenseAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView expenseType;
        public TextView expenseCost;
        public FloatingActionButton expenseItemFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseType = itemView.findViewById(R.id.expenseType);
            expenseCost = itemView.findViewById(R.id.expenseCost);
            expenseItemFab = itemView.findViewById(R.id.expenseItemFab);
            itemView.setOnClickListener(this);
            expenseItemFab.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.OnClick(view, getAbsoluteAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_expense, parent, false);
        return new ExpenseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        holder.expenseType.setText(expense.get(position).getExpenseName());
        holder.expenseCost.setText("Cost: " + expense.get(position).getAmount() + " USD");
    }

    @Override
    public int getItemCount() {
        return expense.size();
    }
}
