package edu.project2;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedgenerators.Generator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeneratorTest {

    private final static String SIZE_RESTRICTION_MESSAGE =
        "Unable to create maze with this values of height and width.";
    private final static String RESET_RESTRICTION_MESSAGE = "Maze instance does not exist. Unable to reset.";

    private static Generator[] provideGenerators() {
        return new Generator[] {
            new BacktrackingGenerator(true, false),
            new EllersAlgorithmGenerator(true, false)
        };
    }

    @ParameterizedTest
    @DisplayName("Test size restriction.")
    @MethodSource("provideGenerators")
    void testSizeRestriction(Generator generator) {
        //Then
        assertThatThrownBy(() -> generator.generate(0, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(SIZE_RESTRICTION_MESSAGE);
        assertThatThrownBy(() -> generator.generate(Integer.MIN_VALUE, -4))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(SIZE_RESTRICTION_MESSAGE);
    }

    @ParameterizedTest
    @DisplayName("Test reset restriction.")
    @MethodSource("provideGenerators")
    void testResetRestriction(Generator generator) {
        //Then
        assertThatThrownBy(generator::reset)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(RESET_RESTRICTION_MESSAGE);
    }

    @ParameterizedTest
    @DisplayName("Test normal funcioning.")
    @MethodSource("provideGenerators")
    void testNormalFunctioning(Generator generator) {
        //Then
        assertThatCode(() -> {
            generator.generate(10, 10);
            generator.generate();
            generator.reset();
        })
            .doesNotThrowAnyException();
    }
}
