package edu.project2.cellbasedmaze;

public record Cell(int row, int column, Cell.Type type) {

    public enum Type {
        WALL,
        PASSAGE,
        ROUTE
    }

}
