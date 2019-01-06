package com.example.parik.demomm;

import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class HttpCallActivity {

    //WebService Code
    private final String serverUrl = "http://172.26.34.230:80/BreakQueue/index.php";

    public HttpCallActivity(){
    }

    //Call The AsyncTask Class For Calling Http
    public String callServices(String data) {
        String responseData = "";
        try {
            responseData = new callServiceUrl().execute(data).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    //Used For Http Request-Response
    private class callServiceUrl extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String jsonResult = "";
            try {
                URL url = new URL(serverUrl);

                // Send POST data request
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(7000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(urls[0]);
                wr.flush();

                jsonResult = inputStreamToString(conn.getInputStream()).toString();
                return jsonResult;

            } catch (ClientProtocolException e) {
                e.getMessage();
                cancel(true);
            } catch (IOException e) {
                e.getMessage();
                cancel(true);
            }

            return jsonResult;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public StringBuilder inputStreamToString(InputStream content) {

        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(content));

        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }

    public int returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        int returnedResult = 0;

        try {

            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
}
