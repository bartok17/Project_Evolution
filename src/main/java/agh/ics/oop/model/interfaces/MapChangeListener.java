package agh.ics.oop.model.interfaces;

@FunctionalInterface
public interface MapChangeListener {
    public void onMapChanged(WorldMap worldMap, String message);
}
