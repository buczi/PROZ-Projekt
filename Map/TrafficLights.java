package Map;

import Vehicle.Car;

import java.util.*;

import Utils.*;

public class TrafficLights implements Time {
    private float greenLightTime;
    private boolean isItGreenLight;
    private LinkedList<Car> que;
    private float time;
    private float waitTime;

    private static float carDelay = 0.5f;

    public TrafficLights(float greenLightTime) {
        this.greenLightTime = greenLightTime;
        this.isItGreenLight = false;
        this.time = 0f;
        this.waitTime = 0f;
        this.que = new LinkedList<Car>();
    }

    public void ChangeLights() {
        isItGreenLight = !isItGreenLight;
    }

    public Car ReleaseCars() {
        //return this.que.remove(this.que.size() - 1);
        return this.que.removeLast();
    }

    public boolean AddCarToQue(Car car) {
        if (!que.contains(car))
            que.add(car);
        else
            return false;
        return true;
    }

    public float getGreenLightTime() {
        return greenLightTime;
    }

    public boolean getIsItGreenLight() {
        return isItGreenLight;
    }

    public LinkedList<Car> getQue() {
        return que;
    }

    @Override
    public void Update() {

        if (this.que.size() != 0 && this.time >= carDelay && this.isItGreenLight) {
            this.time = 0f;
            this.ReleaseCars().StartJourney();
        }

        if (this.isItGreenLight) {
            time += TimeController.getInstance().getDeltaTime();
            waitTime = 0f;
        } else {
            waitTime += TimeController.getInstance().getDeltaTime();
            time = 0;
        }

    }
}
