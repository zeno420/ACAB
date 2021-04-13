package de.ndfnb.acab.interfaces;

import de.ndfnb.acab.connections.TCPClient;

public interface AsyncTCPClientResponse {
    TCPClient processFinish(TCPClient output);
}