package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.MoveValidator;
import agh.ics.oop.model.interfaces.WorldElement;
import javafx.scene.image.Image;

import javax.swing.*;
import java.util.*;

public class Animal implements WorldElement,Comparable<Animal> {
    final static private HashMap<String, Image> images = new  HashMap<String,Image>();

    final HashSet<Animal> relatives = new HashSet<Animal>();
    public void addRelatives(HashSet<Animal> animals) {
        relatives.addAll(animals);
    }
    public void addRelatives(Animal animal) {
        relatives.add(animal);
    }
    public HashSet<Animal> getRelatives() {return relatives;}

    private final String name;
    private int age = 0;
    public void ageAnimal(){age++;}
    public int getAge(){return age;}

    private int children;
    private int TotalChildren;
    public int getChildren(){return children;}
    public void addChildren(){this.children++;}
    public int getTotalChildren(){return TotalChildren;}
    public void addTotalChildren(){TotalChildren++;}


    public String getName(){return name;}

    private MapDirection direction = MapDirection.NORTH;
    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }
    public MapDirection getDirection() {
        return direction;
    }

    private int energy = 0;
    public void eat(int energyAddition) {
        energy += energyAddition;
        foodEaten++;
    }
    public void useEnergy(int energyUsage) {energy -= energyUsage; }
    public int getEnergy() {return energy;}

    private final boolean geneSwap;
    private final int gensSize;
    private int[] gens;
    public  int[] getGens() {return gens;}
    private  int currentGen = 0;
    public int getCurrentGen() {return currentGen;}
    private final int minMutations;
    private final int maxMutations;

    private int diedAtTurn = 0;
    public void setDiedAtTurn(int turn) {diedAtTurn = turn;}
    public int getDiedAtTurn() {return diedAtTurn;}

    private int foodEaten = 0;
    public int getFoodEaten() {return foodEaten;}

    private Vector2d position = null;
    public Vector2d getPosition() {
        return position;
    }
    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Animal(Vector2d startCoordinates,int gensSize,int minMutations, int maxMutations, boolean geneSwap,int startingEnergy,String name)
    {
        this.position = startCoordinates;
        this.geneSwap = geneSwap;
        this.gensSize = gensSize;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.energy = startingEnergy;
        this.name=name;

        InitializeGenes();
    }
    public Animal(Vector2d startCoordinates,int gensSize,int minMutations, int maxMutations, boolean geneSwap,int startingEnergy,String name, int[] gens1,  int[] gens2, int gen1Amount)
    {
        this.position = startCoordinates;
        this.geneSwap = geneSwap;
        this.gensSize = gensSize;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.energy = startingEnergy;
        this.name=name;

        InitializeGenes(gens1,gens2,gen1Amount);
    }
    private void InitializeGenes()
    {

        int[] newGens = new int[gensSize];
        Random rand = new Random();
        for (int i = 0; i < gensSize; i++)
        {
            newGens[i] = rand.nextInt(8);
        }
        InitializeGenes(newGens,newGens,gensSize);
    }
    private void InitializeGenes( int[] gens1,  int[] gens2, int gen1Amount)
    {

        gens = new int[gensSize];
        Random rand = new Random();
        direction = MapDirection.values()[rand.nextInt(MapDirection.values().length)];
        currentGen = rand.nextInt(gensSize);
        int side = rand.nextInt(0,2);
        int[] right;
        int[] left;
        int rightCount = 0;
        int leftCount = 0;
        if(side==1)
        {
            right = gens1;
            left = gens2;
            rightCount = gen1Amount;
            leftCount = gensSize - gen1Amount;
        }
        else
        {
            right = gens2;
            left = gens1;
            rightCount = gensSize - gen1Amount;
            leftCount = gen1Amount;
        }
        if (leftCount >= 0) System.arraycopy(left, 0, gens, 0, leftCount);
        if (rightCount >= 0) System.arraycopy(right, leftCount, gens, 0, rightCount);
        int mutations = rand.nextInt(minMutations,maxMutations);
        for (int i = 0; i < mutations; i++) {
            int type = 0;
            if (geneSwap)
                type = rand.nextInt(0,2);
            if(type == 0)
            {
                this.gens[rand.nextInt(0,gensSize)] = rand.nextInt(0,8);
            }
            else
            {
                int gen1 = rand.nextInt(0,gensSize);
                int gen2 = rand.nextInt(0,gensSize);
                int temp = gens[gen1];
                gens[gen1] = gens[gen2];
                gens[gen2] = temp;
            }
        }
        System.out.println("Create genes:"+ Arrays.toString(gens));
    }
    public String toString()
    {
        return position.toString();
    }
    public boolean isAt(Vector2d coordinates)
    {
        return this.position == coordinates;
    }

    @Override
    public Image getImage()
    {
        if(!images.containsKey(direction.toString()))
        {
            images.put(direction.toString(),new Image(getResourceName()));
        }
        return images.get(direction.toString());
    }
    @Override
    public String getResourceName() {
       return switch (direction)
       {
           case NORTH -> "Animal_down.png";
           case SOUTH -> "Animal_up.png";
           case EAST -> "Animal_right.png";
           case WEST -> "Animal_left.png";
           case NORTH_EAST ->  "Animal_down_right.png";
           case SOUTH_EAST ->  "Animal_up_right.png";
           case SOUTH_WEST ->  "Animal_up_left.png";
           case NORTH_WEST ->  "Animal_down_left.png";

       };


    }
    public void updateGenealogy()
    {
        for (Animal animal : relatives)
        {
            animal.addTotalChildren();
        }
    }

    @Override
    public String getInfo() {
        return Integer.toString(energy);
    }
    public void useGen()
    {
        if(currentGen >= gensSize)
        {
            currentGen = 0;
        }
        turn(gens[currentGen]);
        currentGen++;
    }
    private void turn(int direction)
    {
        this.direction = MapDirection.values()[direction];
    }
    public void move(MoveDirection direction, MoveValidator moveValidator)
    {

        switch (direction) {
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD -> {
                Vector2d movePosition = moveValidator.convertMove( this.position.add(this.direction.toUnitVector()));
                if(moveValidator.canMoveTo(movePosition))
                    this.position =movePosition;
                else move(MoveDirection.BACKWARD,moveValidator);
            }
            case BACKWARD ->
            {
                Vector2d movePosition = moveValidator.convertMove( this.position.subtract(this.direction.toUnitVector()));
                if(moveValidator.canMoveTo(movePosition))
                    this.position =movePosition;
            }
        }
        return;
    }

    @Override
    public int compareTo(Animal other) {
        if (!this.position.equals(other.position))
            return this.position.compareTo(other.position);
        else if(this.energy != other.energy)
            return -Integer.compare( this.energy , other.energy);
        else if(this.age != other.age)
            return -Integer.compare(this.age, other.age);
        else if(this.children != other.children)
            return -Integer.compare(this.children, other.children);
        else return 0;

    }


}
