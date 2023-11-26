package edu.hw8.task1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

class Client {

    private final int port;
    private String response;

    public Client(int port) {
        this.port = port;
    }

    public void sendMessage(String message) {
        try (
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(port))
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
            socketChannel.read(buffer);
            response = new String(buffer.array()).trim();
        } catch (IOException e) {
            throw new RuntimeException("Unable to get response.", e);
        }
    }

    public String getMessage() {
        if (response == null) {
            throw new IllegalStateException("Unable to get response message before sending.");
        }
        return response;
    }

}
