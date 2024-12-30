package agh.ics.oop.model;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.util.ConsoleMapDisplay;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    @Test
    void runSync() {
        try {
            List<Simulation> simulations = new ArrayList<>();
            List<ConsoleMapDisplay> consoleMapDisplays = new ArrayList<>();
            String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b"};
            List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
            for (int i = 0; i < 100; i++) {
                AbstractWorldMap worldMap1 = new RectangularMap(4, 4, i);
                Simulation simulation1 = new Simulation(positions, directions, worldMap1);
                ConsoleMapDisplay display1 = new ConsoleMapDisplay();
                consoleMapDisplays.add(display1);
                worldMap1.addListener(display1);
                simulations.add(simulation1);
            }
            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runSync();
            for (ConsoleMapDisplay display : consoleMapDisplays) {
                assertEquals(12,display.getMoveCount());
                //System.out.println(display.getMoveCount());

            }

        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println("\n \n \nSystem finished the program");
    }

    @Test
    void runAsync() {

        try {
            List<Simulation> simulations = new ArrayList<>();
            List<ConsoleMapDisplay> consoleMapDisplays = new ArrayList<>();
            String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b"};
            List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
            for (int i = 0; i < 2; i++) {
                AbstractWorldMap worldMap1 = new RectangularMap(4, 4, i);
                Simulation simulation1 = new Simulation(positions, directions, worldMap1);
                ConsoleMapDisplay display1 = new ConsoleMapDisplay();
                consoleMapDisplays.add(display1);
                worldMap1.addListener(display1);
                simulations.add(simulation1);
            }
            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runAsync();
            engine.awaitSimulationsEnd();
            for (ConsoleMapDisplay display : consoleMapDisplays) {
                assertEquals(12,display.getMoveCount());
                //System.out.println(display.getMoveCount());

            }
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n \n \nSystem finished the program");
    }

    @Test
    void runAsyncInThreadPool() {
        try {
            List<Simulation> simulations = new ArrayList<>();
            List<ConsoleMapDisplay> consoleMapDisplays = new ArrayList<>();
            String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b"};
            List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
            for (int i = 0; i < 2; i++) {
                AbstractWorldMap worldMap1 = new RectangularMap(4, 4, i);
                Simulation simulation1 = new Simulation(positions, directions, worldMap1);
                ConsoleMapDisplay display1 = new ConsoleMapDisplay();
                consoleMapDisplays.add(display1);
                worldMap1.addListener(display1);
                simulations.add(simulation1);
            }
            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runAsyncInThreadPool();
            engine.awaitSimulationsEnd();
            for (ConsoleMapDisplay display : consoleMapDisplays) {
                assertEquals(12,display.getMoveCount());
                //System.out.println(display.getMoveCount());

            }
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n \n \nSystem finished the program");
    }
}