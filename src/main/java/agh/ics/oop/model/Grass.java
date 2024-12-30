package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;

public class Grass implements WorldElement {
    final private Vector2d position;
    public Vector2d getPosition() {
        return position;
    }
    public Grass(Vector2d position) {
        this.position = position;
    }


    @Override
    public boolean isAt(Vector2d coordinates) {
        return false;
    }

    @Override
    public String getResourceName() {
        return "grass_MC.png";
    }

    @Override
    public String getInfo() {
        return "Grass";
    }

    @Override
    public String toString() {
        return "*";
    }


}
