package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task7Test {

    private static Object[][] provideLegalArgsWithOutputForRightShift() {
        return new Object[][] {
            {13, 2, 7},   //1101 -> 0111
            {13, 64, 13}, //1101 -> 1101
            {0, 8, 0},    //0 -> 0
            {-1, 7, -1},  //FFFFFFFF -> FFFFFFFF
            {Integer.MAX_VALUE, 3, Integer.MAX_VALUE}, // 7FFFFFFF -> 7FFFFFFF
            {Integer.MIN_VALUE, 5, 67108864}    // 80000000 -> 4000000
        };
    }

    @ParameterizedTest
    @MethodSource("provideLegalArgsWithOutputForRightShift")
    @DisplayName("Test right shift")
    void testRightShift(int input, int shift, int expected) {
        assertThat(Task7.rotateRight(input, shift)).isEqualTo(expected);
    }

}
