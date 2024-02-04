package edu.hw9.task3;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedgenerators.Generator;
import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import edu.project2.cellbasedpathfinders.Pathfinder;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

class MultiThreadPathfinderTest {

    private static Maze getGeneratedDemoMazeWithoutBlock() {
        Generator generator = new BacktrackingGenerator(10, 5, false, false);
        return generator.generate();
    }

    private static Maze getGeneratedDemoMazeWithBlock() {
        Generator generator = new EllersAlgorithmGenerator(5, 10, false, true);
        return generator.generate();
    }

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
                getGeneratedDemoMazeWithoutBlock(),
                new DFSMultiThreadPathfinder(),
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
                getGeneratedDemoMazeWithoutBlock(),
                new DFSMultiThreadPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(1, 0)
            },
            {
                List.of(),
                getGeneratedDemoMazeWithoutBlock(),
                new DFSMultiThreadPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(0, 0)
            },
            {
                List.of(),
                getGeneratedDemoMazeWithoutBlock(),
                new DFSMultiThreadPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(0, 0)
            },
            {
                List.of(),
                getGeneratedDemoMazeWithBlock(),
                new DFSMultiThreadPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(4, 0)
            },
            {
                List.of(),
                getGeneratedDemoMazeWithBlock(),
                new DFSMultiThreadPathfinder(),
                new Coordinate(0, 0),
                new Coordinate(4, 0)
            }
        };
    }

    @ParameterizedTest
    @MethodSource("providePathfinderTest")
    void testMultiThreadPathfinderWithDemoMaze(
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
