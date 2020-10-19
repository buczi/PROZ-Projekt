package Map;

import Vehicle.Car;

import java.util.LinkedList;

public class Map {
    private GridNode[][] node_map;
    private LinkedList<Car> cars;
    private int carLimit;
    private int carsStartingAmount;
    private float max_level_time;
    private float time_between_car_spawn;

    public Map()
    {

    }

    public boolean SpawnCar()
    {
        return true;
    }

    public boolean DespawnCar()
    {
        return true;
    }

    public void LoadMapFromFile(String pathfile)
    {
        // Wczytanie mapy z pliku txt
    }

    public void Start()
    {
        // Start the game
    }
}
