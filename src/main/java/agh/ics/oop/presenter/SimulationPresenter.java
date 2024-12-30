package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import agh.ics.oop.model.interfaces.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationPresenter{
    private static final int MAX_SIMULTANEOUS_SIMULATIONS = 4;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_SIMULTANEOUS_SIMULATIONS);

    @FXML
    private Button startButton;
    @FXML
    private TextField argsTextField;
    private int lastIndex = 1;

    public void onSimulationStartClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulationResult.fxml"));
            BorderPane simulationRoot = loader.load();
            SimulationExecutionPresenter newPresenter = loader.getController();

            String[] strings = argsTextField.getText().split(" ");
            List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
            AbstractWorldMap newWorldMap = new GrassField(10, lastIndex);
            lastIndex++;
            Simulation simulation = new Simulation(positions, directions, newWorldMap);

            newPresenter.setWorldMap(newWorldMap);
            newWorldMap.addListener(newPresenter);

            MapChangeListener loggingObserver = (map, message) -> {
                String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println(datetime + " " + message);
            };
            newWorldMap.addListener(loggingObserver);

            Stage simulationStage = new Stage();
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
}
