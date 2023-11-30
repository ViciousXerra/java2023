package edu.hw9.task1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcurrencyStatCollectorUnusualBehaviourTest {

    @Test
    @DisplayName("Test null stat generator type value.")
    void testNullStatGeneratorType() {
        assertThatThrownBy(() -> new ConcurrencyDoubleStatCollector().push(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Generator type can't be null.");
    }

    @Test
    @DisplayName("Test null var-args.")
    void testNullVarArgs() {
        assertThatThrownBy(() -> new ConcurrencyDoubleStatCollector().push(DoubleStatGeneratorType.MAX, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Values var-args can't be null");
    }

    @Test
    @DisplayName("Test zero length var-args.")
    void testZeroLengthVarArgs() {
        assertThatThrownBy(() -> new ConcurrencyDoubleStatCollector().push(DoubleStatGeneratorType.MAX))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Values length must be at least 1 element.");
    }

}
