package me.exceptions;

public class ComputationException extends RequestException {
    public ComputationException(String message) {
        super("(ComputationException) " + message);
    }
}
