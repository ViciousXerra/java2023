package edu.project4.renderers;

import java.io.IOException;
import java.util.List;

public interface Renderer {
    void render(
        int width,
        int height,
        int samples,
        int iterations,
        long seed,
        int affineTransformationCount,
        List<String> nonLinearKeys,
        boolean withSymmetry
    );

    void save() throws IOException;

}
