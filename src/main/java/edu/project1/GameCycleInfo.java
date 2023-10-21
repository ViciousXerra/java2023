package edu.project1;

record GameCycleInfo(char[] currentTable, int attempt, int attemptLimit, int correctGuess, boolean isPlayerWin) {

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
        if (isEnded()) {
            builder.append("\nGame over.\n");
        }
        if (isPlayerWin()) {
            builder.append("\nCongrats.\n");
        }
        return builder.toString();
    }

    boolean isEnded() {
        return attempt() >= attemptLimit();
    }

}
