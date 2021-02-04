package map;

import map.intersection.IntersectionType;
import map.intersection.StreetType;
import map.intersection.Type;
import mvc.exception.CriticalException;
import mvc.exception.ExceptionType;
import mvc.exception.MildException;
import map.event.MapEvent;
import vehicle.event.CarEvents;
import algorithm.*;
import gui.event.UI_Events;
import vehicle.*;
import util.*;

import java.io.*;
import java.util.*;

public class Map implements CarEvents, Time {
    public static final int ySize = 5;
    public static final int xSize = 5;

    private final GridNode[][] nodeMap;
    private final LinkedList<Car> cars;
    private int carLimit;
    private final float maxLevelTime;
    private final float timeBetweenCarSpawn;
    private float time;
    private float points;

    private UI_Events uiListener;
    private final List<Car> carsToPurge;
    private int carsToAdd;

    private static boolean DEMO_FLAG = false;
    private static boolean TEST_FLAG = false;

    public Map(int carLimit, int carsStartingAmount, float maxLevelTime, float timeBetweenCarSpawn, String path) throws CriticalException {
        this.nodeMap = new GridNode[ySize][xSize];
        for (int j = 0; j < ySize; j++)
            for (int i = 0; i < xSize; i++) {
                this.nodeMap[j][i] = new GridNode(new Coords(new Pair<>(j, i)));
            }

        this.carLimit = carLimit;
        this.maxLevelTime = maxLevelTime;
        this.timeBetweenCarSpawn = timeBetweenCarSpawn;
        this.cars = new LinkedList<>();
        this.carsToPurge = new LinkedList<>();
        this.carsToAdd = carsStartingAmount;
        this.time = 0;
        this.points = 0;
        this.uiListener = null;

        this.LoadMapFromFile(path);
        this.connectNodes();
    }

    public Map(){
        this.nodeMap = null;
        this.carLimit = 0;
        this.maxLevelTime = 0;
        this.timeBetweenCarSpawn = 0;
        this.cars = null;
        this.carsToPurge = null;
        this.carsToAdd = 0;
        this.time = 0;
        this.points = 0;
        this.uiListener = null;
    }

    public void SpawnCar() {

        if (DEMO_FLAG) {
            carLimit = 2;
            SpawnCar(new SPair<>(1, 1), new SPair<>(4, 1));
            SpawnCar(new SPair<>(2, 1), new SPair<>(4, 1));
        } else {
            for (int i = 0; i < carsToAdd; i++) {
                try {
                    if (cars.size() + 1 <= carLimit) {
                        LinkedList<GridNode> road = new A_star(nodeMap).findPathTo();
                        System.gc();
                        Street street = road.get(0).pickStartingPoint();
                        cars.add(new Car(20f, road.get(0), street, road.get(0), road, this));
                        if (!TEST_FLAG)
                            try {
                                int quePosition = cars.getLast().getCurrentStreet().getLights().getQue().size();
                                if(quePosition > 1){
                                    cars.getLast().setInQue(true);
                                    cars.getLast().resetStreetState((int)cars.getLast().getCurrentStreet().getDistanceToCrossing() - 40 * (quePosition - 1));
                                }
                                    Car.getEvents().spawnCar(cars.getLast().getId(), road.getFirst().getPosition().getX(),
                                            road.getFirst().getPosition().getY(), street.direction,quePosition);
                            } catch (MildException exception) {
                                cars.removeLast();
                                System.out.println(exception.getMildExceptionTrace());
                            }

                    }
                } catch (MildException exception) {
                    if (i > 0)
                        i--;
                    System.out.println(exception.getMildExceptionTrace());
                }

            }
        }
        uiListener.onCarsChange(Integer.toString(cars.size()));
        carsToAdd = 0;
    }

    public void SpawnCar(SPair<Integer> start, SPair<Integer> end) {
        if (cars.size() + 1 <= carLimit) {
            LinkedList<GridNode> road = new A_star(nodeMap, start, end).findPathTo();
            Street street = road.getFirst().getStreets(Direction.West).getX();
            cars.add(new Car(20f, road.getFirst(), street, road.getFirst(), road, this));
            try {
                Car.getEvents().spawnCar(cars.getLast().getId(), road.getFirst().getPosition().getX(), road.getFirst().getPosition().getY(), street.direction,1);
            } catch (MildException exception) {
                cars.removeFirst();
            }
            uiListener.onCarsChange(Integer.toString(cars.size()));
            return;
        }
        carsToAdd = -1;
    }

    private void destroyCar() {
        for (Car item : this.carsToPurge) {
            uiListener.remove(item.getId());
            Car.getEvents().destroyCar(item.getId());
            this.cars.remove(item);
        }

        uiListener.onCarsChange(Integer.toString(cars.size()));
        this.carsToPurge.clear();
    }

    public void resetCarPos(int id, int reset) {
        for (Car item : this.cars) {
            if (item.getId() == id) {
                item.resetStreetState(reset);
                item.setOnIntersection(false);
                break;
            }
        }
    }

    public Car getCarById(int id) {
        for (Car item : this.cars) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void triggerCarEvent(int id) {
        uiListener.addGCar(getCarById(id), getCarById(id).getGCarById(id));
    }

    public void LoadMapFromFile(String pathFile) throws CriticalException {
        try {
            File file = new File(pathFile);
            Scanner scanner = new Scanner(file);
            List<String> data = new LinkedList<>();
            int m = 0;
            int i = 0;
            int j = 0;
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
                m++;

                if (m >= 6) {
                    m = 0;
                    this.nodeMap[j][i].fillNode(data);
                    data = new LinkedList<>();
                    i++;
                    if (i >= xSize) {
                        i = 0;
                        j++;
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("[USER]: Check if the given path is valid, maybe some folders are deleted or file with map? \nnPath:" + pathFile);
            throw new CriticalException("FAILED TO INITIALIZE MAP", this.getClass().getName(), ExceptionType.LOAD_MAP_FROM_FILE_EXCEPTION);
        }
    }

    public void connectNodes() {
        for (int j = 0; j < ySize; j++)
            for (int i = 0; i < xSize; i++) {
                IntersectionType type = nodeMap[j][i].getType();
                if (!type.get_Type().equals(Type.oType)) {
                    Street inStreet;
                    Street outStreet;

                    //N
                    if (type.getN() && j - 1 >= 0) {
                        if (type.getIN().getX().equals(StreetType.inStreet.getVal()) && !nodeMap[j - 1][i].checkIfEmpty() && nodeMap[j - 1][i].getType().getIS().getY().equals(StreetType.outStreet.getVal()))
                            inStreet = new Street(new Coords(new Pair<>(j, i), Direction.South),
                                    nodeMap[j][i], nodeMap[j - 1][i], nodeMap[j][i].getType().getNsTime());
                        else
                            inStreet = null;
                        if (type.getIN().getY().equals(StreetType.outStreet.getVal()) && !nodeMap[j - 1][i].checkIfEmpty() && nodeMap[j - 1][i].getType().getIS().getX().equals(StreetType.inStreet.getVal()))
                            outStreet = new Street(new Coords(new Pair<>(j - 1, i), Direction.North),
                                    nodeMap[j - 1][i], nodeMap[j][i], nodeMap[j - 1][i].getType().getNsTime());
                        else
                            outStreet = null;

                        nodeMap[j][i].addStreets(new Pair<>(inStreet, outStreet), Direction.North);
                        nodeMap[j - 1][i].addStreets(new Pair<>(outStreet, inStreet), Direction.South);
                    } else
                        nodeMap[j][i].addStreets(new Pair<>(null, null), Direction.North);


                    //S
                    if (type.getS() && j + 1 < ySize) {
                        if (type.getIS().getX().equals(StreetType.inStreet.getVal()) && !nodeMap[j + 1][i].checkIfEmpty() && nodeMap[j + 1][i].getType().getIN().getY().equals(StreetType.outStreet.getVal()))
                            inStreet = new Street(new Coords(new Pair<>(j, i), Direction.North),
                                   nodeMap[j][i], nodeMap[j + 1][i], nodeMap[j][i].getType().getNsTime());
                        else
                            inStreet = null;
                        if (type.getIS().getY().equals(StreetType.outStreet.getVal()) && !nodeMap[j + 1][i].checkIfEmpty() && nodeMap[j + 1][i].getType().getIN().getX().equals(StreetType.inStreet.getVal()))
                            outStreet = new Street(new Coords(new Pair<>(j + 1, i), Direction.South),
                                    nodeMap[j + 1][i], nodeMap[j][i], nodeMap[j + 1][i].getType().getNsTime());
                        else
                            outStreet = null;

                        nodeMap[j][i].addStreets(new Pair<>(inStreet, outStreet), Direction.South);
                        nodeMap[j + 1][i].addStreets(new Pair<>(outStreet, inStreet), Direction.North);
                    } else
                        nodeMap[j][i].addStreets(new Pair<>(null, null), Direction.South);


                    //W
                    if (type.getW() && i - 1 >= 0) {
                        if (type.getIW().getX().equals(StreetType.inStreet.getVal()) && !nodeMap[j][i - 1].checkIfEmpty() && nodeMap[j][i - 1].getType().getIE().getY().equals(StreetType.outStreet.getVal()))
                            inStreet = new Street(new Coords(new Pair<>(j, i), Direction.East),
                                    nodeMap[j][i], nodeMap[j][i - 1], nodeMap[j][i].getType().getWeTime());
                        else
                            inStreet = null;
                        if (type.getIW().getY().equals(StreetType.outStreet.getVal()) && !nodeMap[j][i - 1].checkIfEmpty() && nodeMap[j][i - 1].getType().getIE().getX().equals(StreetType.inStreet.getVal()))
                            outStreet = new Street(new Coords(new Pair<>(j, i - 1), Direction.West),
                                    nodeMap[j][i - 1], nodeMap[j][i], nodeMap[j][i - 1].getType().getWeTime());
                        else
                            outStreet = null;

                        nodeMap[j][i].addStreets(new Pair<>(inStreet, outStreet), Direction.West);
                        nodeMap[j][i - 1].addStreets(new Pair<>(outStreet, inStreet), Direction.East);
                    } else
                        nodeMap[j][i].addStreets(new Pair<>(null, null), Direction.West);

                    //E
                    if (type.getE() && i + 1 < xSize) {
                        if (type.getIE().getX().equals(StreetType.inStreet.getVal()) && !nodeMap[j][i + 1].checkIfEmpty() && nodeMap[j][i + 1].getType().getIW().getY().equals(StreetType.outStreet.getVal()))
                            inStreet = new Street(new Coords(new Pair<>(j, i), Direction.West),
                                    nodeMap[j][i], nodeMap[j][i + 1], nodeMap[j][i].getType().getWeTime());
                        else
                            inStreet = null;
                        if (type.getIE().getY().equals(StreetType.outStreet.getVal()) && !nodeMap[j][i + 1].checkIfEmpty() && nodeMap[j][i + 1].getType().getIW().getX().equals(StreetType.inStreet.getVal()))
                            outStreet = new Street(new Coords(new Pair<>(j, i + 1), Direction.East),
                                    nodeMap[j][i + 1], nodeMap[j][i], nodeMap[j][i + 1].getType().getWeTime());
                        else
                            outStreet = null;

                        nodeMap[j][i].addStreets(new Pair<>(inStreet, outStreet), Direction.East);
                        nodeMap[j][i + 1].addStreets(new Pair<>(outStreet, inStreet), Direction.West);
                    } else
                        nodeMap[j][i].addStreets(new Pair<>(null, null), Direction.East);
                } else
                    nodeMap[j][i].checkIfEmpty();

                GridNode node = nodeMap[j][i];
                if (!node.checkIfEmpty()) {
                    node.getType().adjustType(node.getStreets(Direction.North).getX() != null || node.getStreets(Direction.North).getY() != null,
                            node.getStreets(Direction.East).getX() != null || node.getStreets(Direction.East).getY() != null,
                            node.getStreets(Direction.South).getX() != null || node.getStreets(Direction.South).getY() != null,
                            node.getStreets(Direction.West).getX() != null || node.getStreets(Direction.West).getY() != null);

                    node.adjustLights();
                    node.adjustStreets();
                }
            }
    }

    public void addMapEvent(MapEvent event) {
        for (GridNode[] nodeJ : nodeMap)
            for (GridNode node : nodeJ) {
                node.setEvent(event);
                node.changeLights(GridNode.START_FLAG);
            }
    }

    public float getMaxLevelTime() {
        return maxLevelTime;
    }

    @Override
    public void onCarAccident(Car car) {
        carsToPurge.add(car);
        carsToAdd++;
    }

    @Override
    public void onLifeTimeEnd(Car car) {
        carsToPurge.add(car);
        carsToAdd++;
    }

    @Override
    public void onSuccessfulJourneyEnd(Car car, float points) {
        carsToPurge.add(car);

        if (points < 0.25)
            this.points += 5;
        else if (points < 0.5)
            this.points += 3;
        else if (points < 1)
            this.points += 2;
        else
            this.points += 1;

        uiListener.onPointsChange(Float.toString(this.points));
    }

    @Override
    public void update() {
        uiListener.onTimeChangeChange(Float.toString(TimeController.getInstance().getTime()));

        for(GridNode[] nodeY : nodeMap)
            for(GridNode nodeX : nodeY)
                    nodeX.update();


        if (this.cars.size() != 0)
            for (Car car : this.cars)
                car.update();


        this.destroyCar();
        this.SpawnCar();

        this.time += TimeController.getInstance().getDeltaTime();
        if (this.time >= this.timeBetweenCarSpawn) {
            this.time = 0;
            this.carsToAdd++;
            this.SpawnCar();
        }

        if (cars.size() < Math.floor((float) carLimit / 2)) {
            this.carsToAdd = (int) Math.floor((float) carLimit / 2) - cars.size();
            this.SpawnCar();
        }
    }

    public static int getXSize() {
        return xSize;
    }

    public static int getYSize() {
        return ySize;
    }

    public GridNode[][] getNodeMap() {
        return nodeMap;
    }

    public float getPoints() {
        return points;
    }

    public void addUIListener(UI_Events uiListener) {
        this.uiListener = uiListener;
    }

    public static void displayDemo() {
        DEMO_FLAG = true;
    }

    public static void stopDemo(){DEMO_FLAG = false;}

    public static boolean getDEMO() {
        return DEMO_FLAG;
    }

    public static boolean getTEST(){return TEST_FLAG;}

    public static void setTEST(boolean flag) {
        TEST_FLAG = flag;
    }

    public LinkedList<Car> getCars() {
        return cars;
    }
}
