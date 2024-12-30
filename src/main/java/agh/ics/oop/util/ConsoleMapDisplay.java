package agh.ics.oop.util;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {

    private  int moveCount = 0;
    public int getMoveCount(){
        return moveCount;
    }
    @Override
    synchronized public void onMapChanged(WorldMap worldMap, String message) {
        synchronized (Simulation.class) {
            moveCount++;
            System.out.println("Mapa o Id: " + worldMap.getId());
            System.out.println(message);
            System.out.println(worldMap);
            System.out.println("Aktualna ilość zmian: " + moveCount);
            System.out.println();
        }
    }
}
