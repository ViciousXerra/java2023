package edu.hw10.task2.cacheproxy;

import java.io.BufferedWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CacheProxy {

    private final Map<Object[], Object> previousRequests = new HashMap<>();
    private Object target;
    private final InvocationHandler handler =
        (proxy, method, args) -> {
            if (!method.isAnnotationPresent(Cache.class)) {
                return method.invoke(target, args);
            }
            var res = previousRequests.computeIfAbsent(args, key -> {
                try {
                    return method.invoke(target, key);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Unable to invoke original target method.", e);
                }
            });
            Path filePath = Path.of(method.getDeclaredAnnotation(Cache.class).filePath());
            if (method.getDeclaredAnnotation(Cache.class).persist()) {
                try (
                    BufferedWriter writer = Files.newBufferedWriter(
                        filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)
                ) {
                    for (Map.Entry<Object[], Object> entry : previousRequests.entrySet()) {
                        writer.write(Arrays.toString(entry.getKey()));
                        writer.write(" : ");
                        writer.write(entry.getValue().toString());
                        writer.write(System.lineSeparator());
                    }
                }
            }
            return res;
        };

    private CacheProxy(Object target) {
        this.target = target;
    }

    public static <T> T getCacheProxyInstance(T proxyTo, Class<? extends T> c) {
        try {
            validate(proxyTo, c);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        CacheProxy proxy = new CacheProxy(proxyTo);
        return (T) Proxy.newProxyInstance(c.getClassLoader(), c.getInterfaces(), proxy.handler);
    }

    private static <T> void validate(T proxyTo, Class<? extends T> c) throws IllegalAccessException {
        if (proxyTo == null || c == null) {
            throw new IllegalArgumentException("Arguments must be not null.");
        }
        if (proxyTo.getClass() != c) {
            throw new IllegalAccessException("Unsafe type cast.");
        }
        long count = Arrays.stream(c.getMethods()).filter(method -> method.isAnnotationPresent(Cache.class)).count();
        if (!c.isInterface() || count == 0) {
            throw new IllegalArgumentException(
                "Class instance must be parameterized with interface type, " +
                "interface must have method annotated with @Cache."
            );
        }
        if (count != 1) {
            throw new IllegalStateException(
                "Cache proxy currently supports interfaces only with 1 method annotated with @Cache."
            );
        }
    }

}
