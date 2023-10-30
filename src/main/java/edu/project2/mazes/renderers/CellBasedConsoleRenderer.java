package edu.project2.mazes.renderers;

import edu.project2.mazes.cellbasedmaze.Cell;
import edu.project2.mazes.cellbasedmaze.Coordinate;
import java.util.List;

public class CellBasedConsoleRenderer implements ConsoleRenderer {

    private final Cell[][] grid;
    private final List<Coordinate> route;

    public CellBasedConsoleRenderer(Cell[][] grid, List<Coordinate> route) {
        this.grid = grid;
        this.route = route;
    }

    @Override
    public String render(boolean withRoute) {
        StringBuilder builder = new StringBuilder();
        if (withRoute) {

        } else {
            for (Cell[] row : grid) {
                for (Cell cell : row) {
                    builder.append(cell.type().getValue());
                }
                builder.append('\n');
            }
        }
        return builder.toString();
    }

}
