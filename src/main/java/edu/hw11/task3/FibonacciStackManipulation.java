package edu.hw11.task3;

import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import static net.bytebuddy.jar.asm.Opcodes.F_APPEND;
import static net.bytebuddy.jar.asm.Opcodes.F_SAME;
import static net.bytebuddy.jar.asm.Opcodes.GOTO;
import static net.bytebuddy.jar.asm.Opcodes.I2L;
import static net.bytebuddy.jar.asm.Opcodes.ICONST_2;
import static net.bytebuddy.jar.asm.Opcodes.IFGT;
import static net.bytebuddy.jar.asm.Opcodes.IFNE;
import static net.bytebuddy.jar.asm.Opcodes.LADD;
import static net.bytebuddy.jar.asm.Opcodes.LCMP;
import static net.bytebuddy.jar.asm.Opcodes.LCONST_0;
import static net.bytebuddy.jar.asm.Opcodes.LCONST_1;
import static net.bytebuddy.jar.asm.Opcodes.LLOAD;
import static net.bytebuddy.jar.asm.Opcodes.LONG;
import static net.bytebuddy.jar.asm.Opcodes.LRETURN;
import static net.bytebuddy.jar.asm.Opcodes.LSTORE;

enum FibonacciStackManipulation implements StackManipulation {

    INSTANCE;

    private static final int ARG_INDEX = 1;
    private static final int LOCAL_VALUE1 = 3;
    private static final int LOCAL_VALUE0 = 5;
    private static final int RESULT_INDEX = 7;
    private static final int COUNTER_INDEX = 9;
    private static final int SIZE_IMPACT = 2;
    private static final int MAX_SIZE = 4;


    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Implementation.Context context) {
        //labels
        Label startLabel = new Label();
        Label lessThanOrEqualZeroLabel = new Label();
        Label equalOneLabel = new Label();
        Label moreThanOneLabel = new Label();
        Label endingLabel = new Label();
        //method visitor code
        methodVisitor.visitCode();

        methodVisitor
            .visitLocalVariable("arg", "J", null, startLabel, endingLabel, ARG_INDEX);
        methodVisitor
            .visitLocalVariable("value1", "J", null, startLabel, endingLabel, LOCAL_VALUE1);
        methodVisitor
            .visitLocalVariable("value0", "J", null, startLabel, endingLabel, LOCAL_VALUE0);
        methodVisitor
            .visitLocalVariable("res", "J", null, startLabel, endingLabel, RESULT_INDEX);
        methodVisitor
            .visitLocalVariable("counter", "J", null, startLabel, endingLabel, COUNTER_INDEX);

        methodVisitor.visitLabel(startLabel);
        methodVisitor.visitVarInsn(LLOAD, ARG_INDEX);
        methodVisitor.visitInsn(LCONST_0);
        methodVisitor.visitInsn(LCMP);
        methodVisitor.visitJumpInsn(IFGT, lessThanOrEqualZeroLabel);
        methodVisitor.visitInsn(LCONST_0);
        methodVisitor.visitInsn(LRETURN);

        methodVisitor.visitLabel(lessThanOrEqualZeroLabel);
        methodVisitor.visitFrame(F_SAME, 0, null, 0, null);
        methodVisitor.visitVarInsn(LLOAD, ARG_INDEX);
        methodVisitor.visitInsn(LCONST_1);
        methodVisitor.visitInsn(LCMP);
        methodVisitor.visitJumpInsn(IFNE, equalOneLabel);
        methodVisitor.visitInsn(LCONST_1);
        methodVisitor.visitInsn(LRETURN);

        methodVisitor.visitLabel(equalOneLabel);
        methodVisitor.visitFrame(F_SAME, 0, null, 0, null);
        methodVisitor.visitInsn(LCONST_0);
        methodVisitor.visitVarInsn(LSTORE, LOCAL_VALUE1);
        methodVisitor.visitInsn(LCONST_1);
        methodVisitor.visitVarInsn(LSTORE, LOCAL_VALUE0);
        methodVisitor.visitInsn(LCONST_0);
        methodVisitor.visitVarInsn(LSTORE, RESULT_INDEX);
        methodVisitor.visitFrame(
            F_APPEND, LOCAL_VALUE1,
            new Object[] {LONG, LONG, LONG},
            0,
            null
        );
        methodVisitor.visitInsn(ICONST_2);
        methodVisitor.visitInsn(I2L);
        methodVisitor.visitVarInsn(LSTORE, COUNTER_INDEX);
        methodVisitor.visitFrame(F_APPEND, ARG_INDEX, new Object[] {LONG}, 0, null);

        methodVisitor.visitLabel(moreThanOneLabel);
        methodVisitor.visitFrame(F_SAME, 0, null, 0, null);
        methodVisitor.visitVarInsn(LLOAD, COUNTER_INDEX);
        methodVisitor.visitVarInsn(LLOAD, ARG_INDEX);
        methodVisitor.visitInsn(LCMP);
        methodVisitor.visitJumpInsn(IFGT, endingLabel);
        methodVisitor.visitVarInsn(LLOAD, LOCAL_VALUE1);
        methodVisitor.visitVarInsn(LLOAD, LOCAL_VALUE0);
        methodVisitor.visitInsn(LADD);
        methodVisitor.visitVarInsn(LSTORE, RESULT_INDEX);
        methodVisitor.visitVarInsn(LLOAD, LOCAL_VALUE0);
        methodVisitor.visitVarInsn(LSTORE, LOCAL_VALUE1);
        methodVisitor.visitVarInsn(LLOAD, RESULT_INDEX);
        methodVisitor.visitVarInsn(LSTORE, LOCAL_VALUE0);
        methodVisitor.visitVarInsn(LLOAD, COUNTER_INDEX);
        methodVisitor.visitInsn(LCONST_1);
        methodVisitor.visitInsn(LADD);
        methodVisitor.visitVarInsn(LSTORE, COUNTER_INDEX);
        methodVisitor.visitJumpInsn(GOTO, moreThanOneLabel);

        methodVisitor.visitLabel(endingLabel);
        methodVisitor.visitFrame(F_SAME, 0, null, 0, null);
        methodVisitor.visitVarInsn(LLOAD, RESULT_INDEX);
        methodVisitor.visitInsn(LRETURN);
        return new Size(SIZE_IMPACT, MAX_SIZE);
    }
}
