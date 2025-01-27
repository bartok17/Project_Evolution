package agh.ics.oop.model.interfaces;

import agh.ics.oop.model.Vector2d;

public interface MoveValidator {

    /**
     * Indicate if any object can move to the given position.
     *
     * @param position The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Returns correct position by defined rules
     *
     * @param position The position that would be used before conversion
     * @return New position using rules
     */
    Vector2d convertMove(Vector2d position);
}