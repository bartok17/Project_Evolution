package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class WorldElementBox extends VBox {

    public WorldElementBox(WorldElement worldElement,float imageSize) {
        this(worldElement,imageSize,false);

    }
    public WorldElementBox(WorldElement element,float imageSize,boolean isLabel) {
        Image image = element.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);


        if(isLabel) {
            Label infoLabel = new Label(element.getInfo());
            infoLabel.setFont(new Font(imageSize /4));
            this.getChildren().addAll(infoLabel);
        }
        this.getChildren().addAll(imageView);
        this.setAlignment(Pos.CENTER);

    }

}