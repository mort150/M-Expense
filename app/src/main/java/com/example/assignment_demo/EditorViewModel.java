package com.example.assignment_demo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Database.Entity.Trip;

public class EditorViewModel extends ViewModel {
    private AppDB db;

    public AppDB getDb() {
        return db;
    }

    public void setDb(AppDB db) {
        this.db = db;
    }

}