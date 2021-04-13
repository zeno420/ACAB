package de.ndfnb.acab.interfaces;

import org.json.JSONObject;

import de.ndfnb.acab.connection.TCPClient;

public interface AsyncTCPClientResponse {
    TCPClient processFinish(TCPClient output);
}