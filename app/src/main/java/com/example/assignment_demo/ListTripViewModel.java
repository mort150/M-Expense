package com.example.assignment_demo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment_demo.Database.AppDB;
import com.example.assignment_demo.Database.Entity.Trip;

import java.util.List;

public class ListTripViewModel extends ViewModel {
    MutableLiveData<List<Trip>> trips = new MutableLiveData<>();
    private AppDB db;

    public LiveData<List<Trip>> getListTrips() {
        LiveData<List<Trip>> listTrip = db.tripDAO().getAllData();
        listTrip.observeForever(tripData -> {
            trips.setValue(tripData);
        });
        return trips;
    }

    public AppDB getDb() {
        return db;
    }

    public void setDb(AppDB db) {
        this.db = db;
    }

    public LiveData<List<Trip>> searchTripByName(String tripName) {
        if (tripName.isEmpty()) {
            return db.tripDAO().getAllData();
        }

        return db.tripDAO().findTripByName(tripName);
    }
}
