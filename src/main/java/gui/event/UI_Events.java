package gui.event;

import gui.objectrepresetnation.event.UI_CarEvent;

public interface UI_Events extends UI_CarEvent {
    void onPointsChange(String newPoints);

    void onCarsChange(String newCars);

    void onTimeChangeChange(String newTime);
}