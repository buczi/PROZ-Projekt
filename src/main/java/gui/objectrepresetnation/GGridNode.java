package gui.objectrepresetnation;

import mvc.exception.CriticalException;
import mvc.exception.ExceptionType;
import gui.event.InputEvent;
import map.*;
import util.DataSet;
import util.SPair;
import util.TimeController;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class GGridNode extends Transform {
    private final static String imageName = "Crossing.png";
    private final static int intersectionSide = 60;

    protected static final List<SPair<Integer>> nodesToNotify = new LinkedList<>();
    private static InputEvent inputListener = null;


    private static GTrafficLights[][] gLights = new GTrafficLights[Map.ySize][Map.xSize];

    public GGridNode(int spawnX, int spawnY, int y, int x) throws CriticalException {
        this.currentAngle = 0;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (TimeController.getInstance().isRunning()) {
                    if (gLights[y][x] != null) {
                        gLights[y][x].changeLights();
                        nodesToNotify.add(new SPair<>(x, y));
                    }
                    if (inputListener != null)
                        inputListener.onInputsChange(Integer.toString(UI_Panel.incrementInputs()));
                }
            }
        });

        try {
            loadImageToJLabel(this, pathToImageFolder + imageName);
        } catch (IOException e) {
            throw new CriticalException("Error while loading file: " + pathToImageFolder + imageName,this.getClass().getName(), ExceptionType.LOAD_MAP_EXCEPTION);
        }
        this.updateImageCenter();
    }

    public static GGridNode[][] generateMap(boolean[][] map, DataSet[][] dMap, int spawnX, int spawnY, int streetSize, JFrame frame) throws CriticalException{
        int yT = Map.getYSize();
        int xT = Map.getXSize();
        int x, y;
        GGridNode[][] g_map = new GGridNode[yT][xT];
        gLights = GTrafficLights.generateMap(dMap, spawnX - 2, spawnY - 2, GStreet.getSizeX(), frame);

        for (int j = 0; j < yT; j++)
            for (int i = 0; i < xT; i++) {
                x = spawnX + i * (streetSize + intersectionSide);
                y = spawnY + j * (streetSize + intersectionSide);

                if (map[j][i])
                    g_map[j][i] = new GGridNode(x, y, j, i);

                if (g_map[j][i] != null) {
                    frame.add(g_map[j][i]);

                    if (dMap[j][i].getN().getX())
                        frame.add(new GStreet(x, y - GStreet.getSizeX(), Direction.South));

                    if (dMap[j][i].getN().getY())
                        frame.add(new GStreet(x + intersectionSide / 2, y - GStreet.getSizeX(), Direction.North));

                    if (dMap[j][i].getE().getX())
                        frame.add(new GStreet(x + intersectionSide, y, Direction.West));

                    if (dMap[j][i].getE().getY())
                        frame.add(new GStreet(x + intersectionSide, y + intersectionSide / 2, Direction.East));
                }

            }

        return g_map;
    }

    @Override
    protected SPair<Integer> getSizeTransform() {
        return new SPair<>(intersectionSide, intersectionSide);
    }

    public static int getIntersectionSide() {
        return intersectionSide;
    }

    protected static GTrafficLights[][] getGLights() {
        return gLights;
    }

    public static void addInputListener(InputEvent listener) {
        if (inputListener == null)
            inputListener = listener;
    }


}
