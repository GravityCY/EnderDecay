package me.gravityio.enderdecay;

public class Helper {

    public static int getByteAtInt(int value, int index, int size, boolean reverse) {
        return getByteAt(value, index, size, reverse) & 0xFF;
    }

    public static byte getByteAt(int value, int index, int size, boolean reverse) {
        index = reverse ? size - 1 - index : index;
        return (byte) ((value >> (index * 8)) & 0xFF);
    }

    public static byte[] getBytes(int value, boolean left, int size) {
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = getByteAt(value, i, size, left);
        }
        return bytes;
    }

    public static int[] getBytesInt(int value, boolean left, int size) {
        int[] byteInts = new int[size];
        for (int i = 0; i < size; i++) {
            byteInts[i] = getByteAtInt(value, i, size, left);
        }
        return byteInts;
    }

    public static int setByteAt(int value, byte b, int index, int size, boolean reverse) {
        index = reverse ? size - 1 - index : index;
        int cleared = value & ~(0xFF << (index * 8));
        return cleared | ((b & 0xFF) << (index * 8));
    }

    public static float[] toFloat(int v, int size) {
        float[] floats = new float[size];
        int[] vBytes = Helper.getBytesInt(v, true, size);
        for (int i = 0; i < vBytes.length; i++) {
            floats[i] = vBytes[i] / 255f;
        }
        return floats;
    }

}
