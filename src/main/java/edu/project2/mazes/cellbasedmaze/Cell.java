package edu.project2.mazes.cellbasedmaze;

public record Cell(int height, int width, Cell.Type type) {

    public enum Type {
        WALL,
        PASSAGE,
        ROUTE
    }

}
