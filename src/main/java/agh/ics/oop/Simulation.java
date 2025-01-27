package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.Exceptions.IncorrectPositionException;
import agh.ics.oop.model.Exceptions.PlacementThresholdExceededException;
import agh.ics.oop.model.interfaces.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {

    private final Object pauseLock = new Object();

    private boolean running = true;
    private boolean paused = false;

    public int getAnimalCount() {
        return animals.size();
    }

    public int getGrassCount() {
        return worldMap.getGrassCount();
    }

    private int speed = 5; // czemu ten atrybut jest tutaj?

    public void increaseSpeed() {
        speed--; // increase to --?
        if (speed <= 0) speed = 1;
    }

    public void decreaseSpeed() {
        speed++;
        if (speed >= 10) speed = 10;
    }

    public boolean getPaused() {
        return paused;
    }

    final NameGenerator nameGenerator = new NameGenerator(); // a tu kolejne atrybuty

    final List<Animal> animals = new ArrayList<>();

    final GrassField worldMap;

    final int genSize; // gene; czy to cecha symulacji?
    final int minMutations;
    final int maxMutations;

    final int grassSpawn;
    final int energyToReproduce;
    final int startingEnergy;
    final int energyReproductionCost;

    public Simulation(int animalCount, GrassField worldMap, int genSize, int minMutations, int maxMutations, int energyToReproduce, int energyReproductionCost, int startingEnergy, int grassSpawn) {
        this.genSize = genSize;
        this.worldMap = worldMap;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.energyToReproduce = energyToReproduce;
        this.startingEnergy = startingEnergy;
        this.grassSpawn = grassSpawn;

        this.energyReproductionCost = energyReproductionCost;

        for (int i = 0; i < animalCount; i++) {

            try {
                Vector2d pos = Vector2d.random(worldMap.getCurrentBounds());

                while (worldMap.isOccupied(pos)) {
                    pos = Vector2d.random(worldMap.getCurrentBounds());
                }
                Animal animal = new Animal(pos, genSize, minMutations, maxMutations, worldMap.getGeneSwap(), startingEnergy, nameGenerator.generateName());

                worldMap.place(animal);
                animals.add(animal);
                System.out.println("Animal placed: " + animal);
            } catch (IncorrectPositionException | PlacementThresholdExceededException e) {
                //This Exception should not do any further actions
                System.out.println(e.getMessage());
            }

        }

    }

    public void executeTurn() {
        for (int i = 0; i < animals.size(); i++) {

            if (animals.get(i).getEnergy() <= 0) {
                kill(animals.get(i));
            }
        }

        for (Animal animal : animals) {
            // po co tyle pustych linii?

            worldMap.move(animal, MoveDirection.FORWARD);
            animal.useGen();
            animal.ageAnimal();


        }
        for (int y = 0; y < worldMap.getHeight(); y++) {
            for (int x = 0; x < worldMap.getWidth(); x++) {

                Vector2d vector2d = new Vector2d(x, y);
                List<Animal> animalList = worldMap.getOrderedAnimals(vector2d);
                if (!animalList.isEmpty()) {
                    Animal animal = animalList.get(0);
                    if (worldMap.isOccupiedByGrass(animal.getPosition())) {
                        worldMap.removeGrass(animal.getPosition());
                        animal.eat(worldMap.getGrassValue());
                    }
                }

            }
        }

        for (int y = 0; y < worldMap.getHeight(); y++) {
            for (int x = 0; x < worldMap.getWidth(); x++) {

                reproduce(new Vector2d(x, y));


            }
        }
        for (int i = 0; i < grassSpawn; i++)
            worldMap.addGrass();
        turn++;
    }

    private void reproduce(Vector2d pos) { // czy to zadanie dla symulacji?
        List<Animal> animalList = worldMap.getOrderedAnimals(pos);
        if (animalList.size() >= 2) {

            if (animalList.get(1).getEnergy() > energyToReproduce) {
                System.out.println("Ready at " + pos.toString());
                Animal animal1 = animalList.get(0);
                Animal animal2 = animalList.get(1);
                int energy1 = animal1.getEnergy();
                int energy2 = animal2.getEnergy();

                int energy1Percentage = (genSize * energy1) / (energy1 + energy2);

                Animal newAnimal = new Animal(pos, genSize, minMutations, maxMutations, worldMap.getGeneSwap(), 2 * energyReproductionCost, nameGenerator.generateName(), animal1.getGens(), animal2.getGens(), energy1Percentage);
                try {
                    worldMap.place(newAnimal);
                    animals.add(newAnimal);
                    newAnimal.addRelatives(animal1);
                    newAnimal.addRelatives(animal2);
                    newAnimal.addRelatives(animal1.getRelatives());
                    newAnimal.addRelatives(animal2.getRelatives());
                    newAnimal.updateGenealogy();
                    animal1.addChildren();
                    animal2.addChildren();
                    animalList.get(0).useEnergy(energyReproductionCost);
                    animalList.get(1).useEnergy(energyReproductionCost);

                    System.out.println("Animal placed: " + newAnimal);
                } catch (IncorrectPositionException e) { // czy to się może zdarzyć?
                    System.out.println(e.getMessage()); // czy to dobry wybór?
                }


            }
        }
    }

    private void kill(Animal animal) {
        animals.remove(animal);
        worldMap.removeAnimal(animal, animal.getPosition());
        worldMap.addDeadAnimal(animal.getAge());
        animal.setDiedAtTurn(turn);

    }

    private int turn = 0;

    public int getTurn() {
        return turn;
    }


    public void exit() {
        running = false;
    }

    public void pause() {
        paused = true;
        System.out.println("Simulation paused");
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Notify the simulation thread to continue
        }
        System.out.println("Simulation resumed");
    }

    @Override

    public void run() {
        System.out.println("Simulation started");
        try {
            while (running) {
                executeTurn();
                System.out.println("Turn " + turn);
                Thread.sleep(200L * speed);

                synchronized (pauseLock) {
                    while (paused) {
                        pauseLock.wait();
                    }
                }
            }

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
