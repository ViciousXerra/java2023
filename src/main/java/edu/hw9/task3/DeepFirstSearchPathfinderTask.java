package edu.hw9.task3;

import edu.project2.cellbasedmaze.Cell;
import edu.project2.cellbasedmaze.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class DeepFirstSearchPathfinderTask extends RecursiveTask<List<Coordinate>> {

    private final static int TURN_DELTA = 2;
    private final static int WALL_DELTA = 1;
    private final static int MAX_TURNS = 4;
    private final Cell[][] grid;
    private final Coordinate startPoint;
    private final Coordinate exitPoint;

    DeepFirstSearchPathfinderTask(Cell[][] grid, Coordinate startPoint, Coordinate exitPoint) {
        this.grid = grid;
        this.startPoint = startPoint;
        this.exitPoint = exitPoint;
    }

    @Override
    protected List<Coordinate> compute() {
        List<Coordinate> route = new ArrayList<>();
        List<DeepFirstSearchPathfinderTask> subTasks = new ArrayList<>();
        Coordinate current = startPoint;
        route.add(current);
        if (exitPoint.equals(startPoint)) {
            return route;
        }
        List<Coordinate> turns = checkTurns(current);
        while (turns.size() == 1) {
            current = turns.get(0);
            route.add(current);
            if (route.getLast().equals(exitPoint)) {
                return route;
            }
            turns = checkTurns(current);
        }
        if (turns.isEmpty()) {
            return List.of();
        } else {
            turns.forEach(turn -> subTasks.add(new DeepFirstSearchPathfinderTask(grid, turn, exitPoint)));
            subTasks.forEach(ForkJoinTask::fork);
            List<Coordinate> res = subTasks
                .stream()
                .map(ForkJoinTask::join)
                .reduce((list1, list2) -> list1.isEmpty() ? list2 : list1)
                .orElse(List.of());
            route.addAll(res);
            return route.getLast().equals(exitPoint) ? route : List.of();
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
