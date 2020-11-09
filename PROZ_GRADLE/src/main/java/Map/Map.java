package Map;

import Algorithm.*;
import GUI.ObjectRepresentation.GGridNode;
import GUI.UI_Events;
import Vehicle.*;
import Utils.*;

import java.io.*;
import java.util.*;

public class Map implements CarEvents, Time {
    public static int _ySize = 5;
    public static int _xSize = _ySize;
    public static float distance_between_crossings = 50f;

    private GridNode[][] node_map;
    private LinkedList<Car> cars;
    private int carLimit;
    private float max_level_time;
    private float time_between_car_spawn;
    private float time;
    private float _Points;

    private UI_Events uiListener;
    private LinkedList<Car> carsToPurge;
    private int carsToAdd;

    private static boolean DEMO_FLAG = false;

    public Map(int carLimit, int carsStartingAmount, float max_level_time, float time_between_car_spawn, String path) {
        this.node_map = new GridNode[_ySize][_xSize];
        for (int j = 0; j < _ySize; j++)
            for (int i = 0; i < _xSize; i++) {
                this.node_map[j][i] = new GridNode(new Coords(new Pair<Integer, Integer>(j, i)));
            }

        this.carLimit = carLimit;
        this.max_level_time = max_level_time;
        this.time_between_car_spawn = time_between_car_spawn;
        this.cars = new LinkedList<Car>();
        this.carsToPurge = new LinkedList<>();
        this.carsToAdd = carsStartingAmount;
        this.time = 0;
        this._Points = 0;
        this.uiListener = null;

        this.LoadMapFromFile(path);
        this.ConnectNodes();
    }

    public void SpawnCar() {

        if(DEMO_FLAG)
        {
            SpawnCar(new SPair<>(1,0), new SPair<>(4,0));
        }
        else {
            for (int i = 0; i < carsToAdd; i++) {
                if (cars.size() + 1 <= carLimit) {
                    LinkedList<GridNode> road = new A_star(node_map).findPathTo();
                    Street street = road.get(0).PickStartingPoint();
                    cars.add(new Car(20f, road.get(0), street, road.get(0), road, this));
                    Car.getEvents().SpawnCar(cars.getLast().getId(), road.getFirst().getPosition().getX_(), road.getFirst().getPosition().getY_(), street.direction, street.direction);
                }
            }
        }
        uiListener.OnCarsChange(Integer.toString(cars.size()));
        carsToAdd = 0;
    }

    public boolean SpawnCar(SPair<Integer> start, SPair<Integer> end) {
        if (cars.size() + 1 <= carLimit) {
            LinkedList<GridNode> road = new A_star(node_map, start, end).findPathTo();
            Street street = road.get(0).PickStartingPoint();
            //Street street = road.getFirst().getStreets(Direction.East).getX_();
            cars.add(new Car(20f, road.getFirst(), street, road.getFirst(), road, this));
            System.out.println(street.direction);
            Car.getEvents().SpawnCar(cars.getLast().getId(),road.getFirst().getPosition().getX_(),road.getFirst().getPosition().getY_(),street.direction,street.direction);
            uiListener.OnCarsChange(Integer.toString(cars.size()));
            return true;
        }
        carsToAdd = -1;
        return false;
    }

    private boolean DespawnCars() {
        for (Car item : this.carsToPurge)
        {
            item.getEvents().DespawnCar(item.getId());
            this.cars.remove(item);
        }

        uiListener.OnCarsChange(Integer.toString(cars.size()));

        this.carsToPurge.clear();
        return true;
    }

    public void LoadMapFromFile(String pathfile) {
       /* Forma Pliku TXT
          0 POZYCJA Y:X
          1 IMIE name
          2 SYMBOLE WEJSC     NN EE SS WW
          3 WARTOŚCI WĘZŁÓW   00 00 00 00  0 - brak drogi -1 - wyjazd ze skrzyzowania 1 - wjazd na skrzyzowanie
          4 CZAS SWIATLA ZIELONEGO NA LINI NS x.xx
          5 CZAS SWIATLA ZIELONEGO NA LINI WE x.xx
        */

        try {
            File file = new File(pathfile);
            Scanner scanner = new Scanner(file);
            List<String> data = new LinkedList<String>();
            int iter = 0;
            int i = 0;
            int j = 0;
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
                iter++;

                if (iter >= 6) {
                    iter = 0;
                    this.node_map[j][i].FillNode(data);
                    data = new LinkedList<String>();
                    i++;
                    if (i >= _xSize) {
                        i = 0;
                        j++;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(pathfile);
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR WHILE ADDING VALUES TO NODESDATA");
            e.printStackTrace();
        }
    }

    public void ConnectNodes() {
        for (int j = 0; j < _ySize; j++)
            for (int i = 0; i < _xSize; i++) {
                IntersectionType type = node_map[j][i].getType();
                if (type.get_Type() != IntersectionType.o_Type) {
                    Street inStreet;
                    Street outStreet;

                    //N
                    if (type.getN() && j - 1 >= 0) {
                        if (type.get_N().getX_() == IntersectionType.in_street && !node_map[j - 1][i].CheckIfEmpty() && node_map[j - 1][i].getType().get_S().getY_() == IntersectionType.out_street)
                            inStreet = new Street(new Coords(new Pair<Integer, Integer>(j, i), Direction.South),
                                    distance_between_crossings, node_map[j][i], node_map[j - 1][i], node_map[j][i].getType().getNS_time());
                        else
                            inStreet = null;
                        if (type.get_N().getY_() == IntersectionType.out_street && !node_map[j - 1][i].CheckIfEmpty() && node_map[j - 1][i].getType().get_S().getX_() == IntersectionType.in_street)
                            outStreet = new Street(new Coords(new Pair<Integer, Integer>(j - 1, i), Direction.North),
                                    distance_between_crossings, node_map[j - 1][i], node_map[j][i], node_map[j - 1][i].getType().getNS_time());
                        else
                            outStreet = null;

                        node_map[j][i].AddStreets(new Pair<Street, Street>(inStreet, outStreet), Direction.North);
                        node_map[j - 1][i].AddStreets(new Pair<Street, Street>(outStreet, inStreet), Direction.South);
                    } else
                        node_map[j][i].AddStreets(new Pair<Street, Street>(null, null), Direction.North);


                    //S
                    if (type.getS() && j + 1 < _ySize) {
                        if (type.get_S().getX_() == IntersectionType.in_street && !node_map[j + 1][i].CheckIfEmpty() && node_map[j + 1][i].getType().get_N().getY_() == IntersectionType.out_street)
                            inStreet = new Street(new Coords(new Pair<Integer, Integer>(j, i), Direction.North),
                                    distance_between_crossings, node_map[j][i], node_map[j + 1][i], node_map[j][i].getType().getNS_time());
                        else
                            inStreet = null;
                        if (type.get_S().getY_() == IntersectionType.out_street && !node_map[j + 1][i].CheckIfEmpty() && node_map[j + 1][i].getType().get_N().getX_() == IntersectionType.in_street)
                            outStreet = new Street(new Coords(new Pair<Integer, Integer>(j + 1, i), Direction.South),
                                    distance_between_crossings, node_map[j + 1][i], node_map[j][i], node_map[j + 1][i].getType().getNS_time());
                        else
                            outStreet = null;

                        node_map[j][i].AddStreets(new Pair<Street, Street>(inStreet, outStreet), Direction.South);
                        node_map[j + 1][i].AddStreets(new Pair<Street, Street>(outStreet, inStreet), Direction.North);
                    } else
                        node_map[j][i].AddStreets(new Pair<Street, Street>(null, null), Direction.South);


                    //W
                    if (type.getW() && i - 1 >= 0) {
                        if (type.get_W().getX_() == IntersectionType.in_street && !node_map[j][i - 1].CheckIfEmpty() && node_map[j][i - 1].getType().get_E().getY_() == IntersectionType.out_street)
                            inStreet = new Street(new Coords(new Pair<Integer, Integer>(j, i), Direction.East),
                                    distance_between_crossings, node_map[j][i], node_map[j][i - 1], node_map[j][i].getType().getWE_time());
                        else
                            inStreet = null;
                        if (type.get_W().getY_() == IntersectionType.out_street && !node_map[j][i - 1].CheckIfEmpty() && node_map[j][i - 1].getType().get_E().getX_() == IntersectionType.in_street)
                            outStreet = new Street(new Coords(new Pair<Integer, Integer>(j, i - 1), Direction.West),
                                    distance_between_crossings, node_map[j][i - 1], node_map[j][i], node_map[j][i - 1].getType().getWE_time());
                        else
                            outStreet = null;

                        node_map[j][i].AddStreets(new Pair<Street, Street>(inStreet, outStreet), Direction.West);
                        node_map[j][i - 1].AddStreets(new Pair<Street, Street>(outStreet, inStreet), Direction.East);
                    } else
                        node_map[j][i].AddStreets(new Pair<Street, Street>(null, null), Direction.West);

                    //E
                    if (type.getE() && i + 1 < _xSize) {
                        if (type.get_E().getX_() == IntersectionType.in_street && !node_map[j][i + 1].CheckIfEmpty() && node_map[j][i + 1].getType().get_W().getY_() == IntersectionType.out_street)
                            inStreet = new Street(new Coords(new Pair<Integer, Integer>(j, i), Direction.West),
                                    distance_between_crossings, node_map[j][i], node_map[j][i + 1], node_map[j][i].getType().getWE_time());
                        else
                            inStreet = null;
                        if (type.get_E().getY_() == IntersectionType.out_street && !node_map[j][i + 1].CheckIfEmpty() && node_map[j][i + 1].getType().get_W().getX_() == IntersectionType.in_street)
                            outStreet = new Street(new Coords(new Pair<Integer, Integer>(j, i + 1), Direction.East),
                                    distance_between_crossings, node_map[j][i + 1], node_map[j][i], node_map[j][i + 1].getType().getWE_time());
                        else
                            outStreet = null;

                        node_map[j][i].AddStreets(new Pair<Street, Street>(inStreet, outStreet), Direction.East);
                        node_map[j][i + 1].AddStreets(new Pair<Street, Street>(outStreet, inStreet), Direction.West);
                    } else
                        node_map[j][i].AddStreets(new Pair<Street, Street>(null, null), Direction.East);
                } else
                    node_map[j][i].CheckIfEmpty();

                GridNode node = node_map[j][i];
                if (!node.CheckIfEmpty()) {
                    node.getType().AdjustType(node.getStreets(Direction.North).getX_() != null || node.getStreets(Direction.North).getY_() != null,
                            node.getStreets(Direction.East).getX_() != null || node.getStreets(Direction.East).getY_() != null,
                            node.getStreets(Direction.South).getX_() != null || node.getStreets(Direction.South).getY_() != null,
                            node.getStreets(Direction.West).getX_() != null || node.getStreets(Direction.West).getY_() != null);

                    node.AdjustLights();
                    node.AdjustStreets();
                }
            }
    }

    public void AddMapEvent(MapEvent event) {
        for (int j = 0; j < _ySize; j++)
            for (int i = 0; i < _xSize; i++) {
                this.node_map[j][i].setEvent(event);
                //TODO
                this.node_map[j][i].ChangeLights(GridNode.START_FLAG);
            }
    }

    public LinkedList<Car> getCars() {
        return cars;
    }

    public float getMax_level_time() {
        return max_level_time;
    }

    @Override
    public void OnCarAccident(Car car) {
        carsToPurge.add(car);
        carsToAdd++;
    }

    @Override
    public void OnLifeTimeEnd(Car car) {
        carsToPurge.add(car);
        carsToAdd++;
    }

    @Override
    public void OnSuccessfulJourneyEnd(Car car, float points) {
        carsToPurge.add(car);

        if (points < 0.25)
            _Points += 5;
        else if (points < 0.5)
            _Points += 3;
        else if (points < 1)
            _Points += 2;
        else
            _Points += 1;

        uiListener.OnPointsChange(Float.toString(_Points));
        //System.out.println("P:  " + _Points);
    }

    @Override
    public void Update() {
        uiListener.OnTimeChangeChange(Float.toString(TimeController.getInstance().get_time()));

        for (int j = 0; j < _ySize; j++)
            for (int i = 0; i < _xSize; i++) {
                this.node_map[j][i].Update();
            }


        if (this.cars.size() != 0)
            for (Car car : this.cars)
                car.Update();


        this.DespawnCars();
        this.SpawnCar();

        this.time += TimeController.getInstance().getDeltaTime();
        if (this.time >= this.time_between_car_spawn) {
            this.time = 0;
            this.carsToAdd++;
            this.SpawnCar();
        }

        if (cars.size() < Math.floor(carLimit/2)) {
            this.carsToAdd = (int)Math.floor(carLimit/2) - cars.size();
            this.SpawnCar();
        }
    }

    public static int get_xSize() {
        return _xSize;
    }

    public static int get_ySize() {
        return _ySize;
    }

    public GridNode[][] getNode_map() {
        return node_map;
    }

    public void AddUIListener(UI_Events uiListener)
    {
        if(this.uiListener == null)
            this.uiListener = uiListener;
    }

    public static void displayDemo()
    {
        DEMO_FLAG = true;
    }

    public static boolean getDEMO(){
        return DEMO_FLAG;
    }
}
