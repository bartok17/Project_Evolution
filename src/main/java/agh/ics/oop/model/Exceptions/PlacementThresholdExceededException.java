package agh.ics.oop.model.Exceptions;

public class PlacementThresholdExceededException extends RuntimeException {
    public PlacementThresholdExceededException(int threshold) {
        super("Couldn't find position to place object after " + threshold + " tries");
    }
}
