package de.ndfnb.acab.tasks;

import android.os.AsyncTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;


public class APITasks extends AsyncTask<String, Void, JSONObject> {

    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        JSONObject processFinish(JSONObject output);
    }

    public APITasks(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected void onPreExecute() {
    }

    protected void onPostExecute(JSONObject result) {
        delegate.processFinish(result);
    }

    protected JSONObject doInBackground(String... params) {
        String route = params[0];
        String[] args = Arrays.copyOfRange(params, 1, params.length);
        String result = "";

        if (route == "auth_signin") {
            try {
                result = this.signIn(args[0], args[1]);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        } else if (route == "get_route") {
            try {

                result = this.getRouteByName(args[0], args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (route == "update_route") {
            try {
                result = this.updateRoute(args[0], args[1]);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else if (route == "create_user") {
            try {
                result = this.createUser(args[0], args[1], args[2]);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            result = "no matching route";
        }
        JSONObject resultJSON = null;
        try {
            resultJSON = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJSON;
    }

    private String updateRoute(String jwtToken, String route) throws IOException, JSONException {
        URL url = new URL("https://h2896907.stratoserver.net/registry");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("x-access-token", jwtToken);
        con.setDoOutput(true);
        String result = "";
        JSONObject requestBody = new JSONObject();
        requestBody.put("route", route);
        final String requestBodyString = requestBody.toString();


        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
            try (OutputStream os = con.getOutputStream()) {
                os.write(requestBody.toString().getBytes("UTF-8"));
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                result = response.toString();

                return result;
            }
        } else {
            return null;
        }
    }


    private String getRouteByName(String jwtToken, String name) throws IOException {
        URL url = new URL("https://h2896907.stratoserver.net/registry?name=" + name);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("x-access-token", jwtToken);
        String result = "";

        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            result = response.toString();
            // print result
            System.out.println(response.toString());
            return result;
        } else {
            return null;
        }


    }


    public String signIn(String username, String password) throws JSONException, IOException {
        URL url = new URL("https://h2896907.stratoserver.net/auth/signin");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", username);
        requestBody.put("passwd", password);
        final String requestBodyString = requestBody.toString();

        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
            try (OutputStream os = con.getOutputStream()) {
                os.write(requestBody.toString().getBytes("UTF-8"));
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());


                return response.toString();
            }
        } else {
            return null;
        }

    }


    public String createUser(String jwtToken, String username, String password) throws JSONException, IOException {
        URL url = new URL("https://h2896907.stratoserver.net/registry");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("x-access-token", jwtToken);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", username);
        requestBody.put("passwd", password);
        final String requestBodyString = requestBody.toString();

        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
            try (OutputStream os = con.getOutputStream()) {
                os.write(requestBody.toString().getBytes("UTF-8"));
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());


                return response.toString();
            }
        } else {
            return null;
        }
    }


}

