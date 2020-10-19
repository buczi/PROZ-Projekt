package Map;

import Vehicle.Car;
import java.util.*;

public class TrafficLights {
    private float greenLightTime;
    private boolean isItGreenLight;
    private LinkedList<Car> que;

    public TrafficLights(float greenLightTime)
    {
        this.greenLightTime = greenLightTime;
        this.isItGreenLight = false;
        this.que = new LinkedList<Car>();
    }

    public void ChangeLights()
    {
        isItGreenLight = !isItGreenLight;
    }

    public Car RealeseCars()
    {
        return this.que.removeLast();
    }
}
