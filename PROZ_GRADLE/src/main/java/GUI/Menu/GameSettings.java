package GUI.Menu;

import java.util.ArrayList;

public class GameSettings {
    //IN ORDER TO CHANGE MAP CHANGE PATHS
    private static final String firstMapPath = "/src/map.txt";
    private static final String secondMapPath = "/src/Map1.txt";
    private static final String thirdMapPath = "/src/map2.txt";

    private static class DifficultySettings {
        protected int carLimit;
        protected int carsStartingAmount;
        protected float max_level_time;
        protected float time_between_car_spawn;

        protected DifficultySettings(int carLimit_n, int carsStartingAmount_n, float max_level_time_n, float time_between_car_spawn_n)
        {
            carLimit = carLimit_n;
            carsStartingAmount = carsStartingAmount_n;
            max_level_time = max_level_time_n;
            time_between_car_spawn = time_between_car_spawn_n;
        }

        protected DifficultySettings(){};
    }

    //MODIFY DIFFICULTY BY CHANGING THIS VALUES
    private static final ArrayList<DifficultySettings> settings = new ArrayList<DifficultySettings>(){{
            add(new DifficultySettings(1,1,180,10));
            add(new DifficultySettings(3,3,360,8));
            add(new DifficultySettings(10,10,480,5));
    }};

    private String mapPath;
    private DifficultySettings difficultySettings;

    public GameSettings(int mapNumber, int difficultyNumber)
    {
        switch (mapNumber)
        {
            case 0: mapPath = firstMapPath;
            break;
            case 1: mapPath = secondMapPath;
            break;
            case 2: mapPath = thirdMapPath;
            break;
        }

        switch (difficultyNumber)
        {
            case 0: difficultySettings = settings.get(0);
                break;
            case 1: difficultySettings = settings.get(1);
                break;
            case 2: difficultySettings = settings.get(2);
                break;
        }

    }

    public String getMapPath() {
        return mapPath;
    }

    public int getCarLimit(){
        return  difficultySettings.carLimit;
    }

    public int getCarsStartingAmount(){
        return  difficultySettings.carsStartingAmount;
    }

    public float getMax_level_time(){
        return  difficultySettings.max_level_time;
    }

    public float getTime_between_car_spawn(){
        return  difficultySettings.time_between_car_spawn;
    }
}
