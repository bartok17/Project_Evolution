package agh.ics.oop.model;

public class NormalMapMovementLogistic extends AbstractMapMovementLogistic {

    public NormalMapMovementLogistic() {
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
        }
        return position;
    }

    @Override
    public void turnEvent() {
    }

    @Override
    public int getConsumedEnergy(Vector2d position) {
        return 1;
    }
}
