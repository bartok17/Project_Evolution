package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.GrassSetFullException;

public class RequiredGrassField extends GrassField {

    private final int equatorSize;
    private final int equatorStart;

    public RequiredGrassField(int width, int height, int grassCount, boolean geneSwap, int mapId, int grassValue, AbstractMapMovementLogistic mapMovementLogistic) {
        super(width, height, geneSwap, mapId, grassValue);
        setMapLogic(mapMovementLogistic);
        this.equatorSize = height / 5;
        equatorStart = (height - equatorSize) / 2;

        initializeGrassPlaces();

        for (int i = 0; i < grassCount; i++) {
            addGrass();
        }


    }

    @Override
    public boolean isPriorityPosition(Vector2d position) {
        return (position.y() >= equatorStart && position.y() < equatorStart + equatorSize);
    }

    @Override
    public void addGrass() {
        boolean isOnEquator = getTargetField();
        Vector2d newPosition;
        try {
            if (isOnEquator) {
                if (availablePriorityPositions.isEmpty()) {
                    addGrass();
                    return;
                    //throw new GrassSetFullException("No more positions in the priority available to place grass.");
                }
                int randIndex = rand.nextInt(availablePriorityPositions.size());
                newPosition = availablePriorityPositions.stream().skip(randIndex).findFirst().orElseThrow();
                availablePriorityPositions.remove(newPosition);
            } else {
                if (availableNonPriorityPositions.isEmpty()) {
                    throw new GrassSetFullException("No more positions in the non-priority available to place grass.");
                }
                int randIndex = rand.nextInt(availableNonPriorityPositions.size());
                newPosition = availableNonPriorityPositions.stream().skip(randIndex).findFirst().orElseThrow();
                availableNonPriorityPositions.remove(newPosition);
            }
            grasses.put(newPosition, new Grass(newPosition));
        } catch (GrassSetFullException e) { // wyjątek krótkodystansowy
            System.out.println(e.getMessage());
        }
    }


}
