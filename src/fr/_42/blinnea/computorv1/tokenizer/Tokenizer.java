package fr._42.blinnea.computorv1.tokenizer;

import fr._42.blinnea.computorv1.Loggable;
import fr._42.blinnea.computorv1.tokenizer.tokens.Token;
import fr._42.blinnea.computorv1.tokenizer.tokens.TokenFactory;

import java.util.Iterator;

public class Tokenizer implements Iterable<Token>, Loggable {
    private final String input;
    private int index;

    public Tokenizer(String input) throws IllegalArgumentException {
        //TODO: Change error management later
        if (input == null) throw new IllegalArgumentException("Input string could not be null");
        this.input = input;
        this.index = 0;
    }

    @Override
    public Iterator<Token> iterator() {
        return new Iterator<Token>() {
            @Override
            public boolean hasNext() {
                logger.entering(getClass().toString(), "hasNext");
                return input.length() > index;
            }

            @Override
            public Token next() throws IllegalTokenException {
                logger.entering(getClass().toString(), "next");
                Token result = null;
                char c = input.charAt(index);
                while (Character.isSpaceChar(c)) {
                    c = input.charAt(++index);
                }
                switch (c) {
                    case 'X':
                        result = TokenFactory.getToken(TokenFactory.TokenType.X, "X", index++);
                        break;
                    case '*':
                        result = TokenFactory.getToken(TokenFactory.TokenType.ASTERISK, "*", index++);
                        break;
                    case '^':
                        result = TokenFactory.getToken(TokenFactory.TokenType.CARET, "^", index++);
                        break;
                    case '=':
                        result = TokenFactory.getToken(TokenFactory.TokenType.EQUALS, "=", index++);
                        break;
                    case '-':
                        result = TokenFactory.getToken(TokenFactory.TokenType.MINUS, "-", index++);
                        break;
                    case '+':
                        result = TokenFactory.getToken(TokenFactory.TokenType.PLUS, "+", index++);
                        break;
                    case '(':
                        result = TokenFactory.getToken(TokenFactory.TokenType.PARENTHESIS_LEFT, "(", index++);
                        break;
                    case ')':
                        result = TokenFactory.getToken(TokenFactory.TokenType.PARENTHESIS_RIGHT, ")", index++);
                        break;
                    case '/':
                        result = TokenFactory.getToken(TokenFactory.TokenType.SLASH, "/", index++);
                        break;
                    default:
                        break;
                }
                if (Character.isDigit(c)) {
                    int endIndex = index + 1;
                    boolean pointWasMet = false;
                    for (; endIndex < input.length(); endIndex++) {
                        c = input.charAt(endIndex);
                        if (c == '.') {
                            if (pointWasMet) throw new IllegalTokenException(input, endIndex);
                            pointWasMet = true;
                        } else if (!Character.isDigit(c)) {
                            break;
                        }
                    }
                    result = TokenFactory.getToken(TokenFactory.TokenType.NUMBER,
                            input.substring(index, endIndex), index, endIndex);
                    index = endIndex;
                }
                if (result == null) throw new IllegalTokenException(input, index);
                logger.finer("Returning " + result);
                return result;
            }

            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
    }

    class IllegalTokenException extends IllegalArgumentException {
        IllegalTokenException(String str, int index) {
            super(String.format("Illegal token '%c' was met at %d in string \"%s\"", str.charAt(index), index, str));
        }
    }

    public static void main(String[] args) {
        //Loggable.setupDefaultLogger();
        String input = "5.5. * X^0 + 4 * X^1 - 9.3 * X^2 = 1 * X^0";
        Tokenizer tokenizer = new Tokenizer(input);
        for (Token token : tokenizer) {
            System.out.println(token.toString());
        }
    }
}
