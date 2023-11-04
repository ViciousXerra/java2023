package edu.project2.cellbasedpathfinders;

import edu.project2.cellbasedmaze.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BreadthFirstSearchPathfinder extends CellBasedPathfinder {

    @Override
    protected List<Coordinate> generateRoute() {
        if (exitPoint.equals(startPoint)) {
            return List.of();
        }
        Queue<Coordinate> reached = new LinkedList<>();
        Map<Coordinate, Coordinate> parents = new HashMap<>();
        Coordinate parent;
        List<Coordinate> possibleTurns;
        reached.add(startPoint);
        parents.put(startPoint, startPoint);
        while (!reached.isEmpty() && !parents.containsKey(exitPoint)) {
            parent = reached.poll();
            possibleTurns = checkTurns(parent, parents.keySet());
            for (Coordinate turn : possibleTurns) {
                reached.add(turn);
                parents.put(turn, parent);
            }
        }
        if (parents.containsKey(exitPoint)) {
            return retrieveRouteCoordinates(parents);
        } else {
            return List.of();
        }
    }

    private List<Coordinate> retrieveRouteCoordinates(Map<Coordinate, Coordinate> parents) {
        List<Coordinate> route = new ArrayList<>();
        route.add(exitPoint);
        while (!route.getLast().equals(startPoint)) {
            route.add(parents.get(route.getLast()));
        }
        return route.reversed();
    }

}
