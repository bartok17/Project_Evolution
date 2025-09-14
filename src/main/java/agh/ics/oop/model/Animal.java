package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.MoveValidator;
import agh.ics.oop.model.interfaces.WorldElement;
import javafx.scene.image.Image;

import java.util.*;

public class Animal implements WorldElement, Comparable<Animal> {
    // Cache of images per direction
    private static final HashMap<String, Image> images = new HashMap<String, Image>();

    // Set of related animals (ancestors/descendants)
    final HashSet<Animal> relatives = new HashSet<Animal>();

    // Add many relatives
    public void addRelatives(HashSet<Animal> animals) {
        relatives.addAll(animals);
    }

    // Add a single relative
    public void addRelatives(Animal animal) {
        relatives.add(animal);
    }

    // Get all relatives
    public HashSet<Animal> getRelatives() {
        return relatives;
    }

    private final String name;
    private int age = 0;

    // Increase age by one turn
    public void ageAnimal() {
        age++;
    }

    // Current age
    public int getAge() {
        return age;
    }

    private int children;
    private int TotalChildren;

    // Children count (direct)
    public int getChildren() {
        return children;
    }

    // Increment direct children count
    public void addChildren() {
        this.children++;
    }

    // Total descendants count (broader)
    public int getTotalChildren() {
        return TotalChildren;
    }

    // Increment total descendants count
    public void addTotalChildren() {
        TotalChildren++;
    }

    // Animal identifier
    public String getName() {
        return name;
    }

    private MapDirection direction = MapDirection.NORTH;

    // Set facing direction
    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    // Get facing direction
    public MapDirection getDirection() {
        return direction;
    }

    private int energy = 0;

    // Eat and gain energy (also counts food eaten)
    public void eat(int energyAddition) {
        energy += energyAddition;
        foodEaten++;
    }

    // Spend energy
    public void useEnergy(int energyUsage) {
        energy -= energyUsage;
    }

    // Current energy
    public int getEnergy() {
        return energy;
    }

    // Gene configuration
    private final boolean geneSwap;   // mutation type toggle
    private final int gensSize;       // number of genes
    private int[] gens;               // genes array

    // Access genes
    public int[] getGens() {
        return gens;
    }

    private int currentGen = 0;

    // Index of current gene used for movement/turning
    public int getCurrentGen() {
        return currentGen;
    }

    private final int minMutations;
    private final int maxMutations;

    private int diedAtTurn = 0;

    // Set the turn when the animal died
    public void setDiedAtTurn(int turn) {
        diedAtTurn = turn;
    }

    // Get the turn when the animal died
    public int getDiedAtTurn() {
        return diedAtTurn;
    }

    private int foodEaten = 0;

    // Number of times the animal has eaten
    public int getFoodEaten() {
        return foodEaten;
    }

    private Vector2d position = null;

    // Current position
    public Vector2d getPosition() {
        return position;
    }

    // Set current position
    public void setPosition(Vector2d position) {
        this.position = position;
    }

    // Create an animal with random genes
    public Animal(
            Vector2d startCoordinates,
            int gensSize,
            int minMutations,
            int maxMutations,
            boolean geneSwap,
            int startingEnergy,
            String name
    ) {
        this.position = startCoordinates;
        this.geneSwap = geneSwap;
        this.gensSize = gensSize;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.energy = startingEnergy;
        this.name = name;

        InitializeGenes();
    }

    // Create an animal from two parents' genes
    public Animal(
            Vector2d startCoordinates,
            int gensSize,
            int minMutations,
            int maxMutations,
            boolean geneSwap,
            int startingEnergy,
            String name,
            int[] gens1,
            int[] gens2,
            int gen1Amount
    ) {
        this.position = startCoordinates;
        this.geneSwap = geneSwap;
        this.gensSize = gensSize;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.energy = startingEnergy;
        this.name = name;

        InitializeGenes(gens1, gens2, gen1Amount);
    }

    // Initialize random genes
    private void InitializeGenes() {
        int[] newGens = new int[gensSize];
        Random rand = new Random();
        for (int i = 0; i < gensSize; i++) {
            newGens[i] = rand.nextInt(8);
        }
        InitializeGenes(newGens, newGens, gensSize);
    }

    // Combine and mutate genes from two arrays
    private void InitializeGenes(int[] gens1, int[] gens2, int gen1Amount) {
        gens = new int[gensSize];
        Random rand = new Random();
        direction = MapDirection.values()[rand.nextInt(MapDirection.values().length)];
        currentGen = rand.nextInt(gensSize);
        int side = rand.nextInt(0, 2);
        int[] right;
        int[] left;
        int rightCount = 0;
        int leftCount = 0;
        if (side == 1) {
            right = gens1;
            left = gens2;
            rightCount = gen1Amount;
            leftCount = gensSize - gen1Amount;
        } else {
            right = gens2;
            left = gens1;
            rightCount = gensSize - gen1Amount;
            leftCount = gen1Amount;
        }
        if (leftCount >= 0) {
            System.arraycopy(left, 0, gens, 0, leftCount);
        }
        if (rightCount >= 0) {
            System.arraycopy(right, leftCount, gens, 0, rightCount);
        }
        int mutations = rand.nextInt(minMutations, maxMutations);
        for (int i = 0; i < mutations; i++) {
            int type = 0;
            if (geneSwap) {
                type = rand.nextInt(0, 2);
            }
            if (type == 0) {
                this.gens[rand.nextInt(0, gensSize)] = rand.nextInt(0, 8);
            } else {
                int gen1 = rand.nextInt(0, gensSize);
                int gen2 = rand.nextInt(0, gensSize);
                int temp = gens[gen1];
                gens[gen1] = gens[gen2];
                gens[gen2] = temp;
            }
        }
        System.out.println("Create genes:" + Arrays.toString(gens));
    }

    // For UI/logging: show position
    public String toString() {
        return position.toString();
    }

    // Check if at given coordinates
    public boolean isAt(Vector2d coordinates) {
        return this.position == coordinates;
    }

    @Override
    // Get cached image for the current direction
    public Image getImage() {
        if (!images.containsKey(direction.toString())) {
            images.put(direction.toString(), new Image(getResourceName()));
        }
        return images.get(direction.toString());
    }

    @Override
    // Map direction to image resource name
    public String getResourceName() {
        return switch (direction) {
            case NORTH -> "Animal_down.png";
            case SOUTH -> "Animal_up.png";
            case EAST -> "Animal_right.png";
            case WEST -> "Animal_left.png";
            case NORTH_EAST -> "Animal_down_right.png";
            case SOUTH_EAST -> "Animal_up_right.png";
            case SOUTH_WEST -> "Animal_up_left.png";
            case NORTH_WEST -> "Animal_down_left.png";
        };
    }

    // Update relatives' total descendants
    public void updateGenealogy() {
        for (Animal animal : relatives) {
            animal.addTotalChildren();
        }
    }

    @Override
    // Short info string (energy)
    public String getInfo() {
        return Integer.toString(energy);
    }

    // Use current gene to turn and advance pointer
    public void useGen() {
        if (currentGen >= gensSize) {
            currentGen = 0;
        }
        turn(gens[currentGen]);
        currentGen++;
    }

    // Turn to a direction based on gene value
    private void turn(int direction) {
        this.direction = MapDirection.values()[direction];
    }

    // Perform a move using a validator (handles wrapping/collisions)
    public void move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD -> {
                Vector2d movePosition = moveValidator.convertMove(this.position.add(this.direction.toUnitVector()));
                if (moveValidator.canMoveTo(movePosition)) {
                    this.position = movePosition;
                } else {
                    move(MoveDirection.BACKWARD, moveValidator);
                }
            }
            case BACKWARD -> {
                Vector2d movePosition = moveValidator.convertMove(this.position.subtract(this.direction.toUnitVector()));
                if (moveValidator.canMoveTo(movePosition)) {
                    this.position = movePosition;
                }
            }
        }
    }

    @Override
    // Ordering: position, then energy, age, children (desc for stats)
    public int compareTo(Animal other) {
        if (!this.position.equals(other.position)) {
            return this.position.compareTo(other.position);
        } else if (this.energy != other.energy) {
            return -Integer.compare(this.energy, other.energy);
        } else if (this.age != other.age) {
            return -Integer.compare(this.age, other.age);
        } else if (this.children != other.children) {
            return -Integer.compare(this.children, other.children);
        } else {
            return 0;
        }
    }
}
