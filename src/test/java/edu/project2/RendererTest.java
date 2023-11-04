package edu.project2;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedgenerators.Generator;
import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import edu.project2.cellbasedpathfinders.BacktrackingPathfinder;
import edu.project2.cellbasedpathfinders.BreadthFirstSearchPathfinder;
import edu.project2.cellbasedpathfinders.Pathfinder;
import edu.project2.renderers.CellBasedConsoleRenderer;
import edu.project2.renderers.Renderer;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RendererTest {

    private final static String NULL_RESTRICTION = "%s can't be null.";

    @Test
    @DisplayName("Rendering a before-known invariant maze of 1x1 size with Eller's algorithm generation.")
    void testRendererWithInvariantMazeSample1() {
        //Given
        String expected = " __ \n|__|\n";
        //When
        Generator generator = new EllersAlgorithmGenerator(true, false);
        Renderer renderer = new CellBasedConsoleRenderer();
        String actual = renderer.render(generator.generate(1, 1));
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Rendering a before-known invariant maze of 1x1 size with backtracking algorithm generation.")
    void testRendererWithInvariantMazeSample2() {
        //Given
        String expected = " __ \n|__|\n";
        //When
        Generator generator = new BacktrackingGenerator(true, false);
        Renderer renderer = new CellBasedConsoleRenderer();
        String actual = renderer.render(generator.generate(1, 1));
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideDemoMazeWithoutBlock() {
        return new Object[][] {
            {
                """
             __ __ __ __ __\040
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __   |
            |__ __ __ __ __|
            """,
                false,
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BacktrackingPathfinder()
            },
            {
                """
             __ __ __ __ __\040
            |_* _* _* _*  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |_* _* _* _* _*|
            """,
                true,
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BacktrackingPathfinder()
            },
            {
                """
             __ __ __ __ __\040
            |_* _* _* _*  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |_* _* _* _* _*|
            """,
                true,
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BreadthFirstSearchPathfinder()
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test renderer with demo maze.")
    @MethodSource("provideDemoMazeWithoutBlock")
    void testDemoMaze(String expected, boolean withRoute, Maze maze, Pathfinder pathfinder) {
        //When
        Renderer renderer = new CellBasedConsoleRenderer();
        String actual;
        if (withRoute) {
            List<Coordinate> route = pathfinder.findPath(
                maze,
                new Coordinate(0, 0),
                new Coordinate(9, 0)
            );
            actual = renderer.render(maze, route);
        } else {
            actual = renderer.render(maze);
        }
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideRestrictedArgs() {
        return new Object[][] {
            {
                String.format(NULL_RESTRICTION, "Maze"),
                null,
                null,
                false
            },
            {
                String.format(NULL_RESTRICTION, "Route list or coordinate elements"),
                new Maze(),
                null,
                true
            },
            {
                String.format(NULL_RESTRICTION, "Route list or coordinate elements"),
                new Maze(),
                Arrays.asList(
                    new Coordinate(0, 0),
                    new Coordinate(0, 2),
                    new Coordinate(0, 4),
                    null,
                    new Coordinate(2, 4)
                ),
                true
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test renderer restriction.")
    @MethodSource("provideRestrictedArgs")
    void testRendererRestriction(String expected, Maze maze, List<Coordinate> route, boolean withRoute) {
        Renderer renderer = new CellBasedConsoleRenderer();
        //Then
        if (withRoute) {
            assertThatThrownBy(
                () -> renderer.render(maze, route)
            )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expected);
        } else {
            assertThatThrownBy(
                () -> renderer.render(maze, route)
            )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expected);
        }
    }

}
