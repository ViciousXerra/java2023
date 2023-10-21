package edu.project1;

import org.jetbrains.annotations.NotNull;

interface WordProvider {
    @NotNull String getRandomWord() throws EmptyWordsStockException;
}
