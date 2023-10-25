package edu.hw3;

import edu.hw3.task6.Market;
import edu.hw3.task6.Stock;
import edu.hw3.task6.StockMarket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task6Test {

    private static Object[][] provideMarketsWithStocks() {
        return new Object[][] {
            {
                Arrays.asList(new Stock(5), new Stock(1), new Stock(3)),
                new Stock(5)
            },
            {
                Arrays.asList(new Stock(Integer.MIN_VALUE), new Stock(1), new Stock(0)),
                new Stock(1)
            },
            {
                Arrays.asList(new Stock(Integer.MAX_VALUE), new Stock(Integer.MAX_VALUE), new Stock(0)),
                new Stock(Integer.MAX_VALUE)
            },
            {
                Arrays.asList(new Stock(Integer.MIN_VALUE), new Stock(Integer.MIN_VALUE), new Stock(Integer.MIN_VALUE)),
                new Stock(0)
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test mostValuableStock() method on expected most valuable stock.")
    @MethodSource("provideMarketsWithStocks")
    void testMostValuableStock(List<Stock> stocks, Stock expected) {
        //Given
        StockMarket market = new Market();
        for (Stock stock : stocks) {
            market.add(stock);
        }
        //When
        Stock actual = market.mostValuableStock();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test behavior of removing unexisting stock from market")
    void testUnexistingStockRemoving() {
        //Given
        StockMarket market = new Market();
        //When
        Stock stock = new Stock(3);
        market.add(new Stock(10));
        //Then
        assertThatThrownBy(() -> market.remove(stock))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("Unexisting stock or market is empty.");
    }

    @Test
    @DisplayName("Test behavior of removing stock from empty market.")
    void testRemovingFromEmptyMarket() {
        //Given
        StockMarket market = new Market();
        //Then
        assertThatThrownBy(() -> {
            Stock stock = market.mostValuableStock();
        })
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("Market is empty.");
    }
}
