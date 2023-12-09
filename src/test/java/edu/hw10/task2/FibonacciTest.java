package edu.hw10.task2;

import edu.hw10.task2.testsamples.Calculator;
import edu.hw10.task2.testsamples.FibonacciCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FibonacciTest {

    @Test
    @Order(1)
    @DisplayName("Fibonacci calculator toString() test.")
    void testFibonacciCalcToString() {
        //Given
        String expected = "This is fibonacci calculator.";
        //When
        Calculator calc = new FibonacciCalculator();
        String actual = calc.toString();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("Test non positive num.")
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void testNonPositiveNum(int num) {
        assertThatThrownBy(() -> {
            Calculator calc = new FibonacciCalculator();
            calc.calc(num);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Num argument must be not negative value.");
    }

    private static Object[][] provideFibonacciTest() {
        return new Object[][] {
            {0, 0},
            {1, 1},
            {2, 1},
            {3, 2},
            {4, 3},
            {5, 5},
            {6, 8},
            {10, 55}
        };
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("Test legal args.")
    @MethodSource("provideFibonacciTest")
    void testFibonacciCalculator(int num, long expected) {
        //When
        Calculator calc = new FibonacciCalculator();
        long actual = calc.calc(num);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(4)
    @DisplayName("Test cache file absence.")
    void testCacheFileAbsence() {
        assertThat(Files.exists(Path.of("src/test/resources/hw10testresources/fibonaccicache.txt"))).isFalse();
    }

}
