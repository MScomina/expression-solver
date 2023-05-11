package me.exceptions;

public class ParsingException extends RequestException {
    public ParsingException(String message) {
        super("(ParsingException) " + message);
    }
}
