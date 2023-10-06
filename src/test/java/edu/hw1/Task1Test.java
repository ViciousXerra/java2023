package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task1Test {

    private static Object[][] provideLegalArgsAndOutputs() {
        return new Object[][] {
            {"01:00", 60L},
            {"25:47", 1547L},
            {"136:57", 8217L},
            {"00:12", 12L}
        };
    }

    @ParameterizedTest
    @MethodSource("provideLegalArgsAndOutputs")
    @DisplayName("Testing with legal arguments")
    void testWithLegalArgs(String input, long expected) {
        assertThat(Task1.minutesToSeconds(input)).isEqualTo(expected);
    }

    private static String[] provideIllegalArgs() {
        return new String[] {
            "45:60",
            "169:78",
            "486f:9a",
            ":",
            "123:",
            ":4",
            "  :  ",
            null
        };
    }

    @ParameterizedTest
    @MethodSource("provideIllegalArgs")
    @DisplayName("Testing with illegal args")
    void testWithIllegalArgs(String input) {
        assertThat(Task1.minutesToSeconds(input)).isEqualTo(-1L);
    }
}
