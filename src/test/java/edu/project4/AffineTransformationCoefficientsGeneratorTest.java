package edu.project4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static java.lang.Math.pow;
import static org.assertj.core.api.Assertions.assertThat;

class AffineTransformationCoefficientsGeneratorTest {

    @RepeatedTest(10)
    @DisplayName("Test affine transformation coefficients for compression.")
    void testCompression() {
        //When
        AffineTransformationCoefficients coeffs = AffineTransformationCoefficientsGenerator.generate();
        double actual =
            pow(coeffs.a(), 2) +
            pow(coeffs.b(), 2) +
            pow(coeffs.d(), 2) +
            pow(coeffs.e(), 2) -
            pow((coeffs.a() * coeffs.e() - coeffs.b() * coeffs.d()), 2);
        assertThat(actual).isLessThan(1.0);
    }

}
