package de.ndfnb.acab.interfaces;

import org.json.JSONObject;

public interface AsyncAPIResponse {
    JSONObject processFinish(JSONObject output);
}