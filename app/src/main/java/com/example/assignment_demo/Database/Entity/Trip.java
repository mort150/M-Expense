package com.example.assignment_demo.Database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Trip {
    @PrimaryKey(autoGenerate = true)
    private int tripId;
    private String name;
    private String destination;
    private String date;
    private boolean assessment;
    private String description;
    private double expectedCost;
    private int membersAmount;

    public Trip() {
    }

    public Trip(String name, String destination, String date, boolean assessment, String description, double expectedCost, int membersAmount) {
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.assessment = assessment;
        this.description = description;
        this.expectedCost = expectedCost;
        this.membersAmount = membersAmount;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAssessment() {
        return assessment;
    }

    public void setAssessment(boolean assessment) {
        this.assessment = assessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getExpectedCost() {
        return expectedCost;
    }

    public void setExpectedCost(double expectedCost) {
        this.expectedCost = expectedCost;
    }

    public int getMembersAmount() {
        return membersAmount;
    }

    public void setMembersAmount(int membersAmount) {
        this.membersAmount = membersAmount;
    }
}
