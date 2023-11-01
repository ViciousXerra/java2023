package edu.project2.mazes.cellbasedmaze;

import edu.project2.mazes.GenType;
import edu.project2.mazes.Maze;
import edu.project2.mazes.PathfinderType;
import edu.project2.mazes.RendererType;
import edu.project2.mazes.generators.CellBasedDFSGenerator;
import edu.project2.mazes.generators.CellBasedEllersAlgorithmGenerator;
import edu.project2.mazes.generators.CellBasedMazeGenerator;
import edu.project2.mazes.pathfinders.CellBasedBFSPathfinder;
import edu.project2.mazes.pathfinders.CellBasedDFSPathfinder;
import edu.project2.mazes.pathfinders.CellBasedMazePathfinder;
import edu.project2.mazes.renderers.CellBasedConsoleRenderer;
import edu.project2.mazes.renderers.ConsoleRenderer;

import java.util.ArrayList;
import java.util.List;

public class CellBasedMaze implements Maze {

    private final static int DEFAULT_MAZE_GRID_SIDE = 3;
    private final static String INVALID_POINT_MESSAGE = "Unable to set point.";

    private final Cell[][] grid;

    private CellBasedMazeGenerator generator;
    private CellBasedMazePathfinder pathfinder;
    private ConsoleRenderer renderer;

    private Coordinate startPoint;
    private Coordinate exitPoint;
    private List<Coordinate> route;

    private boolean generated;

    public CellBasedMaze(GenType genType, PathfinderType pathfinderType, RendererType rendererType) {
        this(DEFAULT_MAZE_GRID_SIDE, DEFAULT_MAZE_GRID_SIDE, genType, pathfinderType, rendererType);
    }

    public CellBasedMaze(
        int height,
        int width,
        GenType genType,
        PathfinderType pathfinderType,
        RendererType rendererType
    ) {
        int actualHeight = (height << 1) + 1;
        int actualWidth = (width << 1) + 1;
        grid = new Cell[actualHeight][actualWidth];
        initGrid();
        route = new ArrayList<>();
        setGenerator(genType);
        setPathfinder(pathfinderType);
        setRenderer(rendererType);
        generated = false;
    }

    public void setGenerator(GenType type) {
        switch (type) {
            case DFS_GENERATOR -> generator = new CellBasedDFSGenerator(grid);
            case ELLERS_ALGORITHM -> generator = new CellBasedEllersAlgorithmGenerator(grid);
            default -> throw new UnsupportedOperationException();
        }
    }

    public void setPathfinder(PathfinderType type) {
        switch (type) {
            case DFS_PATHFINDER -> pathfinder = new CellBasedDFSPathfinder(grid, startPoint, exitPoint);
            case BFS_PATHINDER -> pathfinder = new CellBasedBFSPathfinder(grid, startPoint, exitPoint);
            default -> throw new UnsupportedOperationException();
        }
    }

    public void setRenderer(RendererType type) {
        switch (type) {
            case CONSOLE_RENDERER -> renderer = new CellBasedConsoleRenderer(grid, route);
            default -> throw new UnsupportedOperationException();
        }
    }

    public void setStartPoint(int height, int width) {
        if (isThisPointLegal((height << 1) + 1, (width << 1) + 1)) {
            startPoint = new Coordinate((height << 1) + 1, (width << 1) + 1);
        } else {
            throw new IllegalArgumentException(INVALID_POINT_MESSAGE);
        }
    }

    public void setExitPoint(int height, int width) {
        if (isThisPointLegal((height << 1) + 1, (width << 1) + 1)) {
            exitPoint = new Coordinate((height << 1) + 1, (width << 1) + 1);
        } else {
            throw new IllegalArgumentException(INVALID_POINT_MESSAGE);
        }
    }

    @Override
    public void generateMaze() {
        generator.generate();
        generated = true;
    }

    @Override
    public void findPath() {
        route = pathfinder.findPath();
    }

    @Override
    public void render() {
        System.out.println(renderer.render(false));
    }

    @Override
    public void renderRoute() {
        System.out.println(renderer.render(true));
    }

    @Override
    public void reset() {
        initGrid();
        generateMaze();
    }

    private void initGrid() {
        for (int curHeight = 0; curHeight < grid.length; curHeight++) {
            for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                grid[curHeight][curWidth] = new Cell(curHeight, curWidth, Cell.Type.PASSAGE);
            }
        }
    }

    private boolean isThisPointLegal(int height, int width) {
        if (height < 0 || height >= grid.length) {
            return false;
        }
        if (width < 0 || width >= grid[height].length) {
            return false;
        }
        return generated && grid[height][width].type() == Cell.Type.PASSAGE;
    }
}
