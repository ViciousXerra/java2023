package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task2Test {

    @ParameterizedTest
    @DisplayName("Passing positive integers and zero")
    @CsvSource({
        "1, 1",
        "156, 3",
        "99, 2",
        "0, 1"
    })
    void testWithPosNumsAndZero(int input, int expected) {
        assertThat(Task2.countDigits(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Passing negative integers")
    @CsvSource({
        "-21, 2",
        "-3275, 4",
        "-1, 1",
        "-147, 3"
    })
    void testWithNegativeNums(int input, int expected) {
        assertThat(Task2.countDigits(input)).isEqualTo(expected);
    }
}
