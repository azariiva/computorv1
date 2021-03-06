package fr._42.blinnea.computorv1.equation;

import fr._42.blinnea.computorv1.math.ComplexNumber;
import fr._42.blinnea.computorv1.math.Math;
import fr._42.blinnea.computorv1.tokenizer.tokens.TokenNumber;
import fr._42.blinnea.computorv1.tokenizer.tokens.TokenX;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Map;
import java.util.TreeMap;

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

    public PolynomialEquation(double value, int exponent) {
        equation = new TreeMap<>();
        if (Math.abs(value) >= EPS)
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
        // REVIEW
        //  Может быть заменено одним действием multiplicativeInverseCopy()
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
    public Equation multiplicativeInverse() {
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
        equation.forEach((k, v) -> {
            if (k == 0)
                stringBuilder.append(format.format(v)).append(" + ");
            else if (k == 1)
                stringBuilder.append(format.format(v)).append(" * ").append("X").append(" + ");
            else
                stringBuilder.append(format.format(v)).append(" * ").append("X^").append(format.format(k)).append(" + ");
        });
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
                double D = Math.raise(b,2) - 4 * a * c;
                StringBuilder result = new StringBuilder();
                if (Math.abs(D) < EPS) {
                    root = -b / (2 * a);

                    result.append("The discriminant is equal to zero.\n");
                    if (hasZeroSolution) result.append("The solutions are:\n").append(format.format(0.0)).append("\n").append(format.format(root));
                    else result.append("The solution is:\n").append(format.format(root));
                } else if (D < 0.0) {
                    ComplexNumber sqrtD = new ComplexNumber(0, Math.sqrt(Math.abs(D), EPS));
                    ComplexNumber complexB = new ComplexNumber(-b, 0);

                    result.append("The discriminant is negative. There are two complex solutions.\n");
                    result.append("The solutions are:\n");
                    if (hasZeroSolution) result.append(format.format(0.0)).append("\n");
                    result.append(ComplexNumber.add(complexB, sqrtD).divide(2 * a)).append("\n");
                    result.append(ComplexNumber.subtract(complexB, sqrtD).divide(2 * a));
                } else {
                    root = ((Math.sqrt(D, EPS) - b) / (2 * a));
                    double rootB = ((-Math.sqrt(D, EPS) - b) / (2 * a));
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
        if (!equation.getClass().equals(getClass())) throw new EquationException("Could only add LinearEquation and LinearEquation");
    }

    private void addWithoutCheck(Equation equation) {
        PolynomialEquation polynomialEquation = (PolynomialEquation) equation;
        polynomialEquation.equation.forEach((k, v)->{
            if (this.equation.putIfAbsent(k,v) != null) {
                double sum = this.equation.get(k) + v;
                if (Math.abs(sum) >= EPS)
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
                if (Math.abs(product) >= EPS)
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
        } else //noinspection StatementWithEmptyBody
            if (this.equation.size() == 0)  {
        } else if (polynomialEquation.equation.size() != 1 ||
                polynomialEquation.equation.firstEntry().getKey() != 0) {
            throw new EquationException("This operation could not be performed because result is not poly");
        } else if (this.equation.size() != 1) {
            // REVIEW
            //  MAYBE Может быть реализовано с использованием полинома Ньютона
            throw new EquationException("Could not raise poly");
        } else {
            Map.Entry<Integer,Double> entry = polynomialEquation.equation.firstEntry();
            if (entry.getValue() % 1 != 0) throw new EquationException("Not whole exponent is not supported");
            int exponent = entry.getValue().intValue();
            entry = this.equation.firstEntry();
            this.equation.remove(entry.getKey());
            double value = Math.raise(entry.getValue(), exponent);
            if (Math.abs(value) >= EPS)
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
