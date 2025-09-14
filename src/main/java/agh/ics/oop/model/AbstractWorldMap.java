package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.IncorrectPositionException;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Base implementation of WorldMap with common animal storage and listener logic.
 */
public abstract class AbstractWorldMap implements WorldMap {
    // Unique identifier of the map
    private final int mapId;

    // Stats: number of dead animals and cumulative lifespan
    private int deadAnimals = 0;
    private int deadLifeSpan = 0;

    // Subscribers for map change events
    private final List<MapChangeListener> listeners = new ArrayList<>();

    // Animals grouped by position
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();

    // Helper to visualize the map
    protected MapVisualizer mapVisualizer;

    protected AbstractWorldMap(int mapId) {
        this.mapId = mapId;
    }

    // Rectangular boundary of the map
    public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {}

    // Update dead animal stats
    public void addDeadAnimal(int lifeSpan) {
        deadAnimals++;
        deadLifeSpan += lifeSpan;
    }

    // Average lifespan of dead animals
    public int getAvgDeadAnimals() {
        if (deadAnimals == 0) return 0;
        return deadLifeSpan / deadAnimals;
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        // Insert animal at its position
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        notifyListeners("Zwierze postawione na " + animal.getPosition());
    }

    // Remove animal from a given position
    public void removeAnimal(Animal animal, Vector2d position) {
        List<Animal> oldList = animals.get(position);
        if (oldList != null) {
            oldList.remove(animal);
            if (oldList.isEmpty()) {
                animals.remove(position);
            }
        }
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        // Move animal between position buckets
        Vector2d oldPosition = animal.getPosition();
        removeAnimal(animal, oldPosition);

        animal.move(direction, this);
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        notifyListeners("Zwierze porusza się z pozycji " + oldPosition + " do " + animal.getPosition());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        // Return first animal at position (if any)
        if (isOccupied(position)) {
            return Optional.of(animals.get(position).get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        // List view of elements at position (currently first animal only)
        List<WorldElement> objects = new ArrayList<>();
        if (isOccupied(position)) {
            objects.add(animals.get(position).get(0));
        }
        return objects;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        // Default: no collision constraints
        return true;
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(getCurrentBounds());
    }

    // Flat collection of all elements on the map
    public Collection<WorldElement> getElements() {
        return animals.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    // Current visible bounds (implemented by concrete maps)
    public abstract AbstractWorldMap.Boundary getCurrentBounds();

    // Subscribe/unsubscribe listeners
    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    // Notify listeners about a change
    void notifyListeners(String message) {
        for (MapChangeListener listener : listeners) {
            listener.onMapChanged(this, message);
        }
    }

    @Override
    public int getId() {
        return mapId;
    }

    @Override
    public List<Animal> getOrderedAnimals() {
        // All animals sorted by their natural order
        return animals.values().stream()
                .flatMap(List::stream)
                .sorted()
                .collect(Collectors.toList());
    }

    // Animals at a position, sorted
    public List<Animal> getOrderedAnimals(Vector2d position) {
        if (!animals.containsKey(position)) return new ArrayList<>();
        if (animals.get(position).isEmpty()) return new ArrayList<>();
        return animals.get(position).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    // Average energy of all animals (assumes non-empty)
    public int getAvgEnergy() {
        List<Animal> animals = getOrderedAnimals();
        int totalEnergy = 0;
        for (Animal animal : animals) {
            totalEnergy += animal.getEnergy();
        }
        return totalEnergy / animals.size();
    }

    // Average number of children (assumes non-empty)
    public int getAvgChildren() {
        List<Animal> animals = getOrderedAnimals();
        int totalChildren = 0;
        for (Animal animal : animals) {
            totalChildren += animal.getChildren();
        }
        return totalChildren / animals.size();
    }
}
