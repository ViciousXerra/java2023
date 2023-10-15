package edu.hw2;

import edu.hw2.task1.Expr;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task1Test {

    @ParameterizedTest
    @DisplayName("Constant testing")
    @ValueSource(ints = {5, 6, 4, 2, 0, 9, -1})
    void testConstant(int value) {
        //When
        Expr testValue = new Expr.Constant(value);
        //Then
        assertThat(testValue.evaluate()).isEqualTo(value);
    }

    private static Object[][] provideNegateTesting() {
        return new Object[][] {
            {new Expr.Constant(5), -5},
            {new Expr.Exponent(new Expr.Constant(2), 3), -8},
            {new Expr.Multiplication(new Expr.Exponent(new Expr.Constant(3), 2), new Expr.Constant(2)), -18}
        };
    }

    @ParameterizedTest
    @DisplayName("Negate testing")
    @MethodSource("provideNegateTesting")
    void testNegate(Expr expression, double result) {
        //When
        Expr testValue = new Expr.Negate(expression);
        //Then
        assertThat(testValue.evaluate()).isEqualTo(result);
    }


    private static Object[][] provideExponentTesting() {
        return new Object[][] {
            {new Expr.Constant(5), 2, 25},
            {new Expr.Addition(new Expr.Constant(6), new Expr.Constant(-3)), 3, 27},
            {new Expr.Negate(new Expr.Constant(11)), 2, 121}
        };
    }

    @ParameterizedTest
    @DisplayName("Exponent testing")
    @MethodSource("provideExponentTesting")
    void testExponent(Expr expression, int power, double result) {
        //When
        Expr testValue = new Expr.Exponent(expression, power);
        //Then
        assertThat(testValue.evaluate()).isEqualTo(result);
    }

    @ParameterizedTest
    @DisplayName("Addition testing")
    @CsvSource({
        "5, 4, 9",
        "-12, 7, -5",
        "-50, -70, -120",
    })
    void testAddition(int num1, int num2, double result) {
        //Given
        Expr.Constant value1 = new Expr.Constant(num1);
        Expr.Constant value2 = new Expr.Constant(num2);
        //When
        Expr testValue = new Expr.Addition(value1, value2);
        //Then
        assertThat(testValue.evaluate()).isEqualTo(result);
    }

    @ParameterizedTest
    @DisplayName("Multiplication testing")
    @CsvSource({
        "12, 3, 36",
        "-4, 6, -24",
        "-13, -13, 169",
    })
    void testMultiplication(int num1, int num2, double result) {
        //Given
        Expr.Constant value1 = new Expr.Constant(num1);
        Expr.Constant value2 = new Expr.Constant(num2);
        //When
        Expr testValue = new Expr.Multiplication(value1, value2);
        //Then
        assertThat(testValue.evaluate()).isEqualTo(result);
    }

}
