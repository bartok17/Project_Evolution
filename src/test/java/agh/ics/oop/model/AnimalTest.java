package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.MoveValidator;
import agh.ics.oop.model.interfaces.WorldElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.*;


class AnimalTest {

    private Animal animal;
    private MoveValidator mockValidator;

    @BeforeEach
    void setUp() {
        animal = new Animal(new Vector2d(0, 0), 8, 1, 3, true, 100, "TestAnimal");
        mockValidator = new MoveValidator() {
            @Override
            public boolean canMoveTo(Vector2d position) {
                return true;
            }

            @Override
            public Vector2d convertMove(Vector2d position) {
                return position;
            }
        };
    }

    @Test
    void testMoveAndRotate() {
        animal.setDirection(MapDirection.EAST);

        animal.move(MoveDirection.FORWARD, mockValidator);

        assertEquals(new Vector2d(1, 0), animal.getPosition(), "Animal should move east correctly");
    }

    @Test
    void testInitializeGenes() {
        Animal animalWithGenes = new Animal(
                new Vector2d(0, 0), 8, 1, 3, true, 100, "TestAnimalWithGenes",
                new int[]{0, 1, 2, 3,4,5,6,7}, new int[]{0,1,2,3,4, 5, 6, 7}, 2
        );

        int[] genes = animalWithGenes.getGens();
        assertEquals(8, genes.length, "Genes should be correct size");
        assertNotNull(genes, "genes need to be inicialized");
    }

    @Test
    void testGeneMutations() {
        Animal animalWithMutation = new Animal(
                new Vector2d(0, 0), 8, 3, 6, true, 100, "Azbest"
        );
        Animal animalWithoutMutation = new Animal(
                new Vector2d(0, 0), 8, 3, 6, true, 100, "Azbest"
        );




        animalWithMutation.useGen();

        assertNotEquals(animalWithoutMutation, animalWithMutation.getGens(), "Genes shouldn't be the same after mutation, possible random failure caused by luck, multiple tries suggested :)");
    }


    @Test
    void testCompareAnimals() {
        Animal weakerAnimal = new Animal(new Vector2d(0, 0), 8, 1, 3, true, 50, "Mezuwiusz");
        Animal strongerAnimal = new Animal(new Vector2d(0, 0), 8, 1, 3, true, 200, "Nejman");


        assertTrue(strongerAnimal.compareTo(weakerAnimal) < 0, "Stronger animal should come before weaker animal");
        assertTrue(weakerAnimal.compareTo(strongerAnimal) > 0, "Weaker animal should come after stronger animal");
    }

    @Test
    void testGetResourceName() {
        animal.setDirection(MapDirection.NORTH);
        assertEquals("Animal_down.png", animal.getResourceName(), "Resource name should match the direction");

        animal.setDirection(MapDirection.SOUTH);
        assertEquals("Animal_up.png", animal.getResourceName(), "Resource name should match the direction");
    }


    @Test
    void testToString() {
        assertEquals("0,0", animal.toString(), "toString method should return the position");
    }
}
