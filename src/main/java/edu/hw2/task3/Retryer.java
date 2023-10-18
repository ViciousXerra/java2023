package edu.hw2.task3;

import java.util.function.Consumer;

public class Retryer<T, R extends AutoCloseable> {

    private final T target;
    private final R resource;

    public Retryer(T target, R resource) {
        this.target = target;
        this.resource = resource;
    }

    public void proceed(Consumer<? super T> consumer, int maxAttempts) throws Exception {
        for (int i = 0; i < maxAttempts; i++) {
            try (resource) {
                consumer.accept(target);
                break;
            } catch (Exception e) {
                if (i == maxAttempts - 1) {
                    throw e;
                }
            }
        }
    }

}
