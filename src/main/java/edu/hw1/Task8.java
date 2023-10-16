package edu.hw1;

public final class Task8 {

    private final static int[] X_AXIS_DELTA = {-2, -1, 1, 2};
    private final static int[] Y_AXIS_DELTA = {-2, -1, 1, 2};
    private static final int CHESSBOARD_SIDE_LENGTH = 8;

    private Task8() {

    }

    /**
     * This is a static method, which check for anything knight's capture.
     *
     * @param board two dimension array representation of chessboard
     * @return true - if capture is available, false - if not.
     * @throws IllegalArgumentException if passed two dimension array with sides length unequal to 8 cells.
     */
    public static boolean knightBoardCapture(int[][] board) {
        if (!boardIsValid(board)) {
            throw new IllegalArgumentException(
                "Passed wrong sizes of chessboard (each side should be equal to 8 cells).");
        }
        boolean result = true;
        out:
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 1 && knightIsCaptured(board, y, x)) {
                    result = false;
                    break out;
                }
            }
        }
        return result;
    }

    private static boolean boardIsValid(int[][] board) {
        if (board == null || board.length != CHESSBOARD_SIDE_LENGTH) {
            return false;
        }
        boolean isValid = true;
        for (int[] row : board) {
            if (row.length != CHESSBOARD_SIDE_LENGTH) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    private static boolean knightIsCaptured(int[][] board, int y, int x) {
        int xTarget;
        int yTarget;
        for (int xDelta : X_AXIS_DELTA) {
            xTarget = x + xDelta;
            if (xTarget < 0 || xTarget >= CHESSBOARD_SIDE_LENGTH) {
                continue;
            }
            for (int yDelta : Y_AXIS_DELTA) {
                if (xDelta % 2 == 0 && yDelta % 2 != 0 || xDelta % 2 != 0 && yDelta % 2 == 0) {
                    yTarget = y + yDelta;
                    if (yTarget < 0 || yTarget >= CHESSBOARD_SIDE_LENGTH) {
                        continue;
                    }
                    if (board[yTarget][xTarget] == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
