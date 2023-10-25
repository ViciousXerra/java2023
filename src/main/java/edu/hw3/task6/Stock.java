package edu.hw3.task6;

import java.util.Objects;

public class Stock {

    private int price;

    public Stock(int price) {
        if (price > 0) {
            this.price = price;
        }
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Stock stock)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return this.price == stock.price;
    }

}
