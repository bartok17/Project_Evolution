package agh.ics.oop.model;

import agh.ics.oop.Simulation;
import agh.ics.oop.util.ConsoleMapDisplay;
import agh.ics.oop.OptionsParser;
import agh.ics.oop.util.FileMapDisplay;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

    @Test
    void move() {
        String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b"};
        List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
        GrassField worldMap = new GrassField(10,1);
        Simulation simulation = new Simulation(positions, directions,worldMap);
        ConsoleMapDisplay display = new ConsoleMapDisplay();
        FileMapDisplay fileMapDisplay = new FileMapDisplay();
        worldMap.addListener(display);
        worldMap.addListener(fileMapDisplay);
        simulation.run();
        assertTrue(worldMap.isOccupied(new Vector2d(2,0)));
        assertTrue(worldMap.isOccupied(new Vector2d(3,8)));
        assertFalse(worldMap.isOccupied(new Vector2d(2,1)));
        assertEquals(10,worldMap.grasses.size());

    }
    @Test
    void GetSortedAnimals() {
        String[] strings = {"l", "r" ,"l", "r", "f", "b", "f", "b", "f", "b", "f", "b"};
        List<MoveDirection> directions = OptionsParser.ParseMoveDirection(strings);
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4),new Vector2d(2,4));
        GrassField worldMap = new GrassField(10,1);
        Simulation simulation = new Simulation(positions, directions,worldMap);
        ConsoleMapDisplay display = new ConsoleMapDisplay();
        worldMap.addListener(display);
        List<Animal> aniList =  new ArrayList<>(worldMap.animals.values());
        List<Animal> correctOrder = List.of(aniList.get(0),aniList.get(2),aniList.get(1));
        assertEquals(worldMap.getOrderedAnimals(),correctOrder);
        simulation.run();

    }

}