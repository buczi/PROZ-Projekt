package gui.menu;

import java.util.*;

public class GameSettings {
    //IN ORDER TO CHANGE MAP CHANGE PATHS
    private static final String firstMapPath = "/maps/map.txt";
    private static final String secondMapPath = "/maps/Map1.txt";
    private static final String thirdMapPath = "/maps/map2.txt";

    private static class DifficultySettings {
        protected final int carLimit;
        protected final int carsStartingAmount;
        protected final float maxLevelTime;
        protected final float timeBetweenCarSpawn;

        protected DifficultySettings(int carLimitN, int carsStartingAmountN, float maxLevelTimeN, float timeBetweenCarSpawnN) {
            carLimit = carLimitN;
            carsStartingAmount = carsStartingAmountN;
            maxLevelTime = maxLevelTimeN;
            timeBetweenCarSpawn = timeBetweenCarSpawnN;
        }

    }

    //MODIFY DIFFICULTY BY CHANGING THIS VALUES
    private static final List<DifficultySettings> settings = new ArrayList<>() {{
        add(new DifficultySettings(1, 1, 180, 10));
        add(new DifficultySettings(3, 3, 360, 8));
        add(new DifficultySettings(10, 10, 480, 5));
    }};

    private String mapPath;
    private DifficultySettings difficultySettings;

    public GameSettings(int mapNumber, int difficultyNumber) {
        switch (mapNumber) {
            case 0:
                mapPath = firstMapPath;
                break;
            case 1:
                mapPath = secondMapPath;
                break;
            case 2:
                mapPath = thirdMapPath;
                break;
        }

        switch (difficultyNumber) {
            case 0:
                difficultySettings = settings.get(0);
                break;
            case 1:
                difficultySettings = settings.get(1);
                break;
            case 2:
                difficultySettings = settings.get(2);
                break;
        }

    }

    public String getMapPath() {
        return mapPath;
    }

    public int getCarLimit() {
        return difficultySettings.carLimit;
    }

    public int getCarsStartingAmount() {
        return difficultySettings.carsStartingAmount;
    }

    public float getMaxLevelTime() {
        return difficultySettings.maxLevelTime;
    }

    public float getTimeBetweenCarSpawn() {
        return difficultySettings.timeBetweenCarSpawn;
    }
}
