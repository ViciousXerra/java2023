package edu.hw1;

public final class Task7 {

    private final static int INT_MAX_BIT_SIZE = 32;

    private Task7() {

    }

    public static int rotateLeft(int n, int shift) throws IllegalArgumentException {
        if (shift < 0) {
            throw new IllegalArgumentException();
        }
        int bitSize = n < 0 ? INT_MAX_BIT_SIZE : (int) (Math.log(n) / Math.log(2)) + 1;
        int actualShift = shift % bitSize;
        if (n == 0 || n == Integer.MAX_VALUE || n == -1 || actualShift == 0) {
            return n;
        }
        return rotateRight(n, bitSize - actualShift, bitSize);
    }

    public static int rotateRight(int n, int shift) throws IllegalArgumentException {
        if (shift < 0) {
            throw new IllegalArgumentException();
        }
        int bitSize = n < 0 ? INT_MAX_BIT_SIZE : (int) (Math.log(n) / Math.log(2)) + 1;
        int actualShift = shift % bitSize;
        if (n == 0 || n == Integer.MAX_VALUE || n == -1 || actualShift == 0) {
            return n;
        }
        return rotateRight(n, actualShift, bitSize);
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
