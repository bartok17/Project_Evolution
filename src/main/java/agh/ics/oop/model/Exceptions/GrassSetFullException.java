package agh.ics.oop.model.Exceptions; // nazwy modułów małymi literami

public class GrassSetFullException extends RuntimeException { // czemu RuntimeException?
    public GrassSetFullException(String message) {
        super(message);
    }
}
