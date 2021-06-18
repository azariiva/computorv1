package fr._42.blinnea.computorv1.tokenizer.tokens;

import java.util.Objects;

public abstract class TokenOperation extends Token<String> {
    String operation;

    TokenOperation(String operation, int index) {
        super(index);
        this.operation = operation;
    }

    TokenOperation(String operation, int startIndex, int endIndex) {
        super(startIndex, endIndex);
        this.operation = operation;
    }

    @Override
    public final String getTokenRepresentation() {
        return operation;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != getClass()) return false;
        TokenOperation other = (TokenOperation) otherObject;
        return Objects.equals(operation, other.operation) && Objects.equals(getStartIndex(), other.getStartIndex()) &&
                Objects.equals(getEndIndex(), other.getEndIndex());
    }

    public static void main(String[] args) {
        Token token = new TokenOperationPlus(1);
        System.out.println(token.getClass().getSimpleName());
    }
}

class TokenOperationPlus extends TokenOperation {
    public TokenOperationPlus(int index) {
        super("+", index);
    }
}

class TokenOperationCaret extends TokenOperation {
    public TokenOperationCaret(int index) {
        super("^", index);
    }
}

class TokenOperationAsterisk extends TokenOperation {
    public TokenOperationAsterisk(int index) {
        super("*", index);
    }
}

class TokenOperationEquals extends TokenOperation {
    public TokenOperationEquals(int index) {
        super("=", index);
    }
}

class TokenOperationMinus extends TokenOperation {
    public TokenOperationMinus(int index) {
        super("-", index);
    }
}

class TokenOperationSlash extends TokenOperation {
    public TokenOperationSlash(int index) {
        super("/", index);
    }
}

class TokenOperationParenthesisLeft extends TokenOperation {
    public TokenOperationParenthesisLeft(int index) {
        super("(", index);
    }
}

class TokenOperationParenthesisRight extends TokenOperation {
    public TokenOperationParenthesisRight(int index) {
        super(")", index);
    }
}
