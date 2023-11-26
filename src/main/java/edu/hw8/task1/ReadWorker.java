package edu.hw8.task1;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

class ReadWorker implements Callable<String> {

    private final static int BUFFER_SIZE = 32;
    private final SocketChannel clientChannel;

    ReadWorker(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public String call() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        clientChannel.read(buffer);
        return Utils.getSuitableQuotes(new String(buffer.array(), StandardCharsets.UTF_8));
    }

}
