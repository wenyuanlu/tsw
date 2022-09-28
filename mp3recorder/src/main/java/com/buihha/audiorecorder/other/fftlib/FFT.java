package com.buihha.audiorecorder.other.fftlib;

/**
 * author ：yh
 * date : 2021/1/27 16:22
 * description :
 */
public class FFT {

    public static Complex[] fft (Complex[] x) {
        int n = x.length;

        if (n == 1)
            return new Complex[]{x[0]};

        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        Complex[] even = new Complex[n / 2];
        for (int k = 0; k < n / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        for (int k = 0; k < n / 2; k++) {
            even[k] = x[2 * k + 1];
        }
        Complex[] r = fft(even);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            double  kth = -2 * k * Math.PI / n;
            Complex wk  = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + n / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    public static double[] fft (double[] x) {
        int len = x.length;
        if (len == 1) {
            return x;
        }
        Complex[] cs = new Complex[len];
        double[]  ds = new double[len / 2];
        for (int i = 0; i < len; i++) {
            cs[i] = new Complex(x[i], 0);
        }
        Complex[] ffts = fft(cs);

        for (int i = 0; i < ds.length; i++) {
            ds[i] = Math.sqrt(Math.pow(ffts[i].re(), 2) + Math.pow(ffts[i].im(), 2)) / x.length;
        }
        return ds;
    }

    public static void show (Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < SIZE; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }

    private static final int SIZE = 16384 / 4;
}