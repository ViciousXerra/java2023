package edu.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task4Test {

    private static String[][] provideStrsForFix() {
        return new String[][] {
            {"hTsii  settss rtnig", "This is test string"},
            {"rCaeuter", "Creature"},
            {"hSpa eemomyra llyo", "Shape memory alloy"},
            {"hTsii  s aimex dpus rtni.g", "This is a mixed up string."},
            {"", ""},
            {"g", "g"},
            {"hTnak", "Thank"},
            {null, "DEFAULT"}
        };
    }

    @ParameterizedTest
    @MethodSource("provideStrsForFix")
    @DisplayName("Testing with strings arguments")
    void testWithLegalArgsForTrue(String input, String expected) {
        assertThat(Task4.fixString(input)).isEqualTo(expected);
    }

}
