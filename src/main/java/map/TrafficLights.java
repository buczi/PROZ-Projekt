package map;

import vehicle.Car;

import java.util.*;

import util.*;

public class TrafficLights implements Time {
    private final float greenLightTime;
    private boolean isItGreenLight;
    private final LinkedList<Car> que;
    private float time;
    private float waitTime;

    private static final float carDelay = 0.5f;

    public TrafficLights(float greenLightTime) {
        this.greenLightTime = greenLightTime;
        this.isItGreenLight = false;
        this.time = 0f;
        this.waitTime = 0f;
        this.que = new LinkedList<>();
    }

    public void changeLights() {
        waitTime = 0;
        time = 0;
        isItGreenLight = !isItGreenLight;
    }

    public Car releaseCars() {
        return this.que.removeFirst();
    }

    public void addCarToQue(Car car) {
        if (!que.contains(car))
            que.add(car);
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
    public void update() {

        if (this.que.size() != 0 && this.time >= carDelay && this.isItGreenLight) {
            this.time = 0f;
            this.releaseCars().startJourney();
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
