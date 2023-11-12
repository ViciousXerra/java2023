package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task6Test {

    private final static String NULL_STRING_MESSAGE = "String arg can't be null.";
    private final static String EMPTY_BLANK_MESSAGE = "String arg can't have length of 0 or contains only whitespaces";

    private static Object[][] provideTestingMethodWithValidData() {
        return new Object[][] {
            {
                true,
                "abc",
                "abc"
            },
            {
                true,
                "abc",
                "a___b_ _c"
            },
            {
                true,
                "abc",
                "____a__b _c____"
            },
            {
                true,
                "abc",
                "___abc_"
            },
            {
                true,
                "abc",
                "___abc"
            },
            {
                true,
                "abc",
                "abc____"
            },
            {
                false,
                "abc",
                "ab____"
            },
            {
                false,
                "abc",
                "ab"
            },
            {
                false,
                "abc",
                "____a___c____"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test subsequence method with valid data.")
    @MethodSource("provideTestingMethodWithValidData")
    void testSubseqenceCheckMethod(boolean expected, String subsequence, String sequence) {
        //When
        boolean actual = Task6.isFirstArgIsSubsequenceOfSecond(subsequence, sequence);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test passing null string args")
    void testNullArg() {
        assertThatThrownBy(() -> Task6.isFirstArgIsSubsequenceOfSecond(null, "fgazxgvwe"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_STRING_MESSAGE);
        assertThatThrownBy(() -> Task6.isFirstArgIsSubsequenceOfSecond("fgazxgvwe", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_STRING_MESSAGE);
    }

    @Test
    @DisplayName("Test passing empty and blank string args")
    void testEmptyAndBlankArgs() {
        assertThatThrownBy(() -> Task6.isFirstArgIsSubsequenceOfSecond("gydsghsd", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
        assertThatThrownBy(() -> Task6.isFirstArgIsSubsequenceOfSecond("", "gydsghsd"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
        assertThatThrownBy(() -> Task6.isFirstArgIsSubsequenceOfSecond("gydsghsd", " "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
        assertThatThrownBy(() -> Task6.isFirstArgIsSubsequenceOfSecond(" ", "gydsghsd"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_BLANK_MESSAGE);
    }
}
