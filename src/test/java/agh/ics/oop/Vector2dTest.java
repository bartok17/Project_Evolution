package agh.ics.oop;

import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void getX() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);

        //then
        assertEquals(1, vector2d.x());

    }

    @Test
    void getY() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);

        //then

        assertEquals(2, vector2d.y());
    }

    @Test
    void testToString() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);


        //then
        assertEquals("1,2", vector2d.toString());
    }

    @Test
    void precedes() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d vector2d2 = new Vector2d(2, 2);
        Vector2d vector2d3 = new Vector2d(1, 4);
        Vector2d vector2d4 = new Vector2d(2, 4);
        Vector2d vector2d5 = new Vector2d(0 ,0);

        //then
        assertTrue(vector2d.precedes(vector2d));
        assertTrue(vector2d.precedes(vector2d2));
        assertTrue(vector2d.precedes(vector2d3));
        assertTrue(vector2d.precedes(vector2d4));
        assertFalse(vector2d.precedes(vector2d5));

    }

    @Test
    void follows() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d vector2d2 = new Vector2d(0, 2);
        Vector2d vector2d3 = new Vector2d(1, 0);
        Vector2d vector2d4 = new Vector2d(0, 0);
        Vector2d vector2d5 = new Vector2d(2, 4);

        //then
        assertTrue(vector2d.follows(vector2d));
        assertTrue(vector2d.follows(vector2d2));
        assertTrue(vector2d.follows(vector2d3));
        assertTrue(vector2d.follows(vector2d4));
        assertFalse(vector2d.follows(vector2d5));

    }

    @Test
    void add() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d vector2d2 = new Vector2d(2, -2);
        Vector2d vector2dResult = new Vector2d(3,0 );

        //then

        assertEquals(vector2dResult.x(), vector2d.add(vector2d2).x());
        assertEquals(vector2dResult.y(), vector2d.add(vector2d2).y());

    }

    @Test
    void subtract() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d vector2d2 = new Vector2d(2, -2);
        Vector2d vector2dResult = new Vector2d(-1,4 );

        //then
        assertEquals(vector2dResult.x(), vector2d.subtract(vector2d2).x());
        assertEquals(vector2dResult.y(), vector2d.subtract(vector2d2).y());
    }

    @Test
    void upperRight() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d vector2d2 = new Vector2d(2, -2);
        Vector2d vector2dResult = new Vector2d(2,2);
        //then
        assertEquals(vector2dResult.x(), vector2d.upperRight(vector2d2).x());
        assertEquals(vector2dResult.y(), vector2d.upperRight(vector2d2).y());
    }

    @Test
    void lowerLeft() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d vector2d2 = new Vector2d(2, -2);
        Vector2d vector2dResult = new Vector2d(1,-2);
        //then
        assertEquals(vector2dResult.x(), vector2d.lowerLeft(vector2d2).x());
        assertEquals(vector2dResult.y(), vector2d.lowerLeft(vector2d2).y());
    }

    @Test
    void opposite() {
        //Given
        Vector2d vector2d = new Vector2d(1, 2);
        Vector2d vector2d2 = new Vector2d(-1, -2);

        //then
        assertEquals(vector2d.x(), vector2d2.opposite().x());
        assertEquals(vector2d.y(), vector2d2.opposite().y());

    }

    @Test
    void testEquals() {
        //Given
Vector2d vector2d = new Vector2d(1, 2);
Vector2d vector2d2 = new Vector2d(2, -2);
Vector2d vector2d3 = new Vector2d(1, 2);

        //then
       
        assertFalse(vector2d.equals(vector2d2));
        assertTrue(vector2d.equals(vector2d3));
    }
}