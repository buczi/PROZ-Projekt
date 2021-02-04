package gui.objectrepresetnation;

import mvc.exception.CriticalException;
import mvc.exception.ExceptionType;
import map.Direction;
import util.SPair;

import java.io.IOException;

public class GStreet extends Transform {
    private final static String imageName = "Street.png";
    private final static int sizeX = 140;
    private final static int sizeY = GGridNode.getIntersectionSide() / 2;

    public GStreet(int spawnX, int spawnY, Direction direction) throws CriticalException {
        this.currentAngle = 270;
        this.spawnX = spawnX;
        this.spawnY = spawnY;


        try {
            loadImageToJLabel(this, pathToImageFolder + imageName);
        } catch (IOException e) {
            throw new CriticalException("Error while loading file: " + pathToImageFolder + imageName,this.getClass().getName(), ExceptionType.LOAD_MAP_EXCEPTION);
        }

        this.rotated = this.image;
        this.rotateImageToDirection(this.image, direction);

        this.updateImageCenter();

    }

    @Override
    protected SPair<Integer> getSizeTransform() {
        return new SPair<>(sizeX, sizeY);
    }

    public static int getSizeX() {
        return sizeX;
    }
}
