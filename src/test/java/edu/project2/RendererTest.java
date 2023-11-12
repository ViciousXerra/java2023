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
    private final static String LINE_SEPARATOR = System.lineSeparator();

    @Test
    @DisplayName("Rendering a before-known invariant maze of 1x1 size with Eller's algorithm generation.")
    void testRendererWithInvariantMazeSample1() {
        //Given
        String expected = " __ " + LINE_SEPARATOR + "|__|" + LINE_SEPARATOR;
        //When
        Generator generator = new EllersAlgorithmGenerator(1, 1, true, false);
        Renderer renderer = new CellBasedConsoleRenderer();
        String actual = renderer.render(generator.generate());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Rendering a before-known invariant maze of 1x1 size with backtracking algorithm generation.")
    void testRendererWithInvariantMazeSample2() {
        //Given
        String expected = " __ " + LINE_SEPARATOR + "|__|" + LINE_SEPARATOR;
        //When
        Generator generator = new BacktrackingGenerator(1, 1, true, false);
        Renderer renderer = new CellBasedConsoleRenderer();
        String actual = renderer.render(generator.generate());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static String getDemoMazeRepresentation(boolean withRoute) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            switch (i) {
                case 0:
                    builder.append(" __ __ __ __ __ ");
                    break;
                case 1:
                    if (withRoute) {
                        builder.append("|_* _* _* _*  *|");
                    } else {
                        builder.append("|__ __ __ __   |");
                    }
                    break;
                case 10:
                    if (withRoute) {
                        builder.append("|_* _* _* _* _*|");
                    } else {
                        builder.append("|__ __ __ __ __|");
                    }
                    break;
                default:
                    if (withRoute) {
                        builder.append("|__ __ __ __  *|");
                    } else {
                        builder.append("|__ __ __ __   |");
                    }
            }
            builder.append(LINE_SEPARATOR);
        }
        return builder.toString();
    }

    private static Object[][] provideDemoMazeWithoutBlock() {
        return new Object[][] {
            {
                getDemoMazeRepresentation(false),
                false,
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BacktrackingPathfinder()
            },
            {
                getDemoMazeRepresentation(true),
                true,
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BacktrackingPathfinder()
            },
            {
                getDemoMazeRepresentation(true),
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
