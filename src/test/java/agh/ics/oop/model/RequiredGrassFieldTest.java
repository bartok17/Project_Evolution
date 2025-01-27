package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.GrassSetFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequiredGrassFieldTest {

    private RequiredGrassField grassField;
    private final int width = 10;
    private final int height = 10;
    private final int grassCount = 20;
    private final boolean geneSwap = false;
    private final int mapId = 1;
    private final int grassValue = 5;

    @BeforeEach
    void setUp() {
        AbstractMapMovementLogistic mockLogistic = new AbstractMapMovementLogistic() {
            @Override
            public boolean canMoveTo(Vector2d position) {
                return true;
            }

            @Override
            public Vector2d convertMove(Vector2d position) {
                return position;
            }

            @Override
            public void turnEvent() {
            }

            @Override
            public int getConsumedEnergy(Vector2d position) {
                return 1;
            }
        };
        grassField = new RequiredGrassField(width, height, grassCount, geneSwap, mapId, grassValue, mockLogistic);
    }

    @Test
    void testInitialization() {
        int totalFreeSpaces = grassField.getFreeSpaces();
        assertEquals((width * height) - grassCount, totalFreeSpaces, "FreeSpaces not equal");
        assertEquals(grassCount, grassField.getGrassCount(), "GrassCount not equal");
    }

    @Test
    void testIsPriorityPosition() {
        int equatorStart = (height - height / 5) / 2;
        int equatorEnd = equatorStart + height / 5;

        for (int y = 0; y < height; y++) {
            Vector2d position = new Vector2d(0, y);
            boolean expected = y >= equatorStart && y < equatorEnd;
            assertEquals(expected, grassField.isPriorityPosition(position),
                    "Incorrect position Priority: " + position);
        }
    }


    @Test
    void testAddGrassExceptionHandling() {

        while (grassField.getFreeSpaces() > 0) {
            grassField.addGrass();
        }

        assertDoesNotThrow(grassField::addGrass, "Check if error occurs");
    }
}
