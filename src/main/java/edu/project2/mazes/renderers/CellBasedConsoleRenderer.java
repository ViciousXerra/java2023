package edu.project2.mazes.renderers;

import edu.project2.mazes.cellbasedmaze.Cell;
import edu.project2.mazes.cellbasedmaze.Coordinate;
import java.util.Arrays;
import java.util.List;

public class CellBasedConsoleRenderer implements ConsoleRenderer {

    private final Cell[][] grid;
    private final List<Coordinate> route;

    private final static String VERTICAL_WALL = "|";
    private final static String NO_VERTICAL_WALL = " ";
    private final static String HORIZONTAL_WALL = "__";
    private final static String WITHOUT_HORIZONTAL_WALL = "  ";
    private final static String ROUTE_WITH_WALL = "_*";
    private final static String ROUTE_WITHOUT_WALL = " *";

    public CellBasedConsoleRenderer(Cell[][] grid, List<Coordinate> route) {
        this.grid = grid;
        this.route = route;
    }

    @Override
    public String render(boolean withRoute) {
        int rows = (grid.length - 1) >> 1;
        int columns = (grid[0].length - 1) >> 1;
        Cell[][] source;
        source = withRoute ? getModifiedGridCopy() : grid;
        StringBuilder builder = new StringBuilder(printFirstRow(columns));
        for (int i = 0; i < rows; i++) {
            builder.append(VERTICAL_WALL);
            for (int j = 0; j < columns; j++) {
                if (source[(i << 1) + 1][(j << 1) + 1].type() == Cell.Type.ROUTE
                    && source[(i << 1) + 2][(j << 1) + 1].type() == Cell.Type.WALL) {
                    builder.append(ROUTE_WITH_WALL);
                } else if (source[(i << 1) + 1][(j << 1) + 1].type() == Cell.Type.ROUTE) {
                    builder.append(ROUTE_WITHOUT_WALL);
                } else if (source[(i << 1) + 2][(j << 1) + 1].type() == Cell.Type.WALL) {
                    builder.append(HORIZONTAL_WALL);
                } else {
                    builder.append(WITHOUT_HORIZONTAL_WALL);
                }
                if (source[(i << 1) + 1][(j << 1) + 2].type() == Cell.Type.WALL) {
                    builder.append(VERTICAL_WALL);
                } else {
                    builder.append(NO_VERTICAL_WALL);
                }
            }
            builder.append('\n');
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
        outerWall.append('\n');
        return outerWall.toString();
    }

}
