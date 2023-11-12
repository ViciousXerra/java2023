package edu.hw4;

import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SortingTest {

    private final static String INVALID_FIRST_COUNTERS = "The first counters must be greater or equal to 0.";

    @Test
    @Order(1)
    @DisplayName("Test sorting by height.")
    void testSortingByHeight() {
        //When
        List<Animal> actual = HomeWork4Util.getSortedByHeight(DataProvider.provideTestWithData());
        //Then
        assertThat(actual).isSortedAccordingTo(Comparator.comparing(Animal::height));
    }

    @Test
    @Order(2)
    @DisplayName("Test sorting by descent weight.")
    void testSortingByDescentWeight() {
        //Given
        int expectedFirstCounters = 5;
        //When
        List<Animal> actual =
            HomeWork4Util.getSortedByDescentWeight(DataProvider.provideTestWithData(), expectedFirstCounters);
        //Then
        assertThat(actual).hasSize(expectedFirstCounters)
            .isSortedAccordingTo(Comparator.comparing(Animal::weight).reversed());
    }

    @Test
    @Order(3)
    @DisplayName("Test passing restricted first counters value")
    void testRestrictedFirstCountersValue() {
        int firstCounterValue = -1;
        //Then
        assertThatThrownBy(() -> HomeWork4Util.getSortedByDescentWeight(DataProvider.provideTestWithData(), firstCounterValue))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(INVALID_FIRST_COUNTERS);
    }

    @Test
    @Order(4)
    @DisplayName("Test sorting by several parameters.")
    void testSortingWithSeveralParameters() {
        //When
        List<Animal> actual = HomeWork4Util.getSortedByTypeAndSexAndName(DataProvider.provideTestWithData());
        //Then
        assertThat(actual).isSortedAccordingTo(
            Comparator.comparing(Animal::type)
            .thenComparing(Animal::sex)
            .thenComparing(Animal::name)
        );
    }

}
