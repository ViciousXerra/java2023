package edu.hw3;

import edu.hw3.task8.BackwardIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Task8Test {

    private static Object[][] getListAndItsReverseList() {
        return new Object[][] {
            {
                List.of(1, 2, 3, 4, 5),
                List.of(5, 4, 3, 2, 1)
            },
            {
                List.of(),
                List.of()
            },
            {
                List.of(4, 5),
                List.of(5, 4)
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test backward iterator")
    @MethodSource("getListAndItsReverseList") <T> void testBackwardIterator(List<T> input, List<T> expected) {
        //Given
        BackwardIterator<T> customIterator = new BackwardIterator<>(input);
        List<T> actual = new ArrayList<>();
        while (customIterator.hasNext()) {
            actual.add(customIterator.next());
        }
        assertThat(actual).isEqualTo(expected);
    }

}
