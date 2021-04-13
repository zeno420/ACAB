package de.ndfnb.acab.interfaces;


import org.json.JSONObject;

interface AsyncTCPMessageResponse {
    JSONObject processFinish(JSONObject output);
}