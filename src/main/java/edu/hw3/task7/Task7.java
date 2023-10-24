package edu.hw3.task7;

import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

public final class Task7 {

    private Task7() {

    }

    public static <K extends Comparable<K>> Comparator<K> getNullableTreeMapComparator(@NotNull Comparator<K> comparator) {
        return Comparator.nullsFirst(comparator);
    }

}
