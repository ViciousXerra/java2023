package edu.hw1;

public final class Task7 {

    private final static int INT_MAX_BIT_SIZE = 32;

    private Task7() {

    }

    /**
     * This is a static method for left cycle bit shift of integer.
     *
     * @param n     int value over which cycle bit shift would be performed
     * @param shift length of shifting (in bits)
     * @return performed int value.
     * @throws IllegalArgumentException if passed shift value is a negative number.
     */
    public static int rotateLeft(int n, int shift) {
        return calcRotate(n, shift, true);
    }

    /**
     * This is a static method for right cycle bit shift of integer.
     *
     * @param n     int value over which cycle bit shift would be performed
     * @param shift length of shifting (in bits)
     * @return performed int value.
     * @throws IllegalArgumentException if passed shift value is a negative number.
     */
    public static int rotateRight(int n, int shift) {
        return calcRotate(n, shift, false);
    }

    private static int calcRotate(int num, int shift, boolean rotateLeft) {
        validation(shift);
        int bitSize = getBitSize(num);
        int actualShift = getActualShift(shift, bitSize);
        if (num == 0 || num == Integer.MAX_VALUE || num == -1 || actualShift == 0) {
            return num;
        }
        return rotateLeft ? rotateRight(num, bitSize - actualShift, bitSize) : rotateRight(num, actualShift, bitSize);
    }

    private static void validation(int shift) {
        if (shift < 0) {
            throw new IllegalArgumentException("Passed negative value of shift length.");
        }
    }

    private static int getBitSize(int num) {
        return num < 0
            ? INT_MAX_BIT_SIZE
            : (int) (Math.log(num) / Math.log(2)) + 1;
    }

    private static int getActualShift(int shift, int bitSize) {
        return shift % bitSize;
    }

    private static int rotateRight(int n, int actualShift, int bitSize) {
        int subMask = 0;
        for (int i = 0; i < actualShift; i++) {
            subMask = subMask << 1;
            subMask += 1;
        }
        int mask1 = (n & subMask) << (bitSize - actualShift);
        int mask2 = n >>> actualShift;
        return mask1 | mask2;
    }

}
