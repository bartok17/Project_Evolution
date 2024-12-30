package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.util.ConsoleMapDisplay;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void run() {
        try {
            String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b"};
            List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4),new Vector2d(3,4));
            AbstractWorldMap worldMap = new GrassField(7,1);
            Simulation simulation = new Simulation(positions, directions,worldMap);
            ConsoleMapDisplay display = new ConsoleMapDisplay();

            worldMap.addListener(display);
            simulation.run();
            assertEquals(new Vector2d(2, 0), simulation.animals.get(0).getPosition());
            assertEquals(new Vector2d(3, 8), simulation.animals.get(1).getPosition());
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void runFailed() {
        try {
            String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b","wrong_arg"};
            List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4),new Vector2d(3,4));
            AbstractWorldMap worldMap = new RectangularMap(4,4,1);
            Simulation simulation = new Simulation(positions, directions,worldMap);
            ConsoleMapDisplay display = new ConsoleMapDisplay();
            worldMap.addListener(display);
            simulation.run();
            fail();
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }
}