package edu.hw2.task1;

public sealed interface Expr {

    double evaluate();

    record Constant(int constNum) implements Expr {
        @Override
        public double evaluate() {
            return constNum;
        }

        @Override
        public String toString() {
            return String.valueOf(evaluate());
        }
    }

    record Negate(Expr expression) implements Expr {
        @Override
        public double evaluate() {
            return expression.evaluate() * -1;
        }

        @Override
        public String toString() {
            return String.valueOf(evaluate());
        }
    }

    record Exponent(Expr base, int power) implements Expr {
        @Override
        public double evaluate() {
            return Math.pow(base.evaluate(), power);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder
                .append(base.evaluate())
                .append('^')
                .append(this.power)
                .append('=')
                .append(evaluate());
            return builder.toString();
        }
    }

    record Addition(Expr num1, Expr num2) implements Expr {
        @Override
        public double evaluate() {
            return num1.evaluate() + num2.evaluate();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder
                .append(num1.evaluate())
                .append('+')
                .append(num2.evaluate())
                .append('=')
                .append(evaluate());
            return builder.toString();
        }
    }

    record Multiplication(Expr num1, Expr num2) implements Expr {
        @Override
        public double evaluate() {
            return num1.evaluate() * num2.evaluate();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder
                .append(num1.evaluate())
                .append('*')
                .append(num2.evaluate())
                .append('=')
                .append(evaluate());
            return builder.toString();
        }
    }

}
