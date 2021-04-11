package de.ndfnb.acab.connection;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import de.ndfnb.acab.ACAB;
import de.ndfnb.acab.data.model.Message;

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
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            // Listen for the messages sent by the server, stopClient breaks this loop
            clientMessage = in.readLine();
            if (clientMessage != null) {
                this.handleIncomingMessage(clientMessage);
                System.out.println(clientMessage);
                clientMessage = null;
            }


        } catch (IOException e) {
            System.out.println("IOException, Handler-run");
        } finally {
            if (!client.isClosed()) {
                System.out.println("****** Handler:Client close");
                try {
                    client.close();
                } catch (IOException e) {
                }
            }
        }
    }  //Ende run

    void handleIncomingMessage(String clientMessage) {
        try{

        String[] res = clientMessage.split(":");

        String name = res[0];
        int secVisible = Integer.parseInt(res[1]);

        StringBuilder messageText = new StringBuilder();
        for(int i = 2; i < res.length; i++){
            messageText.append(res[i]);
        }

        Message message1 = new Message(messageText.toString(), secVisible);

        if (ACAB.getChatListsMap().containsKey(name)) {
            ACAB.getChatListsMap().get(name).add(message1);
        } else {
            //TODO msg von "unbekannt", wie wolllen wir das handlen
        }} catch (Exception e) {
            e.printStackTrace();
        }
    }
}

