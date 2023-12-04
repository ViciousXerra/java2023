package edu.project4;

import edu.project4.plotters.SingleThreadPlotter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlotterUnusualBehaviourTest {

    private static Object[][] provideTestMethod() {
        return new Object[][] {
            {
                -15, 1080, 5, List.of("waves"), 20, 1000000, "Image resolution values must be a positive nums."
            },
            {
                1920, 0, 5, List.of("waves"), 20, 1000000, "Image resolution values must be a positive nums."
            },
            {
                1920, 1080, 1, List.of("waves"), 20, 1000000, "Affine transformation count argument must be at least 2."
            },
            {
                1920, 1080, 5, null, 20, 1000000, "Non-linear transformation keys must exist."
            },
            {
                1920, 1080, 5, List.of(), 20, 1000000, "Non-linear transformation keys must exist."
            },
            {
                1920, 1080, 5, List.of("waves"), 0, 1000000, "Num of samples and iterations must be at least 1."
            },
            {
                1920, 1080, 5, List.of("waves"), 20, 0, "Num of samples and iterations must be at least 1."
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test unusual behaviour.")
    @MethodSource("provideTestMethod")
    void testUnusualBehaviour(
        int width,
        int height,
        int affineTransformationCount,
        List<String> nonLinearKeys,
        int samples,
        int iterations,
        String message
    ) {
        assertThatThrownBy(() -> new SingleThreadPlotter(
            width,
            height,
            samples,
            iterations,
            affineTransformationCount,
            nonLinearKeys,
            true
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(message);
    }

}
