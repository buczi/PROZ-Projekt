package gui.objectrepresetnation;

import mvc.exception.CriticalException;
import mvc.exception.MildException;

import map.Direction;
import map.Map;
import util.*;
import gui.objectrepresetnation.event.UI_notification;

import javax.swing.*;
import java.util.*;

public class GMap {
    private final GGridNode[][] gMap;
    private final List<Pair<Integer, GCar>> keyList;

    public GMap(boolean[][] bMap, DataSet[][] dMap, JFrame frame) throws CriticalException {
        int y = 50;//STARTING POINT OF MAP
        int x = 50;
        this.gMap = GGridNode.generateMap(bMap, dMap, x, y, GStreet.getSizeX(), frame);
        this.keyList = new LinkedList<>();
    }

    public void changeLights(int y, int x) {
        if (y >= 0 && y < Map.ySize && x >= 0 && x < Map.xSize && GGridNode.getGLights()[y][x] != null)
            GGridNode.getGLights()[y][x].changeLights();
    }

    public void addCar(int id, int x, int y, Direction direction,int quePlace, JFrame frame, UI_notification notification) throws MildException {
        int side = GGridNode.getIntersectionSide();
        int spawnX = 50 + y * (GStreet.getSizeX() + GGridNode.getIntersectionSide());
        int spawnY = 50 + x * (GStreet.getSizeX() + GGridNode.getIntersectionSide());
        switch (direction) {
            case North:
                spawnX += side / 2 + 5;
                spawnY += side + 5;
                if(quePlace > 1)
                    spawnY += (quePlace - 1) * 40;
                break;
            case South:
                spawnX += 5;
                spawnY -= 35;
                if(quePlace > 1)
                    spawnY -= (quePlace - 1) * 40;
                break;
            case West:
                spawnX += side + 5;
                spawnY += 5;
                if(quePlace > 1)
                    spawnX += (quePlace - 1) * 40;
                break;
            case East:
                spawnX -= (30 + 5);
                spawnY += side / 2 + 5;
                if(quePlace > 1)
                    spawnX -= (quePlace - 1) * 40;
                break;
        }
        GCar carToAdd = new GCar(spawnX, spawnY, direction, notification, id);
        keyList.add(new Pair<>(id, carToAdd));
        frame.getLayeredPane().add(keyList.get(keyList.size() - 1).getY(), 100);
    }

    public void moveCar(int id, int speed, Direction side) {
        GCar car = getCarByID(id);
        if (car == null)
            return;

        float distanceX = 0;
        float distanceY = 0;
        float deltaTime = TimeController.getInstance().getDeltaTime();
        switch (side) {
            case North:
                distanceY = -speed * deltaTime;
                break;
            case South:
                distanceY = speed * deltaTime;
                break;
            case West:
                distanceX = -speed * deltaTime;
                break;
            case East:
                distanceX = speed * deltaTime;
                break;
        }
        Transform.moveIcon(car, (int) (car.getX() + distanceX), (int) (car.getY() + distanceY));
    }

    public void rotate(int id, Direction direction) {
        GCar car = getCarByID(id);
        if (car == null)
            return;
        car.rotateImageToDirection(car.getRotated(), direction);
    }

    public void destroyCar(int id, JFrame frame) {
        frame.getLayeredPane().remove(getCarByID(id));
        removeFromKeyList(id);
        frame.revalidate();
        frame.repaint();
    }

    public static List<SPair<Integer>> getNodesToNotify() {
        return GGridNode.nodesToNotify;
    }

    public GCar getCarByID(int id) {
        for (Pair<Integer, GCar> item : keyList) {
            if (item.getX() == id)
                return item.getY();
        }
        return null;
    }

    public void removeFromKeyList(int id) {
        for (Pair<Integer, GCar> item : keyList) {
            if (item.getX() == id) {
                keyList.remove(item);
                break;
            }
        }
    }

    public GGridNode getNode(SPair<Integer> pair) {
        return gMap[pair.getX()][pair.getY()];
    }
}
