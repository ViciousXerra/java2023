package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task1Test {

    private final static String NULL_RESTRICTION = "%s can't be null.";
    private final static String ZERO_DURATION_MESSAGE = "Session duration time can't be zero.";
    private final static String INVALID_PLACEMENT_MESSAGE = "Invalid start and end time placement.";
    private final static String MISMATCH_MESSAGE = "Invalid format.";

    private static String[] provideTestWithData() {
        return new String[] {
            //2h40min
            "2023-05-20, 20:50 - 2023-05-20, 23:30",
            //4h00min
            "2023-05-21, 23:30 - 2023-05-22, 03:30",
            //3h15min
            "2023-05-19, 07:00 - 2023-05-19, 10:15",
            //1h20min
            "2023-05-25, 23:50 - 2023-05-26, 01:10",
            //average time - 2h48min
        };
    }

    @Test
    @DisplayName("Test passing a null string.")
    void testNullArg() {
        String[] args = provideTestWithData();
        args[args.length - 1] = null;
        //Then
        assertThatThrownBy(() -> Task1.getAvgSessionTime(args))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(String.format(NULL_RESTRICTION, "Session string"));
    }

    @Test
    @DisplayName("Test passing a null array.")
    void testNullArrayRef() {
        String[] args = null;
        //Then
        assertThatThrownBy(() -> Task1.getAvgSessionTime(args))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(String.format(NULL_RESTRICTION, "Passing arg"));
    }

    @Test
    @DisplayName("Test passing a session log with zero time length.")
    void testZeroSessionTimeLength() {
        //Then
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-05-20, 20:50 - 2023-05-20, 20:50"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ZERO_DURATION_MESSAGE);
    }

    @Test
    @DisplayName("Test invalid time placement.")
    void testInvalidTimePlacement() {
        //Then
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-05-20, 21:50 - 2023-05-20, 20:50"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(INVALID_PLACEMENT_MESSAGE);
    }

    @Test
    @DisplayName("Test invalid format.")
    void testInvalidFormat() {
        //Then
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-05-20, 20:50 - 2023-05-20, 20:60"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MISMATCH_MESSAGE);
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-05-20, 20:50 - 2023-05-20, 21:0"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MISMATCH_MESSAGE);
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-05-20, 20:50 - 2023-05-20, 25:30"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MISMATCH_MESSAGE);
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-05-20, 20:500 - 2023-05-20, 20:55"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MISMATCH_MESSAGE);
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-00-20, 20:500 - 2023-05-20, 20:55"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MISMATCH_MESSAGE);
        assertThatThrownBy(() -> Task1.getAvgSessionTime("2023-01-32, 20:50 - 2023-05-20, 20:55"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MISMATCH_MESSAGE);
    }

    @Test
    @DisplayName("Test average session time.")
    void testAvgSessionTime() {
        //Given
        Duration expected = Duration.ofHours(2L).plusMinutes(48L);
        //When
        Duration actual = Task1.getAvgSessionTime(provideTestWithData());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

}
