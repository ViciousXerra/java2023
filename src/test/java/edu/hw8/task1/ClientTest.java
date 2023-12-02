package edu.hw8.task1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClientTest {

    @Test
    @DisplayName("Passing null message.")
    void testNullMessage() {
        assertThatThrownBy(() -> new Client(Utils.getFreePort(), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Message string can't be null.");
    }

    @ParameterizedTest
    @DisplayName("Passing illegal port.")
    @ValueSource(ints = {Integer.MIN_VALUE, 0, 65536})
    void testIllegalPort(int port) {
        assertThatThrownBy(() -> new Client(port, "hello"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Port number must be between 0 and 65535.");
    }

}
