package edu.hw8.task3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbstractPasswordBruteForceTest {

    @Test
    @DisplayName("Test passing null path.")
    void testnullPath() {
        assertThatThrownBy(() -> new SingleThreadMdHashPasswordBruteForce(null, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path can't be null.");
    }

}
