package edu.project2.cellbasedgenerators;

import edu.project2.cellbasedmaze.Cell;
import edu.project2.cellbasedmaze.Maze;
import java.util.HashMap;
import java.util.Map;

public final class EllersAlgorithmGenerator extends CellBasedGenerator {

    private int lastRowIndex;
    private long id;

    public EllersAlgorithmGenerator(boolean isRandomProvided, boolean isMazeBlocked) {
        super(isRandomProvided, isMazeBlocked);
    }

    @Override
    protected void initializeMaze(Maze maze) {
        this.grid = maze.getGrid();
        id = 1;
        lastRowIndex = grid.length - INDEX_DELTA;
        generateGrid();
        initFlag = true;
    }

    @Override
    protected void setUp() {
        for (int curHeight = 0; curHeight < grid.length; curHeight++) {
            if (curHeight == 0 || curHeight == grid.length - 1) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth++) {
                    placeCell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else if (curHeight % INDEX_DELTA == 0) {
                for (int curWidth = 0; curWidth < grid[curHeight].length; curWidth += INDEX_DELTA) {
                    placeCell(curHeight, curWidth, Cell.Type.WALL);
                }
            } else {
                placeCell(curHeight, 0, Cell.Type.WALL);
                placeCell(curHeight, grid[curHeight].length - 1, Cell.Type.WALL);
            }
        }
    }

    @Override
    protected void startGeneration() {
        long[] ids = dataInitialization();
        for (int curHeight = 1; curHeight < grid.length; curHeight += 2) {
            verticalWallsSetUp(ids, curHeight);
            if (curHeight != lastRowIndex) {
                horizontalWallsSetUp(ids, curHeight + 1);
            } else {
                lastRowRecombination(ids);
            }
        }
    }

    private long[] dataInitialization() {
        int columns = (grid[0].length - 1) >> 1;
        long[] ids = new long[columns];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = id++;
        }
        return ids;
    }

    private void verticalWallsSetUp(long[] ids, int currentHeight) {
        int widthIndex;

        for (int i = 0; i < ids.length - 1; i++) {
            if (ids[i] == ids[i + 1] || RANDOM.nextBoolean()) {
                widthIndex = calcVerticalWallWidthIndex(i);
                placeCell(currentHeight, widthIndex, Cell.Type.WALL);
            } else {
                linkUp(ids, ids[i + 1], ids[i]);
            }
        }
    }

    private void linkUp(long[] ids, long indexOld, long indexReplace) {
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == indexOld) {
                ids[i] = indexReplace;
            }
        }
    }

    private int calcVerticalWallWidthIndex(int numOfColumn) {
        return (numOfColumn + 1) << 1;
    }

    private void horizontalWallsSetUp(long[] ids, int currentHeight) {
        int idCount;
        int widthIndex;
        Map<Long, Integer> idFreq = new HashMap<>();
        for (long identificator : ids) {
            idFreq.put(identificator, idFreq.getOrDefault(identificator, 0) + 1);
        }
        for (int i = 0; i < ids.length; i++) {
            idCount = idFreq.get(ids[i]);
            if (RANDOM.nextBoolean() && idCount > 1) {
                widthIndex = calcHorizontalWallWidthIndex(i);
                placeCell(currentHeight, widthIndex, Cell.Type.WALL);
                idFreq.put(ids[i], --idCount);
                ids[i] = id++;
            }
        }
    }

    private int calcHorizontalWallWidthIndex(int numOfColumn) {
        return (numOfColumn << 1) + 1;
    }

    private void lastRowRecombination(long[] ids) {
        int widthIndex;
        for (int i = 0; i < ids.length - 1; i++) {
            if (ids[i] != ids[i + 1]) {
                widthIndex = calcVerticalWallWidthIndex(i);
                placeCell(lastRowIndex, widthIndex, Cell.Type.PASSAGE);
                linkUp(ids, ids[i + 1], ids[i]);
            }
        }
    }

}
