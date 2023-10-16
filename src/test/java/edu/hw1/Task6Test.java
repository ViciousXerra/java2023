package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task6Test {

    private static Object[][] provideArgsAndOutputs() {
        return new Object[][] {
            {6621, 5},
            {6554, 4},
            {1234, 3},
            {3524, 3},
            {-3214, 3},
            {1112, 5},
            {6174, 1}
        };
    }

    @ParameterizedTest
    @MethodSource("provideArgsAndOutputs")
    @DisplayName("Testing with legal arguments")
    void testForTrue(int input, int expected) {
        assertThat(Task6.countStepsForKarpekarRoutine(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Testing with illegal arguments")
    @ValueSource(ints = {-945, 1000, 4444})
    void testForThrows(int input) {
        assertThatThrownBy(() -> Task6.countStepsForKarpekarRoutine(input)).isInstanceOf(IllegalArgumentException.class);
    }
}
