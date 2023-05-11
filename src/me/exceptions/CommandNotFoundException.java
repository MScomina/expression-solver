package me.exceptions;

public class CommandNotFoundException extends RequestException {
    public CommandNotFoundException(String message) {
        super("(CommandNotFoundException) " + message);
    }
}
