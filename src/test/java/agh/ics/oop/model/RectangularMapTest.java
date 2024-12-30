package agh.ics.oop.model;

import agh.ics.oop.Simulation;
import agh.ics.oop.util.ConsoleMapDisplay;
import agh.ics.oop.OptionsParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class RectangularMapTest {

    @Test
    void move() {
        String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b"};
        List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
        RectangularMap worldMap = new RectangularMap(5,5,1);
        Simulation simulation = new Simulation(positions, directions,worldMap);
        ConsoleMapDisplay display = new ConsoleMapDisplay();
        worldMap.addListener(display);
        simulation.run();
        assertTrue(worldMap.isOccupied(new Vector2d(2,0)));
        assertTrue(worldMap.isOccupied(new Vector2d(3,4)));
        assertFalse(worldMap.isOccupied(new Vector2d(2,1)));

    }
}