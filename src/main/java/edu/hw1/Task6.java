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

    public static int countStepsForKarpekarRoutine(int num) throws IllegalArgumentException {
        int aux = Math.abs(num);
        if (aux <= LOWER_NUM_RESTRICTION) {
            throw new IllegalArgumentException();
        }
        List<Integer> list = new ArrayList<>();
        while (aux != 0) {
            list.add(aux % DECIMAL_BASE);
            aux /= DECIMAL_BASE;
        }
        if (list.stream().distinct().count() == 1) {
            throw new IllegalArgumentException();
        }
        int num1 = parseByDigits(list, true);
        int num2 = parseByDigits(list, false);
        int result = Math.abs(num1 - num2);
        return result == KARPEKAR_NUMBER ? 1 : 1 + countStepsForKarpekarRoutine(result);
    }

}
