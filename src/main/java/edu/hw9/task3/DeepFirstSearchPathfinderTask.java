package edu.hw9.task3;

import edu.project2.cellbasedmaze.Cell;
import edu.project2.cellbasedmaze.Coordinate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class DeepFirstSearchPathfinderTask extends RecursiveTask<List<Coordinate>> {

    private final static int TURN_DELTA = 2;
    private final static int WALL_DELTA = 1;
    private final static int MAX_TURNS = 4;
    private final Cell[][] grid;
    private final Coordinate startPoint;
    private final Coordinate exitPoint;
    private Set<Coordinate> visited;

    DeepFirstSearchPathfinderTask(Cell[][] grid, Coordinate startPoint, Coordinate exitPoint) {
        this.grid = grid;
        this.startPoint = startPoint;
        this.exitPoint = exitPoint;
        this.visited = new HashSet<>();
    }

    private DeepFirstSearchPathfinderTask(
        Set<Coordinate> visited,
        Cell[][] grid,
        Coordinate startPoint,
        Coordinate exitPoint
    ) {
        this(grid, startPoint, exitPoint);
        this.visited = visited;
    }

    @Override
    protected List<Coordinate> compute() {
        List<Coordinate> route = new ArrayList<>();
        Coordinate current = startPoint;
        route.add(current);
        visited.add(current);
        List<Coordinate> turns = checkTurns(current).stream().filter(turn -> !visited.contains(turn)).toList();
        while (turns.size() == 1) {
            current = turns.getFirst();
            route.add(current);
            visited.add(current);
            if (exitPoint.equals(current)) {
                return route;
            }
            turns = checkTurns(current).stream().filter(turn -> !visited.contains(turn)).toList();
        }
        if (turns.isEmpty()) {
            return List.of();
        } else {
            DeepFirstSearchPathfinderTask subTask;
            List<DeepFirstSearchPathfinderTask> subTasks = new ArrayList<>();
            for (Coordinate turn : turns) {
                subTask = new DeepFirstSearchPathfinderTask(visited, grid, turn, exitPoint);
                subTask.fork();
                subTasks.add(subTask);
            }
            List<Coordinate> subRoute = subTasks
                .stream()
                .map(ForkJoinTask::join)
                .reduce((list1, list2) -> !list1.isEmpty() ? list1 : list2)
                .orElse(List.of());
            if (subRoute.isEmpty()) {
                return List.of();
            } else {
                route.addAll(subRoute);
                return route;
            }
        }
    }

    private List<Coordinate> checkTurns(Coordinate coordinate) {
        List<Coordinate> list = new ArrayList<>(MAX_TURNS);
        Coordinate lookingTurn;
        Cell lookingBorder;
        int[] wallDeltas = new int[] {-WALL_DELTA, WALL_DELTA};
        int[] turnDeltas = new int[] {-TURN_DELTA, TURN_DELTA};
        for (int i = 0; i < 2; i++) {
            lookingBorder = grid[coordinate.height() + wallDeltas[i]][coordinate.width()];
            lookingTurn = new Coordinate(coordinate.height() + turnDeltas[i], coordinate.width());
            if (lookingBorder.type() != Cell.Type.WALL) {
                list.add(lookingTurn);
            }
            lookingBorder = grid[coordinate.height()][coordinate.width() + wallDeltas[i]];
            lookingTurn = new Coordinate(coordinate.height(), coordinate.width() + turnDeltas[i]);
            if (lookingBorder.type() != Cell.Type.WALL) {
                list.add(lookingTurn);
            }
        }
        return list;
    }

}
