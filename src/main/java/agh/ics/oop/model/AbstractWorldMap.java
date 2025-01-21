package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.IncorrectPositionException;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap implements WorldMap {
    private final int mapId;

    private int deadAnimals = 0;
    private int deadLifeSpan = 0;
    public void addDeadAnimal(int lifeSpan) {deadAnimals++;
    deadLifeSpan += lifeSpan;}
    public int getAvgDeadAnimals() {
        if(deadAnimals == 0) return 0;
        return deadLifeSpan /deadAnimals;}


    private final List<MapChangeListener> listeners = new ArrayList<>();

    protected AbstractWorldMap(int mapId) {
        this.mapId = mapId;
    }

    public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {}

    protected final Map<Vector2d,List<Animal>> animals = new HashMap<>();

    protected MapVisualizer mapVisualizer;


    @Override
    public void place(Animal animal) throws IncorrectPositionException {

            animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
            notifyListeners("Zwierze postawione na " + animal.getPosition());


    }
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
        if (isOccupied(position)) {
            return Optional.of(animals.get(position).get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        List<WorldElement> objects = new ArrayList<>();
        if (isOccupied(position)) {objects.add(animals.get(position).get(0));
        }
        return objects;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public String toString()
    {
        return mapVisualizer.draw(getCurrentBounds());
    }

    public Collection<WorldElement> getElements() {
        return animals.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public abstract AbstractWorldMap.Boundary getCurrentBounds();


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
                .flatMap(List::stream)
                .sorted()
                .collect(Collectors.toList());

    }
    public List<Animal> getOrderedAnimals(Vector2d position) {

        if(!animals.containsKey(position)) return new ArrayList<>();
        if(animals.get(position).isEmpty()) return new ArrayList<>();
        return  animals.get(position).stream()
                .sorted()
                .collect(Collectors.toList());

    }
    public int getAvgEnergy()
    {
        List<Animal> animals = getOrderedAnimals();
        int totalEnergy = 0;
        for (Animal animal : animals) {
            totalEnergy += animal.getEnergy();
        }
        return totalEnergy/animals.size();
    }
    public int getAvgChildren()
    {
        List<Animal> animals = getOrderedAnimals();
        int totalChildren = 0;
        for (Animal animal : animals) {
            totalChildren += animal.getChildren();
        }
        return totalChildren/animals.size();
    }
}
