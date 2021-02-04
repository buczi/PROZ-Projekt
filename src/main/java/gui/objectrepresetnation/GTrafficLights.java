package gui.objectrepresetnation;

import mvc.exception.CriticalException;
import mvc.exception.ExceptionType;
import map.*;
import util.DataSet;
import util.SPair;

import javax.swing.*;
import java.io.IOException;

public class GTrafficLights extends Transform {
    private final static String imageNameNS = "LightsNS.png";
    private final static String imageNameWE = "LightsWE.png";
    private String currentImage;
    private final static int lightsSide = 64;

    public GTrafficLights(int spawnX, int spawnY, Direction direction) throws CriticalException{
        this.currentAngle = 0;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        try {
            if (direction == Direction.North || direction == Direction.South) {
                loadImageToJLabel(this, pathToImageFolder + imageNameNS);
                this.currentImage = imageNameNS;
            } else {
                loadImageToJLabel(this, pathToImageFolder + imageNameWE);
                this.currentImage = imageNameWE;
            }
        } catch (IOException e) {
            if (direction == Direction.North || direction == Direction.South)
                throw new CriticalException("Error while loading file: " + pathToImageFolder + imageNameNS,this.getClass().getName(), ExceptionType.LOAD_MAP_EXCEPTION);
            else
                throw new CriticalException("Error while loading file: " + pathToImageFolder + imageNameWE,this.getClass().getName(), ExceptionType.LOAD_MAP_EXCEPTION);
        }
        this.updateImageCenter();
    }

    public void changeLights() {
        try {
            if (this.currentImage.equals(imageNameWE)) {
                loadImageToJLabel(this, pathToImageFolder + imageNameNS);
                this.currentImage = imageNameNS;
            } else {
                loadImageToJLabel(this, pathToImageFolder + imageNameWE);
                this.currentImage = imageNameWE;
            }
        } catch (IOException e) {
            // If the picture is already loaded there is no need to propagate this exception
            System.out.println("[USER]: Check if any of the files wasn't removed during game :" + pathToImageFolder);
            System.out.println(e.getMessage());
        }
        repaint();
    }

    public static GTrafficLights[][] generateMap(DataSet[][] dMap, int spawnX, int spawnY, int streetSize, JFrame frame) throws CriticalException{
        int y = Map.getYSize();
        int x = Map.getXSize();
        GTrafficLights[][] gMap = new GTrafficLights[y][x];
        for (int j = 0; j < y; j++)
            for (int i = 0; i < x; i++) {
                if (dMap[j][i] != null)
                    if (dMap[j][i].isLights())
                        gMap[j][i] = new GTrafficLights(spawnX + i * (streetSize + lightsSide) - 4 * i, spawnY + j * (streetSize + lightsSide) - 4 * j, Direction.North);
                    else
                        gMap[j][i] = null;

                if (gMap[j][i] != null) {
                    frame.add(gMap[j][i]);
                    frame.getLayeredPane().add(gMap[j][i], 200);
                }

            }

        return gMap;
    }

    @Override
    protected SPair<Integer> getSizeTransform() {
        return new SPair<>(lightsSide, lightsSide);
    }
}
