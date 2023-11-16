package edu.hw6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;

class Task6Test {

    @Test
    @DisplayName("Test Task6 method.")
    void task6Method() {
        //Then
        assertThatCode(Task6::getPortsInfo).doesNotThrowAnyException();
    }

}
