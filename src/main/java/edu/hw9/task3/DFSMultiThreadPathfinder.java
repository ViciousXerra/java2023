package edu.hw9.task3;

import edu.project2.cellbasedmaze.Coordinate;
import edu.project2.cellbasedpathfinders.CellBasedPathfinder;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class DFSMultiThreadPathfinder extends CellBasedPathfinder {

    @Override
    protected List<Coordinate> generateRoute() {
        try (ForkJoinPool service = ForkJoinPool.commonPool()) {
            ForkJoinTask<List<Coordinate>> task =
                service.submit(new DeepFirstSearchPathfinderTask(grid, startPoint, exitPoint));
            return task.join();
        }
    }

}
