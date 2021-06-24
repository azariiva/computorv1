package fr._42.blinnea.computorv1.tokenizer.tokens;

import fr._42.blinnea.computorv1.equation.Equation;
import fr._42.blinnea.computorv1.equation.PolynomialEquation;

import java.util.Objects;

public class TokenNumber extends Token<Double> implements Equation.Member {
    Double number;

    public TokenNumber(Double number, int index) {
        super(index);
        this.number = number;
    }

    public TokenNumber(Double number, int startIndex, int endIndex) {
        super(startIndex, endIndex);
        this.number = number;
    }

    @Override
    public Double getTokenRepresentation() {
        return number;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != getClass()) return false;
        TokenNumber other = (TokenNumber) otherObject;
        return Objects.equals(number, other.number) && Objects.equals(getStartIndex(), other.getStartIndex()) &&
                Objects.equals(getEndIndex(), other.getEndIndex());
    }

    @Override
    public Equation toEquation() {
        return new PolynomialEquation(number, 0);
    }
}
