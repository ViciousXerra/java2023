package edu.hw2.task3testpackage;

import edu.hw2.task3.ConnectionException;
import edu.hw2.task3.PopularCommandExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
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
        assertThatThrownBy(executor::updatePackages)
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
        assertThatThrownBy(executor::updatePackages)
            .isInstanceOf(exceptionClass)
            .hasCauseInstanceOf(exceptionClass)
            .hasMessage("Exceeding the maximum number of attempts");
    }

    @Test
    @DisplayName("Test successfuly connection with faulty connection")
    void testSuccessfullyConnectionWithFaultyConnection() {
        //When
        MockDefaultConnectionManager manager = new MockDefaultConnectionManager(true, false);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 10);
        //Then
        assertThatCode(executor::updatePackages)
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test successfuly connection with stable connection")
    void testSuccessfullyConnectionWithStableConnection() {
        //When
        MockDefaultConnectionManager manager = new MockDefaultConnectionManager(false, false);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 10);
        //Then
        assertThatCode(executor::updatePackages)
            .doesNotThrowAnyException();
    }

}



