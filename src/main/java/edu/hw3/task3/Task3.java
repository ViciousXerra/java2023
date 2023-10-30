package edu.hw3.task3;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Task3 {

    private Task3() {

    }

    public static <T> Map<T, Integer> getFrequencyDictionary(@NotNull List<T> list) {
        Map<T, Integer> map = new HashMap<>();
        int currentFrequency;
        for (T obj : list) {
            currentFrequency = map.getOrDefault(obj, 0);
            map.put(obj, ++currentFrequency);
        }
        return map;
    }

}
