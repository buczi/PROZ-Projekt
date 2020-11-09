package GUI.ObjectRepresentation;

import Utils.DataSet;

import Map.Direction;
import Map.Map;
import Utils.*;
import Utils.TimeController;

import javax.swing.*;
import java.util.*;

public class GMap {
    private GGridNode[][]g_map;
    private List<GCar>cars;
    private List<Pair<Integer,GCar>>keylist;

    //STARTING POINT OF MAP
    private final int _X = 50;
    private final int _Y = 50;

    public GMap(boolean[][] b_map, DataSet[][]d_map, JFrame frame)
    {
        this.g_map = GGridNode.GenerateMap(b_map,d_map,_X,_Y,GStreet.getSizeX(),frame);
        this.cars = new LinkedList<>();
        this.keylist = new LinkedList<>();
    }

    public void ChangeLights(int y, int x)
    {
        if( y >= 0 && y < Map._ySize && x >= 0 && x < Map._xSize && GGridNode.getG_lights()[y][x] != null)
            GGridNode.getG_lights()[y][x].ChangeLights();
    }

    public void AddCar(int id,int x , int y,Direction direction, JFrame frame)
    {
        //                _x = spawnX + i*(streetSize + intersectionSide);
        //                _y = spawnY + j*(streetSize + intersectionSide);
        int side = GGridNode.getIntersectionSide();
        int spawnX = 50 + y*(GStreet.getSizeX() + GGridNode.getIntersectionSide());
        int spawnY = 50 + x*(GStreet.getSizeX() + GGridNode.getIntersectionSide());
        switch( direction)
        {
            case North: spawnX += side/2 + 5;
                spawnY += side + 5;
                break;
            case South: spawnX += 5;
                spawnY -= 35;
                break;
            case West:  spawnX += side + 5;
                spawnY += 5;
                break;
            case East:  spawnX -= (30 + 5);
                spawnY += side/2 + 5;
                break;
        }

        keylist.add(new Pair<Integer,GCar>(id,new GCar(spawnX,spawnY,direction)));
        frame.getLayeredPane().add(keylist.get(keylist.size() - 1).getY_(),100);
    }

    public void MoveCar(int id,int speed, Direction side)
    {
        GCar car = getCarByID(id);
        if(car == null)
            return;

        float distanceX = 0;
        float distanceY = 0;
        float deltaTime = TimeController.getInstance().getDeltaTime();
        switch (side)
        {
            case North: distanceY = -speed * deltaTime;
            break;
            case South: distanceY = speed * deltaTime;
                break;
            case West: distanceX = -speed * deltaTime;
                break;
            case East: distanceX = speed * deltaTime;
                break;
        }
        //Transform.MoveIcon(cars.get(id),(int)(cars.get(id).getX() + distanceX), (int)(cars.get(id).getY() + distanceY));
        Transform.MoveIcon(car,(int)(car.getX() + distanceX), (int)(car.getY() + distanceY));
    }

    public void Rotate(int id, Direction direction)
    {
        GCar car = getCarByID(id);
        if(car == null)
            return;
        //cars.get(id).rotateImageToDirection(cars.get(0).getRotated(),direction);
        car.rotateImageToDirection(car.getRotated(),direction);
    }

    public void DespawnCar(int id, JFrame frame)
    {
        frame.getLayeredPane().remove(getCarByID(id));
        RemoveFromKeyList(id);
        frame.revalidate();
        frame.repaint();
    }

    public static List<SPair<Integer>> getNodesToNotify() {
        return GGridNode.nodesToNotify;
    }

    public GCar getCarByID(int id)
    {
        for(Pair<Integer,GCar>item : keylist)
        {
            if(item.getX_() == id)
                return item.getY_();
        }
        return null;
    }

    public void RemoveFromKeyList(int id)
    {
        for(Pair<Integer,GCar>item : keylist)
        {
            if(item.getX_() == id) {
                keylist.remove(item);
                break;
            }
        }
    }
}
