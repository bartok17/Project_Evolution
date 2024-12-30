package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.MoveValidator;
import agh.ics.oop.model.interfaces.WorldElement;

public class Animal implements WorldElement,Comparable<Animal> {
    private MapDirection direction = MapDirection.NORTH;
    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }
    public MapDirection getDirection() {
        return direction;
    }

    private int energy = 0;
    public void eat(int energyAddition) {energy += energyAddition; }
    public int getEnergy() {return energy;}
    private Vector2d position = null;
    public Vector2d getPosition() {
        return position;
    }
    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Animal()
    {
        this.position = new Vector2d(2,2);
    }
    public Animal(Vector2d startCoordinates){
        this.position = startCoordinates;
    }

    public String toString()
    {
        return direction.toString();
    }
    public boolean isAt(Vector2d coordinates)
    {
        return this.position == coordinates;
    }

    @Override
    public String getResourceName() {
       return switch (direction)
       {
           case NORTH -> "Animal_down.png";
           case SOUTH -> "Animal_up.png";
           case EAST -> "Animal_right.png";
           case WEST -> "Animal_left.png";
           case NORTH_EAST ->  "Animal_down_right.png";
           case SOUTH_EAST ->  "Animal_up_right.png";
           case SOUTH_WEST ->  "Animal_up_left.png";
           case NORTH_WEST ->  "Animal_down_left.png";

       };


    }

    @Override
    public String getInfo() {
        return position.toString();
    }

    public void move(MoveDirection direction, MoveValidator moveValidator)
    {
        switch (direction) {
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD -> {

                if(moveValidator.canMoveTo(this.position.add(this.direction.toUnitVector())))
                    this.position =this.position.add(this.direction.toUnitVector());
            }
            case BACKWARD ->
            {

                if(moveValidator.canMoveTo(this.position.subtract(this.direction.toUnitVector())))
                    this.position =this.position.subtract(this.direction.toUnitVector());
            }
        }
        return;
    }
    @Override
    public int compareTo(Animal other) {
        return this.position.compareTo(other.position);
    }

}
