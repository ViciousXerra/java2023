package edu.hw3;

import edu.hw3.task5.Person;
import edu.hw3.task5.SortingOrder;
import edu.hw3.task5.Task5;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task5Test {

    private static Object[][] provideValidArray() {
        return new Object[][] {
            {
                new String[] {"Juliet Burke", "Kate Austen", "Michael Dawson", "Boone Carlyle", "Sayid Jarrah"},
                new ArrayList<>(Arrays.asList(
                    new Person("Kate Austen"),
                    new Person("Juliet Burke"),
                    new Person("Boone Carlyle"),
                    new Person("Michael Dawson"),
                    new Person("Sayid Jarrah")
                )),
                SortingOrder.ASC
            },
            {
                new String[] {"Juliet Burke", "Kate Austen", "Michael Dawson", "Boone Carlyle", "Sayid Jarrah"},
                new ArrayList<>(Arrays.asList(
                    new Person("Sayid Jarrah"),
                    new Person("Michael Dawson"),
                    new Person("Boone Carlyle"),
                    new Person("Juliet Burke"),
                    new Person("Kate Austen")
                )),
                SortingOrder.DESC
            },
            {
                new String[] {"John Locke", "Jack", "Benjamin Linus", "Hugo Reyes"},
                new ArrayList<>(Arrays.asList(
                    new Person("Benjamin Linus"),
                    new Person("Hugo Reyes"),
                    new Person("Jack"),
                    new Person("John Locke")
                )),
                SortingOrder.ASC
            },
            {
                new String[] {"John Locke", "Jack", "Benjamin Linus", "Hugo Reyes"},
                new ArrayList<>(Arrays.asList(
                    new Person("John Locke"),
                    new Person("Jack"),
                    new Person("Hugo Reyes"),
                    new Person("Benjamin Linus")
                )),
                SortingOrder.DESC
            },
            {
                new String[] {"Jack", "Elizabeth", "Charlie"},
                new ArrayList<>(Arrays.asList(
                    new Person("Charlie"),
                    new Person("Elizabeth"),
                    new Person("Jack")
                )),
                SortingOrder.ASC
            },
            {
                null,
                List.of(),
                SortingOrder.ASC
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test method on valid array of names")
    @MethodSource("provideValidArray")
    void testOrderingOfContacts(String[] input, List<Person> expected, SortingOrder order) {
        //When
        List<Person> actual = Task5.sortContacts(input, order);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideInvalidNameValues() {
        return new Object[][] {
            {
                new String[] {"Charlie Hustle", "Elizabeth Torres", " Jack Nicholson"},
                SortingOrder.DESC
            },
            {
                new String[] {"Charlie", null, "Jack"},
                SortingOrder.ASC
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test method on invalid array values")
    @MethodSource("provideInvalidNameValues")
    void testInvalidArrayValues(String[] input, SortingOrder order) {
        assertThatThrownBy(() -> Task5.sortContacts(input, order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(
                "Name cannot be null or starts with whitespace, and must be separated by one whitespace.");
    }

    private static Object[][] provideInvalidOrderValues() {
        return new Object[][] {
            {
                new String[] {"Charlie Hustle", "Elizabeth Torres", " Jack Nicholson"},
                null
            },
            {
                new String[] {"Charlie", null, "Jack"},
                null
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test method on invalid array values")
    @MethodSource("provideInvalidOrderValues")
    void testInvalidOrderValues(String[] input, SortingOrder order) {
        assertThatThrownBy(() -> Task5.sortContacts(input, order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Sorting order value must be \"ASC\" or \"DESC\"");
    }

}
