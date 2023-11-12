package edu.hw3;

import edu.hw3.task4.Task4;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task4Test {

    private static Object[][] provideValidArgsAndExpected() {
        return new Object[][] {
            {1, "I"},
            {3999, "MMMCMXCIX"},
            {542, "DXLII"},
            {11, "XI"},
            {459, "CDLIX"}
        };
    }

    @ParameterizedTest
    @DisplayName("Test converting method with legal args")
    @MethodSource("provideValidArgsAndExpected")
    void testRomanLiteralsConverter(int input, String expected) {
        //When
        String actual = Task4.convertToRomanNumerals(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Test converting method with legal args")
    @ValueSource(ints = {0, 4000, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void testRomanLiteralsConverter(int input) {
        //Then
        assertThatThrownBy(() -> Task4.convertToRomanNumerals(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The number must be greater than zero and less than 4000.");
    }

}
