package de.ndfnb.acab.tasks;

import android.content.Context;
import android.os.AsyncTask;

import de.ndfnb.acab.connection.TCPClient;


/**
 * This class handles the creation of the TCPClient and registers for progress (new data) from the server
 */
public class ConnectionManagerTask extends AsyncTask<String, String, TCPClient> {
    public AsyncTCPClientResponse delegate = null;
    public TCPClient mTcpClient;
    private String host;
    private Context context;
    private int port;


    public interface AsyncTCPClientResponse {
        TCPClient processFinish(TCPClient output);
    }
    protected void onPreExecute() { }
    protected void onPostExecute(TCPClient result) {
        delegate.processFinish(result);
    }

    public ConnectionManagerTask(AsyncTCPClientResponse delegate, String host, int port, Context context) {
        this.host = host;
        this.port = port;
        this.context = context;
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
        mTcpClient = new TCPClient(this.host, this.port,
                new TCPClient.OnMessageReceived() {
                    @Override
                    //here the messageReceived method is implemented
                    public void messageReceived(String message) {
                        //this method calls the onProgressUpdate
                        publishProgress(message);
                    }
                }, this.context
        );
        mTcpClient.run();
        return mTcpClient;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}