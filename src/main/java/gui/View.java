package gui;


import javax.swing.*;

import mvc.exception.CriticalException;
import mvc.exception.MildException;
import map.event.MapEvent;
import gui.menu.EndCommunicate;
import gui.menu.MainMenu;
import gui.menu.event.SettingsEvent;
import gui.objectrepresetnation.*;
import mvc.event.EndGameEvent;
import mvc.event.PassEvent;
import map.*;
import util.*;
import vehicle.event.CarGraphicEvents;
import gui.objectrepresetnation.event.ResetCar;
import gui.objectrepresetnation.event.UI_notification;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.util.*;

@SuppressWarnings("unused")
public class View implements MapEvent, CarGraphicEvents, UI_notification, EndGameEvent {
    public final JFrame mainframe;
    public boolean readyToNotify;
    public GMap map;
    private UI_Panel panel;
    private MainMenu menuPanel;
    private final ResetCar listener;
    private final PassEvent passEvent;

    public View(boolean[][] bMap, DataSet[][] dMap, ResetCar resetEvent, PassEvent passEvent) throws CriticalException {
        this.readyToNotify = false;
        this.listener = resetEvent;
        this.passEvent = passEvent;

        //SET UP MAIN FRAME
        this.mainframe = new JFrame();
        this.mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.getContentPane().setBackground(Color.lightGray);
        mainframe.setSize(1400, 1200);


        //MAP SET UP
        map = new GMap(bMap, dMap, mainframe);

        //UI PANEL SET UP
        panel = new UI_Panel();
        mainframe.getLayeredPane().add(panel, 200);
        GGridNode.addInputListener(panel);

        mainframe.revalidate();
        mainframe.repaint();
        mainframe.setLayout(null);
        mainframe.setVisible(true);


    }

    public View(SettingsEvent event, ResetCar resetEvent, PassEvent passEvent) throws CriticalException {
        this.readyToNotify = false;
        this.listener = resetEvent;
        this.passEvent = passEvent;
        //SET UP MAIN FRAME
        this.mainframe = new JFrame();
        this.mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.getContentPane().setBackground(Color.lightGray);
        mainframe.setSize(1400, 1200);
        menuPanel = new MainMenu(mainframe, event);
        mainframe.revalidate();
        mainframe.repaint();
        mainframe.setLayout(null);
        mainframe.setVisible(true);


    }

    public void GoToGame(boolean[][] bMap, DataSet[][] dMap) throws CriticalException {


        //MAP SET UP
        map = new GMap(bMap, dMap, mainframe);

        //UI PANEL SET UP
        panel = new UI_Panel();
        mainframe.getLayeredPane().add(panel, 200);
        GGridNode.addInputListener(panel);

        mainframe.revalidate();
        mainframe.repaint();
        mainframe.setLayout(null);
        mainframe.setVisible(true);

    }

    public List<SPair<Integer>> sendMessageToModel() {
        return GMap.getNodesToNotify();

    }

    public void clearMessage() {
        GMap.getNodesToNotify().clear();
        readyToNotify = false;
    }

    @Override
    public void onLightsChange(int x, int y) {
        map.changeLights(x, y);
    }

    @Override
    public void spawnCar(int id, int x, int y, Direction direction, int quePlace) throws MildException {
        map.addCar(id, x, y, direction, quePlace, mainframe, this);
    }

    @Override
    public void destroyCar(int id) {
        map.destroyCar(id, mainframe);
    }

    @Override
    public void moveCar(int id, int speed, Direction direction) {
        map.moveCar(id, speed, direction);
        if (map.getCarByID(id).getOnIntersection())
            if (!map.getCarByID(id).checkIfLeftIntersection())
                listener.resetCarPosition(id);
    }

    @Override
    public void rotate(int id, Direction direction) {

        map.rotate(id, direction);
        if (map.getCarByID(id).getOnIntersection())
            map.getCarByID(id).adjustPosition(direction);
    }

    @Override
    public void enterIntersection(int id, SPair<Integer> position) {
        if (!map.getCarByID(id).getOnIntersection()) {
            GGridNode node = map.getNode(position);
            SPair<Integer> pos = new SPair<>();
            if (node != null) {
                pos = new SPair<>(node.getY(), node.getX());
            }
            map.getCarByID(id).setCurrentIntersection(pos);
            map.getCarByID(id).setOnIntersection(true, pos);
        }
    }

    @Override
    public GCar getGCarById(int id) {
        return map.getCarByID(id);
    }

    @Override
    public void notifyController(int id) {
        passEvent.makeCarInterface(id);
    }

    @Override
    public void reset(float score) {
        new EndCommunicate(score, passEvent, mainframe);
    }

    public UI_Panel getPanel() {
        return panel;
    }

    public void removeMenuPanel() {
        mainframe.getLayeredPane().remove(menuPanel);
        mainframe.revalidate();
        mainframe.repaint();
    }

    public void closeWindow() {
        mainframe.dispatchEvent(new WindowEvent(mainframe, WindowEvent.WINDOW_CLOSED));
        mainframe.dispose();
    }
}