package edu.project2.cellbasedmaze;

public final class Maze {

    private final static String SIZE_RESTRICTION_MESSAGE =
        "Unable to create maze with this values of height and width.";
    private final static int DEFAULT_SIZE = 5;

    private final Cell[][] grid;

    public Maze() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public Maze(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException(SIZE_RESTRICTION_MESSAGE);
        }
        int actualHeight = calcActualSize(height);
        int actualWidth = calcActualSize(width);
        grid = new Cell[actualHeight][actualWidth];
        initGridWithPassages();
    }

    public Cell[][] getGrid() {
        return grid;
    }

    private void initGridWithPassages() {
        for (int curHeight = 0; curHeight < grid.length; curHeight++) {
            for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                grid[curHeight][curWidth] = new Cell(curHeight, curWidth, Cell.Type.PASSAGE);
            }
        }
    }

    private int calcActualSize(int value) {
        return (value << 1) + 1;
    }
}
