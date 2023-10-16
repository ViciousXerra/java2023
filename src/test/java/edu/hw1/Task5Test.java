package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task5Test {

    private static int[] provideArgsForTrue() {
        return new int[] {9, 525343, 12312, 40212311, -363};
    }

    @ParameterizedTest
    @MethodSource("provideArgsForTrue")
    @DisplayName("Testing with arguments for \"true\"")
    void testForTrue(int input) {
        assertThat(Task5.isPalindromeDescendant(input)).isEqualTo(true);
    }

    private static int[] provideArgsForFalse() {
        return new int[] {3213, 542607, -12317, 205000, -10};
    }

    @ParameterizedTest
    @MethodSource("provideArgsForFalse")
    @DisplayName("Testing with arguments for \"false\"")
    void testForFalse(int input) {
        assertThat(Task5.isPalindromeDescendant(input)).isEqualTo(false);
    }
}
