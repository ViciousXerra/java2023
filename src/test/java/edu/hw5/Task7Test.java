package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task7Test {

    private final static String NULL_STRING_MESSAGE = "String arg can't be null.";
    private final static String EMPTY_BLANK_MESSAGE = "String arg can't have length of 0 or contains only whitespaces";

    @Test
    @DisplayName("Test passing null string args")
    void testNullArg() {
        assertThatThrownBy(() -> Task7.lengthMoreOrEqualOneAndNoMoreThree(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_STRING_MESSAGE);
    }

    @Test
    @DisplayName("Test passing empty and blank string args")
    void testEmptyAndBlankArgs() {
        assertThatThrownBy(() -> Task7.lengthMoreOrEqualOneAndNoMoreThree(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
        assertThatThrownBy(() -> Task7.lengthMoreOrEqualOneAndNoMoreThree("     "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
    }

    private static Object[][] provideLengthMethod() {
        return new Object[][] {
            {
                true,
                "110"
            },
            {
                true,
                "10001"
            },
            {
                false,
                "10002"
            },
            {
                false,
                "10"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test length and zero at 3 position check method.")
    @MethodSource("provideLengthMethod")
    void testLengthMoreOrEqualThreeAndThirdCharIsZeroMethod(boolean expected, String input) {
        //When
        boolean actual = Task7.lengthMoreOrEqualThreeAndThirdCharIsZero(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideStartEndsMethod() {
        return new Object[][] {
            {
                false,
                "1"
            },
            {
                true,
                "10001"
            },
            {
                true,
                "11"
            },
            {
                false,
                "10000"
            },
            {
                false,
                "10201"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test starts and ends with same char check method.")
    @MethodSource("provideStartEndsMethod")
    void testStartsAndEndsWithSameChar(boolean expected, String input) {
        //When
        boolean actual = Task7.startsAndEndsWithSameChar(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideDiapasonLengthMethod() {
        return new Object[][] {
            {
                true,
                "1"
            },
            {
                false,
                "10001"
            },
            {
                true,
                "11"
            },
            {
                true,
                "110"
            },
            {
                false,
                "10000"
            },
            {
                false,
                "102"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test length diapason check method.")
    @MethodSource("provideDiapasonLengthMethod")
    void testLengthMoreOrEqualOneAndNoMoreThree(boolean expected, String input) {
        //When
        boolean actual = Task7.lengthMoreOrEqualOneAndNoMoreThree(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

}
