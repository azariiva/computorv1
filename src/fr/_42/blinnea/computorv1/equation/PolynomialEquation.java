package fr._42.blinnea.computorv1.equation;

import fr._42.blinnea.computorv1.math.Math;
import fr._42.blinnea.computorv1.tokenizer.tokens.TokenNumber;
import fr._42.blinnea.computorv1.tokenizer.tokens.TokenX;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Map;
import java.util.TreeMap;

public class PolynomialEquation implements Equation, Cloneable {
    private TreeMap<Integer,Double> equation;
    private static double EPS = 1e-10;

    public static double getEPS() {
        return EPS;
    }

    public static void setEPS(double EPS) {
        PolynomialEquation.EPS = EPS;
    }

    public PolynomialEquation() {
        equation = new TreeMap<>();
    }

    public PolynomialEquation(Double value, Integer exponent) {
        equation = new TreeMap<>();
        if (java.lang.Math.abs(value) >= EPS)
            equation.put(exponent, value);
    }

    @Override
    public Equation add(Equation equation) throws EquationException {
       entryCheck(equation);
       addWithoutCheck(equation);
       return this;
    }

    @Override
    public Equation subtract(Equation equation) throws EquationException {
        entryCheck(equation);
        addWithoutCheck(equation.clone().additiveInverse());
        return this;
    }

    @Override
    public Equation multiply(Equation equation) throws EquationException {
        entryCheck(equation);
        multiplyWithoutCheck(equation);
        return this;
    }

    @Override
    public Equation divide(Equation equation) throws EquationException {
        entryCheck(equation);
        //TODO: Should be replaced with call to multiplicativeInverseCopy()
        multiplyWithoutCheck(equation.clone().multiplicativeInverse());
        return this;
    }

    @Override
    public Equation raise(Equation equation) throws EquationException {
        entryCheck(equation);
        PolynomialEquation polynomialEquation = (PolynomialEquation) equation;
        if (polynomialEquation.equation.size() == 0) {
            if (this.equation.size() == 0) throw new EquationException("0^0 is undefined");
            this.equation = new TreeMap<>();
            this.equation.put(0, 1.0);
        } else if (this.equation.size() == 0)  {
          // Leave this empty
        } else if (polynomialEquation.equation.size() != 1 ||
                polynomialEquation.equation.firstEntry().getKey() != 0) {
            throw new EquationException("This operation could not be performed because result is not linear");
        } else if (this.equation.size() != 1) {
            //TODO: Can be implemented later
            throw new EquationException("This operation could not be performed yet");
        } else {
            Map.Entry<Integer,Double> entry = polynomialEquation.equation.firstEntry();
            if (entry.getValue() % 1 != 0) throw new EquationException("Not whole exponent is not supported");
            int exponent = entry.getValue().intValue();
            entry = this.equation.firstEntry();
            this.equation.remove(entry.getKey());
            double value = Math.raise(entry.getValue(), exponent);
            if (java.lang.Math.abs(value) >= EPS)
                this.equation.put(entry.getKey() * exponent, value);
        }
        return this;
    }

    @Override
    public Equation additiveInverse() {
        this.equation.replaceAll((k,v)->-v);
        return this;
    }

    @Override
    public Equation multiplicativeInverse() {
        if (this.equation.size() != 1)
            throw new EquationException("Could not get multiplicative inverse of poly");
        TreeMap<Integer,Double> equation = new TreeMap<>();
        this.equation.forEach((k,v)->equation.put(-k,1.0/v));
        this.equation = equation;
        return this;
    }

    @Override
    public PolynomialEquation clone() {
        PolynomialEquation polynomialEquation = new PolynomialEquation();
        polynomialEquation.equation.putAll(this.equation);
        return polynomialEquation;
    }

    @Override
    public String toString() {
        DecimalFormat formatA = new DecimalFormat("0.######");
        formatA.setNegativePrefix("(" + formatA.getNegativePrefix() + formatA.getPositiveSuffix());
        formatA.setNegativeSuffix(")");

        if (equation.size() == 0) return "0";
        StringBuilder stringBuilder = new StringBuilder();
        equation.forEach((k, v) -> {
            stringBuilder.append(formatA.format(v)).append(" * ").append("X^").append(formatA.format(k)).append(" + ");
//            if (Math.abs(v - v.longValue()) < EPS)
//                stringBuilder.append(k == 0 ? String.format("%.0f + ", v) : String.format("%.0f * X^%d + ", v, k));
//            else
//                stringBuilder.append(k == 0 ? String.format("%.6f + ", v) : String.format("%.6f * X^%d + ", v, k));
        });
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
        return stringBuilder.toString();
    }

    private void entryCheck(Equation equation) throws EquationException {
        if (equation == null) throw new EquationException("Got null as argument");
        if (!equation.getClass().equals(getClass())) throw new EquationException("Could only add LinearEquation and LinearEquation");
    }

    private void addWithoutCheck(Equation equation) {
        PolynomialEquation polynomialEquation = (PolynomialEquation) equation;
        polynomialEquation.equation.forEach((k, v)->{
            if (this.equation.putIfAbsent(k,v) != null) {
                double sum = this.equation.get(k) + v;
                if (java.lang.Math.abs(sum) >= EPS)
                    this.equation.replace(k, sum);
                else
                    this.equation.remove(k);
            }
        });
    }

    private void multiplyWithoutCheck(Equation equation) throws EquationException {
        PolynomialEquation polynomialEquation = (PolynomialEquation) equation;
        if (this.equation.size() == 0 || polynomialEquation.equation.size() == 0) {
            this.equation = new TreeMap<>();
        } else {
            TreeMap<Integer,Double> aEquation;
            Map.Entry<Integer,Double> aMember;
            TreeMap<Integer,Double> newEquation = new TreeMap<>();

            if (polynomialEquation.equation.size() != 1) {
                if (this.equation.size() != 1)
                    throw new EquationException("Could not multiply poly on poly");
                aEquation = polynomialEquation.equation;
                aMember = this.equation.firstEntry();
            } else {
                aEquation = this.equation;
                aMember = polynomialEquation.equation.firstEntry();
            }
            aEquation.forEach((k,v)->{
                double product = v * aMember.getValue();
                Integer exponent = k + aMember.getKey();
                if (java.lang.Math.abs(product) >= EPS)
                    newEquation.put(exponent, product);
            });
            this.equation = newEquation;
        }
    }

    public static void main(String[] args) {
        Equation equation = new TokenX(0)
                .multiply(new TokenNumber(6.0, 0))
                .add(new TokenNumber(10.0, 0)
                        .add(new TokenNumber(3.0, 0))
                        .multiply(new TokenNumber(4.0, 0)))
                .divide(new TokenX(0)
                        .raise(new TokenNumber(-1.0, 0))
                        .raise(new TokenNumber(-2.0,0))
                        .multiply(new TokenNumber(2.0, 0)))
                .subtract(new TokenNumber(-2.0, 0)
                        .raise(new TokenNumber(3.0, 0))
                        .raise(new TokenNumber(-1.0,0))
                        .raise(new TokenNumber(-3.0, 0))
                        .add(new TokenNumber(511.0, 0)))
                .add(new TokenX(0)
                        .multiply(new TokenNumber(13.0, 0))
                        .raise(new TokenNumber(-1.0, 0)))
                .subtract(new TokenX(0)
                        .multiply(new TokenNumber(10.,0))
                        .divide(new TokenX(0)));
        System.out.println(new TokenNumber(0.,0).raise(equation).add(equation));
    }
}
