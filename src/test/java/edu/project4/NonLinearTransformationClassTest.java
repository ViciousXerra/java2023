package edu.project4;

import edu.project4.nonlineartransformations.NonLinearTransformation;
import edu.project4.nonlineartransformations.affinetransformations.AffineTranslation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NonLinearTransformationClassTest {

    private static Object[][] provideTestMethod() {
        return new Object[][] {
            {
                AffineTranslation.getInstance(),
                null
            },
            {
                null,
                "key"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test null.")
    @MethodSource("provideTestMethod")
    void testPassingNull(AffineTranslation translation, String key) {
        assertThatThrownBy(() -> NonLinearTransformation.getInstance(translation, key))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Translation function and key must be non-null.");
    }

}
