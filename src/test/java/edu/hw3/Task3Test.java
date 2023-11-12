package edu.hw3;

import edu.hw3.task3.Task3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class Task3Test {

    private static Object[][] getListAndDictionary() {
        return new Object[][] {
            {
                Arrays.asList("apple", "tangerine", "null", "apple", "null"),
                Map.of(
                    "null", 2,
                    "apple", 2,
                    "tangerine", 1
                )
            },
            {
                Arrays.asList(120, 42, -13, 120),
                Map.of(
                    120, 2,
                    -13, 1,
                    42, 1
                )
            },
            {
                List.of(),
                Map.of()
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test \"getFrequencyDictionary\" method")
    @MethodSource("getListAndDictionary")
    void testFrequencyMethod(List<?> input, Map<?, Integer> expected) {
        //When
        Map<?, Integer> actual = Task3.getFrequencyDictionary(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }
}
