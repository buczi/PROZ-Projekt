package algorithm;

import map.GridNode;
import map.Map;
import util.*;

import java.util.Random;

public class EndPointFinder {
    private final GridNode[][] map;
    private final SPair<Integer> startPoint;
    private final SPair<Integer> endPoint;

    private final static int max_tries = 300;
    private final static int minDistance = (int) (Math.floor((float) Map.xSize / 2) + 1);

    public EndPointFinder(GridNode[][] map) {
        this.map = map;
        this.startPoint = new SPair<>();
        this.endPoint = new SPair<>();
        this.findStartingPosition();
        this.findEndingPosition();
    }

    public EndPointFinder(GridNode[][] map, SPair<Integer> start, SPair<Integer> end) {
        this.map = map;
        this.startPoint = start;
        this.endPoint = end;
    }

    private void findStartingPosition() {
        int attempt = 0;
        Random rand = new Random();
        while (attempt < max_tries) {
            startPoint.setX(rand.nextInt(Map.xSize));
            startPoint.setY(rand.nextInt(Map.ySize));
            if (!map[startPoint.getY()][startPoint.getX()].checkIfEmpty() && !map[startPoint.getY()][startPoint.getX()].getIsWithoutLights()
                    && map[startPoint.getY()][startPoint.getX()].queSize() <= 2)
                break;
            attempt++;
        }
    }

    private void findEndingPosition() {
        int attempt = 0;
        Random rand = new Random();
        while (attempt < max_tries) {
            endPoint.setX(rand.nextInt(Map.xSize));
            endPoint.setY(rand.nextInt(Map.ySize));
            if (!map[endPoint.getY()][endPoint.getX()].checkIfEmpty()
                    && endPoint != startPoint && distanceGreaterThan(startPoint, endPoint))
                break;

            attempt++;
        }
    }

    private boolean distanceGreaterThan(SPair<Integer> a, SPair<Integer> b) {
        return (Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) >= minDistance);
    }

    //GET
    public GridNode[][] getMap() {
        return map;
    }

    public SPair<Integer> getStartPoint() {
        return startPoint;
    }

    public SPair<Integer> getEndPoint() {
        return endPoint;
    }
}
