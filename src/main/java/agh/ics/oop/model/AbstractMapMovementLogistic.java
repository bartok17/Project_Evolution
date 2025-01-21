package agh.ics.oop.model;

public abstract class AbstractMapMovementLogistic {

    protected GrassField grassField;
    GrassField getGrassField() {return grassField;}
    public void setGrassField(GrassField grassField) {this.grassField = grassField;}

    public AbstractMapMovementLogistic() {

    }

    public abstract boolean canMoveTo(Vector2d position);

    public abstract Vector2d convertMove(Vector2d position);

    public abstract void turnEvent();

    public abstract int getConsumedEnergy(Vector2d position);


}
