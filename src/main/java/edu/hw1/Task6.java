package edu.hw1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Task6 {

    private final static int DECIMAL_BASE = 10;
    private final static int KARPEKAR_NUMBER = 6174;
    private final static int LOWER_NUM_RESTRICTION = 1000;

    private Task6() {

    }

    /**
     * This is a static method to count steps of Karpekar's routine for unsigned number.
     *
     * @param num int value to pass. It must be >1000 and need to consist of at less 2 distinct digits
     * @return number of steps of Karpekar's routine.
     * @throws IllegalArgumentException if passed value <= 1000 or consist of 1 distinct digit.
     */
    public static int countStepsForKarpekarRoutine(int num) {
        int aux = Math.abs(num);
        if (aux <= LOWER_NUM_RESTRICTION) {
            throw new IllegalArgumentException("Passed value <= 1000.");
        }
        List<Integer> list = new ArrayList<>();
        while (aux != 0) {
            list.add(aux % DECIMAL_BASE);
            aux /= DECIMAL_BASE;
        }
        if (list.stream().distinct().count() == 1) {
            throw new IllegalArgumentException("Passed value consist of 1 distinct digit.");
        }
        int num1 = parseByDigits(list, true);
        int num2 = parseByDigits(list, false);
        int result = Math.abs(num1 - num2);
        if (result == KARPEKAR_NUMBER) {
            return 1;
        }
        while (result < LOWER_NUM_RESTRICTION) {
            result *= DECIMAL_BASE;
        }
        return 1 + countStepsForKarpekarRoutine(result);
    }

    private static int parseByDigits(List<Integer> list, boolean reverse) {
        int num = 0;
        int radix = 0;
        if (reverse) {
            list.sort(Comparator.reverseOrder());
        } else {
            list.sort(Comparator.naturalOrder());
        }
        for (int x : list) {
            num += x * (int) Math.pow(DECIMAL_BASE, radix++);
        }
        return num;
    }

}
