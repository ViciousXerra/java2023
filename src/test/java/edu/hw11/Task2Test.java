package edu.hw11;

import edu.hw11.task2.ArithmeticUtils;
import edu.hw11.task2.DynamicArithmeticUtilsRedefinition;
import edu.hw11.task2.interceptors.DivideInterceptor;
import edu.hw11.task2.interceptors.MultiplyInterceptor;
import edu.hw11.task2.interceptors.SubtractInterceptor;
import edu.hw11.task2.interceptors.SumInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

class Task2Test {

    private static Object[][] provideTestMethod() {
        return new Object[][] {
            {-10, 10, 0, -100, -20, -1},
            {5, 4, 9, 20, 1, 1},
            {1, 1, 2, 1, 0, 1},
            {15, 3, 18, 45, 12, 5},
            {-50, -10, -60, 500, -40, 5},
            {250, -5, 245, -1250, 255, -50},
        };
    }

    @ParameterizedTest
    @MethodSource("provideTestMethod")
    @DisplayName("Test method original call and interceptor calls.")
    void testOriginalMethod(
        int value1, int value2,
        int expectedSum,
        int expectedMultiply,
        int expectedSubtract,
        int expectedDivide
    ) {
        //When
        int actualSum = ArithmeticUtils.sum(value1, value2);
        //Interception to multiply
        DynamicArithmeticUtilsRedefinition.interceptArithmeticUtilsSum(MultiplyInterceptor.class);
        int actualMultiply = ArithmeticUtils.sum(value1, value2);
        //Interception to subtract
        DynamicArithmeticUtilsRedefinition.interceptArithmeticUtilsSum(SubtractInterceptor.class);
        int actualSubtract = ArithmeticUtils.sum(value1, value2);
        //Interception to divide
        DynamicArithmeticUtilsRedefinition.interceptArithmeticUtilsSum(DivideInterceptor.class);
        int actualDivide = ArithmeticUtils.sum(value1, value2);
        //Then
        Assertions.assertAll(
            () -> assertThat(actualSum).isEqualTo(expectedSum),
            () -> assertThat(actualMultiply).isEqualTo(expectedMultiply),
            () -> assertThat(actualSubtract).isEqualTo(expectedSubtract),
            () -> assertThat(actualDivide).isEqualTo(expectedDivide)
        );
        DynamicArithmeticUtilsRedefinition.interceptArithmeticUtilsSum(SumInterceptor.class);
    }

}
