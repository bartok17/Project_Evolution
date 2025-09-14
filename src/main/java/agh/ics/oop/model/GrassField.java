package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.util.MapVisualizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class GrassField extends AbstractWorldMap {

    protected final int width;
    protected final int height;
    protected final Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected final Boundary boundary;
    protected final boolean geneSwap;
    protected final int grassValue;

    protected AbstractMapMovementLogistic mapLogic;
    protected Random rand = new Random();

    final Map<Vector2d, Grass> grasses = new HashMap<>();
    final Set<Vector2d> availablePriorityPositions = new HashSet<>();
    final Set<Vector2d> availableNonPriorityPositions = new HashSet<>();

    public GrassField(int width, int height, boolean geneSwap, int mapId, int grassValue) {
        super(mapId);
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.boundary = new Boundary(lowerLeft, upperRight);
        this.mapVisualizer = new MapVisualizer(this);
        this.geneSwap = geneSwap;
        this.grassValue = grassValue;
    }

    public int getGrassValue() {
        return grassValue;
    }

    public boolean getGeneSwap() {
        return geneSwap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    protected void setMapLogic(AbstractMapMovementLogistic mapLogic) {
        this.mapLogic = mapLogic;
        this.mapLogic.grassField = this;
    }

    AbstractMapMovementLogistic getMapLogic() {
        return mapLogic;
    }

    public int getGrassCount() {
        return grasses.size();
    }

    public int getFreeSpaces() {
        return availableNonPriorityPositions.size() + availablePriorityPositions.size();
    }

    protected void initializeGrassPlaces() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d position = new Vector2d(i, j);
                if (isPriorityPosition(position)) {
                    availablePriorityPositions.add(position);
                } else {
                    availableNonPriorityPositions.add(position);
                }
            }
        }
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        super.move(animal, direction);
        animal.useEnergy(mapLogic.getConsumedEnergy(animal.getPosition()));
    }

    public boolean isOccupiedByGrass(Vector2d position) {
        return grasses.containsKey(position);
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        Optional<WorldElement> optionalAnimal = super.objectAt(position);
        if (optionalAnimal.isPresent()) {
            return optionalAnimal;
        }
        if (isOccupiedByGrass(position)) {
            return Optional.of(grasses.get(position));
        }
        return Optional.empty();
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        List<WorldElement> objects = super.objectsAt(position);
        if (isOccupiedByGrass(position)) {
            objects.add(grasses.get(position));
        }
        return objects;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return mapLogic.canMoveTo(position);
    }

    @Override
    public Vector2d convertMove(Vector2d position) {
        return mapLogic.convertMove(position);
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
        return boundary;
    }

    protected boolean getTargetField() {
        int result = rand.nextInt(5);
        return result != 0;
    }

    public abstract boolean isPriorityPosition(Vector2d position);

    public abstract void addGrass();

    public void removeGrass(Vector2d position) {
        grasses.remove(position);
        if (isPriorityPosition(position)) {
            availablePriorityPositions.add(position);
        } else {
            availableNonPriorityPositions.add(position);
        }
    }

    public void mapEvent() {
        mapLogic.turnEvent();
    }
}
