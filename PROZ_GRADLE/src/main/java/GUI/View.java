package GUI;


import javax.swing.*;

import GUI.Menu.MainMenu;
import GUI.Menu.SettingsEvent;
import GUI.ObjectRepresentation.*;
import Map.*;
import Utils.*;
import Vehicle.CarGraphicEvents;

import java.awt.Color;
import java.util.*;

public class View implements MapEvent, CarGraphicEvents {
    public JFrame mainframe;
    public boolean readyToNotify;
    public GMap map;
    private UI_Panel panel;
    private MainMenu menuPanel;

    public View(boolean[][]b_map, DataSet[][]d_map)
    {
        this.readyToNotify = false;

        //SET UP MAIN FRAME
        this.mainframe = new JFrame();
        this.mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.getContentPane().setBackground(Color.lightGray);
        mainframe.setSize(1400,1200);


        //MAP SET UP
        map = new GMap(b_map, d_map, mainframe);

        //UI PANEL SET UP
        panel = new UI_Panel();
        mainframe.getLayeredPane().add(panel, 200);
        GGridNode.AddInputListener(panel);

        mainframe.revalidate();
        mainframe.repaint();
        mainframe.setLayout(null);
        mainframe.setVisible(true);


    }

    public View(SettingsEvent event)
    {
        this.readyToNotify = false;

        //SET UP MAIN FRAME
        this.mainframe = new JFrame();
        this.mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.getContentPane().setBackground(Color.lightGray);
        mainframe.setSize(1400,1200);
        menuPanel = new MainMenu("",mainframe,event);
        mainframe.revalidate();
        mainframe.repaint();
        mainframe.setLayout(null);
        mainframe.setVisible(true);


    }

    public void GoToGame(boolean[][]b_map, DataSet[][]d_map)
    {


        //MAP SET UP
        map = new GMap(b_map, d_map, mainframe);

        //UI PANEL SET UP
        panel = new UI_Panel();
        mainframe.getLayeredPane().add(panel, 200);
        GGridNode.AddInputListener(panel);

        mainframe.revalidate();
        mainframe.repaint();
        mainframe.setLayout(null);
        mainframe.setVisible(true);
    }

    public List<SPair<Integer>> sendMessageToModel()
    {            return GMap.getNodesToNotify();

    }

    public void clearMessage()
    {
        GMap.getNodesToNotify().clear();
        readyToNotify = false;
    }

    @Override
    public void OnLightsChange(int x, int y) {
        map.ChangeLights(x,y);
    }

    @Override
    public void SpawnCar(int id,int x, int y, Direction side,Direction direction) {
        map.AddCar(id,x,y,direction,mainframe);
    }

    @Override
    public void DespawnCar(int id) {
        map.DespawnCar(id,mainframe);
    }

    @Override
    public void MoveCar(int id,int speed, Direction direction) {
        map.MoveCar(id, speed,direction);
    }

    @Override
    public void Rotate(int id, Direction direction) {
        map.Rotate(id, direction);
    }

    public UI_Panel getPanel() {
        return panel;
    }

    public void RemoveMenuPanel()
    {
        mainframe.getLayeredPane().remove(menuPanel);
        mainframe.revalidate();
        mainframe.repaint();
    }
}
/*

        GCar label = new GCar(50,50);

        mainframe.getContentPane().add(label);
        mainframe.getLayeredPane().add(label, Layer.Car);
 */
