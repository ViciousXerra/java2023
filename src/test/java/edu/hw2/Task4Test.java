package edu.hw2;

import edu.hw2.task4.CallingInfo;
import edu.hw2.task4.Task4;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Task4Test {

    private static class Example1 {

        private CallingInfo methodExample1() {
            return Task4.callingInfo();
        }

    }

    private static class Example2Wrapper {

        private CallingInfo example2Caller() {
            return new Example2().methodExample2();
        }

        private static class Example2 {

            private CallingInfo methodExample2() {
                return Task4.callingInfo();
            }

        }

    }

    @Test
    @DisplayName("Test with class Example1")
    void testExample1() {
        //Given
        CallingInfo expected = new CallingInfo("edu.hw2.Task4Test$Example1", "methodExample1");
        //When
        CallingInfo info = new Example1().methodExample1();
        //Then
        assertThat(info).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test with nested class Example2")
    void testExample2() {
        //Given
        CallingInfo expected = new CallingInfo("edu.hw2.Task4Test$Example2Wrapper$Example2", "methodExample2");
        //When
        CallingInfo info = new Example2Wrapper().example2Caller();
        //Then
        assertThat(info).isEqualTo(expected);
    }

}
