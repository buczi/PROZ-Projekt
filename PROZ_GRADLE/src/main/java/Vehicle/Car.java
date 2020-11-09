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


    private float turn_time;
    private final static float turn_time_R = 1.7f;
    private final static float turn_time_L = 3.5f;
    private final static float turn_time_U = 2.8f;

    private float turn_timer;
    private boolean turning;
    private boolean turning_back;
    private Direction beforeTurn;

    private float streetState;
    private boolean streetEnd;

    private Street currentStreet;
    private GridNode currentNode;
    private LinkedList<GridNode> road;

    private ArrayList<CarEvents> listener;
    private boolean active;

    private static CarGraphicEvents events;
    private int id;

    public Car(float max_speed, Coords coords, Street currentStreet, GridNode currentNode, LinkedList<GridNode> road, CarEvents events) {
        super(coords);

        this.listener = new ArrayList<CarEvents>();
        this.AddListener(events);

        this.direction = currentStreet.getDirection();
        this.currentStreet = currentStreet;
        this.currentStreet.getDestination().AddCarToLights(this, this.currentStreet.getDirection());
        this.currentNode = currentNode;

        this.max_speed = max_speed;
        this.streetState = 0f;
        this.streetEnd = false;
        this.road = road;
        incrementID();
        this.id = getObjId();

        this.CalculatePerfectTime();
        this.max_LifeTime = 180f;
        this.turn_time = 1f;
        this.turn_timer = 0f;
        this.turning = false;
        this.turning_back = false;
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
                        this.ResetStreetState();
                        if(intersection.getY_().getDirection() != this.direction)
                        {
                            this.beforeTurn = this.direction;
                            if(Direction.CheckIfRightTurn(this.direction,intersection.getY_().getDirection()))
                            {
                                this.turn_time = turn_time_R;
                                this.streetState += 70;
                            }
                            else if(Direction.Oposite(intersection.getY_().getDirection()) == this.direction)
                            {
                                this.turn_time = turn_time_U;
                                this.streetState += 70;
                                this.turning_back = true;
                            }
                            else
                            {
                                this.turn_time = turn_time_L;
                                this.streetState += 40;
                            }


                            this.turning = true;
                        }
                        this.direction = intersection.getY_().getDirection();
                        //TODO
                        //events.Rotate(id,direction);

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
            events.MoveCar(id,(int)this.max_speed,this.getDirection());
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

    public void MoveTroughIntersection()
    {
        if(turn_timer > turn_time)
        {
            events.Rotate(id,getDirection());
            turning = false;
            turn_timer = 0f;
            return;
        }

        events.MoveCar(id,(int)max_speed,beforeTurn);

    }

    public void TurnBack()
    {
        if(turn_timer > turn_time)
        {
            events.Rotate(id,getDirection());
            turning = false;
            turning_back = false;
            turn_timer = 0f;
            return;
        }

        if(1.5 - turn_timer <= 0.1f && 1.5 - turn_timer > 0)
        {
            events.Rotate(id,Direction.getRight(getDirection()));
            beforeTurn = Direction.getRight(getDirection());
        }
        events.MoveCar(id,(int)max_speed,beforeTurn);
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

        if(turning_back)
        {
            this.turn_timer += TimeController.getInstance().getDeltaTime();
            TurnBack();
            return;
        }

        if(turning)
        {
            this.turn_timer += TimeController.getInstance().getDeltaTime();
            MoveTroughIntersection();
            return;
        }

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

    public static void AddCarGraphEventListener(CarGraphicEvents event)
    {
        events = event;
    }

    public static CarGraphicEvents getEvents() {
        return events;
    }

    public int getId() {
        return id;
    }

    public void DeterTurnType()
    {

    }
}
