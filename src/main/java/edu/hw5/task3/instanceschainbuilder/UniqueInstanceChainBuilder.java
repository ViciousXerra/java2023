package edu.hw5.task3.instanceschainbuilder;

import java.util.HashSet;
import java.util.Set;

public abstract class UniqueInstanceChainBuilder<T> extends InstanceChainBuilder<T> {

    protected final Set<T> instances = new HashSet<>();

    protected final boolean isUnique(T instance) {
        return !instances.contains(instance);
    }

}
