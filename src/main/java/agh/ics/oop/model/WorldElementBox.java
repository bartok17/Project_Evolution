package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WorldElementBox extends VBox {
    private static final int IMAGE_SIZE = 40;

    public WorldElementBox(WorldElement element) {
        Image image = new Image(element.getResourceName());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);

        Label infoLabel = new Label(element.getInfo());

        this.getChildren().addAll(imageView);
        this.setAlignment(Pos.CENTER);
    }
}