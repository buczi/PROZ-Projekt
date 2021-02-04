package gui.objectrepresetnation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

import mvc.exception.ExceptionType;
import mvc.exception.MildException;
import map.Direction;
import util.*;
import gui.objectrepresetnation.event.UI_notification;

public class GCar extends Transform {

    private final static List<Pair<String, Integer>> carColor = new ArrayList<>() {{
        add(new Pair<>("R", 0));//R Y B G
        add(new Pair<>("Y", 1));
        add(new Pair<>("B", 2));
        add(new Pair<>("G", 3));
    }};

    private final static String imageName = "Car_";
    private final static String imageExtension = ".png";

    private int sizeX = 30;
    private int sizeY = 20;

    private boolean onIntersection;
    private SPair<Integer> currentIntersection;

    private final static int bigMargin = 30;
    private final static int smallMargin = 5;


    public GCar(int spawnX, int spawnY, Direction direction, UI_notification listener, int id) throws MildException {
        this.currentAngle = 270;
        this.axis = true;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.onIntersection = false;
        this.currentIntersection = new SPair<>();

        String temporaryColor = generateColor();
        try {
            loadImageToJLabel(this, pathToImageFolder + imageName + temporaryColor + imageExtension);
        } catch (IOException e) {
            throw new MildException("UNABLE TO LOAD CAR FROM PICTURE " + pathToImageFolder + imageName + temporaryColor + imageExtension,
                    this.getClass().getName(), ExceptionType.CAR_GUI_EXCEPTION);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (TimeController.getInstance().isRunning())
                    listener.notifyController(id);
            }
        });

        rotateImageToDirection(image, direction);
        this.updateImageCenter();
        this.rotatedInUse = true;
    }


    private String generateColor() {
        Random rand = new Random();
        if (carColor.size() == 0)
            return "";

        switch (rand.nextInt(carColor.size())) {
            case 0:
                return carColor.get(0).getX();

            case 1:
                return carColor.get(1).getX();

            case 2:
                return carColor.get(2).getX();

            case 3:
                return carColor.get(3).getX();
        }

        return carColor.get(0).getX();
    }


    @Override
    protected SPair<Integer> getSizeTransform() {
        return new SPair<>(sizeX, sizeY);
    }

    @Override
    protected void setSizeTransform() {
        int temp = sizeX;
        sizeX = sizeY;
        sizeY = temp;
    }

    public boolean checkIfLeftIntersection() {
        if (!(this.getX() < currentIntersection.getY() && this.getX() > currentIntersection.getY() + GGridNode.getIntersectionSide() &&
                this.getY() < currentIntersection.getX() && this.getY() > currentIntersection.getX() + GGridNode.getIntersectionSide()) && onIntersection) {

            if (this.getX() >= currentIntersection.getY() + GGridNode.getIntersectionSide()) {
                this.setBounds((currentIntersection.getY() + GGridNode.getIntersectionSide()),
                        (currentIntersection.getX() + GGridNode.getIntersectionSide() / 2) + smallMargin,
                        sizeX, sizeY);
                return onIntersection = false;
            } else if (this.getX() < currentIntersection.getY() - bigMargin) {
                this.setBounds((currentIntersection.getY() - bigMargin),
                        currentIntersection.getX() + smallMargin,
                        sizeX, sizeY);
                return onIntersection = false;
            } else if (this.getY() >= currentIntersection.getX() + GGridNode.getIntersectionSide()) {
                this.setBounds((currentIntersection.getY() + smallMargin),
                        (currentIntersection.getX() + GGridNode.getIntersectionSide()),
                        sizeX, sizeY);
                return onIntersection = false;
            } else if (this.getY() < currentIntersection.getX() - bigMargin) {
                this.setBounds((currentIntersection.getY() + GGridNode.getIntersectionSide() / 2 + smallMargin),
                        currentIntersection.getX() - bigMargin,
                        sizeX, sizeY);
                return onIntersection = false;
            }
        }
        return true;
    }

    public void setOnIntersection(boolean val, SPair<Integer> position) {
        this.onIntersection = val;
        this.currentIntersection = position;
    }

    public void adjustPosition(Direction direction) {
        switch (direction) {
            case North:
                this.setBounds(currentIntersection.getY() + GGridNode.getIntersectionSide() / 2 + smallMargin,
                        getY() - 10,
                        sizeX, sizeY);
                break;
            case South:
                this.setBounds(currentIntersection.getY() + smallMargin,
                        getY() + 10,
                        sizeX, sizeY);
                break;
            case East:
                this.setBounds(getX() + 2 * smallMargin,
                        currentIntersection.getX() + GGridNode.getIntersectionSide() / 2 + smallMargin,
                        sizeX, sizeY);
                break;
            case West:
                this.setBounds(getX() - 2 * smallMargin,
                        currentIntersection.getX() + smallMargin,
                        sizeX, sizeY);
                break;
        }
    }

    public void setCurrentIntersection(SPair<Integer> pair) {
        this.currentIntersection = pair;
    }

    public boolean getOnIntersection() {
        return onIntersection;
    }
}
