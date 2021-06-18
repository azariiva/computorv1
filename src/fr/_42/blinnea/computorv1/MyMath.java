package fr._42.blinnea.computorv1;

public interface MyMath {
    static double raise(double value, int exponent) {
        double result = 1.0;
        if (exponent >= 0) {
            for (int i = 0; i < exponent; i++) result *= value;
        } else {
            for (int i = 0; i > exponent; i--) result /= value;
        }
        return result;
    }

    static double sqrt(double value, double eps) {
        double result = 1;
        for (double tmp = (result + value / result) / 2; Math.abs(result - tmp) >= eps; tmp = (result + value / result) / 2)
            result = tmp;
        return result;
    }
}
