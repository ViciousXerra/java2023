package edu.project2.mazes;

import edu.project2.mazes.cellbasedmaze.CellBasedMaze;

public class Demo {
    public static void main(String[] args) {
        /*CellBasedMaze maze =
            new CellBasedMaze(5, 5,
                GenType.DFS_GENERATOR,
                PathfinderType.PATHFINDER_TYPE,
                RendererType.CONSOLE_RENDERER
            );*/

        CellBasedMaze maze =
            new CellBasedMaze(15, 15,
                GenType.DFS_GENERATOR,
                PathfinderType.DFS_PATHFINDER,
                RendererType.CONSOLE_RENDERER
            );
        maze.generateMaze();
        maze.setStartPoint(0, 0);
        maze.setExitPoint(1, 0);
        maze.setPathfinder(PathfinderType.BFS_PATHINDER);
        maze.findPath();
        maze.setRenderer(RendererType.CONSOLE_RENDERER);
        maze.render();
        maze.renderRoute();
    }
}
