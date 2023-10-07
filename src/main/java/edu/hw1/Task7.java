package edu.hw1;

public final class Task7 {

    private Task7() {

    }

    public static int rotateLeft(int n, int shift) {
        int bitSize = n < 0 ? 32 : (int) Math.round((Math.log(n) / Math.log(2))) + 1;
        int actualShift = shift % bitSize;
        return rotateRight(n, bitSize - actualShift);
    }

    public static int rotateRight(int n, int shift) {
        int bitSize = n < 0 ? 32 : (int) Math.round((Math.log(n) / Math.log(2))) + 1;
        int actualShift = shift % bitSize;
        if (n == 0 || n == -1 || actualShift == 0) {
            return n;
        }
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
