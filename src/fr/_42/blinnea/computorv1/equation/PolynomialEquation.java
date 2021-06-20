package fr._42.blinnea.computorv1.equation;

import fr._42.blinnea.computorv1.MyMath;
import fr._42.blinnea.computorv1.tokenizer.tokens.TokenNumber;
import fr._42.blinnea.computorv1.tokenizer.tokens.TokenX;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.*;

public class PolynomialEquation implements Equation, Cloneable {
    private TreeMap<Integer,Double> equation;

    private int lowestExponent;
    private int highestExponent;

    private static double EPS = 1e-10;

    public PolynomialEquation() {
        equation = new TreeMap<>();
        lowestExponent = 0;
        highestExponent = 0;
    }

    public PolynomialEquation(Double value, Integer exponent) {
        equation = new TreeMap<>();
        if (MyMath.abs(value) >= EPS)
            equation.put(exponent, value);
    }


    @Override
    public Equation add(Equation equation) throws EquationException {
       entryCheck(equation);
       addWithoutCheck(equation);
       updateExponents();
       return this;
    }

    @Override
    public Equation subtract(Equation equation) throws EquationException {
        entryCheck(equation);
        addWithoutCheck(equation.clone().additiveInverse());
        updateExponents();
        return this;
    }

    @Override
    public Equation multiply(Equation equation) throws EquationException {
        entryCheck(equation);
        multiplyWithoutCheck(equation);
        updateExponents();
        return this;
    }

    @Override
    public Equation divide(Equation equation) throws EquationException {
        entryCheck(equation);
        //TODO: Should be replaced with call to multiplicativeInverseCopy()
        multiplyWithoutCheck(equation.clone().multiplicativeInverse());
        updateExponents();
        return this;
    }

    @Override
    public Equation raise(Equation equation) throws EquationException {
        entryCheck(equation);
        raiseWithoutChecks(equation);
        updateExponents();
        return this;
    }

    @Override
    public Equation additiveInverse() {
        this.equation.replaceAll((k,v)->-v);
        updateExponents();
        return this;
    }

    @Override
    public Equation multiplicativeInverse() throws EquationException {
        if (this.equation.size() != 1)
            throw new EquationException("Could not get multiplicative inverse of poly");
        TreeMap<Integer,Double> equation = new TreeMap<>();
        this.equation.forEach((k,v)->equation.put(-k,1.0/v));
        this.equation = equation;
        updateExponents();
        return this;
    }

    @Override
    public PolynomialEquation clone() {
        PolynomialEquation polynomialEquation = new PolynomialEquation();
        polynomialEquation.equation.putAll(this.equation);
        polynomialEquation.updateExponents();
        return polynomialEquation;
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("0.######");
        format.setNegativePrefix("(" + format.getNegativePrefix() + format.getPositiveSuffix());
        format.setNegativeSuffix(")");

        if (equation.size() == 0) return "0";
        StringBuilder stringBuilder = new StringBuilder();
        equation.forEach((k, v) -> stringBuilder.append(format.format(v)).append(" * ").append("X^").append(format.format(k)).append(" + "));
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
        return stringBuilder.toString();
    }

    private void updateExponents() {
        if (equation.size() == 0) lowestExponent = highestExponent = 0;
        else {
            lowestExponent = equation.firstKey();
            highestExponent = equation.lastKey();
        }
    }

    public int getLowestExponent() {
        return lowestExponent;
    }

    public int getHighestExponent() {
        return highestExponent;
    }

    /**
     * @return roots of equation in List or null if any real number is solution
     */
    public String getSolution() {
        if (this.equation.size() == 0) return "Any real number is solution";

        boolean hasZeroSolution = false;
        TreeMap<Integer,Double> equation = new TreeMap<>();
        Format format = new DecimalFormat("0.######");

        this.equation.forEach((k,v) -> equation.put(k - lowestExponent, v));
        if (lowestExponent > 0) hasZeroSolution = true;
        switch (equation.lastKey()) {
            case 0:
                if (hasZeroSolution) return "The solution is:\n" + format.format(0.0);
                return "The equation has no solutions";
            case 1:
                double root = -equation.firstEntry().getValue() / equation.lastEntry().getValue();
                if (hasZeroSolution) return "The two solutions are:\n" + format.format(0.0) + "\n" + format.format(root);
                return "The solution is:\n" + format.format(root);
            case 2:
                double a = equation.getOrDefault(2, 0.0),
                        b = equation.getOrDefault(1, 0.0),
                        c = equation.getOrDefault(0, 0.0);
                double D = MyMath.raise(b,2) - 4 * a * c;
                StringBuilder result = new StringBuilder();
                if (MyMath.abs(D) < EPS) {
                    root = -b / (2 * a);

                    result.append("The discriminant is equal to zero.\n");
                    if (hasZeroSolution) result.append("The solutions are:\n").append(format.format(0.0)).append("\n").append(format.format(root));
                    else result.append("The solution is:\n").append(format.format(root));
                } else if (D < 0.0) {
                    double resultReal = -b / (2 * a);
                    resultReal = MyMath.abs(resultReal) < EPS ? 0.0 : resultReal;
                    double resultImaginary = MyMath.sqrt(MyMath.abs(D), EPS) / (2 * a);

                    result.append("The discriminant is negative. There are two complex solutions.\n");
                    result.append("The solutions are:\n");
                    if (hasZeroSolution) result.append(format.format(0.0)).append("\n");
                    result.append(format.format(resultReal)).append(" + ").append(format.format(resultImaginary)).append(" * i").append("\n");
                    result.append(format.format(resultReal)).append(" - ").append(format.format(resultImaginary)).append(" * i");
                } else {
                    root = ((MyMath.sqrt(D, EPS) - b) / (2 * a));
                    double rootB = ((-MyMath.sqrt(D, EPS) - b) / (2 * a));
                    result.append("The discriminant is larger than zero.\n");
                    result.append("The solutions are:\n");
                    if (hasZeroSolution) result.append(format.format(0.0)).append("\n");
                    result.append(format.format(root)).append("\n").append(format.format(rootB));
                }
                return result.toString();
        }
        return "The reduced polynomial degree is strictly greater than 2, I can't solve.";
    }

    private void entryCheck(Equation equation) throws EquationException {
        if (equation == null) throw new EquationException("Got null as argument");
        if (!equation.getClass().equals(getClass())) throw new EquationException("Could only add poly and poly");
    }

    private void addWithoutCheck(Equation equation) {
        PolynomialEquation polynomialEquation = (PolynomialEquation) equation;
        polynomialEquation.equation.forEach((k, v)->{
            if (this.equation.putIfAbsent(k,v) != null) {
                double sum = this.equation.get(k) + v;
                if (MyMath.abs(sum) >= EPS)
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
                if (MyMath.abs(product) >= EPS)
                    newEquation.put(exponent, product);
            });
            this.equation = newEquation;
        }
    }

    private void raiseWithoutChecks(Equation equation) throws EquationException {
        PolynomialEquation polynomialEquation = (PolynomialEquation) equation;
        if (polynomialEquation.equation.size() == 0) {
            if (this.equation.size() == 0) throw new EquationException("0^0 is undefined");
            this.equation = new TreeMap<>();
            this.equation.put(0, 1.0);
        } else if (this.equation.size() == 0)  {
            // Leave this empty
        } else if (polynomialEquation.equation.size() != 1 ||
                polynomialEquation.equation.firstEntry().getKey() != 0) {
            throw new EquationException("This operation could not be performed because result is not poly");
        } else if (this.equation.size() != 1) {
            //TODO: Can be implemented later
            throw new EquationException("Could not raise poly");
        } else {
            Map.Entry<Integer,Double> entry = polynomialEquation.equation.firstEntry();
            if (entry.getValue() % 1 != 0) throw new EquationException("Not whole exponent is not supported");
            int exponent = entry.getValue().intValue();
            entry = this.equation.firstEntry();
            this.equation.remove(entry.getKey());
            double value = MyMath.raise(entry.getValue(), exponent);
            if (MyMath.abs(value) >= EPS)
                this.equation.put(entry.getKey() * exponent, value);
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
