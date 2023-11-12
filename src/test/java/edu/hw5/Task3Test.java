package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static edu.hw5.task3.Task3.parseDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task3Test {

    private final static String NULL_RESTRICTION = "Date string can't be null.";

    private static Object[][] provideTestingMethodWithData() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return new Object[][] {
            {
                Optional.of(LocalDate.parse("1974-12-16", formatter)),
                "1974-12-16"
            },
            {
                Optional.of(LocalDate.now()),
                "today"
            },
            {
                Optional.of(LocalDate.now().minusDays(1)),
                "yesterday"
            },
            {
                Optional.of(LocalDate.now().minusDays(7)),
                "7 days ago"
            },
            {
                Optional.of(LocalDate.now().plusDays(7)),
                "7 days after"
            },
            {
                Optional.of(LocalDate.now().minusDays(1)),
                "1 day ago"
            },
            {
                Optional.empty(),
                "14 days after something"
            },
            {
                Optional.of(LocalDate.parse("1974-12-02", formatter)),
                "1974-12-2"
            },
            {
                Optional.of(LocalDate.parse("2024-01-01", formatter)),
                "1/1/2024"
            },
            {
                Optional.of(LocalDate.parse("2024-01-01", formatter)),
                "1/1/24"
            },
            {
                Optional.of(LocalDate.now().plusDays(1)),
                "tomorrow"
            },
            {
                Optional.empty(),
                "I have no idea what i'm doin'"
            },
            {
                Optional.empty(),
                "24 february of 1997"
            },
            {
                Optional.empty(),
                "14 days before"
            },
        };
    }

    @ParameterizedTest
    @DisplayName("Test valid data.")
    @MethodSource("provideTestingMethodWithData")
    void testValidData(Optional<LocalDate> expected, String input) {
        //When
        Optional<LocalDate> actual = parseDate(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test passing null string.")
    void testNullArg() {
        assertThatThrownBy(() -> parseDate(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_RESTRICTION);
    }
}
