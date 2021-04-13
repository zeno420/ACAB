/*
    ACAB (All Chats Are Beautiful)
    Copyright (C) 2021  Zeno Berkhan, Nico Diefenbacher

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package de.ndfnb.acab.tasks;

import android.content.Context;
import android.os.AsyncTask;
import de.ndfnb.acab.connections.TCPClient;
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