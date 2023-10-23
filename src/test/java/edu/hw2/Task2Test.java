package edu.hw2;

import edu.hw2.task2.Rectangle;
import edu.hw2.task2.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class Task2Test {

    private static Arguments[] provideArgs() {
        return new Arguments[] {
            Arguments.of(new Rectangle()),
            Arguments.of(new Square())
        };
    }

    @ParameterizedTest
    @MethodSource("provideArgs")
    @DisplayName("\"Task 2\" example test")
    void testArea(Rectangle r) {
        //When
        r = r.setWidth(20);
        r = r.setHeight(10);
        //Then
        assertThat(r.area()).isEqualTo(200.0);
    }

    @Test
    @DisplayName("Test Rectangle inheritance")
    void testInstanceOf() {
        Square s = new Square();
        //Then
        assertThat(s).isInstanceOf(Rectangle.class);
    }

    private static Arguments[] provideArgsForDefaultAreas() {
        return new Arguments[] {
            Arguments.of(new Rectangle(), 600.0),
            Arguments.of(new Square(), 400.0)
        };
    }

    @ParameterizedTest
    @MethodSource("provideArgsForDefaultAreas")
    @DisplayName("Default areas test")
    void testDefaultArea(Rectangle r, double expectedArea) {
        assertThat(r.area()).isEqualTo(expectedArea);
    }

    private static Arguments[] provideArgsForAreaTest() {
        return new Arguments[] {
            Arguments.of(new Rectangle(), 40, 1200.0),
            Arguments.of(new Square(), 8, 160.0)
        };
    }

    @ParameterizedTest
    @MethodSource("provideArgsForAreaTest")
    @DisplayName("Test areas after setting field")
    void testSquareArea(Rectangle r, int newHeight, double expectedArea) {
        //When
        r = r.setHeight(newHeight);
        //Then
        assertThat(r.area()).isEqualTo(expectedArea);
    }

}
