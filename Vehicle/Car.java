package Vehicle;

import Map.*;
import Utils.*;


import java.util.*;

public class Car extends Coords implements Time {
    private float speed;
    private float max_speed;
    private float current_time;
    private float perfect_time;
    private float max_LifeTime;

    private float streetState;
    private boolean streetEnd;

    private Street currentStreet;
    private GridNode currentNode;
    private LinkedList<GridNode> road;

    private ArrayList<CarEvents> listener;
    private boolean active;


    public Car(float max_speed, Coords coords, Street currentStreet, GridNode currentNode, LinkedList<GridNode> road, CarEvents events) {
        super(coords);

        this.listener = new ArrayList<CarEvents>();
        this.AddListener(events);

        this.currentStreet = currentStreet;
        this.currentStreet.getDestination().AddCarToLights(this, this.currentStreet.getDirection());
        this.currentNode = currentNode;

        this.max_speed = max_speed;
        this.streetState = 0f;
        this.streetEnd = false;
        this.road = road;

        this.CalculatePerfectTime();
        this.max_LifeTime = 180f;
        this.active = true;
    }

    public void StartJourney() {
        Start();
        FindWayThroughCrossing();
    }

    public void CalculatePerfectTime() {
        float suggestedTime = 0f;
        suggestedTime = (road.size() - 1) * Street.distanceBetween/max_speed;
        perfect_time = suggestedTime;
    }

    public void FindWayThroughCrossing() {
        currentNode = road.get(0);
        road.remove(0);
        if (road.size() == 0) {
            OnJourneyEnd();
            return;
        }


        for (Direction direction : Direction.values()) {
            Pair<Street, Street> intersection = currentNode.getStreets(direction);
            if (intersection != null)
                if (intersection.getY_() != null)
                    if (intersection.getY_().getDestination() == road.get(0)) {

                        currentStreet = intersection.getY_();
                        currentNode = currentStreet.getDestination();
                        this.direction = intersection.getY_().getDirection();
                        this.ResetStreetState();
                        return;
                    }
        }

        OnCarAccident();
    }

    public float EndJourney() {
        return current_time - perfect_time;
    }

    public void Start() {
        speed = max_speed;
    }

    public void Stop() {
        speed = 0;
    }

    public boolean isInMotion() {
        return speed > 0;
    }

    public boolean IsAtTheEndOfTheRoad() {
        if (streetState + TimeController.getInstance().getDeltaTime() * speed >= currentStreet.getDistanceToCrossing())
            return true;

        streetState += TimeController.getInstance().getDeltaTime() * speed;
        return false;
    }

    public void GoTroughStreet() {
        if (this.isInMotion()) {
            this.streetState += this.speed * TimeController.getInstance().getDeltaTime();
            if (this.streetState > this.currentStreet.getDistanceToCrossing())
                this.streetEnd = true;
        }
    }

    public void ResetStreetState() {
        this.streetState = 0f;
        this.streetEnd = false;
    }

    //EVENT
    public void AddListener(CarEvents events) {
        listener.add(events);
    }

    private void OnJourneyEnd() {
        this.active = false;
        float delta = EndJourney();
        for (CarEvents events : listener)
            events.OnSuccessfulJourneyEnd(this, EndJourney() / this.perfect_time);
    }

    private void OnCarAccident() {
        this.active = false;
        float delta = EndJourney();
        for (CarEvents events : listener)
            events.OnCarAccident(this);
    }

    private void OnLifeTimeEnd() {
        this.active = false;
        float delta = EndJourney();
        for (CarEvents events : listener)
            events.OnLifeTimeEnd(this);
    }

    @Override
    public void Update() {

        if (!active)
            return;

        if (this.road.size() == 0) {
            OnJourneyEnd();
            return;
        }
        this.current_time += TimeController.getInstance().getDeltaTime();

        if (this.current_time >= this.max_LifeTime) {
            this.OnLifeTimeEnd();
            return;
        }

        this.GoTroughStreet();

        if (this.streetEnd && !this.currentStreet.getLights().getIsItGreenLight() &&
                this.currentStreet.getDestination().getType().get_Type() != IntersectionType.I_Type &&
                this.currentStreet.getDestination().getType().get_Type() != IntersectionType.L_Type &&
                !this.currentStreet.getDestination().getIsWithoutLights()) {
            if (this.road.size() == 1) {
                this.OnJourneyEnd();
                return;
            }


            this.currentStreet.getDestination().AddCarToLights(this, this.currentStreet.getDirection());
            this.Stop();
            this.ResetStreetState();
        } else if (this.streetEnd && (this.currentStreet.getLights().getIsItGreenLight() || this.currentStreet.getDestination().getIsWithoutLights())) {
            this.FindWayThroughCrossing();
            this.ResetStreetState();
        }
    }

    @Override
    public String toString() {
        return "Speed: " + this.speed + "\n" +
                "Current Position: " + this.currentStreet.getBeginning().getPosition() + "\n" +
                "Current Target: " + this.currentStreet.getDestination().getPosition() + "\n" +
                "Current Direction: " + this.getDirection() + "\n" +
                "Is in que: " + this.currentStreet.getLights().getQue().contains(this) + "\n" +
                "Nodes to target: " + this.road.size();
    }
}
