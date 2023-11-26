package edu.hw8.task1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class WriteWorker implements Runnable {

    private final static long AWAIT_MILLIS = 5000;
    private final SocketChannel clientChannel;
    private final Future<String> response;

    WriteWorker(SocketChannel clientChannel, Future<String> response) {
        this.clientChannel = clientChannel;
        this.response = response;
    }

    @Override
    public void run() {
        try (clientChannel) {
            clientChannel.write(ByteBuffer.wrap(response.get().getBytes(StandardCharsets.UTF_8)));
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
