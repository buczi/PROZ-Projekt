package GUI.ObjectRepresentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import Map.Direction;
import Utils.*;

public class GCar extends Transform {

    private final static ArrayList<Pair<String,Integer>> carColor = new ArrayList<Pair<String,Integer>>(){{add(new Pair<String,Integer>("R",0));
        add(new Pair<String,Integer>("Y",1));
        add(new Pair<String,Integer>("B",2));
        add(new Pair<String,Integer>("G",3));
    }};

    private final static String imageName = "Car_";
    private final static String imageExtention = ".png";

    private int sizeX = 30;
    private int sizeY = 20;


    public GCar(int spawnX, int spawnY, Direction direction)
    {
        this.layer = Layer.Car;
        this.currentAngle = 270;
        this.axis = true;
        this.spawnX = spawnX;
        this.spawnY = spawnY;


        try{
            LoadImageToJLabel(this,pathToImageFolder + imageName + GenerateColor() + imageExtention);
        }
        catch (IOException e)
        {

        }

        rotateImageToDirection(image,direction);
        this.UpdateImageCenter();
        this.rotated_IN_USE = true;
    }


    private String GenerateColor()
    {
        Random rand = new Random();
        if(carColor.size() == 0)
            return "";

        switch (rand.nextInt(carColor.size()))
        {
            case 0: return  carColor.get(0).getX_();

            case 1: return  carColor.get(1).getX_();

            case 2: return  carColor.get(2).getX_();

            case 3: return  carColor.get(3).getX_();
        }

        return  carColor.get(0).getX_();
    }


    @Override
    protected SPair<Integer> getSizeTransform()
    {
        return new SPair<>(sizeX,sizeY);
    }

    protected SPair<Integer> getImageSize()
    {
        return new SPair<>(sizeX,sizeY);
    }


    @Override
    protected void setSizeTransform()
    {
        int temp = sizeX;
        sizeX = sizeY;
        sizeY = temp;
    }
}
