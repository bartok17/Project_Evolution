package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.interfaces.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationPresenter implements Initializable {

    private static final int MAX_SIMULTANEOUS_SIMULATIONS = 4;
    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(MAX_SIMULTANEOUS_SIMULATIONS);

    public TextField presetName;
    public Button savePreset;
    public ComboBox<String> fileSelector;

    @FXML
    private TextField geneLength;
    @FXML
    private ComboBox<String> animalType;
    @FXML
    private ComboBox<String> mapType;
    @FXML
    private ComboBox<String> grassFieldType;
    @FXML
    private ComboBox<String> mutationType;
    @FXML
    private TextField energyToReproduce;
    @FXML
    private TextField reproductionCost;
    @FXML
    private TextField minMutation;
    @FXML
    private TextField maxMutation;
    @FXML
    private TextField startingEnergy;
    @FXML
    private TextField startingAnimals;
    @FXML
    private TextField grassFrequency;
    @FXML
    private TextField grassEnergy;
    @FXML
    private TextField startingGrass;
    @FXML
    private Button startButton;
    @FXML
    private TextField mapHeightField;
    @FXML
    private TextField mapWidthField;

    private int lastIndex = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapType.getItems().addAll("O", "A");
        grassFieldType.getItems().addAll("O");
        mutationType.getItems().addAll("0", "2");
        animalType.getItems().addAll("O");

        mapType.setValue("O");
        grassFieldType.setValue("O");
        mutationType.setValue("0");
        animalType.setValue("O");

        checkAllValues();
        loadTxtFilesFromFolder("/saves");

        fileSelector.setOnAction(event -> {
            String selectedFile = fileSelector.getValue();
            if (selectedFile != null) {
                parseFileContents("/saves/" + selectedFile);
                presetName.setText(selectedFile);
            }
        });
    }

    private void checkAllValues() {
        // Remember to use only already checked variables for reference to next variables
        checkMinMaxValues(mapHeightField, 5, 100);
        checkMinMaxValues(mapWidthField, 5, 100);
        checkMinMaxValues(startingAnimals, 1, getHeight() * getWidth() / 2);
        checkMinMaxValues(startingEnergy, 1, 10000);
        checkMinMaxValues(startingGrass, 0, getHeight() * getWidth());
        checkMinMaxValues(reproductionCost, 1, 10000);
        checkMinMaxValues(energyToReproduce, Integer.parseInt(reproductionCost.getText()), 10000);
        checkMinMaxValues(minMutation, 0, 10000);
        checkMinMaxValues(maxMutation, Integer.parseInt(minMutation.getText()) + 1, 10000);
        checkMinMaxValues(grassFrequency, 1, getHeight() * getWidth());
        checkMinMaxValues(grassEnergy, 1, 10000);
        checkMinMaxValues(geneLength, 1, 100);
    }

    private void checkMinMaxValues(TextField field, int min, int max) {
        if (field.textProperty().get().isEmpty()) {
            field.textProperty().setValue(String.valueOf(min));
        }
        if (!field.textProperty().get().matches("\\d+")) {
            field.textProperty().setValue(String.valueOf(min));
        }
        if (Integer.parseInt(field.textProperty().get()) > max) {
            field.textProperty().setValue(String.valueOf(max));
        }
        if (Integer.parseInt(field.textProperty().get()) < min) {
            field.textProperty().setValue(String.valueOf(min));
        }
    }

    private int getHeight() {
        return Integer.parseInt(mapHeightField.getText());
    }

    private int getWidth() {
        return Integer.parseInt(mapWidthField.getText());
    }

    public void onSimulationStartClicked() {
        checkAllValues();
        try {
            int height = Integer.parseInt(mapHeightField.getText());
            int width = Integer.parseInt(mapWidthField.getText());

            FXMLLoader loader =
                    new FXMLLoader(getClass().getClassLoader().getResource("simulationResult.fxml"));
            BorderPane simulationRoot = loader.load();
            SimulationExecutionPresenter newPresenter = loader.getController();

            AbstractMapMovementLogistic movementLogistic = new NormalMapMovementLogistic();
            if (mapType.getValue().equals("A")) {
                movementLogistic = new PoleMapMovementLogistic();
            }
            boolean geneSwap = mapType.getValue().equals("2");

            GrassField newWorldMap = new RequiredGrassField(
                    width,
                    height,
                    Integer.parseInt(startingGrass.getText()),
                    geneSwap,
                    Integer.parseInt(grassEnergy.getText()),
                    lastIndex,
                    movementLogistic
            );

            lastIndex++;

            Simulation simulation = new Simulation(
                    Integer.parseInt(startingAnimals.getText()),
                    newWorldMap,
                    Integer.parseInt(geneLength.getText()),
                    Integer.parseInt(minMutation.getText()),
                    Integer.parseInt(maxMutation.getText()),
                    Integer.parseInt(energyToReproduce.getText()),
                    Integer.parseInt(reproductionCost.getText()),
                    Integer.parseInt(startingEnergy.getText()),
                    Integer.parseInt(grassFrequency.getText())
            );

            newPresenter.setWorldMap(newWorldMap, simulation);
            newWorldMap.addListener(newPresenter);

            MapChangeListener loggingObserver = (map, message) -> {
                String datetime =
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println(datetime + " " + message);
            };
            newWorldMap.addListener(loggingObserver);

            Stage simulationStage = new Stage();
            simulationStage.setOnCloseRequest(event -> simulation.exit());
            simulationStage.setScene(new Scene(simulationRoot));
            simulationStage.setTitle("Simulation");
            simulationStage.show();

            executorService.submit(simulation);
        } catch (IOException e) {
            System.out.println("Cant load the simulation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadTxtFilesFromFolder(String folderPath) {
        Path folder = Paths.get(System.getProperty("user.dir"), folderPath);

        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
                System.out.println("Folder created: " + folderPath);
            } catch (IOException e) {
                System.err.println("Error creating folder: " + e.getMessage());
                return;
            }
        }

        try (java.util.stream.Stream<Path> files = Files.list(folder)) {
            files.filter(file -> file.toString().endsWith(".txt"))
                    .forEach(file -> fileSelector.getItems().add(file.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Error loading files: " + e.getMessage());
        }
    }

    private void parseFileContents(String filePath) {
        Path fullPath = Paths.get(System.getProperty("user.dir"), filePath);
        try {
            String content = Files.readString(fullPath);
            String[] values = content.split(";");

            mapHeightField.setText(values[0]);
            mapWidthField.setText(values[1]);
            startingAnimals.setText(values[2]);
            startingEnergy.setText(values[3]);
            startingGrass.setText(values[4]);
            reproductionCost.setText(values[5]);
            energyToReproduce.setText(values[6]);
            minMutation.setText(values[7]);
            maxMutation.setText(values[8]);
            grassFrequency.setText(values[9]);
            grassEnergy.setText(values[10]);
            geneLength.setText(values[11]);
            mapType.setValue(values[12]);
            grassFieldType.setValue(values[13]);
            mutationType.setValue(values[14]);
            animalType.setValue(values[15]);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing file content: " + e.getMessage());
        }
    }

    public void onSavePresetClicked(ActionEvent actionEvent) {
        String presetFileName = presetName.getText().trim();
        if (presetFileName.isEmpty()) {
            System.err.println("Preset name cannot be empty.");
            return;
        }

        String filePath = System.getProperty("user.dir") + "/saves/" + presetFileName;
        String content = String.join(
                ";",
                mapHeightField.getText(),
                mapWidthField.getText(),
                startingAnimals.getText(),
                startingEnergy.getText(),
                startingGrass.getText(),
                reproductionCost.getText(),
                energyToReproduce.getText(),
                minMutation.getText(),
                maxMutation.getText(),
                grassFrequency.getText(),
                grassEnergy.getText(),
                geneLength.getText(),
                mapType.getValue(),
                grassFieldType.getValue(),
                mutationType.getValue(),
                animalType.getValue()
        );

        try {
            Files.writeString(Paths.get(filePath), content);
            System.out.println("Preset saved to: " + filePath);
            fileSelector.getItems().add(presetFileName);
        } catch (IOException e) {
            System.err.println("Error saving preset: " + e.getMessage());
        }
    }
}
