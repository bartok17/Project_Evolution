package agh.ics.oop.model;

import agh.ics.oop.util.MapVisualizer;

public class RectangularMap extends AbstractWorldMap{

    private final int width;
    private final int height;
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Boundary boundary;

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }


    public RectangularMap(int width, int height,int mapId)
    {
        super(mapId);
        this.width = width;
        this.height = height;
        mapVisualizer = new MapVisualizer((this));
        lowerLeft = new Vector2d(0, 0);
        upperRight = new Vector2d(width-1, height-1);
        boundary = new Boundary(lowerLeft, upperRight);

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if (isOccupied(position)) return false;
        else if (position.precedes(lowerLeft) || position.follows(upperRight)) return false;
        else return true;
    }


    @Override
    public Boundary getCurrentBounds() {
        return boundary;
    }

}
