package com.example.assignment_demo.Tools;

import android.util.Log;

import com.example.assignment_demo.Database.Entity.Trip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class Uploader {
    private String link;
    private List<AbstractMap.SimpleEntry<String, String>> param;
    private String json_payload;
    private String userId;

    public Uploader(String url){
        this.link = url;
    }

    public String upload() throws IOException {
       return new HttpTools().postJson(new URL(link),this.json_payload,"jsonpayload");
    }

    public String getUrl() {
        return link;
    }

    public void setUrl(String url) {
        this.link = url;
    }

    public List<AbstractMap.SimpleEntry<String, String>> getParam() {
        return param;
    }

    public void setParam(List<Trip> tripList) {
        String detailList = "[";
        for(Trip trip : tripList){
            detailList += "{" + "\"name\": " + "\"" + trip.getName() + "\"" + ", \"description\": " + "\"" + trip.getDescription() + "\"" +"}";
            detailList += ",";
        }
        detailList = detailList.substring(0,detailList.length() - 1);
        detailList += "]";
        this.json_payload = "{\"userId\":\"" + userId + "\",\"detailList\":" + detailList + "}";
        this.param = new ArrayList<>();
        param.add(new AbstractMap.SimpleEntry<String, String>("jsonpayload",this.json_payload));
    }

    public String getJson_payload() {
        return json_payload;
    }

    public void setJson_payload(String json_payload) {
        this.json_payload = json_payload;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
