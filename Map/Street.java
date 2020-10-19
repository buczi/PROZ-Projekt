package Map;

public class Street extends Coords{
    private TrafficLights lights;
    private float distanceToCrossing;
    private GridNode destination;

    public Street(Coords coords, float distanceToCrossing, GridNode destination, float greenLightTime)
    {
        super(coords);
        this.distanceToCrossing = distanceToCrossing;
        this.destination = destination;

        if(greenLightTime == 0)
            this.lights = null;
        else
            this.lights = new TrafficLights(greenLightTime);
    }

}
