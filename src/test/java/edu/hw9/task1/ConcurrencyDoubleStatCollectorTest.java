package edu.hw9.task1;

import edu.hw9.task1.StatGenerators.DoubleDataStatGenerator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ConcurrencyDoubleStatCollectorTest {

    private Runnable getInsert(
        CountDownLatch latch,
        ConcurrencyDoubleStatCollector collector,
        DoubleStatGeneratorType type,
        double... values
    ) {
        return () -> {
            collector.push(type, values);
            latch.countDown();
        };
    }

    private Callable<Double> getResult(
        ConcurrencyDoubleStatCollector collector,
        DoubleStatGeneratorType type
    ) {
        return () -> {
            DoubleDataStatGenerator generator = collector.getStatGenerator(type);
            return generator.getStat();
        };
    }

    @Test
    @DisplayName("Test multi thread inserting.")
    void testMultiThreadInsert() {
        //Given
        double expectedMax = 15.1;
        double expectedMin = 0.2;
        double expectedSum = 7.2;
        double expectedAvg = 4.5;
        //When
        try (ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            ConcurrencyDoubleStatCollector collector = new ConcurrencyDoubleStatCollector();
            CountDownLatch latch = new CountDownLatch(4);
            service.execute(getInsert(latch, collector, DoubleStatGeneratorType.MAX, 1.6, 4.1, 0.2, 1.0, 15.1));
            service.execute(getInsert(latch, collector, DoubleStatGeneratorType.MIN, 5.0, 7.8, 1.0, 0.3, 0.2));
            service.execute(getInsert(latch, collector, DoubleStatGeneratorType.SUM, 1.8, 2.1, 3.3));
            service.execute(getInsert(latch, collector, DoubleStatGeneratorType.AVERAGE, 1.3, 7.7));
            latch.await();
            Future<Double> futureMax = service.submit(getResult(collector, DoubleStatGeneratorType.MAX));
            Future<Double> futureMin = service.submit(getResult(collector, DoubleStatGeneratorType.MIN));
            Future<Double> futureSum = service.submit(getResult(collector, DoubleStatGeneratorType.SUM));
            Future<Double> futureAvg = service.submit(getResult(collector, DoubleStatGeneratorType.AVERAGE));
            assertThat(futureMax.get()).isEqualTo(expectedMax);
            assertThat(futureMin.get()).isEqualTo(expectedMin);
            assertThat(futureSum.get()).isEqualTo(expectedSum);
            assertThat(futureAvg.get()).isEqualTo(expectedAvg);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread has been interrupted.", e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
