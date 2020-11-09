package Utils;

import java.util.*;

public class TimeController {
    private static TimeController instance = null;

    private float _time;
    private float deltaTime;
    private List<Time> timeObjects;
    private boolean isClockRunning;

    private TimeController() {
        this._time = 0f;
        this.deltaTime = 0.1f;
        this.isClockRunning = false;
        timeObjects = new LinkedList<Time>();
    }

    public static TimeController getInstance() {
        if (instance == null)
            instance = new TimeController();
        return instance;
    }

    public boolean StartClock() {
        if (isClockRunning)
            return false;

        isClockRunning = true;
        return true;
    }

    public boolean StopClock() {
        if (!isClockRunning)
            return false;

        isClockRunning = false;
        return true;
    }

    public void Run() {
        if (isClockRunning) {
            try {//TODO
                Thread.sleep((long) (500 * deltaTime));
                _time += deltaTime;
                for (Time item : timeObjects)
                    item.Update();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean Change_DeltaTime(float deltaTime) {
        if (deltaTime <= 0)
            return false;

        this.deltaTime = deltaTime;
        return true;
    }

    public void AddToTimeObjects(Time timeObject) {
        this.timeObjects.add(timeObject);
    }

    public void AddToTimeObjects(List<Time> timeObjects) {
        this.timeObjects.addAll(timeObjects);
    }

    public void RemoveFromTimeObjects(Time timeObject) {
        this.timeObjects.remove(timeObject);
    }

    public void RemoveFromTimeObjects(List<Time> timeObjects) {
        this.timeObjects.removeAll(timeObjects);
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public float get_time() {
        return _time;
    }
}
