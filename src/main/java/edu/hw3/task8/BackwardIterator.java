package edu.hw3.task8;

import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class BackwardIterator<E> implements Iterator<E> {

    private final List<E> collection;
    private int index;

    public BackwardIterator(@NotNull List<E> collection) {
        this.collection = collection;
        index = collection.size();
    }

    @Override
    public boolean hasNext() {
        return index > 0;
    }

    @Override
    public E next() {
        return collection.get(--index);
    }
}
