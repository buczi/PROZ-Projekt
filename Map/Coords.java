package Map;

import Utils.Pair;

public class Coords {
    protected Pair<Integer,Integer> position;
    protected Direction direction;

    public Coords(Pair<Integer,Integer> position)
    {
        this.position = position;
        this.direction = Direction.Default;
    }

    public Coords(Pair<Integer,Integer> position, Direction direction)
    {
        this(position);
        this.direction = direction;
    }

    public Coords(Coords coords)
    {
        this(coords.position, coords.direction);
    }
    
    public Coords()
    {
        this.direction = Direction.North;
        this.position = new Pair<Integer,Integer>(0,0);
    }
}
