package app;

import map.*;
import mvc.Application;
import mvc.Controller;
import mvc.exception.CriticalException;
import org.junit.Assert;
import org.junit.Test;
import util.Pair;
import vehicle.Car;
import vehicle.event.CarEvents;

import java.util.LinkedList;
import java.util.List;

public class AppTest {

    @Test
    public void appTest(){
        Application app = new Application();
        try{
            app.setController1(new Controller(true,app));
        }
        catch(CriticalException exception){
            System.out.println(exception.getCriticalExceptionTrace());
            try{
                app.getController1().join();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        Assert.assertNotNull(app.getController1());
    }

    @Test
    public void idTest(){
        int prevId = Coords.getObjId();
        Car car = new Car(50, new Coords(new Pair<>(0, 0), Direction.North),
                new Street(new Coords(new Pair<>(0, 0), Direction.North), new GridNode(new Coords(new Pair<>(0, 0), Direction.North))
                        , new GridNode(new Coords(new Pair<>(0, 0), Direction.North)), 5f),
                new GridNode(new Coords(new Pair<>(0, 0), Direction.North)),
                new LinkedList<>(), new CarEvents() {
            @Override
            public void onCarAccident(Car car) {

            }

            @Override
            public void onLifeTimeEnd(Car car) {

            }

            @Override
            public void onSuccessfulJourneyEnd(Car car, float points) {

            }
        });
        Assert.assertNotEquals(prevId, car.getId());
    }

    @Test
    public void distinctIdTest(){
        List<Car> cars = new LinkedList<>();
        for(int i = 0; i < 10; i++)
        cars.add( new Car(50, new Coords(new Pair<>(0, 0), Direction.North),
                new Street(new Coords(new Pair<>(0, 0), Direction.North), new GridNode(new Coords(new Pair<>(0, 0), Direction.North))
                        , new GridNode(new Coords(new Pair<>(0, 0), Direction.North)), 5f),
                new GridNode(new Coords(new Pair<>(0, 0), Direction.North)),
                new LinkedList<>(), new CarEvents() {
            @Override
            public void onCarAccident(Car car) {

            }

            @Override
            public void onLifeTimeEnd(Car car) {

            }

            @Override
            public void onSuccessfulJourneyEnd(Car car, float points) {

            }
        }));
        for(int j = 0; j < cars.size(); j++)
            for(int i = 0; i < cars.size(); i++)
                if(i != j)
                    Assert.assertNotEquals(cars.get(i).getId(), cars.get(j).getId());
    }

}
