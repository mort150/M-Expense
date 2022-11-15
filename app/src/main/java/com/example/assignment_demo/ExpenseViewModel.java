package com.example.assignment_demo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Database.Entity.Expense;

import java.util.List;

public class ExpenseViewModel extends ViewModel {
    private MutableLiveData<List<Expense>> expense = new MutableLiveData<>();
    private AppDB db;

    public void setDb(AppDB db) {
        this.db = db;
    }

    public LiveData<List<Expense>> getExpenseByTripId(int tripId){
        return db.expenseDAO().getByTripId(tripId);
    }
}
