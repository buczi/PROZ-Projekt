package Map;

public class Street extends Coords {
    private TrafficLights lights;
    private float distanceToCrossing;
    private GridNode beginning;
    private GridNode destination;

    public final static float distanceBetween = 50f;

    public Street(Coords coords, float distanceToCrossing, GridNode destination, GridNode beginning, float greenLightTime) {
        super(coords.position, beginning.direction, "Street" + destination.direction + beginning.direction + ":" + beginning.position);
        this.distanceToCrossing = distanceToCrossing;
        this.destination = destination;
        this.beginning = beginning;

        if (greenLightTime == 0)
            this.lights = null;
        else
            this.lights = new TrafficLights(greenLightTime);
    }


    public TrafficLights getLights() {
        return lights;
    }

    public GridNode getDestination() {
        return destination;
    }

    public GridNode getBeginning() {
        return beginning;
    }

    public float getDistanceToCrossing() {
        return distanceToCrossing;
    }
}
