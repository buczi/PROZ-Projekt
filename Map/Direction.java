package Map;

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
}
