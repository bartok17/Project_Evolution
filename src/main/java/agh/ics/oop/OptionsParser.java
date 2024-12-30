package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OptionsParser {
    public static List<MoveDirection> ParseMoveDirection(String[] Directions) {
        return Arrays.stream(Directions)
                .map(direction -> switch (direction.toLowerCase()) {
                    case "f", "forward" -> MoveDirection.FORWARD;
                    case "b", "backward" -> MoveDirection.BACKWARD;
                    case "l", "left" -> MoveDirection.LEFT;
                    case "r", "right" -> MoveDirection.RIGHT;
                    default -> throw new IllegalArgumentException("Invalid move direction: " + direction);
                })
                .collect(Collectors.toList());
    }
}