package edu.project5;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@SuppressWarnings("uncommentedmain")
@State(Scope.Thread)
public class ReflectionInvokeBenchmark {

    private static final String METHOD_NAME = "firstName";

    private Person person;
    private Method getterMethod;
    private MethodHandle getterMethodHandle;
    private Function getterLambda;

    public static void main(String[] args) throws RunnerException {
        Options ops = new OptionsBuilder()
            .include(ReflectionInvokeBenchmark.class.getSimpleName())
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .mode(Mode.AverageTime)
            .timeUnit(TimeUnit.NANOSECONDS)
            .forks(1)
            .warmupForks(1)
            .warmupIterations(1)
            .warmupTime(TimeValue.seconds(5))
            .measurementIterations(1)
            .measurementTime(TimeValue.seconds(5))
            .build();

        new Runner(ops).run();
    }

    @Setup
    public void setup() {
        person = new Person("Elijah", 26);
        try {
            getterMethod = getGetterMethod(METHOD_NAME);
            getterMethodHandle = getGetterMethodHandle(METHOD_NAME);
            getterLambda =
                getGetterLambdaFromLambdaMetaFactory(MethodHandles.lookup(), getGetterMethodHandle(METHOD_NAME));
        } catch (Throwable e) {
            throw new RuntimeException("Unable to create a crucial benchmark instances.", e);
        }
    }

    @Benchmark
    public void directCall(Blackhole blackhole) {
        String firstName = person.firstName();
        blackhole.consume(firstName);
    }

    @Benchmark
    public void usingReflectionApiInvoke(Blackhole blackhole) throws InvocationTargetException, IllegalAccessException {
        String firstName = (String) getterMethod.invoke(person);
        blackhole.consume(firstName);
    }

    @Benchmark
    public void usingMethodHandlerInvoke(Blackhole blackhole) throws Throwable {
        String firstName = (String) getterMethodHandle.invoke(person);
        blackhole.consume(firstName);
    }

    @Benchmark
    public void usingLambdaFromLambdaMetaFactoryLinkage(Blackhole blackhole) {
        String firstName = (String) getterLambda.apply(person);
        blackhole.consume(firstName);
    }

    private static Method getGetterMethod(String methodName) throws NoSuchMethodException {
        //pure Reflection API usage
        return Person.class.getMethod(methodName);
    }

    private static MethodHandle getGetterMethodHandle(String methodName)
        throws NoSuchMethodException, IllegalAccessException {
        //Method Handles usage
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType getterMethodType = MethodType.methodType(String.class);
        return lookup.findVirtual(Person.class, methodName, getterMethodType);
    }

    private static Function getGetterLambdaFromLambdaMetaFactory(
        MethodHandles.Lookup lookUp,
        MethodHandle mh
    )
        throws Throwable {
        CallSite callSite = LambdaMetafactory.metafactory(
            lookUp,
            "apply",
            MethodType.methodType(Function.class),
            MethodType.methodType(Object.class, Object.class),
            mh,
            mh.type()
        );
        return (Function) callSite.getTarget().invokeExact();
    }

}
