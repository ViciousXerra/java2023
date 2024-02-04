package edu.hw7;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task2Test {

    private static Object[][] provideTestMethodWithData() {
        return new Object[][] {
            {7, 5040},
            {4, 24},
            {0, 1},
            {1, 1},
            {9, 362880},
        };
    }

    @ParameterizedTest
    @DisplayName("Test Task2 method.")
    @MethodSource("provideTestMethodWithData")
    void testMethod(int input, long expected) {
        //When
        long actual = Task2.getFactorialWithParallelStream(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Test nums greater than 20.")
    @ValueSource(ints = {21, 22, Integer.MAX_VALUE})
    void testPositiveNumRestriction(int num) {
        assertThatThrownBy(() -> Task2.getFactorialWithParallelStream(num))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Method does not support numbers larger than 20.");
    }

    @ParameterizedTest
    @DisplayName("Test nums greater than 20.")
    @ValueSource(ints = {-1, -10, Integer.MIN_VALUE})
    void testNegativeNumRestriction(int num) {
        assertThatThrownBy(() -> Task2.getFactorialWithParallelStream(num))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Method does not support negative integers and gamma function for complex numbers.");
    }

}
