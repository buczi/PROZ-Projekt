package gui.objectrepresetnation.event;

import vehicle.*;
import gui.objectrepresetnation.GCar;

public interface UI_CarEvent {
    void addGCar(Car car, GCar gcar);

    void remove(int id);
}

