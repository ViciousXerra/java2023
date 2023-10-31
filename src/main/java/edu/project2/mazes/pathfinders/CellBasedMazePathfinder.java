package edu.project2.mazes.pathfinders;

import edu.project2.mazes.cellbasedmaze.Cell;
import edu.project2.mazes.cellbasedmaze.Coordinate;
import java.util.List;

public interface CellBasedMazePathfinder {
    List<Coordinate> findPath(Cell[][] grid, Coordinate startPoint, Coordinate exitPoint);
}