package edu.hw8.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors() - 1;
    private final static int AWAIT_MILLIS = 3000;

    private final static Pattern PATTERN = Pattern.compile("^([А-яЁё]+):([А-яЁё \",!.-]+)$");
    private final static int KEYWORD_GROUP = 1;
    private final static int QUOTE_GROUP = 2;
    private final static Map<String, Set<String>> KEYWORD_MAPPING = new HashMap<>() {
        {
            try (
                BufferedReader reader =
                    Files.newBufferedReader(Path.of("src/main/resources/hw8resources/quotes.txt"))
            ) {
                String line;
                Matcher matcher;
                while ((line = reader.readLine()) != null) {
                    matcher = PATTERN.matcher(line);
                    if (matcher.find()) {
                        Set<String> set = getOrDefault(matcher.group(KEYWORD_GROUP), new HashSet<>());
                        if (set.isEmpty()) {
                            put(matcher.group(KEYWORD_GROUP), set);
                        }
                        set.add(matcher.group(QUOTE_GROUP));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to fill keyword mapping object.", e);
            }
        }
    };

    private final int port;
    private boolean isRunning;

    public Server(int port) {
        this.port = port;
        isRunning = true;
        LOGGER.info("Starting server.");
    }

    public void listen() {
        try (
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS)
        ) {
            tuneServerSocketChannel(selector, serverSocketChannel, port);
            while (isRunning) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        registerConnection(selector, serverSocketChannel);
                    } else if (key.isReadable()) {
                        process(key, service);
                    }
                    keyIterator.remove();
                }
                Thread.sleep(AWAIT_MILLIS);
                isRunning = !selector.selectedKeys().isEmpty();
                if (!isRunning) {
                    LOGGER.info("Server shutdown. Awaiting of previous tasks completion.");
                    service.shutdown();
                    while (!service.isTerminated()) {

                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Server error occurred.", e);
        }
    }

    private static void tuneServerSocketChannel(Selector selector, ServerSocketChannel channel, int port)
        throws IOException {
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private static void registerConnection(Selector selector, ServerSocketChannel serverSocketChannel)
        throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private static void process(SelectionKey key, ExecutorService service) {
        service.execute(new Worker(key));
    }

    private static class Worker implements Runnable {

        private final static int BUFFER_SIZE = 32;
        private final SelectionKey key;

        private Worker(SelectionKey key) {
            this.key = key;
        }

        @Override
        public void run() {
            try (SocketChannel channel = (SocketChannel) key.channel()) {
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                channel.read(buffer);
                String keyword = new String(buffer.array()).trim();
                String responseString = getSuitableQuotes(keyword);
                channel.write(ByteBuffer.wrap(responseString.getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                throw new RuntimeException("Unable to process.", e);
            }
        }

        private static String getSuitableQuotes(String keyword) {
            StringBuilder builder = new StringBuilder();
            KEYWORD_MAPPING.getOrDefault(keyword, Set.of("There is no existing suitable quotes."))
                .forEach(quote -> {
                    builder.append(quote);
                    builder.append(System.lineSeparator());
                });
            return builder.toString();
        }

    }

}
