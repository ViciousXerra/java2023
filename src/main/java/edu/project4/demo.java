package edu.project4;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class demo {
    public static void main(String[] args) throws IOException {
        Renderer renderer = new SingleThreadRenderer("src/test/resources/project4testresources/test.bmp");
        renderer.render(
            1920,
            1080,
            20,
            1000000,
            ThreadLocalRandom.current().nextLong(),
            20,
            List.of(
                "hyperbolic"
            )
        );
        renderer.save();
    }
}
