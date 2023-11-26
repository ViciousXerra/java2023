package edu.hw8.task1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String CAUGHT_EXCEPTION_MESSAGE_TEMPLATE = "Caught exception: %s";
    private final static String EMPTY_RESPONSE = "";
    private final static int BUFFER_SIZE = 256;
    private final static int PORT_MAX_VALUE = 65535;
    private final int port;
    private final String message;
    private boolean isResponseReceived;

    public Client(int port, String message) {
        if (port <= 0 || port > PORT_MAX_VALUE) {
            throw new IllegalArgumentException("Port number must be between 0 and 65535.");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message string can't be null.");
        }
        LOGGER.info("Client initialization.");
        this.port = port;
        this.message = message;
    }

    public String getResponse() {
        LOGGER.info("Server requesting...");
        try (
            Selector selector = Selector.open();
            SocketChannel clientChannel = SocketChannel.open();
        ) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_CONNECT);
            clientChannel.connect(new InetSocketAddress(port));
            while (!isResponseReceived) {
                int keysAmount = selector.select();
                if (keysAmount == 0) {
                    LOGGER.info("Client shutdown.");
                    return EMPTY_RESPONSE;
                }
                Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
                while (keysIterator.hasNext()) {
                    SelectionKey key = keysIterator.next();
                    keysIterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isConnectable()) {
                        establishConnection(key, selector);
                    }
                    if (key.isWritable()) {
                        writeToClientChannel(key, selector);
                    }
                    if (key.isReadable()) {
                        return receiveResponse(key);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
        }
        return EMPTY_RESPONSE;
    }

    private void establishConnection(SelectionKey key, Selector selector) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
            LOGGER.info("Connected.");
        }
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    private void writeToClientChannel(SelectionKey key, Selector selector) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
        LOGGER.info("Message transferred.");
        channel.register(selector, SelectionKey.OP_READ);
    }

    private String receiveResponse(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        channel.read(buffer);
        isResponseReceived = true;
        return new String(buffer.array(), StandardCharsets.UTF_8).trim();
    }

}
