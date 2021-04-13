/*
    ACAB (All Chats Are Beautiful)
    Copyright (C) 2021  Zeno Berkhan, Nico Diefenbacher

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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
import de.ndfnb.acab.interfaces.AsyncAPIResponse;


public class APITasks extends AsyncTask<String, Void, JSONObject> {

    public AsyncAPIResponse delegate = null;

    public APITasks(AsyncAPIResponse delegate) {
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
        } else if (route == "get_ip") {
            try {
                result = this.getIPAddr();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            result = "no matching route";
        }
        JSONObject resultJSON = null;
        try {
            resultJSON = new JSONObject(result);
        } catch (JSONException e) {
            try {
                result  = "{ 'ip': " + result + "}";
                resultJSON = new JSONObject(result);

            } catch (Exception ex) {
                e.printStackTrace();
            }
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


        try (OutputStream os = con.getOutputStream()) {
            os.write(requestBody.toString().getBytes("UTF-8"));
        }
        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
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
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", username);
        requestBody.put("passwd", password);
        final String requestBodyString = requestBody.toString();


        try (OutputStream os = con.getOutputStream()) {
            os.write(requestBody.toString().getBytes("UTF-8"));
        }
        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
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


        try (OutputStream os = con.getOutputStream()) {
            os.write(requestBody.toString().getBytes("UTF-8"));
        }
        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
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

    private String getIPAddr() throws IOException {
        URL url = new URL("https://api.ipify.org/");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
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
            return result;
        } else {
            return null;
        }
    }
}