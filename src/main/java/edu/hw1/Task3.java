package edu.hw1;

public final class Task3 {

    private Task3() {

    }

    public static boolean isNestable(int[] a1, int[] a2) {
        boolean isValidInput = (a1 != null && a1.length > 0) && (a2 != null && a2.length > 1);
        if (isValidInput) {
            int[] a1MinAndMax;
            int[] a2MinAndMax = findMinAndMax(a2);
            if (a1.length == 1) {
                return a1[0] > a2MinAndMax[0] && a1[0] < a2MinAndMax[1];
            }
            a1MinAndMax = findMinAndMax(a1);
            return a1MinAndMax[0] > a2MinAndMax[0] && a1MinAndMax[1] < a2MinAndMax[1];
        }
        return false;
    }

    private static int[] findMinAndMax(int[] array) {
        int arrayMin = array[0];
        int arrayMax = array[0];
        for (int i = 1; i < array.length; i++) {
            if (arrayMin > array[i]) {
                arrayMin = array[i];
            }
            if (arrayMax < array[i]) {
                arrayMax = array[i];
            }
        }
        return new int[] {arrayMin, arrayMax};
    }

}
