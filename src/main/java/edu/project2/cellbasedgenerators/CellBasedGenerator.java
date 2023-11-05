package edu.project2.cellbasedgenerators;

import edu.project2.cellbasedmaze.Cell;
import edu.project2.cellbasedmaze.Maze;
import java.util.Random;

abstract class CellBasedGenerator implements Generator {

    protected final static int INDEX_DELTA = 2;
    protected final static Random RANDOM = new Random();

    private final static int DEFAULT_SIZE = 5;

    protected Cell[][] grid;

    private final int height;
    private final int width;
    private final boolean isRandomProvided;
    private final boolean isMazeBlocked;

    protected CellBasedGenerator(int height, int width, boolean isRandomProvided, boolean isMazeBlocked) {
        this.height = height;
        this.width = width;
        this.isRandomProvided = isRandomProvided;
        this.isMazeBlocked = isMazeBlocked;
    }

    protected CellBasedGenerator(boolean isRandomProvided, boolean isMazeBlocked) {
        this(DEFAULT_SIZE, DEFAULT_SIZE, isRandomProvided, isMazeBlocked);
    }

    @Override
    public final Maze generate() {
        Maze maze = new Maze(height, width);
        this.grid = maze.getGrid();
        if (isRandomProvided) {
            generateGrid();
        } else {
            generateDemoMaze(maze);
        }
        return maze;
    }

    protected final void generateGrid() {
        setUp();
        startGeneration();
    }

    protected abstract void setUp();

    protected abstract void startGeneration();

    protected final void placeCell(int wallHeight, int wallWidth, Cell.Type type) {
        grid[wallHeight][wallWidth] = new Cell(wallHeight, wallWidth, type);
    }

    private void generateDemoMaze(Maze maze) {
        this.grid = maze.getGrid();
        for (int curHeight = 0; curHeight < grid.length; curHeight++) {
            if (curHeight == 0 || curHeight == grid.length - 1) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                    placeCell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else if (curHeight % INDEX_DELTA == 0) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                    if (curWidth == grid[curHeight].length - INDEX_DELTA && !isMazeBlocked) {
                        continue;
                    }
                    placeCell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else {
                placeCell(curHeight, 0, Cell.Type.WALL);
                placeCell(curHeight, grid[curHeight].length - 1, Cell.Type.WALL);
                if (isMazeBlocked) {
                    for (int curWidth = INDEX_DELTA; curWidth < grid[curHeight].length - 1; curWidth += INDEX_DELTA) {
                        placeCell(curHeight, curWidth, Cell.Type.WALL);
                    }
                }
            }
        }
    }

}
