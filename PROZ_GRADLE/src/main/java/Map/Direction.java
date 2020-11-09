package Map;

import javax.swing.*;

public enum Direction {
    North,
    East,
    South,
    West,
    Default;

    public static Direction Oposite(Direction direction) {
        switch (direction) {
            case North:
                return South;

            case South:
                return North;

            case East:
                return West;

            case West:
                return East;
        }
        return Default;
    }

    public static boolean CheckIfRightTurn(Direction main, Direction target)
    {
        if(getRight(main) == target)
            return true;
        return false;
    }

    public static Direction getRight(Direction direction)
    {
        switch (direction)
        {
            case North: return East;

            case East: return South;

            case South: return West;

            case West: return North;
        }
        return Default;
    }
    public static Direction getLeft(Direction direction)
    {
        switch (direction)
        {
            case North: return West;

            case East: return North;

            case South: return East;

            case West: return South;
        }
        return Default;
    }
}
