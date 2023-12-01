package edu.hw9.task1;

import edu.hw9.task1.StatGenerators.AverageDoubleStatGenerator;
import edu.hw9.task1.StatGenerators.DoubleDataStatGenerator;
import edu.hw9.task1.StatGenerators.MaxDoubleStatGenerator;
import edu.hw9.task1.StatGenerators.MinDoubleStatGenerator;
import edu.hw9.task1.StatGenerators.SumDoubleStatGenerator;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrencyDoubleStatCollector {

    private final Map<DoubleStatGeneratorType, DoubleDataStatGenerator> statGenerators;

    public ConcurrencyDoubleStatCollector() {
        statGenerators = new ConcurrentHashMap<>();
    }

    public synchronized void push(DoubleStatGeneratorType type, double... values) {
        if (type == null) {
            throw new IllegalArgumentException("Generator type can't be null.");
        }
        DoubleDataStatGenerator generator = statGenerators.get(type);
        if (generator == null) {
            generator = retrieveNewGenerator(type);
            statGenerators.put(type, generator);
        }
        generator.apply(values);
    }

    public synchronized Collection<DoubleDataStatGenerator> getStatGenerators() {
        return statGenerators.values();
    }

    public DoubleDataStatGenerator getStatGenerator(DoubleStatGeneratorType type) {
        return statGenerators.get(type);
    }

    private DoubleDataStatGenerator retrieveNewGenerator(DoubleStatGeneratorType type) {
        return switch (type) {
            case MAX -> new MaxDoubleStatGenerator();
            case MIN -> new MinDoubleStatGenerator();
            case SUM -> new SumDoubleStatGenerator();
            case AVERAGE -> new AverageDoubleStatGenerator();
        };
    }

}
