package de.ndfnb.acab.connection;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import de.ndfnb.acab.interfaces.AsyncTCPMessageResponse;


public class TCPClient implements AsyncTCPMessageResponse {

    private String message;
    private String host;
    private int port;
    private boolean mRun = false;
    private PrintWriter out;
    private BufferedReader in;



    public TCPClient(String host, String port, String message) {
        this.message = message;
        this.host = host;
        this.port = Integer.parseInt(port);
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
        return output;
    }



}