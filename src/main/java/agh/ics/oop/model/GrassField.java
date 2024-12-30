package agh.ics.oop.model;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrassField extends AbstractWorldMap {

     final Map<Vector2d, Grass> grasses = new HashMap<>();

    private final Vector2d lowerLeft;


    public GrassField(int grassCount,int mapId) {

        super(mapId);
        Random rand = new Random();
        int flooredGrass = (int)Math.floor(Math.sqrt(grassCount * 10));
        for (int i = 0; i < grassCount; i++) {

            Vector2d newPosition = new Vector2d(rand.nextInt(flooredGrass), rand.nextInt(flooredGrass));
            while (isOccupiedByGrass(newPosition)) {
                newPosition = new Vector2d(rand.nextInt(flooredGrass), rand.nextInt(flooredGrass));
            }
            grasses.put(newPosition, new Grass(newPosition));

        }
        mapVisualizer = new MapVisualizer(this);
        lowerLeft = new Vector2d(0, 0);

    }






    private boolean isOccupiedByGrass(Vector2d position) {
        return grasses.containsKey(position);
    }
    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {

        Optional<WorldElement> optionalAnimal = super.objectAt(position);
        if (optionalAnimal.isPresent()) { return optionalAnimal; }
        else if (isOccupiedByGrass(position)) { return Optional.of(grasses.get(position)); }
        else return Optional.empty();

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
         if(isOccupied(position)) return false;
         else return !position.precedes(lowerLeft);
    }
    @Override
    public Collection<WorldElement> getElements() {
        return Stream.concat(
                grasses.values().stream(),
                super.getElements().stream()
        ).collect(Collectors.toList());
    }
    @Override

    public Boundary getCurrentBounds() {
        Vector2d upperRight = lowerLeft;
        for (WorldElement worldElement : getElements()) {
            if (worldElement.getPosition().follows(upperRight)) {
                upperRight = upperRight.upperRight(worldElement.getPosition());
            }
        }


        return new Boundary(lowerLeft, upperRight);
    }
}
