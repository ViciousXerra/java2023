package edu.hw3.task6;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;

public class Market implements StockMarket {

    private final static Comparator<Stock> DEFAULT_COMPARING_BY_RAISING_PRICE =
        Comparator.comparing(Stock::getPrice).reversed();

    private final Queue<Stock> stocks;

    public Market() {
        this(DEFAULT_COMPARING_BY_RAISING_PRICE);
    }

    public Market(@NotNull Comparator<Stock> comparator) {
        stocks = new PriorityQueue<>(comparator);
    }

    @Override
    public void add(@NotNull Stock stock) {
        stocks.add(stock);
    }

    @Override
    public void remove(@NotNull Stock stock) {
        if (!stocks.remove(stock)) {
            throw new NoSuchElementException("Unexisting stock or market is empty.");
        }
    }

    @Override
    public @NotNull Stock mostValuableStock() {
        if (stocks.isEmpty()) {
            throw new NoSuchElementException("Market is empty.");
        }
        return stocks.peek();
    }

}
