package edu.hw2.task4;

public final class Task4 {

    private Task4() {

    }

    public static CallingInfo callingInfo() {
        try {
            throw new Throwable();
        } catch (Throwable t) {
            StackTraceElement[] stack = t.getStackTrace();
            String className = stack[1].getClassName();
            String methodName = stack[1].getMethodName();
            return new CallingInfo(className, methodName);
        }
    }

}
