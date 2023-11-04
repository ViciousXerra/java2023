package edu.project2;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import edu.project2.cellbasedpathfinders.BacktrackingPathfinder;
import edu.project2.cellbasedpathfinders.BreadthFirstSearchPathfinder;
import edu.project2.cellbasedpathfinders.Pathfinder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class PathfinderTest {

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
    void test(
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

}
