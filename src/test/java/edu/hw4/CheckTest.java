package edu.hw4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CheckTest {

    private final static String HEIGHT_RESTRICTION = "Height can't be less than or equal to 0.";

    @ParameterizedTest
    @Order(1)
    @DisplayName("Check the existence of dogs according to passed height value method.")
    @CsvSource(value = {
        "100, true",
        "110, false"
    })
    void testDogExisting(int height, boolean expected) {
        //When
        boolean actual =
            HomeWork4Util.isListContainsDogsWithHeightGreaterThan(DataProvider.provideTestWithData(), height);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("Test height restriction.")
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void testHeightRestriction(int height) {
        //Then
        assertThatThrownBy(() -> HomeWork4Util.isListContainsDogsWithHeightGreaterThan(
            DataProvider.provideTestWithData(),
            height
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(HEIGHT_RESTRICTION);
    }

    @Test
    @Order(3)
    @DisplayName("Test counting of biting dogs and spiders method.")
    void testCountingOfBitingDogsAndSpiders() {
        //When
        boolean actual = HomeWork4Util.isSpiderBitesMoreOftenThanDogs(DataProvider.provideTestWithData());
        assertThat(actual).isFalse();
    }
}
