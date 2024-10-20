package com.example.mobilecomms;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class HttpRequestTask extends AsyncTask<String, Void, String> {

    byte[] payload = null;
    AsyncTaskCallback parentCallback = null;

    public HttpRequestTask(AsyncTaskCallback activity, byte[] paylod) {
        this.parentCallback = activity;
        this.payload = paylod;
    }

    public HttpRequestTask(AsyncTaskCallback activity) {
        parentCallback = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String server = params[0];
        String apiCall = params[1];
        String result = null;

        switch (apiCall) {
            case ApiEndPoints.POST_PUBLIC_KEY:
                result = postPublicKey(server, apiCall);
                break;
            case ApiEndPoints.SEND_MESSAGE:
                result = sendMessage(server, apiCall);
                break;
            case ApiEndPoints.GET_PUBLIC_KEY:
                result = getPublicKey(server, apiCall);
                break;
        }
        return result;
    }


    private String sendMessage(String server, String apiCall) {
        StringBuilder result = new StringBuilder();
        URL url = null;
        try {
            url = new URL(server + "/" + apiCall);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new AllowAllHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            out.write(this.payload);
            out.close();
            out.flush();

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            conn.disconnect();
            parentCallback.decryptMessage(result.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    private String postPublicKey(String server, String apiCall) {
        StringBuilder result = new StringBuilder();
        URL url = null;
        try {
            url = new URL(server + "/" + apiCall);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new AllowAllHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            out.write(this.payload);
            out.close();
            out.flush();

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    public void onPreExecute() {
        super.onPreExecute();
        try {
            // Create a TrustManager that allows self-signed certificates
            TrustManager[] trustAllCerts = new TrustManager[]{new SelfSignedTrustManager()};
            // Create an SSLContext that uses the TrustManager
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Set the SSLContext to the HttpsURLConnection
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null && !result.isEmpty()) {
            Log.d("TAG", "OnPostExecute result value: " + result);
            parentCallback.receivePublicKey(result);
        }
    }

    private String getPublicKey(String server, String apiCall) {
        StringBuilder result = new StringBuilder();
        try {
            // Ensure the URL is constructed correctly
            URL url = new URL(server + apiCall);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new AllowAllHostnameVerifier());
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            bufferedReader.close(); // Ensure resources are closed
            conn.disconnect();

        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO Exception: " + e.getMessage(), e);
        }
        return result.toString();
    }
}
