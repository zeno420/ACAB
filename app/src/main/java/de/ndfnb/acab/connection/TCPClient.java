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
    //public SSLSocket socket = null;
    private String serverMessage;
    private String host;
    private int port;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private char[] keystorepass = "thanksmiles".toCharArray(); // If your keystore has a password, put it here

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
            //create a socket to make the connection with the server
            KeyStore ks = KeyStore.getInstance("BKS");
            // Load the keystore file
            //TODO Key Store muss angelegt werden https://docs.wso2.com/display/EMM200/Generating+a+BKS+File+for+Android
            //InputStream keyin = context.getResources().openRawResource(R.raw.client_finished);
            //ks.load(keyin, keystorepass);
            //TODO SSL Socket to fix
            //SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            //socket = (SSLSocket) sslsocketfactory.createSocket(host, port);
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