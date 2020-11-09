package GUI.ObjectRepresentation;

import GUI.InputEvent;
import Map.Coords;
import Map.*;
import Utils.DataSet;
import Utils.Pair;
import Utils.SPair;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class GGridNode extends Transform {
    private final static String imageName = "Crossing.png";
    private final static int intersectionSide = 60;

    protected static List<SPair<Integer>> nodesToNotify = new LinkedList<SPair<Integer>>();
    private static InputEvent inputListener = null;

    private int _x;
    private int _y;


    private static GTrafficLights[][]g_lights = new GTrafficLights[Map._ySize][Map._xSize];

    public GGridNode(int spawnX, int spawnY, int _y, int _x)
    {
        this.layer = Layer.Graph;
        this.currentAngle = 0;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        this._x = _x;
        this._y = _y;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(g_lights[_y][_x] != null)
                {
                    g_lights[_y][_x].ChangeLights();
                    nodesToNotify.add(new SPair<>(_x,_y));
                }
                if(inputListener != null)
                    inputListener.OnInputsChange(Integer.toString(UI_Panel.incrementInputs()));
            }
        });

        try{
            LoadImageToJLabel(this,pathToImageFolder + imageName);
        }
        catch (IOException e)
        {

        }
        this.UpdateImageCenter();
    }

    public static GGridNode[][] GenerateMap(boolean[][] map, DataSet[][]d_map, int spawnX, int spawnY, int streetSize, JFrame frame)
    {
        int y = Map.get_ySize();
        int x = Map.get_xSize();
        int _x = 0;
        int _y = 0;
        GGridNode[][] g_map = new GGridNode[y][x];
        g_lights = GTrafficLights.GenerateMap(d_map,spawnX - 2, spawnY - 2,GStreet.getSizeX(),frame);

        for (int j = 0; j < y; j++)
            for (int i = 0; i < x; i++) {
                _x = spawnX + i*(streetSize + intersectionSide);
                _y = spawnY + j*(streetSize + intersectionSide);

                if(map[j][i])
                    g_map[j][i] = new GGridNode(_x,_y,j,i);

                if(g_map[j][i] != null)
                {
                    frame.add(g_map[j][i]);

                    if(d_map[j][i].getN().getX_())
                        frame.add(new GStreet(_x, _y -GStreet.getSizeX(),Direction.South));

                    if(d_map[j][i].getN().getY_())
                        frame.add(new GStreet(_x + intersectionSide/2, _y -GStreet.getSizeX(),Direction.North));

                    if(d_map[j][i].getE().getX_())
                        frame.add(new GStreet(_x + intersectionSide, _y ,Direction.West));

                    if(d_map[j][i].getE().getY_())
                        frame.add(new GStreet(_x + intersectionSide, _y + intersectionSide/2,Direction.East));

/*

                    if(d_map[j][i].getS().getX_())
                        frame.add(new GStreet(_x + intersectionSide/2, _y + intersectionSide ,Direction.North));

                    if(d_map[j][i].getS().getY_())
                        frame.add(new GStreet(_x , _y + intersectionSide,Direction.South));

                    if(d_map[j][i].getW().getX_())
                        frame.add(new GStreet(_x -GStreet.getSizeX(), _y,Direction.West));

                    if(d_map[j][i].getW().getY_())
                        frame.add(new GStreet(_x -GStreet.getSizeX(), _y + intersectionSide/2,Direction.East));
 */
                }

            }

        return g_map;
    }

    @Override
    protected SPair<Integer> getSizeTransform()
    {
        return new SPair<>(intersectionSide,intersectionSide);
    }

    public static int getIntersectionSide() {
        return intersectionSide;
    }

    protected static GTrafficLights[][] getG_lights(){
        return g_lights;
    }

    public static void AddInputListener(InputEvent listener)
    {
       if( inputListener == null)
           inputListener = listener;
    }


}
