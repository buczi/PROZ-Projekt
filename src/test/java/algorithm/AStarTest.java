package algorithm;

import gui.event.UI_Events;
import gui.objectrepresetnation.GCar;
import map.*;
import mvc.exception.CriticalException;
import org.junit.Assert;
import org.junit.Test;
import vehicle.Car;

import java.io.File;

public class AStarTest {
    @Test
    public void Test(){
        int times = 5;
        Map map = new Map();
        try {
            map = new Map(times, times, 60, 15, new File("").getAbsolutePath() + "/src/main/resources/maps/map.txt");
            map.addUIListener(new UI_Events() {
                @Override
                public void onPointsChange(String newPoints) {

                }

                @Override
                public void onCarsChange(String newCars) {

                }

                @Override
                public void onTimeChangeChange(String newTime) {

                }

                @Override
                public void addGCar(Car car, GCar gcar) {

                }

                @Override
                public void remove(int id) {

                }
            });
            Map.setTEST(true);
            map.SpawnCar();

        }
        catch (CriticalException e){
            System.out.println(e.getCriticalExceptionTrace());
        }
        Assert.assertEquals(times,map.getCars().size());
    }
}
