package edu.hw11.task3;

import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import static net.bytebuddy.jar.asm.Opcodes.ATHROW;
import static net.bytebuddy.jar.asm.Opcodes.DUP;
import static net.bytebuddy.jar.asm.Opcodes.GOTO;
import static net.bytebuddy.jar.asm.Opcodes.ICONST_2;
import static net.bytebuddy.jar.asm.Opcodes.IFGE;
import static net.bytebuddy.jar.asm.Opcodes.IF_ICMPLE;
import static net.bytebuddy.jar.asm.Opcodes.IINC;
import static net.bytebuddy.jar.asm.Opcodes.ILOAD;
import static net.bytebuddy.jar.asm.Opcodes.INVOKESPECIAL;
import static net.bytebuddy.jar.asm.Opcodes.ISTORE;
import static net.bytebuddy.jar.asm.Opcodes.LADD;
import static net.bytebuddy.jar.asm.Opcodes.LCONST_0;
import static net.bytebuddy.jar.asm.Opcodes.LCONST_1;
import static net.bytebuddy.jar.asm.Opcodes.LLOAD;
import static net.bytebuddy.jar.asm.Opcodes.LRETURN;
import static net.bytebuddy.jar.asm.Opcodes.LSTORE;
import static net.bytebuddy.jar.asm.Opcodes.NEW;

public enum FibonacciStackManipulation implements StackManipulation {

    INSTANCE;

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Implementation.Context context) {
        methodVisitor.visitVarInsn(ILOAD, 1);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IFGE, label1);
        //"stay" IFGE case //switch to IFLT in reverse maybe?
        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        //var1 local
        methodVisitor.visitInsn(LCONST_0);
        methodVisitor.visitVarInsn(LSTORE, 2);
        //var2 local
        methodVisitor.visitInsn(LCONST_1);
        methodVisitor.visitVarInsn(LSTORE, 3);
        //counter local
        methodVisitor.visitInsn(ICONST_2);
        methodVisitor.visitVarInsn(ISTORE, 4);
        Label label3 = new Label();
        methodVisitor.visitLabel(label3);
        methodVisitor.visitVarInsn(ILOAD, 4);
        methodVisitor.visitVarInsn(ILOAD, 1);
        //Return label
        Label label4 = new Label();
        methodVisitor.visitJumpInsn(IF_ICMPLE, label4);
        //Sum
        methodVisitor.visitVarInsn(LLOAD, 2);
        methodVisitor.visitVarInsn(LLOAD, 3);
        methodVisitor.visitInsn(LADD);
        //Reassign local vars
        methodVisitor.visitVarInsn(LLOAD, 3);
        methodVisitor.visitVarInsn(LSTORE, 2);
        methodVisitor.visitVarInsn(LSTORE, 3);
        //Increment
        methodVisitor.visitVarInsn(ILOAD, 4);
        methodVisitor.visitInsn(IINC);
        methodVisitor.visitVarInsn(ISTORE, 4);
        //loop
        methodVisitor.visitJumpInsn(GOTO, label3);
        //return
        methodVisitor.visitLabel(label4);
        methodVisitor.visitVarInsn(LLOAD, 3);
        methodVisitor.visitInsn(LRETURN);
        //"jump" IFGE case
        methodVisitor.visitLabel(label1);
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        methodVisitor.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(
            INVOKESPECIAL,
            "java/lang/IllegalArgumentException", "<init>", "()V", false);
        methodVisitor.visitInsn(ATHROW);
        return new Size(-1, 10);
    }
}
