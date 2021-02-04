package vehicle;

import map.intersection.Type;
import vehicle.event.CarEvents;
import vehicle.event.CarGraphicEvents;
import gui.objectrepresetnation.GCar;
import map.*;
import util.*;


import java.util.*;

public class Car extends Coords implements Time {
    private float speed;
    private final float maxSpeed;
    private float currentTime;
    private float perfectTime;
    private final float maxLifeTime;


    private float turnTime;
    private final static float turnTimeR = 1.9f;
    private final static float turnTimeL = 3.2f;
    private final static float turnTimeU = 2.8f;

    private float turnTimer;
    private boolean turning;
    private boolean turningBack;
    private Direction beforeTurn;

    private float streetState;
    private boolean streetEnd;

    private Street currentStreet;
    private GridNode currentNode;
    private final LinkedList<GridNode> road;

    private final List<CarEvents> listener;
    private boolean active;

    private static CarGraphicEvents events;
    private final int id;
    private boolean onIntersection; // Flag for animation
    private boolean inQue;

    public Car(float maxSpeed, Coords coords, Street currentStreet, GridNode currentNode, LinkedList<GridNode> road, CarEvents events) {
        super(coords);

        this.listener = new ArrayList<>();
        this.addListener(events);

        this.direction = currentStreet.getDirection();
        this.currentStreet = currentStreet;
        this.currentStreet.getDestination().addCarToLights(this, this.currentStreet.getDirection());
        this.currentNode = currentNode;

        this.maxSpeed = maxSpeed;
        this.streetState = 0f;
        this.streetEnd = false;
        this.road = road;
        incrementID();
        this.id = getObjId();

        this.calculatePerfectTime();
        this.maxLifeTime = 180f;
        this.turnTime = 1f;
        this.turnTimer = 0f;
        this.turning = false;
        this.turningBack = false;
        this.active = true;
        this.onIntersection = false;
        this.inQue = false;

    }

    public void startJourney() {
        start();
        if(inQue){
            inQue = false;
            return;
        }
        findWayThroughCrossing();
    }

    public void calculatePerfectTime() {
        perfectTime = (road.size() - 1) * Street.distanceBetween/ maxSpeed;
    }

    public void findWayThroughCrossing() {
        currentNode = road.get(0);
        road.remove(0);
        if (road.size() == 0) {
            onJourneyEnd();
            return;
        }


        for (Direction direction : Direction.values()) {
            Pair<Street, Street> intersection = currentNode.getStreets(direction);
            if (intersection != null)
                if (intersection.getY() != null)
                    if (intersection.getY().getDestination() == road.get(0)) {

                        currentStreet = intersection.getY();
                        currentNode = currentStreet.getDestination();
                        this.resetStreetState(0f);
                        if(intersection.getY().getDirection() != this.direction)
                        {
                            this.beforeTurn = this.direction;
                            if(Direction.checkIfRightTurn(this.direction,intersection.getY().getDirection()))
                            {
                                this.turnTime = turnTimeR;
                                this.streetState += 70;
                            }
                            else if(Direction.opposite(intersection.getY().getDirection()) == this.direction)
                            {
                                this.turnTime = turnTimeU;
                                this.streetState += 70;
                                this.turningBack = true;
                            }
                            else
                            {
                                this.turnTime = turnTimeL;
                                this.streetState += 40;
                            }


                            this.turning = true;
                        }
                        this.direction = intersection.getY().getDirection();

                        return;
                    }
        }

        onCarAccident();
    }

    public float endJourney() {
        return currentTime - perfectTime;
    }

    public void start() {
        speed = maxSpeed;
    }

    public void stop() {
        speed = 0;
    }

    public boolean isInMotion() {
        return speed > 0;
    }

    public void goTroughStreet() {
        if (this.isInMotion()) {
            this.streetState += this.speed * TimeController.getInstance().getDeltaTime();
            events.moveCar(id,(int)this.maxSpeed,this.getDirection());

        if (this.streetState > this.currentStreet.getDistanceToCrossing()) {
            this.streetEnd = true;
            return;
        }

            if(this.currentStreet.getDestination().getStreets(Direction.opposite(this.direction)).getX().getLights() != null)
                if(this.currentStreet.getDistanceToCrossing() - this.currentStreet.getDestination().getStreets(Direction.opposite(this.direction)).getX().getLights().getQue().size()*40 < this.streetState) {
                    this.currentStreet.getDestination().addCarToLights(this, this.currentStreet.getDirection());
                    inQue = true;
                    stop();
                }
        }
    }

    public void resetStreetState(float state) {
        this.streetState = state;
        this.streetEnd = false;
    }

    //EVENT
    public void addListener(CarEvents events) {
        listener.add(events);
    }

    private void onJourneyEnd() {
        this.active = false;
        for (CarEvents events : listener)
            events.onSuccessfulJourneyEnd(this, endJourney() / this.perfectTime);
    }

    private void onCarAccident() {
        this.active = false;
        for (CarEvents events : listener)
            events.onCarAccident(this);
    }

    private void onLifeTimeEnd() {
        this.active = false;
        for (CarEvents events : listener)
            events.onLifeTimeEnd(this);
    }

    public void moveTroughIntersection()
    {
        if(turnTimer > turnTime)
        {
            events.enterIntersection(id,new SPair<>(currentStreet.getBeginning().getPosition().getX(),currentStreet.getBeginning().getPosition().getY()));
            events.rotate(id,getDirection());

            turning = false;
            turnTimer = 0f;
            return;
        }

        events.moveCar(id,(int) maxSpeed,beforeTurn);

    }

    public void turnBack()
    {
        if(turnTimer > turnTime)
        {
            events.enterIntersection(id,new SPair<>(currentStreet.getBeginning().getPosition().getX(),currentStreet.getBeginning().getPosition().getY()));
            events.rotate(id,getDirection());

            turning = false;
            turningBack = false;
            turnTimer = 0f;
            return;
        }

        if(1.5 - turnTimer <= 0.1f && 1.5 - turnTimer > 0)
        {
            events.rotate(id,Direction.getRight(getDirection()));
            beforeTurn = Direction.getRight(getDirection());
        }
        events.moveCar(id,(int) maxSpeed,beforeTurn);
    }

    @Override
    public void update() {

        if (!active)
            return;

        if (this.road.size() == 0) {
            onJourneyEnd();
            return;
        }
        this.currentTime += TimeController.getInstance().getDeltaTime();

        if(turningBack)
        {
            this.turnTimer += TimeController.getInstance().getDeltaTime();
            turnBack();
            return;
        }

        if(turning)
        {
            this.turnTimer += TimeController.getInstance().getDeltaTime();
            moveTroughIntersection();
            return;
        }

        if (this.currentTime >= this.maxLifeTime) {
            this.onLifeTimeEnd();
            return;
        }

        this.goTroughStreet();

        if (this.streetEnd && !this.currentStreet.getLights().getIsItGreenLight() &&
                !this.currentStreet.getDestination().getType().get_Type().equals(Type.IType) &&
                !this.currentStreet.getDestination().getType().get_Type().equals(Type.LType) &&
                !this.currentStreet.getDestination().getIsWithoutLights()) {
            if (this.road.size() == 1) {
                this.onJourneyEnd();
                return;
            }


            this.currentStreet.getDestination().addCarToLights(this, this.currentStreet.getDirection());
            this.stop();
            this.resetStreetState(0f);
        } else if (this.streetEnd && (this.currentStreet.getLights().getIsItGreenLight() || this.currentStreet.getDestination().getIsWithoutLights())) {
            this.findWayThroughCrossing();
            this.resetStreetState(0f);
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

    public static void addCarGraphEventListener(CarGraphicEvents event)
    {
        events = event;
    }

    public static CarGraphicEvents getEvents() {
        return events;
    }

    public int getId() {
        return id;
    }

    public SPair<Integer> getDestination()
    {
        return new SPair<>(road.getLast().getPosition().getX(),road.getLast().getPosition().getY());
    }

    public GCar getGCarById(int id){
        return events.getGCarById(id);
    }

    public void setOnIntersection(boolean value){
            onIntersection = value;
    }

    public Street getCurrentStreet() {
        return currentStreet;
    }


    public void setInQue(boolean inQue) {
        this.inQue = inQue;
    }
}
