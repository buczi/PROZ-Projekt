package Algorithm;

import Map.GridNode;
import Map.Map;
import Utils.*;

import java.util.Random;

public class EndPointFinder {
    private GridNode[][] map = null;
    private SPair<Integer> startPoint;
    private SPair<Integer> endPoint;

    private static int minDistance = (int) (Math.floor(Map._xSize / 2) + 1);

    public EndPointFinder(GridNode[][] map) {
        this.map = map;
        this.startPoint = new SPair<>();
        this.endPoint = new SPair<>();
        this.FindStartingPosition(Map._xSize, Map._ySize);
        this.FindEndingPosition(Map._xSize, Map._ySize);
    }

    public EndPointFinder(GridNode[][] map, SPair<Integer> start, SPair<Integer> end) {
        this.map = map;
        this.startPoint = start;
        this.endPoint = end;
    }

    private SPair<Integer> FindStartingPosition(int rangeX, int rangeY) {
        int attempt = 0;
        Random rand = new Random();
        while (attempt < 300) {
            startPoint.setX_(rand.nextInt(rangeX - 0));
            startPoint.setY_(rand.nextInt(rangeY - 0));
            if (!map[startPoint.getY_()][startPoint.getX_()].CheckIfEmpty() && !map[startPoint.getY_()][startPoint.getX_()].getIsWithoutLights())
                break;

            attempt++;
        }
        return startPoint;
    }

    private SPair<Integer> FindEndingPosition(int rangeX, int rangeY) {
        int attempt = 0;
        Random rand = new Random();
        while (attempt < 300) {
            endPoint.setX_(rand.nextInt(rangeX));
            endPoint.setY_(rand.nextInt(rangeY));
            if (!map[endPoint.getY_()][endPoint.getX_()].CheckIfEmpty()
                    && endPoint != startPoint && DistanceGreaterThan(startPoint, endPoint, minDistance))
                break;

            attempt++;
        }
        return endPoint;
    }

    private boolean DistanceGreaterThan(SPair<Integer> a, SPair<Integer> b, int distance) {
        return (Math.abs(a.getX_() - b.getX_()) + Math.abs(a.getY_() - b.getY_()) >= distance);
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
