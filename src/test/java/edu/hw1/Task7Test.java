package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task7Test {

    private static Object[][] provideLegalArgsWithOutputForRightShift() {
        return new Object[][] {
            {15, 2, 15},  //1111 -> 1111
            {13, 6, 7},   //1101 -> 0111
            {13, 64, 13}, //1101 -> 1101
            {0, 8, 0},    //0 -> 0
            {-1, 7, -1},  //FFFFFFFF -> FFFFFFFF
            {1, 0, 1},
            {-1610612736, 2, 671088640},               //A0000000 -> 28000000
            {Integer.MAX_VALUE, 3, Integer.MAX_VALUE}, // 7FFFFFFF -> 7FFFFFFF
            {Integer.MIN_VALUE, 5, 67108864}           // 80000000 -> 4000000
        };
    }

    @ParameterizedTest
    @MethodSource("provideLegalArgsWithOutputForRightShift")
    @DisplayName("Test right shift")
    void testRightShift(int input, int shift, int expected) {
        assertThat(Task7.rotateRight(input, shift)).isEqualTo(expected);
    }

    private static Object[][] provideLegalArgsWithOutputForLeftShift() {
        return new Object[][] {
            {7, 13, 7},   //111 -> 111
            {13, 5, 11}, //1101 -> 1011
            {8, 16, 8}, //1000 -> 1000
            {0, 256, 0},    //0 -> 0
            {-1, 135, -1},  //FFFFFFFF -> FFFFFFFF
            {1, 0, 1},
            {-1610612736, 3, 5},  //A0000000 -> 5
            {Integer.MAX_VALUE, 4, Integer.MAX_VALUE}, // 7FFFFFFF -> 7FFFFFFF
            {Integer.MIN_VALUE, 5, 16}    // 80000000 -> 10
        };
    }

    @ParameterizedTest
    @MethodSource("provideLegalArgsWithOutputForLeftShift")
    @DisplayName("Test left shift")
    void testLeftShift(int input, int shift, int expected) {
        assertThat(Task7.rotateLeft(input, shift)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "54, -1",
        "137, -3",
        "96, -2",
        "4, -1"
    })
    @DisplayName("Test with illegal arguments for right shift")
    void testIllegalArgsForRightShift(int input, int shift) {
        assertThatThrownBy(() -> Task7.rotateRight(input, shift)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
        "54, -1",
        "137, -3",
        "96, -2",
        "4, -1"
    })
    @DisplayName("Test with illegal arguments for left shift")
    void testIllegalArgsForLeftShift(int input, int shift) {
        assertThatThrownBy(() -> Task7.rotateLeft(input, shift)).isInstanceOf(IllegalArgumentException.class);
    }

}
