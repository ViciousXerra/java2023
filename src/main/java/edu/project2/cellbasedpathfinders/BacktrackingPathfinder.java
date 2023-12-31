package edu.project2.cellbasedpathfinders;

import edu.project2.cellbasedmaze.Coordinate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class BacktrackingPathfinder extends CellBasedPathfinder {

    private final static Random RANDOM = new Random();

    @Override
    protected List<Coordinate> generateRoute() {
        if (startPoint.equals(exitPoint)) {
            return List.of();
        }
        Set<Coordinate> visited = new HashSet<>();
        visited.add(startPoint);
        Stack<Coordinate> route = new Stack<>();
        Coordinate current = startPoint;
        List<Coordinate> possibleTurns;
        while (!current.equals(exitPoint)) {
            possibleTurns = checkTurns(current, visited);
            if (!possibleTurns.isEmpty()) {
                route.push(current);
                current = possibleTurns.get(RANDOM.nextInt(possibleTurns.size()));
                visited.add(current);
            } else if (route.isEmpty()) {
                return List.of();
            } else {
                current = route.pop();
            }
        }
        route.push(current);
        return route;
    }

}
