package edu.project4;

import edu.project4.renderers.plotters.MultiThreadPlotter;
import edu.project4.renderers.plotters.PixelsImage;
import edu.project4.renderers.plotters.Plotter;
import edu.project4.renderers.plotters.SingleThreadPlotter;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class demo {

    public static void main(String[] args) {
        Plotter plotter = new MultiThreadPlotter(
            4096,
            2160,
            100,
            1000000,
            ThreadLocalRandom.current().nextLong(),
            10,
            List.of(
                "linear",
                "exponential",
                "waves"
            ),
            true
        );
        PixelsImage image = plotter.plotPixels();
        ImageSaverUtils.saveImage(image, Path.of("src/test/resources/project4testresources/mixmulti.bmp"));
    }

}
