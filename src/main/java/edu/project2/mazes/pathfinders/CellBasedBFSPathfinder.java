package edu.project2.mazes.pathfinders;

import edu.project2.mazes.cellbasedmaze.Cell;
import edu.project2.mazes.cellbasedmaze.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CellBasedBFSPathfinder implements CellBasedMazePathfinder {

    private final Cell[][] grid;
    private Coordinate startPoint;
    private Coordinate exitPoint;

    private final static int TURN_DELTA = 2;
    private final static int WALL_DELTA = 1;
    private final static int MAX_TURNS = 4;

    public CellBasedBFSPathfinder(Cell[][] grid, Coordinate startPoint, Coordinate exitPoint) {
        this.grid = grid;
        this.startPoint = startPoint;
        this.exitPoint = exitPoint;
    }

    @Override
    public List<Coordinate> findPath() {
        if (exitPoint.equals(startPoint)) {
            return List.of();
        }
        Queue<Coordinate> reached = new LinkedList<>();
        Map<Coordinate, Coordinate> parents = new HashMap<>();
        Coordinate parent;
        List<Coordinate> possibleTurns;
        reached.add(startPoint);
        parents.put(startPoint, startPoint);
        while (!reached.isEmpty() && !parents.containsKey(exitPoint)) {
            parent = reached.poll();
            possibleTurns = checkTurns(parent, parents.keySet());
            for (Coordinate turn : possibleTurns) {
                reached.add(turn);
                parents.put(turn, parent);
            }
        }
        if (parents.containsKey(exitPoint)) {
            return retrieveRouteCoordinates(parents);
        } else {
            return List.of();
        }
    }

    private List<Coordinate> retrieveRouteCoordinates(Map<Coordinate, Coordinate> parents) {
        List<Coordinate> route = new ArrayList<>();
        route.add(exitPoint);
        while (!route.getLast().equals(startPoint)) {
            route.add(parents.get(route.getLast()));
        }
        return route.reversed();
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
