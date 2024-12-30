package agh.ics.oop.model.interfaces;

import agh.ics.oop.model.Vector2d;

public interface WorldElement {
    public Vector2d getPosition();

    public boolean isAt(Vector2d coordinates);

    public String getResourceName();

    public String getInfo();
}
