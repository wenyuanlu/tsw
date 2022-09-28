package com.buihha.audiorecorder.other.fftlib;

import java.util.Objects;

/**
 * 复数
 *
 * @author test
 */
public class Complex {

    /**
     * 实数部分
     */
    private final double real;

    /**
     * 虚数部分 imaginary
     */
    private final double im;

    public Complex (double real, double imag) {
        this.real = real;
        im = imag;
    }

    @Override
    public String toString () {
        return String.format("hypot: %s, complex: %s+%si", hypot(), real, im);
    }

    public double hypot () {
        return Math.hypot(real, im);
    }

    /**
     * 复数求和
     */
    public Complex plus (Complex b) {
        double real = this.real + b.real;
        double imag = this.im + b.im;
        return new Complex(real, imag);
    }

    public Complex minus (Complex b) {
        double real = this.real - b.real;
        double imag = this.im - b.im;
        return new Complex(real, imag);
    }

    public Complex times (Complex b) {
        Complex a    = this;
        double  real = a.real * b.real - a.im * b.im;
        double  imag = a.real * b.im + a.im * b.real;
        return new Complex(real, imag);
    }

    public double re () {
        return real;
    }

    public double im () {
        return im;
    }

    @Override
    public boolean equals (Object x) {
        if (x == null)
            return false;
        if (this.getClass() != x.getClass())
            return false;
        Complex that = (Complex) x;
        return (this.real == that.real) && (this.im == that.im);
    }

    @Override
    public int hashCode () {
        return Objects.hash(real, im);
    }
}