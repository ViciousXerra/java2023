package edu.hw3;

import edu.hw3.task5.Task5;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task5Test {

    private static Object[][] provideValidArray() {
        return new Object[][] {
            {
                new String[] {"Juliet Burke", "Kate Austen", "Michael Dawson", "Boone Carlyle", "Sayid Jarrah"},
                new String[] {"Kate Austen", "Juliet Burke", "Boone Carlyle", "Michael Dawson", "Sayid Jarrah"},
                "ASC"
            },
            {
                new String[] {"Juliet Burke", "Kate Austen", "Michael Dawson", "Boone Carlyle", "Sayid Jarrah"},
                new String[] {"Sayid Jarrah", "Michael Dawson", "Boone Carlyle", "Juliet Burke", "Kate Austen"},
                "DESC"
            },
            {
                new String[] {"John Locke", "Jack", "Benjamin Linus", "Hugo Reyes"},
                new String[] {"Benjamin Linus", "Hugo Reyes", "Jack", "John Locke"},
                "ASC"
            },
            {
                new String[] {"John Locke", "Jack", "Benjamin Linus", "Hugo Reyes"},
                new String[] {"John Locke", "Jack", "Hugo Reyes", "Benjamin Linus"},
                "DESC"
            },
            {
                new String[] {"Jack", "Elizabeth", "Charlie"},
                new String[] {"Charlie", "Elizabeth", "Jack"},
                "ASC"
            },
            {
                null,
                new String[0],
                "ASC"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test method on valid array of names")
    @MethodSource("provideValidArray")
    void testOrderingOfContacts(String[] input, String[] expected, String order) {
        //When
        String[] actual = Task5.sortContacts(input, order);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideInvalidNameValues() {
        return new Object[][] {
            {
                new String[] {"Charlie Hustle", "Elizabeth Torres", " Jack Nicholson"},
                "DESC"
            },
            {
                new String[] {"Charlie", null, "Jack"},
                "ASC"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test method on invalid array values")
    @MethodSource("provideInvalidNameValues")
    void testInvalidArrayValues(String[] input, String order) {
        assertThatThrownBy(() -> Task5.sortContacts(input, order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(
                "Name cannot be null or starts with whitespace, and must be separated by one whitespace.");
    }

    private static Object[][] provideInvalidOrderValues() {
        return new Object[][] {
            {
                new String[] {"Charlie Hustle", "Elizabeth Torres", " Jack Nicholson"},
                "DSC"
            },
            {
                new String[] {"Charlie", null, "Jack"},
                "SSC"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test method on invalid array values")
    @MethodSource("provideInvalidOrderValues")
    void testInvalidOrderValues(String[] input, String order) {
        assertThatThrownBy(() -> Task5.sortContacts(input, order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Order String value must be \"ASC\" or \"DESC\"");
    }

}
