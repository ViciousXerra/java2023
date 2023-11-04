package edu.project2;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedgenerators.Generator;
import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import edu.project2.cellbasedpathfinders.BreadthFirstSearchPathfinder;
import edu.project2.cellbasedpathfinders.Pathfinder;
import edu.project2.renderers.CellBasedConsoleRenderer;
import edu.project2.renderers.Renderer;
import java.util.List;

public class demo {
    public static void main(String[] args) {
        Generator generator = new EllersAlgorithmGenerator(false, true);
        Maze maze = generator.generate(5, 10);
        Pathfinder pathfinder = new BreadthFirstSearchPathfinder();
        List<Coordinate> route = pathfinder.findPath(
            maze,
            new Coordinate(0, 0),
            new Coordinate(4, 0)
        );
        Renderer renderer = new CellBasedConsoleRenderer();
        String str1 = renderer.render(maze);
        String str2 = renderer.render(maze, route);
        System.out.println(str1);
        System.out.println(str2);
        String str = """
             __ __ __ __ __
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
             __ __ __ __ __
            |_* _* _* _*  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |__ __ __ __  *|
            |_* _* _* _* _*|""";

        String strBlock = """
             __ __ __ __ __ __ __ __ __ __
            |__|__|__|__|__|__|__|__|__|__|
            |__|__|__|__|__|__|__|__|__|__|
            |__|__|__|__|__|__|__|__|__|__|
            |__|__|__|__|__|__|__|__|__|__|
            |__|__|__|__|__|__|__|__|__|__|""";
    }
}
