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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerNetworkPool implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    public ServerNetworkPool(ExecutorService pool,
                             ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.pool = pool;
    }

    public void run() {
        try {

            while (true) {
                Socket cs = serverSocket.accept();
                pool.execute(new ServerConnectionHandler(serverSocket, cs));
            }
        } catch (IOException ex) {
            System.out.println("--- Interrupt NetworkService-run");
        } finally {
            System.out.println("--- Ende NetworkService(pool.shutdown)");
            pool.shutdown();
            try {
                //warte maximal 4 Sekunden auf Beendigung aller Anforderungen
                pool.awaitTermination(4L, TimeUnit.SECONDS);
                if (!serverSocket.isClosed()) {
                    System.out.println("--- Ende NetworkService:ServerSocket close");
                    serverSocket.close();
                }
            } catch (IOException e) {
            } catch (InterruptedException ei) {
            }
        }
    }
}