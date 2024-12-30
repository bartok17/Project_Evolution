package agh.ics.oop;
import agh.ics.oop.model.*;
import agh.ics.oop.util.ConsoleMapDisplay;

import java.util.ArrayList;
import java.util.List;


public class World {
    public static void main(String[] args) {

        try {
            List<Simulation> simulations = new ArrayList<>();
            List<MoveDirection> directions = OptionsParser.ParseMoveDirection(args);
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
            for (int i = 0; i < 10; i++) {
                //10000 powoduje że printowanie zajmuje zbyt dużo czasu
                AbstractWorldMap worldMap1 = new RectangularMap(4, 4, i);
                Simulation simulation1 = new Simulation(positions, directions, worldMap1);
                ConsoleMapDisplay display1 = new ConsoleMapDisplay();
                worldMap1.addListener(display1);
                simulations.add(simulation1);
            }
            List<Vector2d> positions2 = List.of(new Vector2d(3,3), new Vector2d(1,4));
            for (int i = 5; i < 10; i++) {
                //10000 powoduje że printowanie zajmuje zbyt dużo czasu
                AbstractWorldMap worldMap1 = new RectangularMap(4, 4, i);
                Simulation simulation1 = new Simulation(positions2, directions, worldMap1);
                ConsoleMapDisplay display1 = new ConsoleMapDisplay();
                worldMap1.addListener(display1);
                simulations.add(simulation1);
            }
            AbstractWorldMap worldMap2 = new GrassField(7,2);
            Simulation simulation2 = new Simulation(positions, directions,worldMap2);
            ConsoleMapDisplay display2 = new ConsoleMapDisplay();
            //worldMap2.addListener(display2);
            simulations.add(simulation2);
            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runAsyncInThreadPool();
            try {
                engine.awaitSimulationsEnd();
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println("\n \n \nSystem finished the program");


    }

}