package mvc;

import GUI.Menu.GameSettings;
import GUI.Menu.SettingsEvent;
import GUI.ObjectRepresentation.Transform;
import GUI.View;
import Map.*;
import Utils.*;
import Vehicle.Car;

import java.io.*;


public class Controller extends Thread implements Runnable, SettingsEvent {
    public Map map;
    public TimeController timeController;
    public View view;
    public GameSettings settings;

    public static final String projectName = "PROZ_GRADLE";
    //public static final String absolutePath = new File("").getAbsolutePath();
    public static final String absolutePath = findPath(projectName) + "/src/main/java";

    public static String findPath(String projectName) {
        String temp = new File("").getAbsolutePath();
        String temp2;
        while (true) {
            System.out.println(temp);
            if (temp.lastIndexOf('/') <= 0)
                return projectName;

            temp2 = temp.substring(temp.lastIndexOf('/') + 1);
            if (temp2.equals(projectName))
                return temp;
            else
                temp = temp.substring(0, temp.lastIndexOf('/'));
        }
    }


    public Controller() {
        //DEMO
        Map.displayDemo();
        this.map = new Map(1, 1, 360, 15, absolutePath + "/map.txt");///home/maciek/Desktop/PROZ_PRO/

        this.timeController = TimeController.getInstance();
        this.timeController.AddToTimeObjects(map);

        Transform.setPathToImageFolder(absolutePath);
        this.view = new View(this.generateGridData(this.map.getNode_map()),this.generateStreetData(this.map.getNode_map()));
        this.map.AddMapEvent(this.view);
        this.map.AddUIListener(this.view.getPanel());
        Car.AddCarGraphEventListener(this.view);
        this.map.SpawnCar();
        this.start();
    }

    public Controller(boolean flag)
    {
        this.view = new View(this);
    }

    @Override
    public void run() {
        if(!Map.getDEMO())
        {
            try {
                sleep(200);
                this.view.RemoveMenuPanel();
                sleep(1000);
            }
            catch (InterruptedException e)
            {

            }
        }

        this.timeController.StartClock();
        while (true) {

            this.timeController.Run();

            if (this.timeController.get_time() >= this.map.getMax_level_time())
                break;

           if(this.view.sendMessageToModel().size() != 0)
                OnLightsCliked();
        }


        System.out.println("X");

    }

    public static void main(String[] Args) {
        //DEMO
        Controller controller = new Controller();
        System.out.println("CONT");
        //MENU + GAME
        //Controller controller = new Controller(true);
    }

    private void OnLightsCliked()
    {
        for(SPair<Integer>pair : this.view.sendMessageToModel())
        {
            this.map.getNode_map()[pair.getY_()][pair.getX_()].ChangeLights(0);
            this.map.getNode_map()[pair.getY_()][pair.getX_()].ResetTime();
        }

        view.clearMessage();
    }
    private boolean[][] generateGridData(GridNode[][]map)
    {
        boolean[][]b_map = new boolean[Map._ySize][Map._xSize];
        for (int j = 0; j < Map._ySize; j++)
            for (int i = 0; i < Map._xSize; i++) {
                if(map[j][i].getType().get_Type() != IntersectionType.o_Type)
                    b_map[j][i] = true;
                else
                    b_map[j][i] = false;
            }
        return b_map;
    }

    private DataSet[][] generateStreetData(GridNode[][]map)
    {
        DataSet[][]d_map = new DataSet[Map._ySize][Map._xSize];
        for (int j = 0; j < Map._ySize; j++)
            for (int i = 0; i < Map._xSize; i++) {
                if(map[j][i].getType().get_Type() != IntersectionType.o_Type)
                {
                    boolean set[] = new boolean[8];
                    Pair<Street,Street>pair = map[j][i].getStreets(Direction.North);

                    if(pair != null)
                    {
                        if(pair.getX_() != null)
                            set[0] = true;
                        else
                            set[0] = false;

                        if(pair.getY_() != null)
                            set[1] = true;
                        else
                            set[1] = false;
                    }

                    pair = map[j][i].getStreets(Direction.East);

                    if(pair != null)
                    {
                        if(pair.getX_() != null)
                            set[2] = true;
                        else
                            set[2] = false;

                        if(pair.getY_() != null)
                            set[3] = true;
                        else
                            set[3] = false;
                    }

                    pair = map[j][i].getStreets(Direction.South);

                    if(pair != null)
                    {
                        if(pair.getX_() != null)
                            set[4] = true;
                        else
                            set[4] = false;

                        if(pair.getY_() != null)
                            set[5] = true;
                        else
                            set[5] = false;
                    }

                    pair = map[j][i].getStreets(Direction.West);

                    if(pair != null)
                    {
                        if(pair.getX_() != null)
                            set[6] = true;
                        else
                            set[6] = false;

                        if(pair.getY_() != null)
                            set[7] = true;
                        else
                            set[7] = false;
                    }

                    /*
                                        if(i != 0 && d_map[j][i - 1] != null)
                    {
                        if(d_map[j][i-1].getE().getX_() && set[4])
                            set[4] = false;

                        if(d_map[j][i-1].getE().getY_() && set[3])
                            set[3] = false;
                    }

                    if(j != 0 && d_map[j - 1][i] != null)
                    {
                        if(d_map[j - 1][i].getN().getX_() && set[7])
                            set[7] = false;

                        if(d_map[j - 1][i].getN().getY_() && set[6])
                            set[6] = false;
                    }
                     */

                    d_map[j][i] = new DataSet(new SPair<>(set[0],set[1]), new SPair<>(set[2],set[3]), new SPair<>(set[4],set[5]), new SPair<>(set[6],set[7]), !map[j][i].getIsWithoutLights());
                }
                else
                    d_map[j][i] = null;
            }
        return d_map;
    }

    @Override
    public void SetUpGameSettings(GameSettings settings) {
        this.settings = settings;
        this.GoToGame();
    }

    public void GoToGame()
    {
        this.map = new Map(this.settings.getCarLimit(), this.settings.getCarsStartingAmount(), this.settings.getMax_level_time(), this.settings.getTime_between_car_spawn(), absolutePath + this.settings.getMapPath());///home/maciek/Desktop/PROZ_PRO/

        this.timeController = TimeController.getInstance();
        this.timeController.AddToTimeObjects(map);

        Transform.setPathToImageFolder(absolutePath);
        this.view.GoToGame(this.generateGridData(this.map.getNode_map()),this.generateStreetData(this.map.getNode_map()));
        this.map.AddMapEvent(this.view);
        this.map.AddUIListener(this.view.getPanel());
        Car.AddCarGraphEventListener(this.view);
        this.map.SpawnCar();
        this.start();
    }
}