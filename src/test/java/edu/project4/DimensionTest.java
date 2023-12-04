package edu.project4;

import edu.project4.plotters.Dimension;
import edu.project4.plotters.Point;
import java.util.Random;
import org.junit.jupiter.api.RepeatedTest;
import static org.assertj.core.api.Assertions.assertThat;

class DimensionTest {

    @RepeatedTest(50)
    void testDimensionContains() {
        double xMin = -(double) 1920 / 1080;
        double xMax = -xMin;
        double yMin = -1;
        double yMax = 1;
        Dimension dim = new Dimension(2 * xMax, 2 * yMax, xMin, yMin);
        Random random = new Random();
        Point point = new Point(random.nextDouble(xMin, xMax), random.nextDouble(yMin, yMax));
        assertThat(dim.contains(point)).isTrue();
    }

}
