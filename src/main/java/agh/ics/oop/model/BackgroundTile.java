package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import javafx.scene.image.Image;

import java.util.HashMap;

public class BackgroundTile implements WorldElement { // co to reprezentuje?
    final static private HashMap<String, Image> images = new HashMap<String, Image>();
    final private Vector2d position;

    public Vector2d getPosition() {
        return position;
    }

    public BackgroundTile(Vector2d position) {
        this.position = position;
    }


    @Override
    public boolean isAt(Vector2d coordinates) {
        return false; // que?
    }

    @Override
    public Image getImage() {
        if (!images.containsKey("backgroundTile.png")) {
            images.put("backgroundTile.png", new Image(getResourceName()));
        }
        return images.get("backgroundTile.png");
    }

    @Override
    public String getResourceName() {
        return "backgroundTile.png";
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
