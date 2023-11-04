package edu.project2;

import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import edu.project2.cellbasedpathfinders.BacktrackingPathfinder;
import edu.project2.cellbasedpathfinders.BreadthFirstSearchPathfinder;
import edu.project2.cellbasedpathfinders.Pathfinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathfinderTest {

    private final static String NULL_RESTRICTION = "%s can't be null.";
    private final static String POINT_COORDINATE_RESTRICTION = "Point can't have these coordinates.";

    private static Object[][] providePathfinderTest() {
        return new Object[][] {
            {
                List.of(
                    new Coordinate(1, 1),
                    new Coordinate(1, 3),
                    new Coordinate(1, 5),
                    new Coordinate(1, 7),
                    new Coordinate(1, 9),
                    new Coordinate(3, 9),
                    new Coordinate(3, 7),
                    new Coordinate(3, 5),
                    new Coordinate(3, 3),
                    new Coordinate(3, 1)
                ),
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BacktrackingPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(1, 0)
            },
            {
                List.of(
                    new Coordinate(1, 1),
                    new Coordinate(1, 3),
                    new Coordinate(1, 5),
                    new Coordinate(1, 7),
                    new Coordinate(1, 9),
                    new Coordinate(3, 9),
                    new Coordinate(3, 7),
                    new Coordinate(3, 5),
                    new Coordinate(3, 3),
                    new Coordinate(3, 1)
                ),
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BreadthFirstSearchPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(1, 0)
            },
            {
                List.of(),
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BacktrackingPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(0, 0)
            },
            {
                List.of(),
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new BreadthFirstSearchPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(0, 0)
            },
            {
                List.of(),
                DemoMazeProvider.getGeneratedDemoMazeWithBlock(),
                new BacktrackingPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(4, 0)
            },
            {
                List.of(),
                DemoMazeProvider.getGeneratedDemoMazeWithBlock(),
                new BreadthFirstSearchPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(4, 0)
            }
        };
    }

    @ParameterizedTest
    @MethodSource("providePathfinderTest")
    void testPathfindersWithDemoMaze(
        List<Coordinate> expected,
        Maze maze,
        Pathfinder pathfinder,
        Coordinate startPoint,
        Coordinate exitPoint
    ) {
        //When
        List<Coordinate> actual = pathfinder.findPath(maze, startPoint, exitPoint);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    private static Object[][] provideRestrictedArgs() {
        return new Object[][] {
            {
                String.format(NULL_RESTRICTION, "Maze"),
                null,
                new Coordinate(0, 0),
                new Coordinate(0, 1)
            },
            {
                String.format(NULL_RESTRICTION, "Point"),
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                null,
                new Coordinate(0, 1)
            },
            {
                String.format(NULL_RESTRICTION, "Point"),
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new Coordinate(0, 1),
                null
            },
            {
                POINT_COORDINATE_RESTRICTION,
                DemoMazeProvider.getGeneratedDemoMazeWithoutBlock(),
                new Coordinate(0, 0),
                new Coordinate(10, 5)
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test renderer restriction.")
    @MethodSource("provideRestrictedArgs")
    void testPathfinderRestriction(
        String expected,
        Maze maze,
        Coordinate startPoint,
        Coordinate exitPoint
    ) {
        //When
        Pathfinder backtrackingPathfinder = new BacktrackingPathfinder();
        Pathfinder breadthFirstSearchPathfinder = new BreadthFirstSearchPathfinder();

        //Then
        assertThatThrownBy(() -> backtrackingPathfinder.findPath(maze, startPoint, exitPoint))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(expected);
        assertThatThrownBy(() -> breadthFirstSearchPathfinder.findPath(maze, startPoint, exitPoint))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(expected);
    }

}
