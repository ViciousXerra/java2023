package edu.hw6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DiskMap implements Map<String, String> {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String WRITE_FORMAT = "%s:%s" + System.lineSeparator();
    private final static String READ_MATCH = "(.+):(.+)";
    private final static Pattern READ_PATTERN = Pattern.compile(READ_MATCH);
    private final static int KEY_GROUP = 1;
    private final static int VALUE_GROUP = 2;

    private final static String PATH_DIRECTORY_VIOLATION_MESSAGE = "Path can't be a directory.";
    private final static String FILE_WRITE_ERROR_MESSAGE =
        "Caught I/O Exception. File can't be created, opened or redacted.";
    private final static String FILE_READ_ERROR_MESSAGE =
        "Caught I/O Exception. File can't be opened or be read.";
    private final static String INVALID_FILE_LINE =
        "File line must match %KEY%:%VALUE% pattern, %KEY% and %VALUE% can't be empty.";
    private final static String NULL_RESTRICTION_MESSAGE = "%s can't be null.";

    private final static int INITIAL_CAPACITY = 16;
    private final static float LOAD_FACTOR = 0.75f;
    private final static int DEFAULT_PRIME = 31;

    private int currentCapacity;
    private final float loadFactor;
    private long size;

    private Node[] table;

    public DiskMap() {
        this(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    public DiskMap(int capacity, float loadFactor) {
        this.currentCapacity = capacity;
        this.loadFactor = loadFactor;
        initTable();
    }

    public void saveMap(Path path) {
        validatePath(path);
        try (
            BufferedWriter fileWriter =
                Files.newBufferedWriter(
                    path,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
                )
        ) {
            for (Entry<String, String> entry : this.entrySet()) {
                fileWriter.write(String.format(WRITE_FORMAT, entry.getKey(), entry.getValue()));
            }
        } catch (IOException e) {
            LOGGER.error(FILE_WRITE_ERROR_MESSAGE);
        }
    }

    public void loadMap(Path path) {
        validatePath(path);
        try (
            BufferedReader fileReader = Files.newBufferedReader(path)
        ) {
            Matcher matcher;
            while (fileReader.ready()) {
                matcher = READ_PATTERN.matcher(fileReader.readLine());
                if (matcher.find()) {
                    this.put(matcher.group(KEY_GROUP), matcher.group(VALUE_GROUP));
                } else {
                    throw new IllegalArgumentException(INVALID_FILE_LINE);
                }
            }
        } catch (IOException e) {
            LOGGER.error(FILE_READ_ERROR_MESSAGE);
            clear();
        }
    }

    @Override
    public int size() {
        if (size >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        validate(MappingComponent.KEY, key);
        int bucketIndex = hash((String) key);
        Node currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (key.equals(currentNode.getKey())) {
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        validate(MappingComponent.VALUE, value);
        return getNodesStream().anyMatch(node -> value.equals(node.getValue()));
    }

    @Override
    public String get(Object key) {
        validate(MappingComponent.KEY, key);
        int bucketIndex = hash((String) key);
        Node currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (key.equals(currentNode.getKey())) {
                return currentNode.getValue();
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    @Nullable
    @Override
    public String put(String key, String value) {
        validate(MappingComponent.KEY, key);
        validate(MappingComponent.VALUE, value);
        if (isNeedToEnsureCapacity()) {
            ensureCapacity();
        }
        int bucketIndex = hash(key);
        Node currentNode = table[bucketIndex];
        if (currentNode == null) {
            table[bucketIndex] = new Node(key, value);
            size++;
            return null;
        } else if (key.equals(currentNode.getKey())) {
            return replaceValue(currentNode, value);
        } else {
            Node previousNode = currentNode;
            currentNode = currentNode.getNext();
            while (currentNode != null) {
                if (key.equals(currentNode.getKey())) {
                    return replaceValue(currentNode, value);
                }
                previousNode = currentNode;
                currentNode = currentNode.getNext();
            }
            previousNode.setNext(new Node(key, value));
            size++;
            return null;
        }
    }

    @Override
    public String remove(Object key) {
        validate(MappingComponent.KEY, key);
        int bucketIndex = hash((String) key);
        String toReturn = null;
        Node currentNode = table[bucketIndex];
        if (currentNode == null) {
            return null;
        } else if (key.equals(currentNode.getKey())) {
            toReturn = currentNode.getValue();
            table[bucketIndex] = currentNode.getNext();
            size--;
        } else {
            Node previousNode = currentNode;
            currentNode = currentNode.getNext();
            while (currentNode != null) {
                if (key.equals(currentNode.getKey())) {
                    toReturn = currentNode.getValue();
                    previousNode.setNext(currentNode.getNext());
                    size--;
                    return toReturn;
                }
                previousNode = currentNode;
                currentNode = currentNode.getNext();
            }
        }
        return toReturn;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        if (m == null) {
            throw new NullPointerException(String.format(NULL_RESTRICTION_MESSAGE, "Map argument"));
        }
        boolean nullsExists;
        try {
            nullsExists = m.containsKey(null) || m.containsValue(null);
        } catch (NullPointerException e) {
            m.forEach(this::put);
            return;
        }
        if (!nullsExists) {
            m.forEach(this::put);
        } else {
            throw new NullPointerException(String.format(NULL_RESTRICTION_MESSAGE, "Keys or values"));
        }
    }

    @Override
    public void clear() {
        currentCapacity = INITIAL_CAPACITY;
        initTable();
        size = 0;
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return getNodesStream().map(Node::getKey).collect(Collectors.toSet());
    }

    @NotNull
    @Override
    public Collection<String> values() {
        return getNodesStream().map(Node::getValue).toList();
    }

    @NotNull
    @Override
    public Set<Entry<String, String>> entrySet() {
        return getNodesStream().collect(Collectors.toSet());
    }

    private void initTable() {
        table = new Node[currentCapacity];
    }

    private int hash(String key) {
        int result = 1;
        result = result * DEFAULT_PRIME + key.hashCode();
        return Math.abs(result % currentCapacity);
    }

    private String replaceValue(Node currentNode, String value) {
        String toReturn = currentNode.getValue();
        currentNode.setValue(value);
        return toReturn;
    }

    private void ensureCapacity() {
        this.currentCapacity = this.currentCapacity << 1;
        rehash();
    }

    private boolean isNeedToEnsureCapacity() {
        float count = (float) Arrays.stream(table).filter(Objects::nonNull).count();
        float currentLoad = count / currentCapacity;
        return currentLoad >= loadFactor;
    }

    private void rehash() {
        Node[] tableCopy = this.table;
        initTable();
        getNodesStream(tableCopy)
            .forEach(node -> {
                int bucketIndex = hash(node.getKey());
                Node current = this.table[bucketIndex];
                if (current == null) {
                    this.table[bucketIndex] = node;
                } else {
                    Node previous = current;
                    current = current.getNext();
                    while (current != null) {
                        previous = current;
                        current = current.getNext();
                    }
                    previous.setNext(node);
                }
            });
    }

    private void validate(MappingComponent element, Object o) {
        if (o == null) {
            throw new NullPointerException(String.format(NULL_RESTRICTION_MESSAGE, element.getName()));
        }
    }

    private Stream<Node> getNodesStream() {
        return getNodesStream(this.table);
    }

    private Stream<Node> getNodesStream(Node[] table) {
        return Arrays.stream(table)
            .filter(Objects::nonNull)
            .flatMap(node -> {
                List<Node> result = new ArrayList<>();
                Node currentNode = node;
                while (currentNode != null) {
                    result.add(currentNode);
                    currentNode = currentNode.getNext();
                }
                return result.stream();
            });
    }

    private void validatePath(Path path) {
        if (path == null) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION_MESSAGE, "Path"));
        }
        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException(PATH_DIRECTORY_VIOLATION_MESSAGE);
        }
    }

    private static class Node implements Map.Entry<String, String> {

        private final String key;
        private String value;
        private Node next;

        private Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            if (!key.equals(node.key)) {
                return false;
            }
            return value.equals(node.value);
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String toReturn = this.value;
            this.value = value;
            return toReturn;
        }

        private void setNext(Node next) {
            this.next = next;
        }

        private Node getNext() {
            return next;
        }

    }

    private enum MappingComponent {
        KEY("Key"),
        VALUE("Value");

        private final String name;

        MappingComponent(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
