package fr._42.blinnea.computorv1.math;

import java.text.DecimalFormat;
import java.text.Format;

public class ComplexNumber {
    private static Format format = new DecimalFormat("0.######");

    private double re;
    private double im;

    public ComplexNumber() {
        re = 0;
        im = 0;
    }

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public String toString() {
        if (im < 0)
            return format.format(re) + format.format(im) + "i";
        return format.format(re) + "+" + format.format(im) + "i";
    }
}
