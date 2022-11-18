package com.example.assignment_demo.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment_demo.Database.Entity.Trip;

import java.util.List;

@Dao
public interface TripDAO {

    @Query("SELECT * FROM trip")
    LiveData<List<Trip>> getAllData();

    @Query("SELECT * FROM trip")
    List<Trip> getAllDataWithoutLive();

    @Insert
    void insert(Trip trip);

    @Query("SELECT * FROM trip WHERE name LIKE :name")
    LiveData<List<Trip>> findTripByName(String name);

    @Query("SELECT * FROM trip WHERE tripId = :tripId")
    LiveData<Trip> findTripById(int tripId);

    @Update
    void update(Trip trip);

    @Query("DELETE FROM trip WHERE tripId = :tripId")
    void delete(int tripId);
}
