package de.ndfnb.acab;

import android.os.AsyncTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import  java.nio.charset.Charset;
import javax.net.ssl.HttpsURLConnection;

public class LoginAsyncTask extends AsyncTask<String, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public LoginAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        String result = "";
        try {
            result = this.login(username, password);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }


    public String login(String username, String password) throws JSONException, IOException {
        URL url = new URL("https://h2896907.stratoserver.net/auth/signin");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", username);
        requestBody.put("passwd", password);
        final String requestBodyString = requestBody.toString();



        try (OutputStream os = con.getOutputStream()) {
            os.write(requestBody.toString().getBytes("UTF-8"));
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());


            return response.toString();
        }

    }


}
