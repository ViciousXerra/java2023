package edu.project2.mazes;

import edu.project2.mazes.cellbasedmaze.CellBasedMaze;

public class Demo {
    public static void main(String[] args) {
        /*CellBasedMaze maze =
            new CellBasedMaze(5, 5,
                GenType.DFS_GENERATOR,
                PathfinderType.PATHFINDER_TYPE,
                RendererType.CONSOLE_RENDERER
            );
        maze.generateMaze();
        maze.render();*/
        CellBasedMaze maze1 =
            new CellBasedMaze(10, 10,
                GenType.ELLERS_ALGORITHM,
                PathfinderType.PATHFINDER_TYPE,
                RendererType.CONSOLE_RENDERER
            );
        maze1.generateMaze();
        maze1.render();
    }
}
