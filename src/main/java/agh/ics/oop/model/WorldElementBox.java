package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WorldElementBox extends VBox {
    private static final int IMAGE_SIZE = 60;

    public WorldElementBox(WorldElement worldElement) {
        this(worldElement,false);
    }
    public WorldElementBox(WorldElement element,boolean isLabel) {
        Image image = element.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);


        if(isLabel) {
            Label infoLabel = new Label(element.getInfo());
            this.getChildren().addAll(infoLabel);
        }
        this.getChildren().addAll(imageView);
        this.setAlignment(Pos.CENTER);

    }

}