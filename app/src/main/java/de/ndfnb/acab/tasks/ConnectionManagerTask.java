package de.ndfnb.acab.tasks;

import android.content.Context;
import android.os.AsyncTask;
import de.ndfnb.acab.connection.TCPClient;
import de.ndfnb.acab.interfaces.AsyncTCPClientResponse;


/**
 * This class handles the creation of the TCPClient and registers for progress (new data) from the server
 */
public class ConnectionManagerTask extends AsyncTask<String, String, TCPClient> {
    public AsyncTCPClientResponse delegate = null;
    public TCPClient mTcpClient;
    private Context context;

    public ConnectionManagerTask(AsyncTCPClientResponse delegate, Context context) {
        this.context = context;
        this.delegate = delegate;
    }

    protected void onPreExecute() { }

    protected void onPostExecute(TCPClient result) {
        delegate.processFinish(result);
    }

    protected TCPClient doInBackground(String... params) {
        //Create a TCPClient (the actual socket builder)
        mTcpClient = new TCPClient(params[0], params[1], params[2]);
        mTcpClient.run();
        return mTcpClient;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}