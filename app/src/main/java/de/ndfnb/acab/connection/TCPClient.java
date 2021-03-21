package de.ndfnb.acab.connection;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import de.ndfnb.acab.R;
import de.ndfnb.acab.tasks.TCPMessageSendTask;
import de.ndfnb.acab.tasks.TCPMessageSendTask.AsyncTCPMessageResponse;


/**
 * TCPClient takes the TCP socket and allow to send and receive messages
 * This class is always called by ConnectionManagerTask
 */
public class TCPClient implements AsyncTCPMessageResponse {

    public SSLSocket socket = null;

    private String serverMessage;
    private String host;
    private int port;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private char[] keystorepass = "thanksmiles".toCharArray(); // If your keystore has a password, put it here

    private Context context;
    // These handle the I/O
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(String host, int port, OnMessageReceived listener, Context context) {
        this.host = host;
        this.port = port;
        this.context = context;
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server.
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) throws ExecutionException, InterruptedException {
        // As of Android 4.0 we have to send to network in another thread...
        JSONObject result = new TCPMessageSendTask(TCPClient.this, out, message).execute(message).get();
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

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) sslsocketfactory.createSocket(host, port);

            try {

                // Create the message sender
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                // Create the message receiver
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Listen for the messages sent by the server, stopClient breaks this loop
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }
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