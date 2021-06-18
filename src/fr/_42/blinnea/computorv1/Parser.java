package fr._42.blinnea.computorv1;

import fr._42.blinnea.computorv1.equation.Equation;
import fr._42.blinnea.computorv1.equation.PolynomialEquation;
import fr._42.blinnea.computorv1.tokenizer.tokens.Token;

import java.util.Iterator;

public class Parser implements Loggable {
    private Token currentToken;
    private Iterator<Token> iterator;

    public Parser(Iterator<Token> iterator) throws IllegalArgumentException {
        if (iterator == null) throw new IllegalArgumentException("Null constructor argument");
        currentToken = null;
        this.iterator = iterator;
    }

    public Equation parse() {
        return fExpr();
    }

    private void consumeCurrentToken() {
        currentToken = null;
    }

    private void getCurrentToken() {
        if (currentToken == null && iterator.hasNext())
            currentToken = iterator.next();
    }

    private Equation fExpr() {
        Equation equation = fSum();
        getCurrentToken();
        if (currentToken == null) throw new IllegalArgumentException("Wrong format of equation");
        //TODO:Replace exception
        if (!currentToken.getClass().getSimpleName().equals("TokenOperationEquals"))
            throw new IllegalArgumentException("Got unexpected token: " + currentToken);
        consumeCurrentToken();
        Equation deducable = fSum();
        if (deducable == null) throw new IllegalArgumentException("Unexpected token: " + currentToken);
        equation.subtract(deducable);
        getCurrentToken();
        if (currentToken != null) throw new IllegalArgumentException("Unexpected token: " + currentToken);
        return equation;
    }

    private Equation fSum() {
        getCurrentToken();
        if (currentToken == null)
            return new PolynomialEquation();
        boolean fMinus = false;
        switch (currentToken.getClass().getSimpleName()) {
            case "TokenOperationPlus":
                consumeCurrentToken();
                break;
            case "TokenOperationMinus":
                fMinus = true;
                consumeCurrentToken();
                break;
        }

        Equation equation = fProduct();
        if (equation == null) return equation;
        if (fMinus) equation.additiveInverse();
        while (true) {
            getCurrentToken();
            if (currentToken == null) return equation;
            logger.finer("Got token: " + currentToken);
            switch (currentToken.getClass().getSimpleName()) {
                case "TokenOperationPlus":
                    consumeCurrentToken();
                    equation.add(fProduct());
                    break;
                case "TokenOperationMinus":
                    consumeCurrentToken();
                    equation.subtract(fProduct());
                    break;
                default:
                    return equation;
            }
        }
    }

    private Equation fProduct() {
        Equation equation = fPowRes();

        if (equation == null) return equation;
        while (true) {
            getCurrentToken();
            if (currentToken == null) return equation;
            logger.finer("Got token: " + currentToken);
            switch (currentToken.getClass().getSimpleName()) {
                case "TokenOperationAsterisk":
                    consumeCurrentToken();
                    equation.multiply(fPowRes());
                    break;
                case "TokenOperationSlash":
                    consumeCurrentToken();
                    equation.divide(fPowRes());
                    break;
                default:
                    return equation;
            }
        }
    }

    private Equation fPowRes() {
        Equation equation = fValue();

        if (equation == null) return equation;
        getCurrentToken();
        if (currentToken == null) return equation;
        logger.finer("Got token: " + currentToken);
        if (currentToken.getClass().getSimpleName().equals("TokenOperationCaret")) {
            consumeCurrentToken();
            Equation exponentEquation = fPowRes();
            equation.raise(exponentEquation);
        }
        return equation;
    }

    private Equation fValue() {
        Equation equation = null;

        getCurrentToken();
        if (currentToken == null) return equation;
        logger.finer("Got token: " + currentToken);
        switch (currentToken.getClass().getSimpleName()) {
            case "TokenNumber": case "TokenX":
                equation = ((Equation.Member) currentToken).toEquation();
                consumeCurrentToken();
                break;
            case "TokenOperationParenthesisLeft":
                consumeCurrentToken();
                equation = fSum();
                getCurrentToken();
                if (currentToken == null ||
                        !currentToken.getClass().getSimpleName().equals("TokenOperationParenthesisRight"))
                    throw new IllegalStateException("Missing closing parenthesis");
                consumeCurrentToken();
                break;
        }
        if (equation == null) throw new IllegalArgumentException("Unexpected token: " + currentToken);
        logger.exiting("Parser", "fValue");
        return equation;
    }
}
