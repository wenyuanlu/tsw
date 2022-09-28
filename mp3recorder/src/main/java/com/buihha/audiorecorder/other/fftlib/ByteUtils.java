package com.buihha.audiorecorder.other.fftlib;

import java.util.Arrays;

/**
 * author ：yh
 * date : 2021/1/27 16:22
 * description :
 */
public class ByteUtils {

    /**
     * byte[] 转 short[]
     * short： 2字节
     */
    public static short[] toShorts (byte[] src) {
        int     count = src.length >> 1;
        short[] dest  = new short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) ((src[i * 2] & 0xff) | ((src[2 * i + 1] & 0xff) << 8));
        }
        return dest;
    }

    public static String toString (byte[] b) {
        return Arrays.toString(b);
    }

    public static double[] toHardDouble (short[] shorts) {
        int      length = 512;
        double[] ds     = new double[length];
        for (int i = 0; i < length; i++) {
            ds[i] = shorts[i];
        }
        return ds;
    }

    public static byte[] toHardBytes (double[] doubles) {
        byte[] bytes = new byte[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            double item = doubles[i];
            bytes[i] = (byte) (item > 127 ? 127 : item);
        }
        return bytes;
    }

    public static byte[] toSoftBytes (double[] doubles) {
        double max = getMax(doubles);

        double sc = 1f;
        if (max > 127) {
            sc = (max / 128f);
        }

        byte[] bytes = new byte[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            double item = doubles[i] / sc;
            bytes[i] = (byte) (item > 127 ? 127 : item);
        }
        return bytes;
    }

    public static double getMax (double[] data) {
        double max = 0;
        for (double datum : data) {
            if (datum > max) {
                max = datum;
            }
        }
        return max;
    }
}
