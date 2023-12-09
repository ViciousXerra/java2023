package edu.hw10.task2;

import edu.hw10.task2.testsamples.BasicNumberDivider;
import edu.hw10.task2.testsamples.NumberDivider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NumberDividerTest {

    @Test
    @Order(1)
    @DisplayName("Number divider toString() test.")
    void testNumberDividerToString() {
        //Given
        String expected = "This is basic number divider.";
        //When
        NumberDivider divider = new BasicNumberDivider();
        String actual = divider.toString();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(2)
    @DisplayName("Test divide by 0.")
    void testDivideByZero() {
        assertThatThrownBy(() -> {
            NumberDivider divider = new BasicNumberDivider();
            divider.divide(50, 0);
        })
            .isInstanceOf(ArithmeticException.class)
            .hasMessage("Divide by zero attempt.");
    }

    private static Object[][] provideDivisionTest() {
        return new Object[][] {
            {10L, 2.5f, 4.0},
            {30.0, 10.0f, 3.0},
            {10.1, 2, 5.05}
        };
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("Test legal args.")
    @MethodSource("provideDivisionTest")
    void testLegalArgs(Number num, Number divideBy, Double expected) {
        //When
        NumberDivider divider = new BasicNumberDivider();
        Double actual = divider.divide(num, divideBy);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(4)
    @DisplayName("Test cache file absence.")
    void testCacheFileAbsence() {
        assertThat(Files.exists(Path.of("src/test/resources/hw10testresources/dividercache.txt"))).isFalse();
    }

}
