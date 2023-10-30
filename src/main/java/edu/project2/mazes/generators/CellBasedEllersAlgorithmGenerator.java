package edu.project2.mazes.generators;

import edu.project2.mazes.cellbasedmaze.Cell;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CellBasedEllersAlgorithmGenerator implements CellBasedMazeGenerator {

    private final static Random RANDOM = new Random();
    private long id;

    private final Cell[][] grid;

    public CellBasedEllersAlgorithmGenerator(Cell[][] grid) {
        id = 1;
        this.grid = grid;
    }

    @Override
    public void generate() {
        setUp();
        startGeneration();
    }

    private void setUp() {
        for (int curHeight = 0; curHeight < grid.length; curHeight++) {
            if (curHeight == 0 || curHeight == grid.length - 1) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                    grid[curHeight][curWidth] = new Cell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else if (curHeight % 2 == 0) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth += 2) {
                    grid[curHeight][curWidth] = new Cell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else {
                grid[curHeight][0] = new Cell(curHeight, 0, Cell.Type.WALL);
                grid[curHeight][grid[curHeight].length - 1] =
                    new Cell(curHeight, grid[curHeight].length - 1, Cell.Type.WALL);
            }
        }
    }

    private void startGeneration() {
        long[] data = dataInitialization();
        for (int curHeight = 1; curHeight < grid.length; curHeight += 2) {
            verticalWallsSetUp(data, curHeight);
            if (curHeight != grid.length - 2) {
                horizontalWallsSetUp(data, curHeight + 1);
            } else {
                bottomLevelRecombination(data);
            }
        }
    }

    private long[] dataInitialization() {
        int columnsCount = (grid[0].length - 1) >> 1;
        long[] ids = new long[columnsCount];
        for (int index = 0; index < ids.length; index++) {
            ids[index] = id++;
        }
        return ids;
    }

    private void verticalWallsSetUp(long[] ids, int currentHeight) {
        for (int i = 0; i < ids.length - 1; i++) {
            if (ids[i] == ids[i + 1] || RANDOM.nextBoolean()) {
                grid[currentHeight][(i + 1) << 1] = new Cell(currentHeight, (i + 1) << 1, Cell.Type.WALL);
            } else {
                for (int j = i + 1; j < ids.length; j++) {
                    if (ids[j] == ids[i + 1]) {
                        ids[j] = ids[i];
                    }
                }
            }
        }
    }

    private void horizontalWallsSetUp(long[] ids, int currentHeight) {
        Map<Long, Integer> idsFreq = new HashMap<>();
        int idsCount;
        for (long id : ids) {
            idsFreq.put(id, idsFreq.getOrDefault(id, 0) + 1);
        }
        for (int i = 0; i < ids.length; i++) {
            idsCount = idsFreq.get(ids[i]);
            if (RANDOM.nextBoolean() && idsCount > 1) {
                idsFreq.put(ids[i], idsCount - 1);
                grid[currentHeight][(i << 1) + 1] = new Cell(currentHeight, (i << 1) + 1, Cell.Type.WALL);
                ids[i] = id++;
            }
        }
    }

    private void bottomLevelRecombination(long[] ids) {
        for (int i = 0; i < ids.length - 1; i++) {
            if (ids[i] != ids[i + 1]) {
                grid[grid.length - 2][(i + 1) << 1] = new Cell(grid.length - 2, (i + 1) << 1, Cell.Type.PASSAGE);
                for (int j = i + 1; j < ids.length; j++) {
                    if (ids[j] == ids[i + 1]) {
                        ids[j] = ids[i];
                    }
                }
            }
        }
    }
}
