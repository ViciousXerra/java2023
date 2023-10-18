package edu.hw2;

import edu.hw2.task3.ConnectionException;
import edu.hw2.task3.DefaultConnectionManager;
import edu.hw2.task3.FaultyConnectionManager;
import edu.hw2.task3.PopularCommandExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task3Test {

    @Test
    @DisplayName("Test maxAttempts violation with FaultyConnectionManager")
    void testMaxAttemptsViolationWithFaultyConnectionManager() {
        //Given
        Class<ConnectionException> exceptionClass = ConnectionException.class;
        //When
        FaultyConnectionManager manager = new FaultyConnectionManager(1.0);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 10);
        //Then
        assertThatThrownBy(executor::updatePackages)
            .isInstanceOf(exceptionClass)
            .hasMessage("Exceeding the maximum number of attempts")
            .hasCauseInstanceOf(exceptionClass);
    }

    @Test
    @DisplayName("Test maxAttempts violation with DefaultConnectionManager")
    void testMaxAttemptsViolationWithDefaultConnectionManager() {
        //Given
        Class<ConnectionException> exceptionClass = ConnectionException.class;
        //When
        DefaultConnectionManager manager = new DefaultConnectionManager(1.0, 1.0);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 10);
        //Then
        assertThatThrownBy(executor::updatePackages)
            .isInstanceOf(exceptionClass)
            .hasMessage("Exceeding the maximum number of attempts")
            .hasCauseInstanceOf(exceptionClass);
    }

    @Test
    @DisplayName("Test successfully connection with faulty connection")
    void testSuccessfullyConnectionWithFaultyConnection() {
        //When
        DefaultConnectionManager manager = new DefaultConnectionManager(1.0, 0.0);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 1);
        //Then
        assertThatCode(executor::updatePackages).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test successfully connection with stable connection")
    void testSuccessfullyConnectionWithStableConnection() {
        //When
        DefaultConnectionManager manager = new DefaultConnectionManager(0.0, 0.0);
        PopularCommandExecutor executor = new PopularCommandExecutor(manager, 1);
        //Then
        assertThatCode(executor::updatePackages).doesNotThrowAnyException();
    }

}



