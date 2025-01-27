package agh.ics.oop.model.Exceptions;

import agh.ics.oop.model.Vector2d;

public class IncorrectPositionException extends Exception { // czy to jest używane?
    public IncorrectPositionException(Vector2d position) {
        super("Position (" + position + ") is not correct.");
    }
}