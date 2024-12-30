package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.IncorrectPositionException;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    private final int mapId;

    private final List<MapChangeListener> listeners = new ArrayList<>();

    protected AbstractWorldMap(int mapId) {
        this.mapId = mapId;
    }

    public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {}

    protected final Map<Vector2d,Animal> animals = new HashMap<>();

    protected MapVisualizer mapVisualizer;


    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (!isOccupied(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            notifyListeners("Zwierze postawione na " + animal.getPosition());
        }
        else
            throw new IncorrectPositionException(animal.getPosition());

    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animals.remove(animal.getPosition());
        animal.move(direction,this);
        animals.put(animal.getPosition(), animal);
        notifyListeners("Zwierze porusza się z position " + oldPosition + " do " + animal.getPosition());

    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        if (isOccupied(position)) {
            return Optional.of(animals.get(position));
        }
        return Optional.empty();
    }
    @Override
    public boolean canMoveTo(Vector2d position) {
        if (isOccupied(position)) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return mapVisualizer.draw(getCurrentBounds());
    }

    public Collection<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    public abstract Boundary getCurrentBounds();


    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }
    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }
    void notifyListeners(String message) {

        for (MapChangeListener listener : listeners) {
            listener.onMapChanged(this,message);
        }
    }

    @Override
    public int getId() {
        return mapId;
    }

    @Override
    public List<Animal> getOrderedAnimals() {
        return  animals.values().stream()
                .sorted()
                .collect(Collectors.toList());

    }
}
