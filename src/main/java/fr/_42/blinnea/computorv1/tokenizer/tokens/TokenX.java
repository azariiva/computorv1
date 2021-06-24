package fr._42.blinnea.computorv1.tokenizer.tokens;

import fr._42.blinnea.computorv1.equation.Equation;
import fr._42.blinnea.computorv1.equation.PolynomialEquation;

import java.util.Objects;

public class TokenX extends Token<Character> implements Equation.Member {
    Integer exponent;

    public TokenX(int index) {
        super(index);
        exponent = 1;
    }

    @Override
    public Character getTokenRepresentation() {
        return 'X';
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != getClass()) return false;
        TokenX other = (TokenX) otherObject;
        return Objects.equals(getStartIndex(), other.getStartIndex()) &&
                Objects.equals(getEndIndex(), other.getEndIndex());
    }

    @Override
    public Equation toEquation() {
        return new PolynomialEquation(1.0, exponent);
    }
}
