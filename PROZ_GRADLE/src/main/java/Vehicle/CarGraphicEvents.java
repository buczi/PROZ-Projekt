package Vehicle;

import Map.*;

public interface CarGraphicEvents {
    void SpawnCar(int id, int x , int y, Direction side,Direction direction);
    void DespawnCar(int id);
    void MoveCar(int id,int speed, Direction direction);
    void Rotate(int id, Direction direction);
}
