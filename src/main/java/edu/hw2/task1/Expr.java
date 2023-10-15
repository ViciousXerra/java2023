package edu.hw2.task1;

public sealed interface Expr {

    double evaluate();

    record Constant(int constNum) implements Expr {
        @Override
        public double evaluate() {
            return constNum;
        }
    }

    record Negate(Expr expression) implements Expr {
        @Override
        public double evaluate() {
            return expression.evaluate() * -1;
        }
    }

    record Exponent(Expr base, int power) implements Expr {
        @Override
        public double evaluate() {
            return Math.pow(base.evaluate(), power);
        }
    }

    record Addition(Expr num1, Expr num2) implements Expr {
        @Override
        public double evaluate() {
            return num1.evaluate() + num2.evaluate();
        }
    }

    record Multiplication(Expr num1, Expr num2) implements Expr {
        @Override
        public double evaluate() {
            return num1.evaluate() * num2.evaluate();
        }
    }

}
