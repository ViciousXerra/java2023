package edu.hw8.task1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class WriteWorker implements Runnable {

    private final SocketChannel clientChannel;
    private final Future<String> response;

    WriteWorker(SocketChannel clientChannel, Future<String> response) {
        this.clientChannel = clientChannel;
        this.response = response;
    }

    @Override
    public void run() {
        try {
            String response1 = response.get();
            clientChannel.write(ByteBuffer.wrap(response1.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Unable to write in client socket channel", e);
        }
    }

}
