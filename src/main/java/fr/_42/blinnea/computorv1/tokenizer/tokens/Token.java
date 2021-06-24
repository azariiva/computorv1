package fr._42.blinnea.computorv1.tokenizer.tokens;

import java.util.Objects;

public abstract class Token <T> {
    private Integer startIndex;
    private Integer endIndex;

    public Token(int index) {
        startIndex = index;
        endIndex = index + 1;
    }

    public Token(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public final int getStartIndex() {
        return startIndex;
    }

    public final int getEndIndex() {
        return endIndex;
    }

    public abstract T getTokenRepresentation();

    @Override
    public int hashCode() {
        return Objects.hash(startIndex, endIndex, getTokenRepresentation());
    }

    @Override
    public abstract boolean equals(Object otherObject);

    @Override
    public String toString() {
        return String.format("%s[startIndex=%d,endIndex=%d,repr=\"%s\"]", getClass().getSimpleName(),
                startIndex, endIndex, getTokenRepresentation().toString());
    }
}
