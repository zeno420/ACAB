package de.ndfnb.acab.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.PrintWriter;


/**
 * A simple task for sending messages across the network.
 */
public class TCPMessageSendTask extends AsyncTask<String, Void, JSONObject> {
    private PrintWriter out;
    private String message;

    public AsyncTCPMessageResponse delegate = null;

    public interface AsyncTCPMessageResponse {
        JSONObject processFinish(JSONObject output);
    }



    protected void onPreExecute() {
    }

    protected void onPostExecute(JSONObject result) {
        delegate.processFinish(result);
    }

    public TCPMessageSendTask(AsyncTCPMessageResponse delegate, PrintWriter out, String message) {
        this.delegate = delegate;
        this.out = out;
        this.message = message;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        if (out != null && !out.checkError()) {
            try {
                out.println(message);
                out.flush();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}