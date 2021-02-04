package vehicle.event;

import mvc.exception.MildException;
import gui.objectrepresetnation.GCar;
import map.*;
import util.*;

public interface CarGraphicEvents {
    void spawnCar(int id, int x, int y, Direction direction, int quePlace) throws MildException;
    void destroyCar(int id);
    void moveCar(int id,int speed, Direction direction);
    void rotate(int id, Direction direction);
    void enterIntersection(int id,SPair<Integer> position);
    GCar getGCarById(int id);
}
