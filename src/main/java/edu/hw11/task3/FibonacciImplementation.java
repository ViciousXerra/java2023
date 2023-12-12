package edu.hw11.task3;

import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;

public enum FibonacciImplementation implements Implementation {
    INSTANCE;

    @Override
    public ByteCodeAppender appender(Target target) {
        return FibonacciMethod.INSTANCE;
    }

    @Override
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }
}
