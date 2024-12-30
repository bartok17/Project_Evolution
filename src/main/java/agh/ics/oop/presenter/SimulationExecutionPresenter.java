package agh.ics.oop.presenter;

import agh.ics.oop.model.AbstractWorldMap;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElementBox;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.model.interfaces.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.geometry.HPos;

import javafx.scene.control.Label;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Optional;

public class SimulationExecutionPresenter implements MapChangeListener {
    private static final int CELL_HEIGHT = 40;
    private static final double CELL_WIDTH = 40;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label descriptionLabel;

    private WorldMap worldMap;
    private String storedMessage;
    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    private void drawMap() {
        descriptionLabel.setText(storedMessage);
        clearGrid();
        AbstractWorldMap.Boundary boundary = worldMap.getCurrentBounds();

        //WorldElement elements = mapGrid.


        for (int x = 0; x <= boundary.upperRight().x(); x++) {
            for (int y = 0; y <= boundary.upperRight().y(); y++) {
                if (x == 0) mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
                Optional<WorldElement> object = worldMap.objectAt(new Vector2d(x, y));
                if (object.isPresent()) {
                    WorldElementBox label = new WorldElementBox(object.get());

                    mapGrid.add(label, x + 1, y + 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                }
            }
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));

        for (int x = 0; x <= boundary.upperRight().x(); x++) {
            Label label = new Label();
            label.setText(Integer.toString(x));
            mapGrid.add(label, x + 1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int y = 0; y <= boundary.upperRight().y(); y++) {
            Label label = new Label();
            label.setText(Integer.toString(y));
            mapGrid.add(label, 0, y + 1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        Label label = new Label();
        label.setText("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }


    @Override
    public void onMapChanged(WorldMap worldMap, String message) {
        storedMessage = message;
        Platform.runLater(this::drawMap);
    }
}
