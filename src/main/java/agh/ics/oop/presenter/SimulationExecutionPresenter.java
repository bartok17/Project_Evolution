package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.BackgroundTile;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElementBox;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.model.interfaces.WorldMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimulationExecutionPresenter implements MapChangeListener {

    private float CELL_HEIGHT;

    @FXML
    private Button speedUpButton;

    @FXML
    private Button speedDownButton;

    @FXML
    private Button quitAnimal;

    @FXML
    private Button pauseButton;

    @FXML
    private Label animalCount;

    @FXML
    private Label grassCount;

    @FXML
    private Label freeSpaces;

    @FXML
    private Label averageEnergy;

    @FXML
    private Label averageLifespan;

    @FXML
    private Label averageChildren;

    @FXML
    private StackPane leftPanelStack;

    @FXML
    private VBox infoPanelWorld;

    @FXML
    private VBox infoPanelAnimal;

    @FXML
    private Label turn;

    @FXML
    private Label name;

    @FXML
    private Label genome;

    @FXML
    private Label genomePart;

    @FXML
    private Label energy;

    @FXML
    private Label foodEaten;

    @FXML
    private Label children;

    @FXML
    private Label relativeChildren;

    @FXML
    private Label life;

    @FXML
    private Label dead;

    @FXML
    private GridPane mapGrid;

    private GrassField worldMap;
    private Simulation simulation;
    private Animal observedAnimal;

    public void setWorldMap(GrassField worldMap, Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = worldMap;
        this.CELL_HEIGHT = (float) 800 / worldMap.getHeight();
    }

    private void drawMap() {
        clearGrid();

        animalCount.setText("Animals: " + Integer.toString(simulation.getAnimalCount()));
        grassCount.setText("Grasses: " + Integer.toString(simulation.getGrassCount()));
        freeSpaces.setText("Free Spaces: " + worldMap.getFreeSpaces());
        averageEnergy.setText("Avg. Energy: " + worldMap.getAvgEnergy());
        averageLifespan.setText("Avg. Lifespan: " + worldMap.getAvgDeadAnimals());
        averageChildren.setText("Avg. Children: " + worldMap.getAvgChildren());
        turn.setText("Turn: " + simulation.getTurn());

        if (observedAnimal != null) {
            name.setText("Name: " + observedAnimal.getName());
            genome.setText("Genome: " + Arrays.toString(observedAnimal.getGens()));
            genomePart.setText("Genome Part: " + observedAnimal.getCurrentGen());
            energy.setText("Energy: " + observedAnimal.getEnergy());
            foodEaten.setText("Food Eaten: " + observedAnimal.getFoodEaten());
            children.setText("Children: " + observedAnimal.getChildren());
            relativeChildren.setText("Relative Children: " + observedAnimal.getTotalChildren());
            life.setText("Life Span: " + observedAnimal.getAge());
            if (observedAnimal.getEnergy() <= 0) {
                dead.setText("Died at turn: " + observedAnimal.getDiedAtTurn());
            } else {
                dead.setText("Animal is alive");
            }
        }

        AbstractWorldMap.Boundary boundary = worldMap.getCurrentBounds();

        for (int x = 0; x <= boundary.upperRight().x(); x++) {
            for (int y = 0; y <= boundary.upperRight().y(); y++) {
                if (x == 0) {
                    mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
                }
                List<WorldElement> object = worldMap.objectsAt(new Vector2d(x, y));
                object.add(new BackgroundTile(new Vector2d(x, y)));
                Collections.reverse(object);

                for (int i = 0; i < object.size(); i++) {
                    WorldElementBox label;
                    if (i == object.size() - 1 && object.get(i) instanceof Animal animal) {
                        label = new WorldElementBox(object.get(i), CELL_HEIGHT, true);
                        label.setOnMouseClicked(event -> onAnimalEnter(animal));
                    } else {
                        label = new WorldElementBox(object.get(i), CELL_HEIGHT);
                    }
                    mapGrid.add(label, x + 1, y + 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                }
            }
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_HEIGHT));
        }
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_HEIGHT));
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

        Image mark = new Image("mark.png");
        ImageView markView = new ImageView(mark);
        markView.setFitHeight(CELL_HEIGHT);
        markView.setFitWidth(CELL_HEIGHT);

        mapGrid.add(markView, observedAnimal.getPosition().x() + 1, observedAnimal.getPosition().y() + 1);
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void onMapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::drawMap);
    }

    public void onSimulationPaused(ActionEvent actionEvent) {
        if (!simulation.getPaused()) {
            simulation.pause();
        } else {
            simulation.resume();
        }
    }

    public void onSimulationSpeedUp(ActionEvent actionEvent) {
        simulation.increaseSpeed();
    }

    public void onSimulationSpeedDown(ActionEvent actionEvent) {
        simulation.decreaseSpeed();
    }

    public void onAnimalExit(ActionEvent actionEvent) {
        infoPanelAnimal.setVisible(false);
        infoPanelWorld.setVisible(true);
        observedAnimal = null;
        drawMap();
    }

    public void onAnimalEnter(Animal animal) {
        infoPanelAnimal.setVisible(true);
        infoPanelWorld.setVisible(false);
        observedAnimal = animal;
        drawMap();
    }
}
