package edu.project2.cellbasedgenerators;

import edu.project2.cellbasedmaze.Cell;
import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public final class BacktrackingGenerator extends CellBasedGenerator {

    private final static int MAX_NEIGHBOURS_SIZE = 4;
    private final static Coordinate STARTING_POINT = new Coordinate(1, 1);

    public BacktrackingGenerator(boolean isRandomProvided, boolean isMazeBlocked) {
        super(isRandomProvided, isMazeBlocked);
    }

    @Override
    protected void initializeMaze(Maze maze) {
        this.grid = maze.getGrid();
        generateGrid();
        initFlag = true;
    }

    @Override
    protected void setUp() {
        for (int curHeight = 0; curHeight < grid.length; curHeight++) {
            if (curHeight % INDEX_DELTA == 0) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                    placeCell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth += INDEX_DELTA) {
                    placeCell(curHeight, curWidth, Cell.Type.WALL);
                }
            }
        }
    }

    @Override
    protected void startGeneration() {
        Set<Coordinate> visited = new HashSet<>();
        visited.add(STARTING_POINT);
        Stack<Coordinate> route = new Stack<>();
        route.push(STARTING_POINT);
        Coordinate current;
        Coordinate toLinkUp;
        List<Coordinate> currentNeighbours;
        while (!route.isEmpty()) {
            current = route.peek();
            currentNeighbours = getPossibleTurns(current, visited);
            if (currentNeighbours.isEmpty()) {
                route.pop();
            } else {
                toLinkUp = currentNeighbours.get(RANDOM.nextInt(currentNeighbours.size()));
                carvePassage(current, toLinkUp);
                route.push(toLinkUp);
                visited.add(toLinkUp);
            }
        }
    }

    private List<Coordinate> getPossibleTurns(Coordinate current, Set<Coordinate> visited) {
        List<Coordinate> possibleTurns = new ArrayList<>(MAX_NEIGHBOURS_SIZE);
        int[] deltas = new int[] {-INDEX_DELTA, INDEX_DELTA};
        Coordinate looking;
        for (int delta : deltas) {
            looking = new Coordinate(current.height() + delta, current.width());
            if (isaValidVerticalTurn(visited, looking)) {
                possibleTurns.add(looking);
            }
            looking = new Coordinate(current.height(), current.width() + delta);
            if (isaValidHorizontalTurn(visited, looking)) {
                possibleTurns.add(looking);
            }
        }
        return possibleTurns;
    }

    private boolean isaValidVerticalTurn(Set<Coordinate> visited, Coordinate looking) {
        return looking.height() < grid.length
            && looking.height() > 0
            && !visited.contains(looking);
    }

    private boolean isaValidHorizontalTurn(Set<Coordinate> visited, Coordinate looking) {
        return looking.width() < grid[looking.height()].length
            && looking.width() > 0
            && !visited.contains(looking);
    }

    private void carvePassage(Coordinate first, Coordinate second) {
        int targetHeight = (first.height() + second.height()) >> 1;
        int targetWidth = (first.width() + second.width()) >> 1;
        placeCell(targetHeight, targetWidth, Cell.Type.PASSAGE);
    }

}
