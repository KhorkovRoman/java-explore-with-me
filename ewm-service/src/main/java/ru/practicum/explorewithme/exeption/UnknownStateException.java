package ru.practicum.explorewithme.exeption;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String message) {
        super(message);
    }
}
