package fr._42.blinnea.computorv1.math;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Objects;

public class ComplexNumber {
    private static final Format formatIm = new DecimalFormat("+0.######i;-0.######i");
    private static final Format formatRe = new DecimalFormat("0.######");

    private Double re;
    private Double im;

    public ComplexNumber() {
        re = 0.0;
        im = 0.0;
    }

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getReal() {
        return re;
    }

    public double getImaginary() {
        return im;
    }

    @Override
    public String toString() {
        if (im != 0.0) {
            if (re != 0.0)
                return formatRe.format(re) + formatIm.format(im);
            return formatIm.format(im);
        }
        return formatRe.format(re);
    }

    public ComplexNumber add(ComplexNumber complexNumber) {
        re += complexNumber.re;
        im += complexNumber.im;
        return this;
    }

    public ComplexNumber add(Double realNumber) {
        re += realNumber;
        return this;
    }

    public static ComplexNumber add(ComplexNumber complexNumber, ComplexNumber complexNumber1) {
        ComplexNumber result = new ComplexNumber(complexNumber.re, complexNumber.im);
        return result.add(complexNumber1);
    }

    public ComplexNumber subtract(ComplexNumber complexNumber) {
        re -= complexNumber.re;
        im -= complexNumber.im;
        return this;
    }

    public ComplexNumber subtract(Double realNumber) {
        re -= realNumber;
        return this;
    }

    public static ComplexNumber subtract(ComplexNumber complexNumber, ComplexNumber complexNumber1) {
        ComplexNumber result = new ComplexNumber(complexNumber.re, complexNumber.im);
        return result.subtract(complexNumber1);
    }

    public ComplexNumber multiply(ComplexNumber complexNumber) {
        re = re * complexNumber.re - im * complexNumber.im;
        im = re * complexNumber.im + complexNumber.re * im;
        return this;
    }

    public ComplexNumber multiply(Double realNumber) {
        re = re * realNumber;
        im = im * realNumber;
        return this;
    }

    public static ComplexNumber multiply(ComplexNumber complexNumber, ComplexNumber complexNumber1) {
        ComplexNumber result = new ComplexNumber(complexNumber.re, complexNumber.im);
        return result.multiply(complexNumber1);
    }

    public ComplexNumber divide(ComplexNumber complexNumber) {
        double div = Math.raise(complexNumber.re,2) - Math.raise(complexNumber.im, 2);
        re = (re * complexNumber.re + im * complexNumber.im) / div;
        im = (complexNumber.re * im - re * complexNumber.im) / div;
        return this;
    }

    public ComplexNumber divide(Double realNumber) {
        re = re / realNumber;
        im = im / realNumber;
        return this;
    }

    public static ComplexNumber divide(ComplexNumber complexNumber, ComplexNumber complexNumber1) {
        ComplexNumber result = new ComplexNumber(complexNumber.re, complexNumber.im);
        return result.divide(complexNumber1);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(re, -im);
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }

    @Override
    public boolean equals(Object otherObject) {
        //  FIXME
        //   эта реализация ужасна, но нужен eps, если делать нормальную, но я не знаю, куда его пихнуть и как
        //   согласовать работу equals и hashCode
        if (otherObject == this) return true;
        if (otherObject == null) return false;
        if (this.getClass() != otherObject.getClass()) return false;
        ComplexNumber other = (ComplexNumber) otherObject;
        return re.equals(other.re) && im.equals(other.im);
    }

}
