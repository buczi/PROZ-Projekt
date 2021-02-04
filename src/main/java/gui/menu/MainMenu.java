package gui.menu;

import mvc.Controller;
import mvc.exception.CriticalException;
import gui.menu.event.MenuEvents;
import gui.menu.event.SettingsEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel implements MenuEvents {

    private final PickableImage[] maps;
    private final PickableImage[] difficulty;

    private final SettingsEvent listener;

    private final static String[] mapFiles = {"map1.png", "map2.png", "map3.png"};
    private final static String[] difFiles = {"green.png", "yellow.png", "red.png"};

    private final static int bigFont = 70;
    private final static int smallFont = 30;

    private final static int labelHeight = 100;
    private final static int mainTitleY = 0;
    private final static int mapTitleY = 150;
    private final static int difTitleY = 650;
    private final static int playButtonY = 900;
    private final static Color buttonColor = Color.gray;
    private final static Color unhighlight = Color.white;
    private final static Color highlight = Color.black;

    private final static String title = "TRAFFIC MANAGER";
    private final static String titleMap = "PICK MAP!";
    private final static String titleDif = "SELECT DIFFICULTY!";
    private final static String titlePlay = "PLAY!!!";

    public MainMenu(JFrame frame, SettingsEvent settingsListener) throws CriticalException {
        int quarterFrame = frame.getWidth() / 4;
        int columnPosX = frame.getWidth() * 3 / 8;

        setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.getLayeredPane().add(this, 100);
        setLayout(null);

        JLabel mainTitle = new JLabel(title, SwingConstants.CENTER);
        mainTitle.setFont(new Font(mainTitle.getFont().getName(), Font.PLAIN, bigFont));
        mainTitle.setBounds(quarterFrame, mainTitleY, quarterFrame * 2, labelHeight);
        mainTitle.setBackground(buttonColor);
        mainTitle.setOpaque(true);

        JLabel mapTitle = new JLabel(titleMap, SwingConstants.CENTER);
        mapTitle.setFont(new Font(mainTitle.getFont().getName(), Font.PLAIN, smallFont));
        mapTitle.setBounds(columnPosX, mapTitleY, quarterFrame, labelHeight);
        mapTitle.setBackground(buttonColor);
        mapTitle.setOpaque(true);

        JLabel difficultyTitle = new JLabel(titleDif, SwingConstants.CENTER);
        difficultyTitle.setFont(new Font(mainTitle.getFont().getName(), Font.PLAIN, smallFont));
        difficultyTitle.setBounds(columnPosX, difTitleY, quarterFrame, labelHeight);
        difficultyTitle.setBackground(buttonColor);
        difficultyTitle.setOpaque(true);

        maps = new PickableImage[mapFiles.length];
        difficulty = new PickableImage[difFiles.length];

        for (int i = 0; i < difficulty.length; i++)
            difficulty[i] = new PickableImage(Controller.resourcePath + "/img/Settings/" + difFiles[i], PickableImage.difT, i, this, this);

        for (int i = 0; i < maps.length; i++)
            maps[i] = new PickableImage(Controller.resourcePath + "/img/Settings/" + mapFiles[i], PickableImage.mapT, i, this, this);

        listener = settingsListener;
        JLabel startButton = new JLabel(titlePlay, SwingConstants.CENTER);
        startButton.setBackground(buttonColor);
        startButton.setOpaque(true);
        startButton.setBounds(columnPosX, playButtonY, quarterFrame, labelHeight);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i, j;
                for (i = 0; i < maps.length; i++)
                    if (maps[i].picked)
                        break;

                for (j = 0; j < difficulty.length; j++)
                    if (difficulty[j].picked)
                        break;

                listener.setUpGameSettings(new GameSettings(i, j));
            }

        });

        add(mainTitle);
        add(mapTitle);
        add(difficultyTitle);
        add(startButton);

        //SET default values for map and difficulty choice
        maps[0].pickImage(0, PickableImage.mapT);
        difficulty[0].pickImage(0, PickableImage.difT);
    }

    @Override
    public void onImagePicked(int id, char type) {
        switch (type) {
            case PickableImage.mapT:
                for (PickableImage image : maps) {
                    if (image.id != id) {
                        image.picked = false;
                        image.panel.setOpaque(false);
                        image.panel.repaint();
                    } else {
                        if (image.panel != null) {
                            image.panel.setBackground(highlight);
                            image.panel.setOpaque(true);
                        }

                    }
                }
                break;


            case PickableImage.difT:
                for (PickableImage image : difficulty) {
                    if (image.id != id) {
                        image.panel.setOpaque(false);
                        image.picked = false;
                        image.panel.repaint();
                    } else {
                        if (image.panel != null) {
                            image.panel.setBackground(highlight);
                            image.panel.setOpaque(true);
                        }

                    }
                }
                break;
        }
    }

    protected static Color getHighlight() {
        return highlight;
    }

    protected static Color getAntiHighlight() {
        return unhighlight;
    }
}