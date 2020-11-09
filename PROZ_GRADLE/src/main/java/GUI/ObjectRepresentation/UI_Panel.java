package GUI.ObjectRepresentation;

import GUI.InputEvent;
import GUI.UI_Events;

import javax.swing.*;
import java.awt.*;

public class UI_Panel extends JPanel implements UI_Events, InputEvent {

    private JLabel points;
    private JLabel current_points;
    private JLabel cars;
    private JLabel current_cars;
    private JLabel time;
    private JLabel current_time;
    private JLabel inputs;
    private JLabel current_inputs;

    private static int userInputs = 0;

    public UI_Panel()
    {
        setLayout(null);
        setBounds(1000,40,300,600);
        setBackground(Color.darkGray);

        points = new JLabel("POINTS", SwingConstants.CENTER);
        points.setBounds(25,50,100,30);
        points.setBackground(Color.black);
        points.setOpaque(true);

        current_points = new JLabel("0", SwingConstants.CENTER);
        current_points.setBounds(25,100,100,30);
        current_points.setBackground(Color.white);
        current_points.setOpaque(true);

        inputs = new JLabel("INPUTS", SwingConstants.CENTER);
        inputs.setBounds(175,50,100,30);
        inputs.setBackground(Color.black);
        inputs.setOpaque(true);

        current_inputs = new JLabel("0", SwingConstants.CENTER);
        current_inputs.setBounds(175,100,100,30);
        current_inputs.setBackground(Color.white);
        current_inputs.setOpaque(true);

        cars = new JLabel("CARS", SwingConstants.CENTER);
        cars.setBounds(25,175,100,30);
        cars.setBackground(Color.black);
        cars.setOpaque(true);

        current_cars = new JLabel("0", SwingConstants.CENTER);
        current_cars.setBounds(25,225,100,30);
        current_cars.setBackground(Color.white);
        current_cars.setOpaque(true);

        time = new JLabel("TIME", SwingConstants.CENTER);
        time.setBounds(175,175,100,30);
        time.setBackground(Color.black);
        time.setOpaque(true);

        current_time = new JLabel("0", SwingConstants.CENTER);
        current_time.setBounds(175,225,100,30);
        current_time.setBackground(Color.white);
        current_time.setOpaque(true);


        add(points);
        add(current_points);
        add(cars);
        add(current_cars);
        add(time);
        add(current_time);
        add(inputs);
        add(current_inputs);

    }

    @Override
    public void OnPointsChange(String newPoints) {
        current_points.setText(newPoints);
    }

    @Override
    public void OnCarsChange(String newCars) {
        current_cars.setText(newCars);
    }

    @Override
    public void OnTimeChangeChange(String newTime) {
        newTime = newTime.substring(0,newTime.indexOf('.'));
        current_time.setText(newTime);
    }

    @Override
    public void OnInputsChange(String newInput)
    {
        current_inputs.setText(newInput);
    }

    public static int incrementInputs(){
        userInputs += 1;
        return userInputs;
    }
}
