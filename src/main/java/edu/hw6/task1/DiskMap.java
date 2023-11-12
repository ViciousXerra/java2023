package edu.hw6.task1;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiskMap implements Map<String, String> {

    private final static String NULL_RESTRICTION_MESSAGE = "%s can't be null.";
    private final static String CLASS_CAST_RESTRICTION_MESSAGE = "%s must be String";

    private final static int INITIAL_CAPACITY = 16;
    private final static float LOAD_FACTOR = 0.75f;

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

    }

    public void loadMap(Path path) {

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
        validate(MappingElement.KEY, key);
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
        validate(MappingElement.VALUE, value);
        return
            Arrays.stream(table)
                .filter(Objects::nonNull)
                .flatMap(node -> {
                    List<Node> result = new ArrayList<>();
                    Node currentNode = node;
                    while (currentNode != null) {
                        result.add(currentNode);
                        currentNode = currentNode.getNext();
                    }
                    return result.stream();
                })
                .anyMatch(node -> value.equals(node.getValue()));
    }

    @Override
    public String get(Object key) {
        validate(MappingElement.KEY, key);
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
        validate(MappingElement.KEY, key);
        validate(MappingElement.VALUE, value);
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
        validate(MappingElement.KEY, key);
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
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION_MESSAGE, "Map"));
        }
        if (m.containsKey(null) || m.containsValue(null)) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION_MESSAGE, "Key or value"));
        }
        m.forEach(this::put);
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
            })
            .map(Node::getKey)
            .collect(Collectors.toUnmodifiableSet());
    }

    @NotNull
    @Override
    public Collection<String> values() {
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
            })
            .map(Node::getValue)
            .toList();
    }

    @NotNull
    @Override
    public Set<Entry<String, String>> entrySet() {
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
            })
            .collect(Collectors.toUnmodifiableSet());
    }

    private void initTable() {
        table = new Node[currentCapacity];
    }

    private int hash(String key) {
        int result = 1;
        int prime = 31;
        result = result * prime + key.hashCode();
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
        List<Node> temporaryCopy =
            Arrays.stream(table).filter(Objects::nonNull)
                .flatMap(node -> {
                    List<Node> result = new ArrayList<>();
                    Node currentNode = node;
                    while (currentNode != null) {
                        result.add(currentNode);
                        currentNode = currentNode.getNext();
                    }
                    return result.stream();
                })
                .toList();
        initTable();
        temporaryCopy.forEach(node -> put(node.getKey(), node.getValue()));
    }

    private void validate(MappingElement element, Object o) {
        if (o == null) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION_MESSAGE, element.getName()));
        }
        if (!(o instanceof String)) {
            throw new ClassCastException(String.format(CLASS_CAST_RESTRICTION_MESSAGE, element.getName()));
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

    private enum MappingElement {
        KEY("Key"),
        VALUE("Value");

        private final String name;

        MappingElement(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
