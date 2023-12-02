package edu.hw8.task3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordGeneratorTest {

    @ParameterizedTest
    @DisplayName("Test passing non-positive password length.")
    @ValueSource(ints = {Integer.MIN_VALUE, 0})
    void testNonPositivePasswordLength(int num) {
        assertThatThrownBy(() -> new PasswordGenerator(num))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password length must be positive num.");
    }

}
