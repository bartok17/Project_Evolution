package agh.ics.oop.util;

import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileMapDisplay implements MapChangeListener {
    @Override
    public void onMapChanged(WorldMap worldMap, String message) {
        String fileName = "map_" + worldMap.getId()+".log";
        String toAppend ="Mapa o Id: " + worldMap.getId() + "\n"
                + message + "\n"
                + worldMap + "\n"
                +"\n";



        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(toAppend);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

    }
}
