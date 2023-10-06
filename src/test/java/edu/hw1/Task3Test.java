package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task3Test {

    private static int[][][] provideLegalArgsForTrue() {
        return new int[][][] {
            {{45, 84, 74, 12, 48}, {76, 411, 6}},
            /*
            array1: min = 12, max = 84;
            array2: min = 6, max = 411;
             */
            {{125, 64, 71}, {365, 30, 45, 8, 7}},
            /*
            array1: min = 64, max = 125;
            array2: min = 7, max = 365;
             */
            {{15}, {20, 10}}
            /*
            array1: min = 15, max = 15;
            array2: min = 10, max = 20;
             */
        };
    }

    @ParameterizedTest
    @MethodSource("provideLegalArgsForTrue")
    @DisplayName("Testing with legal arguments for \"true\"")
    void testWithLegalArgsForTrue(int[][] input) {
        assertThat(Task3.isNestable(input[0], input[1])).isEqualTo(true);
    }

    private static int[][][] provideLegalArgsForFalse() {
        return new int[][][] {
            {{17, 65, 47, 2, -1}, {0, 45, 66}},
            /*
            array1: min = -1, max = 65;
            array2: min = 0, max = 66;
             */
            {{24, 99, 68}, {95, 26, 100}},
            /*
            array1: min = 24, max = 99;
            array2: min = 26, max = 100;
             */
            {{10}, {15, 20}},
            /*
            array1: min = 15, max = 15;
            array2: min = 10, max = 20;
             */
            {{35, 7, 98, 25, 6}, {77, 10, 45}},
            /*
            array1: min = 6, max = 98;
            array2: min = 10, max = 77;
             */
            {{9, 8}, {9, 8}},
            /*
            array1: min = 8, max = 9;
            array2: min = 8, max = 9;
             */
        };
    }

    @ParameterizedTest
    @MethodSource("provideLegalArgsForFalse")
    @DisplayName("Testing with legal arguments for \"false\"")
    void testWithLegalArgsForFalse(int[][] input) {
        assertThat(Task3.isNestable(input[0], input[1])).isEqualTo(false);
    }

    @Test
    @DisplayName("Testing with illegalArgs")
    void testWithIllegalArgs() {
        int[] emptyValue = new int[] {};
        int[] storedInts = new int[] {1, 2, 3, 4, 5};
        assertThat(Task3.isNestable(null, null)).isEqualTo(false);
        assertThat(Task3.isNestable(null, storedInts)).isEqualTo(false);
        assertThat(Task3.isNestable(null, emptyValue)).isEqualTo(false);

        assertThat(Task3.isNestable(emptyValue, null)).isEqualTo(false);
        assertThat(Task3.isNestable(emptyValue, storedInts)).isEqualTo(false);
        assertThat(Task3.isNestable(emptyValue, emptyValue)).isEqualTo(false);

        assertThat(Task3.isNestable(storedInts, null)).isEqualTo(false);
        assertThat(Task3.isNestable(storedInts, storedInts)).isEqualTo(false);
        assertThat(Task3.isNestable(storedInts, emptyValue)).isEqualTo(false);
    }

}
