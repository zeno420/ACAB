package de.ndfnb.acab.connection;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//Thread bzw. Runnable zur Realisierung der Client-Anforderungen
class Handler implements Runnable {  //oder 'extends Thread'
    private final Socket client;
    private final ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientMessage;

    Handler(ServerSocket serverSocket, Socket client) { //Server/Client-Socket
        this.client = client;
        this.serverSocket = serverSocket;
    }

    public void run() {
        StringBuffer sb = new StringBuffer();
        try {
            // Create the message sender
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

            // Create the message receiver
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Listen for the messages sent by the server, stopClient breaks this loop
            clientMessage = in.readLine();
            if (clientMessage != null) {
                System.out.println(clientMessage);
                clientMessage = null;
            }


        } catch (IOException e) {
            System.out.println("IOException, Handler-run");
        } finally {
            out.println(sb);  //Rückgabe Ergebnis an den Client
            if (!client.isClosed()) {
                System.out.println("****** Handler:Client close");
                try {
                    client.close();
                } catch (IOException e) {
                }
            }
        }
    }  //Ende run
}

