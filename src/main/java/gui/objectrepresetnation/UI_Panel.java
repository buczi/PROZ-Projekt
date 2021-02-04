package gui.objectrepresetnation;

import gui.event.InputEvent;
import gui.event.UI_Events;
import util.SPair;
import vehicle.Car;

import javax.swing.*;
import java.awt.*;

public class UI_Panel extends JPanel implements UI_Events, InputEvent {

    private final JLabel currentPoints;
    private final JLabel currentCars;
    private final JLabel currentTime;
    private final JLabel currentInputs;
    private final JLabel currentCarsAmount;

    private final JPanel carInfo;
    private int carId;

    private static int userInputs = 0;
    private static int carsFinished = 0;

    public UI_Panel() {
        setLayout(null);
        setBounds(1000, 40, 300, 600);
        setBackground(Color.darkGray);

        JLabel points = new JLabel("POINTS", SwingConstants.CENTER);
        points.setBounds(25, 50, 100, 30);
        points.setBackground(Color.black);
        points.setOpaque(true);

        currentPoints = new JLabel("0", SwingConstants.CENTER);
        currentPoints.setBounds(25, 100, 100, 30);
        currentPoints.setBackground(Color.white);
        currentPoints.setOpaque(true);

        JLabel inputs = new JLabel("INPUTS", SwingConstants.CENTER);
        inputs.setBounds(175, 50, 100, 30);
        inputs.setBackground(Color.black);
        inputs.setOpaque(true);

        currentInputs = new JLabel("0", SwingConstants.CENTER);
        currentInputs.setBounds(175, 100, 100, 30);
        currentInputs.setBackground(Color.white);
        currentInputs.setOpaque(true);

        JLabel cars = new JLabel("CARS", SwingConstants.CENTER);
        cars.setBounds(25, 175, 100, 30);
        cars.setBackground(Color.black);
        cars.setOpaque(true);

        currentCars = new JLabel("0", SwingConstants.CENTER);
        currentCars.setBounds(25, 225, 100, 30);
        currentCars.setBackground(Color.white);
        currentCars.setOpaque(true);

        JLabel time = new JLabel("TIME", SwingConstants.CENTER);
        time.setBounds(175, 175, 100, 30);
        time.setBackground(Color.black);
        time.setOpaque(true);

        currentTime = new JLabel("0", SwingConstants.CENTER);
        currentTime.setBounds(175, 225, 100, 30);
        currentTime.setBackground(Color.white);
        currentTime.setOpaque(true);

        JLabel carsAmount = new JLabel("Cars-Finished", SwingConstants.CENTER);
        carsAmount.setBounds(100, 275, 100, 30);
        carsAmount.setBackground(Color.black);
        carsAmount.setOpaque(true);

        currentCarsAmount = new JLabel("0", SwingConstants.CENTER);
        currentCarsAmount.setBounds(100, 325, 100, 30);
        currentCarsAmount.setBackground(Color.white);
        currentCarsAmount.setOpaque(true);

        carInfo = new JPanel();

        add(points);
        add(currentPoints);
        add(cars);
        add(currentCars);
        add(time);
        add(currentTime);
        add(inputs);
        add(currentInputs);
        add(carsAmount);
        add(currentCarsAmount);
        add(carInfo);

    }

    @Override
    public void onPointsChange(String newPoints) {
        carsFinished++;
        currentCarsAmount.setText(Integer.toString(carsFinished));
        currentPoints.setText(newPoints);
    }

    @Override
    public void onCarsChange(String newCars) {
        currentCars.setText(newCars);
    }

    @Override
    public void onTimeChangeChange(String newTime) {
        newTime = newTime.substring(0, newTime.indexOf('.'));
        currentTime.setText(newTime);
    }

    @Override
    public void onInputsChange(String newInput) {
        currentInputs.setText(newInput);
    }

    @Override
    public void addGCar(Car car, GCar gcar) {
        if (carInfo != new JPanel()) {//!=???
            for (Component comp : carInfo.getComponents())
                carInfo.remove(comp);
            carInfo.setVisible(true);
            this.repaint();
            this.revalidate();
        }
        int width = 90;
        int height = 60;
        carInfo.setLayout(null);
        carInfo.setBounds(25, 375, 250, 200);
        carId = car.getId();
        JLabel carTitle = new JLabel("CAR ID", SwingConstants.CENTER);
        carTitle.setBounds(50, 25, 50, 30);
        carTitle.setBackground(Color.black);
        carTitle.setOpaque(true);
        JLabel carIdL = new JLabel(Integer.toString(car.getId()), SwingConstants.CENTER);
        carIdL.setBounds(150, 25, 50, 30);
        carIdL.setBackground(Color.white);
        carIdL.setOpaque(true);
        JLabel carDestination = new JLabel("DESTINATION", SwingConstants.CENTER);
        carDestination.setBounds(150, 85, 75, 30);
        carDestination.setBackground(Color.black);
        carDestination.setOpaque(true);
        JLabel carDestinationI = new JLabel(car.getDestination().getX() + " : " + car.getDestination().getY(), SwingConstants.CENTER);
        carDestinationI.setBounds(150, 120, 75, 30);
        carDestinationI.setBackground(Color.white);
        carDestinationI.setOpaque(true);

        class CarImage extends Transform {
            public CarImage() {
                super();
            }

            @Override
            protected SPair<Integer> getSizeTransform() {
                return new SPair<>(width, height);
            }
        }
        CarImage carImage = new CarImage();
        carImage.setIcon(gcar.getIcon());
        carImage.image = gcar.image;

        Transform.resizeIcon(carImage, width, height);
        carImage.setBounds(25, 100, carImage.getWidth(), carImage.getHeight());

        carInfo.add(carTitle);
        carInfo.add(carIdL);
        carInfo.add(carDestination);
        carInfo.add(carDestinationI);
        carInfo.add(carImage);

        revalidate();
        repaint();
    }

    @Override
    public void remove(int id) {
        if (carId == id) {
            carInfo.setVisible(false);
            repaint();
            revalidate();
        }
    }

    public static int incrementInputs() {
        userInputs += 1;
        return userInputs;
    }
}
