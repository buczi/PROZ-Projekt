package GUI.ObjectRepresentation;

import Map.GridNode;
import Map.*;
import Utils.DataSet;
import Utils.SPair;

import javax.swing.*;
import java.io.DataInput;
import java.io.IOException;

public class GTrafficLights extends Transform{
    private final static String imageName_NS = "LightsNS.png";
    private final static String imageName_WE = "LightsWE.png";
    private String currentImage;
    private final static int lightsSide = 64;

    public GTrafficLights(int spawnX, int spawnY, Direction direction)
    {
        this.layer = Layer.Lights;
        this.currentAngle = 0;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        try{
            if(direction == Direction.North || direction == Direction.South)
            {
                LoadImageToJLabel(this,pathToImageFolder + imageName_NS);
                this.currentImage = imageName_NS;
            }
            else
            {
                LoadImageToJLabel(this,pathToImageFolder + imageName_WE);
                this.currentImage = imageName_WE;
            }
        }
        catch (IOException e)
        {

        }
        this.UpdateImageCenter();
    }

    public void ChangeLights()
    {
        try{
            if(this.currentImage == imageName_WE)
            {
                LoadImageToJLabel(this,pathToImageFolder + imageName_NS);
                this.currentImage = imageName_NS;
            }
            else
            {
                LoadImageToJLabel(this,pathToImageFolder + imageName_WE);
                this.currentImage = imageName_WE;
            }
        }
        catch (IOException e)
        {

        }
        repaint();
    }

    public static GTrafficLights[][] GenerateMap(DataSet[][] d_map, int spawnX, int spawnY, int streetSize, JFrame frame)
    {
        int y = Map.get_ySize();
        int x = Map.get_xSize();
        GTrafficLights[][] g_map = new GTrafficLights[y][x];
        for (int j = 0; j < y; j++)
            for (int i = 0; i < x; i++) {                //TODO
                if(d_map[j][i] != null)
                    if(d_map[j][i].isLights())
                        g_map[j][i] = new GTrafficLights(spawnX + i*(streetSize + lightsSide) -4*i,spawnY + j*(streetSize + lightsSide) - 4*j,Direction.North);
                    else
                        g_map[j][i] = null;

                if(g_map[j][i] != null)
                {
                    frame.add(g_map[j][i]);
                    frame.getLayeredPane().add(g_map[j][i], Layer.Lights);
                }

            }

        return g_map;
    }

    @Override
    protected SPair<Integer> getSizeTransform()
    {
        return new SPair<>(lightsSide,lightsSide);
    }
}
