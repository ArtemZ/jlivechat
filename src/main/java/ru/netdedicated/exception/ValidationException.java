package ru.netdedicated.exception;

/**
 * Created by artemz on 11.02.17.
 */
public class ValidationException extends Throwable {
    public ValidationException(String message) {
        super(message);
    }
}
