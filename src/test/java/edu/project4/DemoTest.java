package edu.project4;

import edu.project4.plotters.MultiThreadPlotter;
import edu.project4.plotters.Plotter;
import edu.project4.plotters.SingleThreadPlotter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.util.List;

class DemoTest {

    @Test
    @DisplayName("Test single thread generation.")
    void testSingleThread() {
        Plotter plotter = new SingleThreadPlotter(
            4096,
            2160,
            50,
            1000000,
            20,
            List.of(
                "popcorn",
                "exponential",
                "diamond",
                "ex"
            ),
            true
        );
        ImageSaverUtils.saveImage(
            plotter.plotPixels(),
            Path.of("src/test/resources/project4testresources/singlethreadexample.bmp")
        );
    }

    @Test
    @DisplayName("Test multi thread generation.")
    void testMultiThread() {
        Plotter plotter = new MultiThreadPlotter(
            4096,
            2160,
            50,
            1000000,
            20,
            List.of(
                "popcorn",
                "exponential",
                "diamond",
                "ex"
            ),
            true
        );
        ImageSaverUtils.saveImage(
            plotter.plotPixels(),
            Path.of("src/test/resources/project4testresources/multithreadexample.bmp")
        );
    }

}
