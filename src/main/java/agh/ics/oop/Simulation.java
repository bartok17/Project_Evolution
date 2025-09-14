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

    // Returns current number of animals in simulation
    public int getAnimalCount(){return animals.size();}
    // Returns current number of grass tiles on the map
    public int getGrassCount(){return worldMap.getGrassCount();}

    private int speed = 5;
    // Increases simulation speed (lower sleep time)
    public void increaseSpeed() {speed--;
        if (speed <= 0) speed = 1;}
    // Decreases simulation speed (higher sleep time)
    public void decreaseSpeed() {speed++;
        if (speed >= 10) speed = 10;
   }
    // Returns whether simulation is paused
    public boolean getPaused() {return paused;}

    final NameGenerator nameGenerator = new NameGenerator();

    final List<Animal> animals = new ArrayList<>();

    final GrassField worldMap;

    final int genSize;
    final int minMutations;
    final int maxMutations;

    final int grassSpawn;
    final int energyToReproduce;
    final int startingEnergy;
    final int energyReproductionCost;

    // Creates initial population and sets parameters
    public Simulation(int animalCount,GrassField worldMap,int genSize,int minMutations,int maxMutations,int energyToReproduce,int energyReproductionCost,int startingEnergy,int grassSpawn) {
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

                while (worldMap.isOccupied(pos) ) {
                    pos = Vector2d.random(worldMap.getCurrentBounds());
                }
                Animal animal = new Animal(pos,genSize,minMutations,maxMutations,worldMap.getGeneSwap(),startingEnergy, nameGenerator.generateName());

                worldMap.place(animal);
                animals.add(animal);
                System.out.println("Animal placed: " + animal);
            }
            catch (IncorrectPositionException | PlacementThresholdExceededException e)
            {
                //This Exception should not do any further actions
                System.out.println(e.getMessage());
            }

        }

    }

    // Executes a single simulation turn (death, move, eat, reproduce, spawn grass)
    public void executeTurn()
    {
        // Remove dead animals
        for (int i = 0; i < animals.size(); i++) {

            if(animals.get(i).getEnergy() <= 0)
            {
                kill(animals.get(i));
            }
        }

        // Move, use gene, and age each animal
        for (Animal animal : animals) {

            worldMap.move(animal,MoveDirection.FORWARD);
            animal.useGen();
            animal.ageAnimal();

        }

        // Eating phase (strongest animal on a tile eats grass)
        for (int y = 0; y < worldMap.getHeight();y++)
        {
            for (int x = 0; x < worldMap.getWidth(); x++) {

               Vector2d vector2d = new Vector2d(x,y);
                List<Animal> animalList = worldMap.getOrderedAnimals(vector2d);
                if(!animalList.isEmpty())
                {
                    Animal animal = animalList.get(0);
                    if(worldMap.isOccupiedByGrass(animal.getPosition())) {
                        worldMap.removeGrass(animal.getPosition());
                        animal.eat(worldMap.getGrassValue());
                    }
                }

            }
        }

        // Reproduction phase
        for (int y = 0; y < worldMap.getHeight();y++)
        {
            for (int x = 0; x < worldMap.getWidth(); x++) {

                reproduce(new Vector2d(x,y));

            }
        }

        // Spawn new grass and advance turn counter
        for (int i = 0; i < grassSpawn;i++)
            worldMap.addGrass();
        turn++;
    }

    // Tries to reproduce two strongest animals at position
    private void reproduce(Vector2d pos)
    {
        List<Animal> animalList = worldMap.getOrderedAnimals(pos);
        if(animalList.size()>=2)
        {

            if(animalList.get(1).getEnergy() > energyToReproduce)
            {System.out.println("Ready at " + pos.toString());
                Animal animal1 = animalList.get(0);
                Animal animal2 = animalList.get(1);
                int energy1 = animal1.getEnergy();
                int energy2 = animal2.getEnergy();

                int energy1Percentage = (genSize* energy1)/(energy1 + energy2);

                Animal newAnimal = new Animal(pos,genSize,minMutations,maxMutations,worldMap.getGeneSwap(),2*energyReproductionCost, nameGenerator.generateName(),animal1.getGens(),animal2.getGens(),energy1Percentage);
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
                }
                catch (IncorrectPositionException e)
                {
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    // Removes animal from simulation and records death
    private void kill(Animal animal)
    {
         animals.remove(animal);
         worldMap.removeAnimal(animal,animal.getPosition());
         worldMap.addDeadAnimal(animal.getAge());
         animal.setDiedAtTurn(turn);

    }

    private int turn = 0;
    // Returns current turn number
    public int getTurn() {return turn;}

    // Signals simulation loop to stop
    public void exit()
    {
        running = false;
    }

    // Pauses simulation loop
    public void pause() {
        paused = true;
        System.out.println("Simulation paused");
    }

    // Resumes simulation loop
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Notify the simulation thread to continue
        }
        System.out.println("Simulation resumed");
    }

    @Override
    // Main simulation loop (ticks with sleep; supports pause/resume/exit)
    public void run()
    {
        System.out.println("Simulation started");
        try{
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
