package edu.hw3;

import edu.hw3.task2.Task2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class Task2Test {

    private static Object[][] provideClusterizeMethodWithLegalArgs() {
        return new Object[][] {
            {"(()())()", new String[] {"(()())", "()"}},
            {"((())())", new String[] {"((())())"}},
            {"(())()((()))", new String[] {"(())", "()", "((()))"}},
        };
    }

    @ParameterizedTest
    @DisplayName("Provide \"clusterize\" method with legal args")
    @MethodSource("provideClusterizeMethodWithLegalArgs")
    void testWithLegalArguments(String input, String[] expected) {
        //When
        String[] actual = Task2.clusterize(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Provide \"clusterize\" method with illegal args")
    @ValueSource(strings = {"", "))((", "(a)", "(()", "())"})
    void testWithIllegalArguments(String input) {
        //When
        String[] actual = Task2.clusterize(input);
        //Then
        assertThat(actual).isEqualTo(new String[0]);
    }

}
