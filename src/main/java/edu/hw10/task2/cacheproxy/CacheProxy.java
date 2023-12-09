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

public final class CacheProxy {

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

    private CacheProxy(Object target) {
        this.target = target;
    }

    public static <T> T getCacheProxyInstance(T proxyTo, Class<? extends T> c) {
        validate(proxyTo, c);
        CacheProxy proxy = new CacheProxy(proxyTo);
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(
            proxyTo.getClass().getClassLoader(),
            proxyTo.getClass().getInterfaces(),
            proxy.handler
        );
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

    private static <T> void validate(T proxyTo, Class<? extends T> c) {
        if (proxyTo == null || c == null) {
            throw new IllegalArgumentException("Arguments must be not null.");
        }
    }

}
