package edu.hw11.task3;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;

public enum FibonacciMethod implements ByteCodeAppender {

    INSTANCE;

    @Override
    public Size apply(
        MethodVisitor methodVisitor,
        Implementation.Context context,
        MethodDescription methodDescription
    ) {
        if (!methodDescription.getReturnType().asErasure().represents(long.class)) {
            throw new IllegalArgumentException(methodDescription + " must return long.");
        }
        StackManipulation.Size operandStackSize = new StackManipulation.Compound(
            MethodVariableAccess.REFERENCE.loadFrom(0),
            MethodVariableAccess.INTEGER.loadFrom(1),
            FibonacciStackManipulation.INSTANCE,
            MethodReturn.LONG
        ).apply(methodVisitor, context);
        return new Size(operandStackSize.getMaximalSize(),
            methodDescription.getStackSize());
    }
}
