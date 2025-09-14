package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Grass implements WorldElement {
    private static final Map<String, Image> IMAGES = new HashMap<>();

    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d coordinates) {
        return Objects.equals(position, coordinates);
    }

    @Override
    public Image getImage() {
        String resourceName = getResourceName();
        IMAGES.computeIfAbsent(resourceName, rn -> new Image(rn));
        return IMAGES.get(resourceName);
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
