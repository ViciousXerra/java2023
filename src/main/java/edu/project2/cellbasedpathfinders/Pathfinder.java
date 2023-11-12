package edu.project2.cellbasedpathfinders;

import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import java.util.List;

public interface Pathfinder {

    List<Coordinate> findPath(Maze maze, Coordinate startPoint, Coordinate exitPoint);

}
