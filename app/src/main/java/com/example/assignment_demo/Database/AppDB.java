package com.example.assignment_demo.Database;

import androidx.room.RoomDatabase;

import com.example.assignment_demo.Database.DAO.ExpenseDAO;
import com.example.assignment_demo.Database.DAO.TripDAO;
import com.example.assignment_demo.Database.Entity.Expense;
import com.example.assignment_demo.Database.Entity.Trip;

@androidx.room.Database(entities = {Trip.class, Expense.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract TripDAO tripDAO();
    public abstract ExpenseDAO expenseDAO();
}
