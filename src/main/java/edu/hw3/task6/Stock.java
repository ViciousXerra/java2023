package edu.hw3.task6;

import java.util.Objects;

public class Stock {

    private final static String EXPLICIT_PRICE_VALUE_MESSAGE = "Price can't be less or equal 0.";

    private int price;

    public Stock(int price) {
        if (price > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException(EXPLICIT_PRICE_VALUE_MESSAGE);
        }
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Stock stock)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return this.hashCode() == stock.hashCode() && this.price == stock.price;
    }

}
