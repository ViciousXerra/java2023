package edu.project3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DriverTest {

    @Test
    @DisplayName("Passing null array.")
    void testNullArray() {
        assertThatThrownBy(() -> Driver.execute(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Args array or args can't be null.");
    }

    @Test
    @DisplayName("Passing null args.")
    void testNullArgs() {
        assertThatThrownBy(() -> Driver.execute(new String[] {
            "--path",
            null
        }))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Args array or args can't be null.");
    }

    @Test
    @DisplayName("Passing odd length array.")
    void testOddLengthArgs() {
        assertThatThrownBy(() -> Driver.execute(new String[] {
            "--path",
            "some_path",
            "--from"
        }))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid args length.");
    }

    @Test
    @DisplayName("Passing array with no \"--path\" flag at start.")
    void testNoPathFlag() {
        assertThatThrownBy(() -> Driver.execute(new String[] {
            "some_path",
            "--from"
        }))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Args must be start with \"--path\" execution flag.");
    }

}
