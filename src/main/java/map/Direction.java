package map;

public enum Direction {
    North,
    East,
    South,
    West,
    Default;

    public static Direction opposite(Direction direction) {
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

    public static boolean checkIfRightTurn(Direction main, Direction target) {
        return getRight(main) == target;
    }

    public static Direction getRight(Direction direction) {
        switch (direction) {
            case North:
                return East;

            case East:
                return South;

            case South:
                return West;

            case West:
                return North;
        }
        return Default;
    }
    
}
