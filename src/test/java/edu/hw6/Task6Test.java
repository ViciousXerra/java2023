package edu.hw6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.ServerSocket;
import static org.assertj.core.api.Assertions.assertThat;

class Task6Test {

    @Test
    @DisplayName("Test Task6 method.")
    void task6Method() throws IOException {
        //Given
        String expected = "TCP       21     FTP (File Transfer Protocol)";
        //When
        String actual;
        //ftp plug
        try (ServerSocket serverSocket = new ServerSocket(21)) {
            actual = Task6.getPortsInfo();
        }
        assertThat(actual).contains(expected);
    }

}
