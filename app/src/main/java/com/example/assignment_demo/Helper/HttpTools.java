package com.example.assignment_demo.Helper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HttpTools {

    private boolean throwExceptions;
    private static final Logger logger = Logger.getLogger(HttpTools.class.getName());
    private final HashMap<String, String> hashMapHeaders = new HashMap<>();

    private final boolean autoRedirectEnabled = true;

    static {
        if (!Boolean.getBoolean("sun.net.http.errorstream.enableBuffering")) {
            logger.config("System property \"sun.net.http.errorstream.enableBuffering\" is not set to true, this will cause issues");
        }
    }

    public HttpTools() {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    protected void execute(HttpURLConnection connection, OutputStream stream) throws IOException {
        logger.finest(connection.getRequestMethod() + " " + connection.getURL() + " " + connection.getResponseCode()
                + " " + connection.getResponseMessage());
        connection.setInstanceFollowRedirects(autoRedirectEnabled);
        int status = connection.getResponseCode();

        if (throwExceptions && status >= 400) {
            throw new IOException(
                    "Response from server: " + status + " " + connection.getResponseMessage());
        }

        InputStream input;
        if (status >= 200 && status <= 299) {
            input = connection.getInputStream();
        } else {
            input = connection.getErrorStream();
        }
        if (input == null) return;

        if (stream != null) {
            byte[] bytes = new byte[512];
            int i;
            while ((i = input.read(bytes)) > 0) {
                stream.write(bytes, 0, i);
            }
        }
        input.close();
        long lastResponseTime = System.currentTimeMillis();
    }

    public String post(URL url, List<AbstractMap.SimpleEntry<String, String>> params) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        applyHeaders(urlConnection);

        StringBuilder stringBuilder = new StringBuilder();
        for (AbstractMap.SimpleEntry<String, String> pair : params) {
            if (stringBuilder.length() > 0)
                stringBuilder.append('&');

            stringBuilder.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        writeContent(urlConnection, stringBuilder.toString());
        return executeAndGetResponse(urlConnection);
    }

    private void writeContent(HttpURLConnection urlConnection, String data) throws IOException {
        urlConnection.setInstanceFollowRedirects(autoRedirectEnabled);
        logger.finest(data);
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        urlConnection.setDoOutput(true);
        urlConnection.setFixedLengthStreamingMode(bytes.length);
        OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    public String postJson(URL url, String jsonData, String argument) throws IOException {
        List<AbstractMap.SimpleEntry<String, String>> parameters = new ArrayList<>();
        parameters.add(new AbstractMap.SimpleEntry<>(argument, jsonData));
        return post(url, parameters);
    }

    protected String executeAndGetResponse(HttpURLConnection request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        execute(request, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        String ret = byteArrayOutputStream.toString("UTF-8");
        logger.finest(ret);
        return ret;
    }

    protected void applyHeaders(HttpURLConnection request) {
        if (!hashMapHeaders.isEmpty()) {
            for (Map.Entry<String, String> entry : hashMapHeaders.entrySet()) {
                request.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
