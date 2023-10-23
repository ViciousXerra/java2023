package edu.hw3;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public final class Task5 {

    private final static int DEFAULT_NAME_WORDS_COUNT = 2;

    private final static Comparator<String> COMPARE_BY_FIRSTNAME = Comparator.naturalOrder();
    private final static Comparator<String> COMPARE_BY_LASTNAME = (name1, name2) -> {
        Optional<String> lastName1 = Arrays.stream(name1.split(" ")).reduce((x, y) -> y);
        Optional<String> lastName2 = Arrays.stream(name2.split(" ")).reduce((x, y) -> y);
    };

    private Task5() {

    }

    public static String[] sortContacts(String[] names, @NotNull String order) {
        if (names == null) {
            return new String[0];
        }


    }

}
