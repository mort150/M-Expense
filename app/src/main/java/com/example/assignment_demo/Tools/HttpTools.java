package com.example.assignment_demo.Tools;

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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HttpTools {

    private static final int CONN_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 30000;
    private static final Logger logger = Logger.getLogger(HttpTools.class.getName());
    private HashMap<String, String> headers = new HashMap<>();

    // START REQUIRE IMPROVEMENTS
    private CookieManager cookieManager;

    public HttpTools(){
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    private long lastResponseTime = Long.MIN_VALUE;
    private boolean autoRedirectEnabled = true;
    // END REQUIRE IMPROVEMENTS

    static {
        if (!Boolean.getBoolean("sun.net.http.errorstream.enableBuffering")) {
            logger.config("System property \"sun.net.http.errorstream.enableBuffering\" is not set to true, this will cause issues");
        }
    }

    private boolean throwExceptions;

    public String post(URL url, List<AbstractMap.SimpleEntry<String, String>> params) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(CONN_TIMEOUT);
        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        applyHeaders(urlConnection);

        StringBuilder sb = new StringBuilder();
        for (AbstractMap.SimpleEntry<String, String> pair : params) {
            if (sb.length() > 0)
                sb.append('&');

            sb.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        writeContent(urlConnection, sb.toString());
        return executeAndGetResponse(urlConnection);
    }

    public String postJson(URL url, String jsonData, String arg) throws IOException {
        List<AbstractMap.SimpleEntry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry<>(arg, jsonData));
        return post(url, params);
    }

    private void writeContent(HttpURLConnection urlConnection, String data) throws IOException {
        urlConnection.setInstanceFollowRedirects( autoRedirectEnabled );
        logger.finest(data);
        byte[] bytes = data.getBytes("UTF-8");
        urlConnection.setDoOutput(true);
        urlConnection.setFixedLengthStreamingMode(bytes.length);
        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
        out.write(bytes);
        out.flush();
        out.close();
    }

    protected String executeAndGetResponse(HttpURLConnection request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        execute(request, baos);
        baos.flush();
        baos.close();
        String ret = baos.toString("UTF-8");
        logger.finest(ret);
        return ret;
    }

    protected void execute(HttpURLConnection connection, OutputStream stream) throws IOException {
        logger.finest(connection.getRequestMethod() + " " + connection.getURL() + " " + connection.getResponseCode()
                + " " + connection.getResponseMessage());
        connection.setInstanceFollowRedirects( autoRedirectEnabled );
        int status = connection.getResponseCode();

        if (throwExceptions && status >= 400) {
            throw new IOException(
                    "Server has responded with " + status + " " + connection.getResponseMessage());
        }

        InputStream input;
        if (status >= 200 && status <= 299) {
            input = connection.getInputStream();
        } else {
            input = connection.getErrorStream();
        }
        if (input == null) return;

        if (stream != null) {
            byte[] buffer = new byte[512];
            int byteLetti;
            while ((byteLetti = input.read(buffer)) > 0) {
                stream.write(buffer, 0, byteLetti);
            }
        }
        input.close();
        lastResponseTime = System.currentTimeMillis();
    }

    protected void applyHeaders(HttpURLConnection request) {
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
