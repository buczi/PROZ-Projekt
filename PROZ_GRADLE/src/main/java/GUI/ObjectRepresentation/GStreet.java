package GUI.ObjectRepresentation;

import Map.Direction;
import Utils.SPair;

import javax.swing.*;
import java.io.IOException;

public class GStreet extends Transform{
    private final static String imageName = "Street.png";
    private final static int sizeX = 140;
    private final static int sizeY = GGridNode.getIntersectionSide()/2;

    public GStreet(int spawnX, int spawnY, Direction direction)
    {
        this.layer = Layer.Graph;
        this.currentAngle = 270;
        this.spawnX = spawnX;
        this.spawnY = spawnY;



        try{
            LoadImageToJLabel(this,pathToImageFolder + imageName);
        }
        catch (IOException e)
        {
            System.out.println(pathToImageFolder + imageName);
        }

        this.rotated = this.image;
        this.rotateImageToDirection(this.image,direction);

        this.UpdateImageCenter();

    }

    @Override
    protected SPair<Integer> getSizeTransform()
    {
        return new SPair<>(sizeX,sizeY);
    }

    public static int getSizeX() {
        return sizeX;
    }
}
