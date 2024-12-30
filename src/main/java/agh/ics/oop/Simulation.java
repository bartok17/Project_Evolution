package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.Exceptions.IncorrectPositionException;
import agh.ics.oop.model.interfaces.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    final List<MoveDirection> directions;
    final List<Animal> animals = new ArrayList<>();
    private final int animal_count;
    final WorldMap worldMap;
    public Simulation(List<Vector2d> positions, List<MoveDirection> directions,WorldMap worldMap) {
        this.directions = directions;
        this.worldMap = worldMap;
        for (Vector2d position : positions) {
            Animal animal = new Animal(position);
            try {


                worldMap.place(animal);
                animals.add(animal);
            }
            catch (IncorrectPositionException e)
            {
                //This Exception should not do any further actions
                System.out.println(e.getMessage());
            }

        }
        animal_count = animals.size();
    }
    @Override
    public void run()
    {
        try{
        for (int i = 0; i < directions.size(); i++) {

            Thread.sleep(500);


            MoveDirection direction = directions.get(i);
            worldMap.move(animals.get(i % animal_count), direction);
        }

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
