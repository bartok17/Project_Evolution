package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class BackgroundTile implements WorldElement {

    private static final String RESOURCE_NAME = "backgroundTile.png";
    private static final Map<String, Image> IMAGES = new HashMap<>();

    private final Vector2d position;

    public BackgroundTile(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d coordinates) {
        return false;
    }

    @Override
    public Image getImage() {
        if (!IMAGES.containsKey(RESOURCE_NAME)) {
            IMAGES.put(RESOURCE_NAME, new Image(getResourceName()));
        }
        return IMAGES.get(RESOURCE_NAME);
    }

    @Override
    public String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    public String getInfo() {
        return "Background";
    }

    @Override
    public String toString() {
        return " ";
    }
}
