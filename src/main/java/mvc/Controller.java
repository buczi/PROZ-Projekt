package mvc;

import map.intersection.Type;
import mvc.event.CreateThread;
import mvc.event.EndGameEvent;
import mvc.event.PassEvent;
import mvc.exception.CriticalException;
import mvc.exception.ExceptionType;
import gui.menu.GameSettings;
import gui.menu.event.SettingsEvent;
import gui.objectrepresetnation.event.ResetCar;
import gui.objectrepresetnation.Transform;
import gui.View;
import map.*;
import util.*;
import vehicle.Car;

import java.io.*;


public class Controller extends Thread implements Runnable, SettingsEvent, ResetCar, PassEvent {
    private Map map;
    private TimeController timeController;
    private final View view;
    private GameSettings settings;
    private EndGameEvent endgame;
    private CreateThread createEvent;
    private boolean firstLoop;

    private boolean criticalExceptionFlag;
    private CriticalException exception;

    public static final String absolutePath = new File("").getAbsolutePath();
    public static final String resourcePath = absolutePath + "/src/main/resources/";

    public Controller(String strMap) throws CriticalException {
        //DEMO
        Map.displayDemo();
        this.map = new Map(1, 1, 360, 15, resourcePath  + strMap);

        this.timeController = TimeController.getInstance();
        this.timeController.addToTimeObjects(map);

        Transform.setPathToImageFolder(resourcePath);
        this.view = new View(this.generateGridData(this.map.getNodeMap()), this.generateStreetData(this.map.getNodeMap()), this, this);
        this.map.addMapEvent(this.view);
        this.map.addUIListener(this.view.getPanel());
        Car.addCarGraphEventListener(this.view);
        this.map.SpawnCar();
        this.start();
    }

    public Controller(boolean flag, CreateThread event) throws CriticalException {
        this.createEvent = event;
        this.view = new View(this, this, this);
        this.firstLoop = flag;
        this.criticalExceptionFlag = false;
    }

    @Override
    public void run() {
        if (this.criticalExceptionFlag) {
            this.createEvent.reportException(exception);
            return;
        }

        if (!Map.getDEMO() && !Map.getTEST()) {
            try {
                sleep(200);
                this.view.removeMenuPanel();
                sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("[USER]: Nothing to worry about");
                System.out.println(e.getMessage());
            }
        }

        this.timeController.startClock();
        while (true) {

            this.timeController.run();

            if (this.timeController.getTime() >= this.map.getMaxLevelTime())
                break;

            if (this.view.sendMessageToModel().size() != 0)
                onLightsClicked();
        }
        this.timeController.stopClock();
        this.endgame.reset(this.map.getPoints());


        System.out.println("X");

    }

    private void onLightsClicked() {
        for (SPair<Integer> pair : this.view.sendMessageToModel()) {
            this.map.getNodeMap()[pair.getY()][pair.getX()].changeLights(0);
            this.map.getNodeMap()[pair.getY()][pair.getX()].resetTime();
        }

        view.clearMessage();
    }

    private boolean[][] generateGridData(GridNode[][] map) {
        boolean[][] bMap = new boolean[Map.ySize][Map.xSize];
        for (int j = 0; j < Map.ySize; j++)
            for (int i = 0; i < Map.xSize; i++) {
                bMap[j][i] = !map[j][i].getType().get_Type().equals(Type.oType);
            }
        return bMap;
    }

    private DataSet[][] generateStreetData(GridNode[][] map) {
        DataSet[][] dMap = new DataSet[Map.ySize][Map.xSize];
        for (int j = 0; j < Map.ySize; j++)
            for (int i = 0; i < Map.xSize; i++) {
                if (!map[j][i].getType().get_Type().equals(Type.oType)) {
                    boolean[] set = new boolean[8];
                    Pair<Street, Street> pair = map[j][i].getStreets(Direction.North);

                    if (pair != null) {
                        set[0] = pair.getX() != null;

                        set[1] = pair.getY() != null;
                    }

                    pair = map[j][i].getStreets(Direction.East);

                    if (pair != null) {
                        set[2] = pair.getX() != null;

                        set[3] = pair.getY() != null;
                    }

                    pair = map[j][i].getStreets(Direction.South);

                    if (pair != null) {
                        set[4] = pair.getX() != null;

                        set[5] = pair.getY() != null;
                    }

                    pair = map[j][i].getStreets(Direction.West);

                    if (pair != null) {
                        set[6] = pair.getX() != null;

                        set[7] = pair.getY() != null;
                    }
                    dMap[j][i] = new DataSet(new SPair<>(set[0], set[1]), new SPair<>(set[2], set[3]), new SPair<>(set[4], set[5]), new SPair<>(set[6], set[7]), !map[j][i].getIsWithoutLights());
                } else
                    dMap[j][i] = null;
            }
        return dMap;
    }

    @Override
    public void makeCarInterface(int id) {
        this.map.triggerCarEvent(id);
    }

    @Override
    public void goToMainMenu() {
        this.timeController.removeFromTimeObjects(map);
        this.view.mainframe.dispose();
        this.createEvent.createThread();
    }

    @Override
    public void setUpGameSettings(GameSettings settings) {
        this.settings = settings;
        this.goToGame();
    }

    @Override
    public void resetCarPosition(int id) {
        int reset = 92;

        this.map.resetCarPos(id, reset);
    }

    public void goToGame() {

        System.gc();
        try {
            this.map = new Map(this.settings.getCarLimit(), this.settings.getCarsStartingAmount(),
                    this.settings.getMaxLevelTime(), this.settings.getTimeBetweenCarSpawn(),
                    resourcePath  + this.settings.getMapPath());
        }
        catch(CriticalException e){
            this.criticalExceptionFlag = true;
            this.exception = e;
        }
        this.timeController = TimeController.getInstance();
        this.timeController.addToTimeObjects(map);

        this.timeController.stopClock();
        this.timeController.resetClock();
        if (this.firstLoop && !this.criticalExceptionFlag)
            Transform.setPathToImageFolder(resourcePath);
        try {
            this.view.GoToGame(this.generateGridData(this.map.getNodeMap()), this.generateStreetData(this.map.getNodeMap()));
        } catch (CriticalException exception) {
            this.criticalExceptionFlag = true;
            this.exception = exception;
        } catch (NullPointerException exception) {
            this.criticalExceptionFlag = true;
            this.exception = new CriticalException("FAILED TO INITIALIZE MAP", this.getClass().getName(), ExceptionType.READ_FROM_NULL_VAL);
        }


        if (!this.criticalExceptionFlag) {
            this.map.addMapEvent(this.view);
            this.map.addUIListener(this.view.getPanel());
            this.endgame = this.view;
            Car.addCarGraphEventListener(this.view);
            this.map.SpawnCar();
        }
        this.start();
    }

    public View getView() {
        return view;
    }
}