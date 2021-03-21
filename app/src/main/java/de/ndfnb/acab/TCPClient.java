package de.ndfnb.acab;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import de.ndfnb.acab.MainActivity;
import de.ndfnb.acab.R;
import de.ndfnb.acab.APITasks.AsyncResponse;
public class TCPClient  {

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
    public TCPClient(String host, int port, OnMessageReceived listener) {
        this.host = host;
        this.port = port;
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server.
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        // As of Android 4.0 we have to send to network in another thread...
        TCPMessageSendTask sender = new TCPMessageSendTask(out, message);
        sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            InputStream keyin = context.getResources().openRawResource(R.raw.client_finished);
            ks.load(keyin, keystorepass);

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

    // Declare the interface. The method messageReceived(String message) will must be implemented
    // in the implementing class
    public interface OnMessageReceived {
        void messageReceived(String message);
    }

    /**
     * A simple task for sending messages across the network.
     */
    public static class TCPMessageSendTask extends AsyncTask<String, Void, JSONObject> {
        private PrintWriter out;
        private String message;

        public AsyncResponse delegate = null;

        public interface AsyncResponse {
            JSONObject processFinish(JSONObject output);
        }


        protected void onPreExecute() {
        }

        protected void onPostExecute(JSONObject result) {
            delegate.processFinish(result);
        }

        public TCPMessageSendTask(AsyncResponse delegate, PrintWriter out, String message) {
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
}