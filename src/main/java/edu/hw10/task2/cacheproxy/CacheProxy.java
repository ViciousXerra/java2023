package edu.hw10.task2.cacheproxy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CacheProxy<T> {

    private final Map<List<Object>, Object> previousRequests = new HashMap<>();
    private Object target;
    private final InvocationHandler handler =
        (proxy, method, args) -> {
            if (!method.isAnnotationPresent(Cache.class)) {
                return method.invoke(target, args);
            }
            boolean isPresent = previousRequests.containsKey(List.of(args));
            var res = previousRequests.computeIfAbsent(List.of(args), key -> {
                try {
                    return method.invoke(target, key.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Unable to invoke original target method.", e);
                }
            });
            if (!isPresent && method.getDeclaredAnnotation(Cache.class).persist()) {
                appendCacheFile(method, args, res);
            }
            return res;
        };

    private final T targetProxy;

    private CacheProxy(Object target) {
        this.target = target;
        //noinspection unchecked
        targetProxy = (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            handler
        );
    }

    public T getTargetProxy() {
        return targetProxy;
    }

    public Map<List<Object>, Object> getRuntimeCache() {
        return Map.copyOf(previousRequests);
    }

    public static <T> CacheProxy<T> getCacheProxyInstance(T target) {
        validate(target);
        return new CacheProxy<>(target);
    }

    private void appendCacheFile(Method method, Object[] args, Object res) throws IOException {
        Path filePath = Path.of(method.getDeclaredAnnotation(Cache.class).filePath());
        try (
            BufferedWriter writer = Files.newBufferedWriter(
                filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)
        ) {
            writer.write(Arrays.toString(args));
            writer.write(" : ");
            writer.write(res.toString());
            writer.write(System.lineSeparator());
        }
    }

    private static <T> void validate(T target) {
        if (target == null) {
            throw new IllegalArgumentException("Arguments must be not null.");
        }
    }

}
