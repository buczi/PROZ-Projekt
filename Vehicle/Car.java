package Vehicle;
import Map.*;
import Utils.Pair;
import java.lang.*;
import java.util.*;

public class Car extends Coords{
    private float speed;
    private float max_speed;
    private float current_time;
    private float perfect_time;

    private GridNode closestNode;
    private LinkedList<GridNode> road;

    public Car(float max_speed, Pair<Integer,Integer> position, LinkedList<GridNode> road)
    {
        super(position);
        this.max_speed = max_speed;
        this.road = road;
        this.StartJourney();
    }

    public void StartJourney()
    {

    }

    public void CalculatePerfectTime()
    {
        float suggestedTime = 0f;
        // Przeliczenie odleglosci miedzy droga i podzielenie jej na predkosc
        perfect_time = suggestedTime;
    }
    public void FindWayThroughCrossing()
    {
        // Przeprowadzenie samochodu przez skrzyżowanie
    }

    public float EndJourney()
    {
        //Zatrzymanie pojazdu na koncu jego podróży, zwrocenie roznicy miedzy idealnym czasem a czasem podrozy w celu przyznania punktow
        return current_time - perfect_time;
    }

    public void Start()
    {
        // Poruszenie pojazdu po zatrzymaniu na swiatlach
        speed = max_speed;
    }

    public void Stop()
    {
        // Zatrzymanie pojazdu przed swiatlami
        speed = 0;
    }

}
