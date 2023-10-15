package edu.hw2.task3testpackage;

import edu.hw2.task3.ConnectionException;
import edu.hw2.task3.PopularCommandExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task3Test {

    @Test
    @DisplayName("Test maxAttempts violation with MockFaultyConnectionManager")
    void testMaxAttemptsViolationWithMockFaultyConnectionManager() {
        //Given
        Class<ConnectionException> exceptionClass = ConnectionException.class;
        //When
        MockFaultyConnectionManager manager = new MockFaultyConnectionManager(true);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 10);
        //Then
        assertThatThrownBy(() -> {
            executor.tryExecute("ls -la");
        })
            .isInstanceOf(exceptionClass)
            .hasCauseInstanceOf(exceptionClass)
            .hasMessage("Exceeding the maximum number of attempts");
    }

    @Test
    @DisplayName("Test maxAttempts violation with MockDefaultConnectionManager")
    void testMaxAttemptsViolationWithMockDefaultConnectionManager() {
        //Given
        Class<ConnectionException> exceptionClass = ConnectionException.class;
        //When
        MockDefaultConnectionManager manager = new MockDefaultConnectionManager(true, true);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 10);
        //Then
        assertThatThrownBy(() -> {
            executor.tryExecute("ls -la");
        })
            .isInstanceOf(exceptionClass)
            .hasCauseInstanceOf(exceptionClass)
            .hasMessage("Exceeding the maximum number of attempts");
    }

}



