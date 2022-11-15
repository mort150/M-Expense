package com.example.assignment_demo.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment_demo.Database.Entity.Expense;
import com.example.assignment_demo.Database.Entity.Trip;

import java.util.List;

@Dao
public interface ExpenseDAO {
    @Insert
    public void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Query("SELECT * FROM expense WHERE tripId = :tripId")
    LiveData<List<Expense>> getByTripId(int tripId);

    @Query("DELETE FROM expense WHERE expenseId = :expenseId")
    void delete(int expenseId);

    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    LiveData<Expense> findExpenseById(int expenseId);
}
