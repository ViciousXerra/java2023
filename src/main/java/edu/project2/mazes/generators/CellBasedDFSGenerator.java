package edu.project2.mazes.generators;

import edu.project2.mazes.cellbasedmaze.Cell;
import edu.project2.mazes.cellbasedmaze.Coordinate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class CellBasedDFSGenerator implements CellBasedMazeGenerator {

    private final static int MAX_NEIGHBOURS_SIZE = 4;
    private final static int DEFAULT_DELTA = 2;
    private final static Coordinate STARTING_POINT = new Coordinate(1, 1);
    private final static Random RANDOM = new Random();

    private final Cell[][] grid;

    public CellBasedDFSGenerator(Cell[][] grid) {
        this.grid = grid;
    }

    @Override
    public void generate() {
        setUp();
        startGeneration();
    }

    private void setUp() {
        for (int curHeight = 0; curHeight < grid.length; curHeight++) {
            if (curHeight % 2 == 0) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                    grid[curHeight][curWidth] = new Cell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth += 2) {
                    grid[curHeight][curWidth] = new Cell(curHeight, curWidth, Cell.Type.WALL);
                }
            }
        }
    }

    private void startGeneration() {
        Set<Coordinate> visited = new HashSet<>();
        visited.add(STARTING_POINT);
        Stack<Coordinate> route = new Stack<>();
        route.push(STARTING_POINT);
        Coordinate current;
        Coordinate toLinkUp;
        List<Coordinate> currentNeighbours;
        while (!route.isEmpty()) {
            current = route.peek();
            currentNeighbours = getUnvisitedNeighbourCells(current, visited);
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

    private List<Coordinate> getUnvisitedNeighbourCells(Coordinate current, Set<Coordinate> visited) {
        List<Coordinate> candidates = new ArrayList<>(MAX_NEIGHBOURS_SIZE);
        int[] deltas = new int[] {-DEFAULT_DELTA, DEFAULT_DELTA};
        Coordinate looking;
        for (int delta : deltas) {
            looking = new Coordinate(current.height() + delta, current.width());
            if (looking.height() < grid.length
                && looking.height() > 0
                && !visited.contains(looking)) {
                candidates.add(looking);
            }
            looking = new Coordinate(current.height(), current.width() + delta);
            if (looking.width() < grid[looking.height()].length
                && looking.width() > 0
                && !visited.contains(looking)) {
                candidates.add(looking);
            }
        }
        return candidates;
    }

    private void carvePassage(Coordinate first, Coordinate second) {
        int targetHeight = (first.height() + second.height()) >> 1;
        int targetWidth = (first.width() + second.width()) >> 1;
        grid[targetHeight][targetWidth] = new Cell(targetHeight, targetWidth, Cell.Type.PASSAGE);
    }

}
