package edu.hw8.task1;

import java.io.IOException;
import java.net.ServerSocket;

class Utils {

    public static int getFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

}
