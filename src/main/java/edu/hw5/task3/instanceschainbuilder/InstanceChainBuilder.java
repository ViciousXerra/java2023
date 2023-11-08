package edu.hw5.task3.instanceschainbuilder;

public abstract class InstanceChainBuilder <T> {

    protected T lastInstance;

    public final T getLastInstance() {
        if (lastInstance == null) {
            throw new ZeroLengthChainException();
        }
        return lastInstance;
    }

    protected abstract InstanceChainBuilder<T> apply(T instance);

}
