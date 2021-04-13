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

package de.ndfnb.acab.connections;

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