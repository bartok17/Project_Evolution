package agh.ics.oop.model;

import static java.lang.Math.abs;

public class PoleMapMovementLogistic extends AbstractMapMovementLogistic {

    public PoleMapMovementLogistic() {
        super();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(position.precedes(grassField.lowerLeft) || position.follows(grassField.upperRight));
    }

    @Override
    public Vector2d convertMove(Vector2d position) {
        if (position.x() < grassField.lowerLeft.x()) {
            return new Vector2d(grassField.width - 1, position.y());
        } else if (position.x() > grassField.upperRight.x()) {
            return new Vector2d(0, position.y());
        } else if (position.y() < grassField.lowerLeft.y()) {
            return new Vector2d(position.x(), grassField.height - 1);
        } else if (position.y() > grassField.upperRight.y()) {
            return new Vector2d(position.x(), 0);
        }
        return position;
    }

    @Override
    public void turnEvent() {
        // no-op
    }

    @Override
    public int getConsumedEnergy(Vector2d position) {
        return abs((grassField.height / 2) - position.y());
    }
}
