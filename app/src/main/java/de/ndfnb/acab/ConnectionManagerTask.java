package de.ndfnb.acab;

import android.os.AsyncTask;


/**
 * This class handles the creation of the TCPClient and registers for progress (new data) from the server
 * Created by miles on 12/16/15.
 */
public class ConnectionManagerTask extends AsyncTask<String, String, TCPClient> {
    public AsyncResponse delegate = null;
    public TCPClient mTcpClient;
    private String host;
    private int port;


    public interface AsyncResponse {
        TCPClient processFinish(TCPClient output);
    }
    protected void onPreExecute() { }
    protected void onPostExecute(TCPClient result) {
        delegate.processFinish(result);
    }

    public ConnectionManagerTask(AsyncResponse delegate, String host, int port) {
        this.host = host;
        this.port = port;
        this.delegate = delegate;
    }

    /**
     * This function is basically an initializer and creates the TCPClient in a separate thread
     *
     * @param message Message to send upon connection to the server
     * @return
     */
    @Override
    protected TCPClient doInBackground(String... message) {

        //Create a TCPClient (the actual socket builder)
        mTcpClient = new TCPClient(host, port,
                new TCPClient.OnMessageReceived() {
                    @Override
                    //here the messageReceived method is implemented
                    public void messageReceived(String message) {
                        //this method calls the onProgressUpdate
                        publishProgress(message);
                    }
                }
        );
        mTcpClient.run();
        return mTcpClient;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}