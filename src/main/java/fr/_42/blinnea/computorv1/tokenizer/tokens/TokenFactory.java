package fr._42.blinnea.computorv1.tokenizer.tokens;

import fr._42.blinnea.computorv1.Loggable;

public class TokenFactory implements Loggable {
    /**
     * Retrieve token of the given type
     * @param type type of token
     * @param stringRepresentation string representation of token
     * @param startIndex where token begins
     * @param endIndex where token ends
     * @return token of corresponding type
     * @throws IllegalTokenTypeException if type isn't correct
     */
    public static Token getToken(TokenType type, String stringRepresentation,
                          int startIndex, int endIndex) throws IllegalTokenTypeException {
        logger.finer(String.format("type=%s, stringRepresentation=\"%s\", startIndex=%d, endIndex=%d",
                type, stringRepresentation, startIndex, endIndex));
        if (startIndex + 1 == endIndex) return getToken(type, stringRepresentation, startIndex);
        if (type == TokenType.NUMBER) {
            return new TokenNumber(new Double(stringRepresentation), startIndex, endIndex);
        }
        throw new IllegalTokenTypeException(type);
    }

    /**
     * Retrieve token of the given type
     * @param type type of token
     * @param stringRepresentation string representation of token
     * @param index where token
     * @return token of corresponding type
     * @throws IllegalTokenTypeException if type isn't correct
     */
    public static Token getToken(TokenType type, String stringRepresentation,
                                 int index) throws IllegalTokenTypeException {
        logger.finer(String.format("type=%s, stringRepresentation=\"%s\", index=%d",
                type, stringRepresentation, index));
        switch (type) {
            case X:
                return new TokenX(index);
            case NUMBER:
                return new TokenNumber(new Double(stringRepresentation), index);
            case ASTERISK:
                return new TokenOperationAsterisk(index);
            case CARET:
                return new TokenOperationCaret(index);
            case EQUALS:
                return new TokenOperationEquals(index);
            case PLUS:
                return new TokenOperationPlus(index);
            case MINUS:
                return new TokenOperationMinus(index);
            case PARENTHESIS_RIGHT:
                return new TokenOperationParenthesisRight(index);
            case PARENTHESIS_LEFT:
                return new TokenOperationParenthesisLeft(index);
            case SLASH:
                return new TokenOperationSlash(index);
            default:
                throw new IllegalTokenTypeException(type);
        }
    }

    /**
     * Token types supported by {@link TokenFactory} <br>
     * Types are X, NUMBER, ASTERISK, CARET, EQUALS, MINUS, PLUS
     */
    public enum TokenType {
        X("X"),
        NUMBER("Number"),
        ASTERISK("Asterisk"),
        CARET("Caret"),
        EQUALS("Equals"),
        MINUS("Minus"),
        PLUS("Plus"),
        PARENTHESIS_RIGHT("Parenthesis right"),
        PARENTHESIS_LEFT("Parenthesis left"),
        SLASH("Slash");

        private final String type;

        TokenType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public static class IllegalTokenTypeException extends IllegalArgumentException {
        IllegalTokenTypeException(TokenType tokenType) {
            super("token of type " + tokenType.toString() + " is not supported");
        }
    }
}
