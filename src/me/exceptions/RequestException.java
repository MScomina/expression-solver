package me.exceptions;

public abstract class RequestException extends Exception {
    public RequestException(String message) {
        super("ERR;"+message);
    }
}
