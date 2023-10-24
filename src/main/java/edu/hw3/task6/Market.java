package edu.hw3.task6;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

class Market implements StockMarket {

    private final static Comparator<Stock> DEFAULT_COMPARING_BY_RAISING_PRICE = Comparator.comparing(Stock::getPrice);

    private final Queue<Stock> stocks;

    public Market() {
        this(DEFAULT_COMPARING_BY_RAISING_PRICE);
    }

    public Market(Comparator<Stock> comparator) {
        stocks = new PriorityQueue<>(comparator);
    }

    @Override
    public void add(Stock stock) {
        stocks.add(stock);
    }

    @Override
    public void remove(Stock stock) {
        stocks.remove(stock);
    }

    @Override
    public Stock mostValuableStock() {
        return stocks.peek();
    }
}
