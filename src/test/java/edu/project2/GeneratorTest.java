package edu.project2;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedgenerators.Generator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeneratorTest {

    private final static String SIZE_RESTRICTION_MESSAGE =
        "Unable to create maze with this values of height and width.";

    private static Generator[] provideGenerators() {
        return new Generator[] {
            new BacktrackingGenerator(true, false),
            new EllersAlgorithmGenerator(true, false)
        };
    }

    @Test
    @DisplayName("Test size restriction.")
    void testSizeRestriction() {
        //Then
        assertThatThrownBy(() -> {
            Generator generator =
                new BacktrackingGenerator(0, 0, true, false);
            generator.generate();
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(SIZE_RESTRICTION_MESSAGE);
        assertThatThrownBy(() -> {
            Generator generator =
                new EllersAlgorithmGenerator(Integer.MIN_VALUE, -4, true, false);
            generator.generate();
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(SIZE_RESTRICTION_MESSAGE);
    }

    @ParameterizedTest
    @DisplayName("Test normal funcioning.")
    @MethodSource("provideGenerators")
    void testNormalFunctioning(Generator generator) {
        //Then
        assertThatCode(generator::generate)
            .doesNotThrowAnyException();
    }
}
