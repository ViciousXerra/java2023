package edu.hw1;

public final class Task8 {

    private final static int[] X_AXIS_DELTA = {-2, -1, 1, 2};
    private final static int[] Y_AXIS_DELTA = {-2, -1, 1, 2};
    private static final int CHESSBOARD_SIDE_LENGTH = 8;

    private Task8() {

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
        boolean isCaptured = false;
        out: for (int xDelta : X_AXIS_DELTA) {
            for (int yDelta : Y_AXIS_DELTA) {
                if (xDelta % 2 == 0 && yDelta % 2 != 0
                    || xDelta % 2 != 0 && yDelta % 2 == 0) {
                    try {
                        isCaptured = board[y + yDelta][x + xDelta] == 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                    if (isCaptured) {
                        break out;
                    }
                }
            }
        }
        return isCaptured;
    }

    public static boolean knightBoardCapture(int[][] board) throws IllegalArgumentException {
        if (!boardIsValid(board)) {
            throw new IllegalArgumentException();
        }
        boolean result = true;
        out: for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 1 && knightIsCaptured(board, y, x)) {
                    result = false;
                    break out;
                }
            }
        }
        return result;
    }

}
