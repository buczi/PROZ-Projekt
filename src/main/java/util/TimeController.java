package util;

import java.util.*;

public enum TimeController {
    INSTANCE;
    private float time;
    private final float deltaTime;
    private final List<Time> timeObjects;
    private boolean isClockRunning;

    TimeController() {
        this.time = 0f;
        this.deltaTime = 0.1f;
        this.isClockRunning = false;
        timeObjects = new LinkedList<>();
    }

    public static TimeController getInstance() {
        return INSTANCE;
    }

    public void startClock() {
        isClockRunning = true;
    }

    public void stopClock() {
        isClockRunning = false;
    }

    public void resetClock() {
        if (!isClockRunning){
            time = 0f;
        }
    }

    public void run(){
        if (isClockRunning) {
            try {
                Thread.sleep((long) (500 * deltaTime));
                time += deltaTime / 2;
                for (Time item : timeObjects)
                    item.update();
            } catch (InterruptedException e) {
                System.out.println("[USER]: Clock error, reset game");
                System.out.println(e.getMessage());
            }
        }
    }

    public void addToTimeObjects(Time timeObject) {
        this.timeObjects.add(timeObject);
    }

    public void removeFromTimeObjects(Time timeObject) {
        this.timeObjects.remove(timeObject);
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public float getTime() {
        return time;
    }
    public boolean isRunning() {
        return isClockRunning;
    }
}
