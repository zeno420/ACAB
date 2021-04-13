package de.ndfnb.acab.interfaces;


import org.json.JSONObject;

public interface AsyncTCPMessageResponse {
    JSONObject processFinish(JSONObject output);
}