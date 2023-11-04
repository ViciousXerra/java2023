package edu.project2.cellbasedpathfinders;

import edu.project2.cellbasedmaze.Cell;
import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

abstract class CellBasedPathfinder implements Pathfinder {

    private final static String NULL_RESTRICTION = "%s can't be null.";
    private final static String POINT_COORDINATE_RESTRICTION = "Point can't have these coordinates.";

    protected final static int TURN_DELTA = 2;
    protected final static int WALL_DELTA = 1;
    protected final static int MAX_TURNS = 4;

    protected Cell[][] grid;
    protected Coordinate startPoint;
    protected Coordinate exitPoint;

    @Override
    public List<Coordinate> findPath(Maze maze, Coordinate startPoint, Coordinate exitPoint) {
        validation(maze, startPoint, exitPoint);
        return generateRoute();
    }

    protected abstract List<Coordinate> generateRoute();

    protected final List<Coordinate> checkTurns(Coordinate coordinate, Set<Coordinate> visited) {
        List<Coordinate> list = new ArrayList<>(MAX_TURNS);
        Coordinate lookingTurn;
        Cell lookingBorder;
        int[] wallDeltas = new int[] {-WALL_DELTA, WALL_DELTA};
        int[] turnDeltas = new int[] {-TURN_DELTA, TURN_DELTA};
        for (int i = 0; i < 2; i++) {
            lookingBorder = grid[coordinate.height() + wallDeltas[i]][coordinate.width()];
            lookingTurn = new Coordinate(coordinate.height() + turnDeltas[i], coordinate.width());
            if (lookingBorder.type() != Cell.Type.WALL && !visited.contains(lookingTurn)) {
                list.add(lookingTurn);
            }
            lookingBorder = grid[coordinate.height()][coordinate.width() + wallDeltas[i]];
            lookingTurn = new Coordinate(coordinate.height(), coordinate.width() + turnDeltas[i]);
            if (lookingBorder.type() != Cell.Type.WALL && !visited.contains(lookingTurn)) {
                list.add(lookingTurn);
            }
        }
        return list;
    }

    private void validation(Maze maze, Coordinate startPoint, Coordinate exitPoint) {
        if (maze == null) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION, "Maze"));
        }
        this.grid = maze.getGrid();
        if (startPoint == null || exitPoint == null) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION, "Point"));
        }
        if (isValidCoordinate(startPoint) && isValidCoordinate(exitPoint)) {
            this.startPoint = new Coordinate((startPoint.height() << 1) + 1, (startPoint.width() << 1) + 1);
            this.exitPoint = new Coordinate((exitPoint.height() << 1) + 1, (exitPoint.width() << 1) + 1);
        } else {
            throw new IllegalArgumentException(POINT_COORDINATE_RESTRICTION);
        }
    }

    private boolean isValidCoordinate(Coordinate coordinate) {
        boolean heightCheck = coordinate.height() >= 0 && coordinate.height() < (grid.length - 1) >> 1;
        boolean widthCheck = coordinate.width() >= 0
            && coordinate.width() < (grid[coordinate.height()].length - 1) >> 1;
        return heightCheck && widthCheck;
    }

}
