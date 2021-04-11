package de.ndfnb.acab.connection;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import de.ndfnb.acab.tasks.TCPMessageSendTask.AsyncTCPMessageResponse;

public class TCPClient implements AsyncTCPMessageResponse {

    private String message;
    private String serverMessage;
    private String host;
    private int port;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    private Context context;

    private PrintWriter out;
    private BufferedReader in;

    public TCPClient(String host, int port, OnMessageReceived listener, Context context) {
        this.host = host;
        this.port = port;
        this.context = context;
        mMessageListener = listener;
    }

    public TCPClient(String host, String port, String message) {
        this.message = message;
        this.host = host;
        this.port = Integer.valueOf(port);
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            Socket socket = new Socket(host, port);
            try {

                // Create the message sender
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                // Create the message receiver
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.print(message);
                out.flush();

            } catch (Exception e) {
                System.out.println("Server Error: " + e);
            } finally {
                // Close the socket after stopClient is called
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    @Override
    public JSONObject processFinish(JSONObject output) {
        return null;
    }

    // Declare the interface. The method messageReceived(String message) will must be implemented
    // in the implementing class
    public interface OnMessageReceived {
        void messageReceived(String message);
    }


}