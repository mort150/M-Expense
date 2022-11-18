package com.example.assignment_demo.Helper;

import com.example.assignment_demo.Database.Entity.Trip;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class Uploader {
    private String json_payload;
    private String userId;
    private final String url;

    public Uploader(String url) {
        this.url = url;
    }

    public String upload() throws IOException {
        return new HttpTools().postJson(new URL(url), this.json_payload, "jsonpayload");
    }

    public void setParameter(List<Trip> tripList) {
        StringBuilder infoList = new StringBuilder("[");
        for (Trip trip : tripList) {
            infoList.append("{" + "\"name\": " + "\"").append(trip.getName()).append("\"").append(", \"description\": ").append("\"").append(trip.getDescription()).append("\"").append("}");
            infoList.append(",");
        }

        infoList = new StringBuilder(infoList.substring(0, infoList.length() - 1));
        infoList.append("]");

        this.json_payload = "{\"userId\":\"" + userId + "\",\"detailList\":" + infoList + "}";

        List<AbstractMap.SimpleEntry<String, String>> parameter = new ArrayList<>();
        parameter.add(new AbstractMap.SimpleEntry<>("jsonpayload", this.json_payload));
    }

    public String getJson_payload() {
        return json_payload;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
