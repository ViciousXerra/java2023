package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task2Test {

    private final static String NULL_LOCAL_DATE_MESSAGE = "Local date can't be null.";
    private final static String INVALID_YEAR_MESSAGE = "Invalid year number";

    private static Object[][] provideTestingMethodWithExpectedListAndYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return new Object[][] {
            {
                List.of(
                    LocalDate.parse("2024-09-13", formatter),
                    LocalDate.parse("2024-12-13", formatter)
                ),
                2024
            },
            {
                List.of(
                    LocalDate.parse("2025-06-13", formatter)
                ),
                2025
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Testing with valid years.")
    @MethodSource("provideTestingMethodWithExpectedListAndYear")
    void testWithValidYears(List<LocalDate> expected, int year) {
        //When
        List<LocalDate> actual = Task2.getAllFriday13(year);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Testing with invalid years.")
    void testWithInvalidYears() {
        //Then
        assertThatThrownBy(() -> Task2.getAllFriday13(Year.MAX_VALUE + 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(INVALID_YEAR_MESSAGE);
        assertThatThrownBy(() -> Task2.getAllFriday13(Year.MIN_VALUE - 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(INVALID_YEAR_MESSAGE);
    }

    private static Object[][] provideTestingMethodWithExpectingLocalDateAndPassingLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return new Object[][] {
            {
                LocalDate.parse("2024-09-13", formatter),
                LocalDate.parse("2023-11-09", formatter)
            },
            {
                LocalDate.parse("2024-12-13", formatter),
                LocalDate.parse("2024-09-13", formatter)
            },
            {
                LocalDate.parse("2024-12-13", formatter),
                LocalDate.parse("2024-09-14", formatter)
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Testing with valid date.")
    @MethodSource("provideTestingMethodWithExpectingLocalDateAndPassingLocalDate")
    void testWithValidDate(LocalDate expected, LocalDate startingFrom) {
        //When
        LocalDate actual = Task2.getClosestFriday13(startingFrom);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Testing passing a null LocalDate.")
    void testNullLocalDate() {
        //Then
        assertThatThrownBy(() -> Task2.getClosestFriday13(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_LOCAL_DATE_MESSAGE);
    }
}
