package edu.project4.plotters;

public record Dimension(double width, double height, double x, double y) {

    public boolean contains(Point point) {
        boolean inRangeOfAxisX = point.x() >= x && point.x() < x + width;
        boolean inRangeOfAxisY = point.y() >= y && point.y() < y + width;
        return inRangeOfAxisX && inRangeOfAxisY;
    }

}
