package Map;

import Utils.Pair;

public class Coords {
    protected Pair<Integer, Integer> position;
    protected Direction direction;
    protected String name;
    private static int objId = 1;
    private static String objName = "Object";

    public Coords(Pair<Integer, Integer> position, Direction direction, String name) {
        this.position = position;
        this.direction = direction;
        this.name = name;
    }

    public Coords(Pair<Integer, Integer> position) {
        this();
        this.position = position;
    }

    public Coords(Pair<Integer, Integer> position, Direction direction) {
        this.position = position;
        this.name = this.objName + "_" + this.objId;
        objId++;
        this.direction = direction;
    }

    public Coords(Coords coords) {
        this(coords.position, coords.direction);
    }

    public Coords() {
        this.direction = Direction.Default;
        this.position = new Pair<Integer, Integer>(0, 0);
        this.name = this.objName + "_" + this.objId;
        objId++;

    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
