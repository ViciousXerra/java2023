package edu.hw8.task1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private final static Logger LOGGER = LogManager.getLogger();
    private final static String CAUGHT_EXCEPTION_MESSAGE_TEMPLATE = "Caught exception: %s";
    private final static String NON_POSITIVE_RESTRICTION_MESSAGE = "%s must be a positive num.";
    private final static int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors() - 1;
    private final static int PORT_MAX_VALUE = 65535;
    private final int port;
    private final long timeout;
    private final Map<SocketChannel, Future<String>> responseMapping = new HashMap<>();
    private final int maxClients;
    private boolean isListening;
    private int currentClients;

    public Server(int port, long timeoutInMillis, int maxClients) {
        if (maxClients <= 0) {
            throw new IllegalArgumentException(String.format(NON_POSITIVE_RESTRICTION_MESSAGE, "Number of clients"));
        }
        if (port <= 0 || port > PORT_MAX_VALUE) {
            throw new IllegalArgumentException("Port number must be between 0 and 65535.");
        }
        if (timeoutInMillis <= 0) {
            throw new IllegalArgumentException(String.format(
                NON_POSITIVE_RESTRICTION_MESSAGE,
                "Timeout duration in milliseconds"
            ));
        }
        LOGGER.info("Server initialization.");
        this.port = port;
        this.timeout = timeoutInMillis;
        this.maxClients = maxClients;
        isListening = true;
    }

    public void listen() {
        LOGGER.info("Server listening...");
        try (
            Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS)
        ) {
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (isListening) {
                int keysAmount = selector.select(timeout);
                if (keysAmount == 0) {
                    isListening = false;
                    LOGGER.info("Server shutdown.");
                    service.shutdown();
                    while (!service.isTerminated()) {

                    }
                    continue;
                }
                Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
                while (keysIterator.hasNext()) {
                    SelectionKey key = keysIterator.next();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable() && currentClients < maxClients) {
                        acceptConnection(key, selector);
                        currentClients++;
                    }
                    if (key.isReadable()) {
                        readFromClientChannel(key, selector, service);
                    }
                    if (key.isWritable()) {
                        writeToClientChannel(key, service);
                        key.cancel();
                        currentClients--;
                    }
                    keysIterator.remove();
                }
            }
        } catch (IOException e) {
            LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
        }
    }

    private void acceptConnection(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void readFromClientChannel(SelectionKey key, Selector selector, ExecutorService service)
        throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        Future<String> stringFuture = service.submit(new ReadWorker(clientChannel));
        //associate this client channel with future response
        responseMapping.put(clientChannel, stringFuture);
        //Setting channel to write-ready mode
        clientChannel.register(selector, SelectionKey.OP_WRITE);
    }

    private void writeToClientChannel(SelectionKey key, ExecutorService service) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        //Retrieving non-null future string
        Future<String> stringFuture = responseMapping.remove(clientChannel);
        service.execute(new WriteWorker(clientChannel, stringFuture));
    }

}
