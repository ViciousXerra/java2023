package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task8Test {

    private final static String NULL_STRING_MESSAGE = "String arg can't be null.";
    private final static String EMPTY_BLANK_MESSAGE = "String arg can't have length of 0 or contains only whitespaces";

    @Test
    @DisplayName("Test passing null string args.")
    void testNullArg() {
        assertThatThrownBy(() -> Task8.isOddLength(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_STRING_MESSAGE);
    }

    @Test
    @DisplayName("Test passing empty and blank string args.")
    void testEmptyAndBlankArgs() {
        assertThatThrownBy(() -> Task8.isOddLength(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
        assertThatThrownBy(() -> Task8.isOddLength("     "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
    }

    private static Object[][] provideTestingMethod1() {
        return new Object[][]{
            {
                true,
                "1"
            },
            {
                false,
                "2"
            },
            {
                false,
                "10"
            },
            {
                true,
                "100"
            },
        };
    }

    @ParameterizedTest
    @DisplayName("Test odd length method check.")
    @MethodSource("provideTestingMethod1")
    void testMethod1(boolean expected, String input) {
        //When
        boolean actual = Task8.isOddLength(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideTestingMethod2() {
        return new Object[][]{
            {
                true,
                "0"
            },
            {
                false,
                "00"
            },
            {
                false,
                "002"
            },
            {
                true,
                "001"
            },
            {
                true,
                "10"
            },
            {
                false,
                "1"
            },
            {
                false,
                "110"
            },
            {
                false,
                "1020"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test starts with and odd/even length check.")
    @MethodSource("provideTestingMethod2")
    void testMethod2(boolean expected, String input) {
        //When
        boolean actual = Task8.isStartsWithZeroAndOddLengthOrOneAndEvenLength(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideTestingMethod3() {
        return new Object[][]{
            {
                true,
                "000"
            },
            {
                true,
                "100101"
            },
            {
                true,
                "0001"
            },
            {
                true,
                "10010"
            },
            {
                false,
                "10020"
            },
            {
                true,
                "100100011110"
            },
            {
                false,
                "10010011011"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test zero count multiples three check.")
    @MethodSource("provideTestingMethod3")
    void testMethod3(boolean expected, String input) {
        //When
        boolean actual = Task8.isZeroCountMultiplesThree(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideTestingMethod4() {
        return new Object[][]{
            {
                false,
                "11"
            },
            {
                false,
                "111"
            },
            {
                true,
                "1011011101"
            },
            {
                false,
                "1010001020"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Not a \"11\" or \"111\" check.")
    @MethodSource("provideTestingMethod4")
    void testMethod4(boolean expected, String input) {
        //When
        boolean actual = Task8.isNot11or111(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideTestingMethod5() {
        return new Object[][]{
            {
                true,
                "1"
            },
            {
                false,
                "0"
            },
            {
                true,
                "101011"
            },
            {
                false,
                "121110"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Odd char is \"1\" check.")
    @MethodSource("provideTestingMethod5")
    void testMethod5(boolean expected, String input) {
        //When
        boolean actual = Task8.isOddCharIsOne(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideTestingMethod6() {
        return new Object[][]{
            {
                true,
                "010"
            },
            {
                true,
                "0100"
            },
            {
                true,
                "0010"
            },
            {
                true,
                "001"
            },
            {
                true,
                "001"
            },
            {
                false,
                "0021"
            },
            {
                false,
                "01000001000"
            },
            {
                false,
                "10"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Contains at least 2 zeroes and no more than one one check.")
    @MethodSource("provideTestingMethod6")
    void testMethod6(boolean expected, String input) {
        //When
        boolean actual = Task8.isContainsNotLessThanTwoZerosAndNoMoreOneOne(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideTestingMethod7() {
        return new Object[][]{
            {
                true,
                "010101010"
            },
            {
                true,
                "1"
            },
            {
                false,
                "10201010100"
            },
            {
                false,
                "11"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Not contains sequenced ones check.")
    @MethodSource("provideTestingMethod7")
    void testMethod7(boolean expected, String input) {
        //When
        boolean actual = Task8.isNotContainsSequencedOnes(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

}
