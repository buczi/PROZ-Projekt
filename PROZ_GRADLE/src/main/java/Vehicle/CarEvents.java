package Vehicle;

public interface CarEvents {
     void OnCarAccident(Car car);
     void OnLifeTimeEnd(Car car);
     void OnSuccessfulJourneyEnd(Car car,float points);
}
