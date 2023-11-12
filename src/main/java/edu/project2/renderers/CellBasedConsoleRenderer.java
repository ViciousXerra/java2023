package edu.project2.renderers;

import edu.project2.cellbasedmaze.Cell;
import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedmaze.Maze;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CellBasedConsoleRenderer implements Renderer {

    private final static String NULL_RESTRICTION = "%s can't be null.";

    private final static String VERTICAL_WALL = "|";
    private final static String NO_VERTICAL_WALL = " ";
    private final static String HORIZONTAL_WALL = "__";
    private final static String WITHOUT_HORIZONTAL_WALL = "  ";
    private final static String ROUTE_WITH_WALL = "_*";
    private final static String ROUTE_WITHOUT_WALL = " *";
    private final static String LINE_SEPARATOR = System.lineSeparator();
    private final static int INDEX_DELTA = 2;

    private Cell[][] grid;
    private List<Coordinate> route;

    @Override
    public String render(Maze maze) {
        validation(maze);
        return buildMaze(false);
    }

    @Override
    public String render(Maze maze, List<Coordinate> route) {
        validation(maze, route);
        return buildMaze(true);
    }

    private String buildMaze(boolean withRoute) {
        int rows = (grid.length - 1) >> 1;
        int columns = (grid[0].length - 1) >> 1;
        Cell[][] source = withRoute ? getModifiedGridCopy() : grid;
        StringBuilder builder = new StringBuilder(printFirstRow(columns));
        for (int i = 0; i < rows; i++) {
            builder.append(VERTICAL_WALL);
            for (int j = 0; j < columns; j++) {
                processHorizontalPart(source, builder, i, j);
                processVerticalPart(source, builder, i, j);
            }
            builder.append(LINE_SEPARATOR);
        }
        return builder.toString();
    }

    private Cell[][] getModifiedGridCopy() {
        Cell[][] copy = Arrays.copyOf(grid, grid.length);
        for (Coordinate coordinate : route) {
            copy[coordinate.height()][coordinate.width()] =
                new Cell(coordinate.height(), coordinate.width(), Cell.Type.ROUTE);
        }
        return copy;
    }

    private String printFirstRow(int columns) {
        StringBuilder outerWall = new StringBuilder(NO_VERTICAL_WALL);
        for (int i = 0; i < columns; i++) {
            outerWall.append(HORIZONTAL_WALL);
            outerWall.append(NO_VERTICAL_WALL);
        }
        outerWall.append(LINE_SEPARATOR);
        return outerWall.toString();
    }

    private void processHorizontalPart(Cell[][] source, StringBuilder builder, int i, int j) {
        if (source[(i << 1) + 1][(j << 1) + 1].type() == Cell.Type.ROUTE
            && source[(i << 1) + INDEX_DELTA][(j << 1) + 1].type() == Cell.Type.WALL) {
            builder.append(ROUTE_WITH_WALL);
        } else if (source[(i << 1) + 1][(j << 1) + 1].type() == Cell.Type.ROUTE) {
            builder.append(ROUTE_WITHOUT_WALL);
        } else if (source[(i << 1) + INDEX_DELTA][(j << 1) + 1].type() == Cell.Type.WALL) {
            builder.append(HORIZONTAL_WALL);
        } else {
            builder.append(WITHOUT_HORIZONTAL_WALL);
        }
    }

    private void processVerticalPart(Cell[][] source, StringBuilder builder, int i, int j) {
        if (source[(i << 1) + 1][(j << 1) + INDEX_DELTA].type() == Cell.Type.WALL) {
            builder.append(VERTICAL_WALL);
        } else {
            builder.append(NO_VERTICAL_WALL);
        }
    }

    private void validation(Maze maze) {
        if (maze == null) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION, "Maze"));
        }
        this.grid = maze.getGrid();
    }

    private void validation(Maze maze, List<Coordinate> route) {
        validation(maze);
        if (route == null || !isRouteFilled(route)) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION, "Route list or coordinate elements"));
        }
        this.route = route;
    }

    private boolean isRouteFilled(List<Coordinate> route) {
        return route.stream().allMatch(Objects::nonNull);
    }

}
