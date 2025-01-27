package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import javafx.scene.image.Image;

import java.util.HashMap;

public class Grass implements WorldElement {
    final static private HashMap<String, Image> images = new HashMap<String, Image>();
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
    public Image getImage() {
        if (!images.containsKey("grass-2.png")) { // czemu tu jest inna nazwa niż zwraca getResourceName?
            images.put("grass-2.png", new Image(getResourceName()));
        }
        return images.get("grass-2.png");
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
