package edu.hw10.task1;

import edu.hw10.task1.annotations.NotNull;
import edu.hw10.task1.classes.AnotherClass;
import edu.hw10.task1.classes.Randomizable;
import edu.hw10.task1.classes.SomeClass;
import edu.hw10.task1.classes.SomeRecord;
import edu.hw10.task1.utils.RandomValueUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class RandomObjectGenerator implements ObjectGenerator {

    private final static Map<Class<? extends Randomizable>, Class<?>[]> PARAMETERS_MAPPING = new HashMap<>() {
        {
            put(SomeClass.class, new Class[] {String.class, int.class});
            put(AnotherClass.class, new Class[] {});
            put(SomeRecord.class, new Class[] {String.class, int.class, int.class});
        }
    };

    @Override
    public Object nextObject(Class<? extends Randomizable> c) {
        if (c == null) {
            throw new IllegalArgumentException("Invalid length of method arguments.");
        }
        try {
            Class<?>[] parametersTypes = getParametersTypes(c);
            if (parametersTypes.length == 0) {
                return c.getDeclaredConstructor().newInstance();
            }
            Constructor<?> constructor = c.getDeclaredConstructor(parametersTypes);
            return constructor.newInstance(getArgs(constructor.getParameters()));
        } catch (ClassCastException e) {
            throw new RuntimeException(
                "Method argument can't be cast to Class instance or this Class instance is not supported.",
                e
            );
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException
                 | NoSuchMethodException e) {
            throw new RuntimeException("Instantiation error.", e);
        }
    }

    private static Class<?>[] getParametersTypes(Class<? extends Randomizable> c) {
        Class<?>[] parametersTypes = PARAMETERS_MAPPING.get(c);
        if (parametersTypes == null) {
            throw new IllegalStateException("Generator can't handle this type.");
        }
        return parametersTypes;
    }

    private static Object[] getArgs(Parameter[] parameters) {
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == String.class && parameters[i].isAnnotationPresent(NotNull.class)) {
                args[i] = RandomValueUtils.generateRandomString();
                continue;
            }
            if (parameters[i].getType().getName().equals("int")) {
                args[i] = RandomValueUtils.generateRandomInt(parameters[i]);
                continue;
            }
            args[i] = null;
        }
        return args;
    }

}
