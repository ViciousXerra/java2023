package edu.project2.mazes.cellbasedmaze;

public record Cell(int height, int width, Cell.Type type) {

    public enum Type {
        HORIZONTAL_WALL("___"),
        WALL("|"),
        PASSAGE("   "),
        ROUTE("");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
