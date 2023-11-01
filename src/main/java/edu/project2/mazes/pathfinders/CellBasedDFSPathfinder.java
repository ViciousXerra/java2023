package edu.project2.mazes.pathfinders;

import edu.project2.mazes.cellbasedmaze.Cell;
import edu.project2.mazes.cellbasedmaze.Coordinate;
import java.util.*;

public class CellBasedDFSPathfinder implements CellBasedMazePathfinder {

    private final static Random RANDOM = new Random();
    private final Cell[][] grid;
    private Coordinate startPoint;
    private Coordinate exitPoint;
    private final static int TURN_DELTA = 2;
    private final static int WALL_DELTA = 1;
    private final static int MAX_TURNS = 4;

    public CellBasedDFSPathfinder(Cell[][] grid, Coordinate startPoint, Coordinate exitPoint) {
        this.grid = grid;
        this.startPoint = startPoint;
        this.exitPoint = exitPoint;
    }

    @Override
    public List<Coordinate> findPath() {
        Set<Coordinate> visited = new HashSet<>();
        visited.add(startPoint);
        Stack<Coordinate> route = new Stack<>();
        Coordinate current = startPoint;
        List<Coordinate> possibleTurns;
        while (!current.equals(exitPoint)) {
            possibleTurns = checkTurns(current, visited);
            if (possibleTurns.size() != 0) {
                route.push(current);
                current = possibleTurns.get(RANDOM.nextInt(possibleTurns.size()));
                visited.add(current);
            } else {
                current = route.pop();
            }
        }
        if (route.isEmpty()) {
            return List.of();
        } else {
            route.push(current);
            return route;
        }
    }

    private List<Coordinate> checkTurns(Coordinate coordinate, Set<Coordinate> visited) {
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
            if (lookingBorder.type() != Cell.Type.WALL
                && !visited.contains(lookingTurn)) {
                list.add(lookingTurn);
            }
        }
        return list;
    }
}
