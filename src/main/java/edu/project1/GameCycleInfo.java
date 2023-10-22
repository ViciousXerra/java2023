package edu.project1;

record GameCycleInfo(char[] currentTable, int attempt, int attemptLimit, int correctGuess, boolean isPlayerWin) {

    GameCycleInfo(boolean isPlayerWin) {
        this(null, 0, 0, 0, isPlayerWin);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (correctGuess() > 0) {
            builder.append("\nCorrect!\n");
            builder.append(String.format("%d correct guessed chars.\n", correctGuess));
        } else {
            builder.append(String.format("\nAttempt %d of %d.\n", attempt(), attemptLimit()));
        }
        builder.append("Word:\n");
        for (char x : currentTable()) {
            builder.append(x);
        }
        if (isEnded() && isPlayerWin()) {
            builder.append("\nCongrats.\n");
        } else if (isEnded() && !isPlayerWin()) {
            builder.append("\nGame over.\n");
        }
        return builder.toString();
    }

    boolean isEnded() {
        return attempt() >= attemptLimit() || isPlayerWin();
    }

}
