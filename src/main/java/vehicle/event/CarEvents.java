package vehicle.event;

import vehicle.Car;

public interface CarEvents {
     void onCarAccident(Car car);
     void onLifeTimeEnd(Car car);
     void onSuccessfulJourneyEnd(Car car, float points);
}
