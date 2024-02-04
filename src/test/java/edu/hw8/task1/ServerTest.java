package edu.hw8.task1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServerTest {

    private final static int PORT;

    static {
        try {
            PORT = Utils.getFreePort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @DisplayName("Passing non-positive num of max clients.")
    @ValueSource(ints = {0, Integer.MIN_VALUE})
    void test(int maxClientsNum) {
        assertThatThrownBy(() -> new Server(PORT, 10000, maxClientsNum))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Number of clients must be a positive num.");
    }

    @ParameterizedTest
    @DisplayName("Passing illegal port.")
    @ValueSource(ints = {Integer.MIN_VALUE, 0, 65536})
    void testIllegalPort(int port) {
        assertThatThrownBy(() -> new Server(port, 10000, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Port number must be between 0 and 65535.");
    }

    @ParameterizedTest
    @DisplayName("Passing illegal timeout.")
    @ValueSource(longs = {Long.MIN_VALUE, 0})
    void testIllegalPort(long timeout) {
        assertThatThrownBy(() -> new Server(PORT, timeout, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Timeout duration in milliseconds must be a positive num.");
    }

}
