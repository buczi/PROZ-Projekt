package GUI.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel implements MenuEvents{

    private JLabel mainTitle;
    private JLabel mapTitle;
    private JLabel difficultyTitle;

    private PickableImage[] maps;
    private PickableImage[] difficulty;

    private JLabel startButton;
    private SettingsEvent listener;

    public MainMenu(String absolutePath, JFrame frame, SettingsEvent settingsListener)
    {

        setBounds(0,0,frame.getWidth(),frame.getHeight());
        frame.getLayeredPane().add(this,100);
        setLayout(null);
        mainTitle = new JLabel("TRAFFIC MANAGER", SwingConstants.CENTER);
        mainTitle.setFont(new Font(mainTitle.getFont().getName(), Font.PLAIN, 70));
        mainTitle.setBounds(frame.getWidth()/4,0,frame.getWidth()/2,100);
        mainTitle.setBackground(Color.gray);
        mainTitle.setOpaque(true);


        mapTitle = new JLabel("PICK MAP!", SwingConstants.CENTER);
        mapTitle.setFont(new Font(mainTitle.getFont().getName(), Font.PLAIN, 30));
        mapTitle.setBounds(frame.getWidth()*3/8,150,frame.getWidth()/4,100);
        mapTitle.setBackground(Color.gray);
        mapTitle.setOpaque(true);

        difficultyTitle = new JLabel("SELECT DIFFICULTY!", SwingConstants.CENTER);
        difficultyTitle.setFont(new Font(mainTitle.getFont().getName(), Font.PLAIN, 30));
        difficultyTitle.setBounds(frame.getWidth()*3/8,500,frame.getWidth()/4,100);
        difficultyTitle.setBackground(Color.gray);
        difficultyTitle.setOpaque(true);

        maps = new PickableImage[3];
        difficulty = new PickableImage[3];
    //TODO
        for(int i = 0; i < difficulty.length; i++)
        {
            difficulty[i] = new PickableImage("/home/maciek/Desktop/PROZ_PRO/src/img/Crossing.png",PickableImage.dif_t,i,this,this);
            //frame.getLayeredPane().add(difficulty[i],100);
        }

        for(int i = 0; i < maps.length; i++)
        {
            maps[i] = new PickableImage("/home/maciek/Desktop/PROZ_PRO/src/img/Crossing.png",PickableImage.map_t,i,this,this);
            //frame.getLayeredPane().add(maps[i],300);
        }

        listener = settingsListener;
        startButton = new JLabel("PLAY!!!", SwingConstants.CENTER);
        startButton.setBackground(Color.gray);
        startButton.setOpaque(true);
        startButton.setBounds(frame.getWidth()*3/8,900,frame.getWidth()/4,100);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = 0;
                int j = 0;
                for(i = 0; i < maps.length; i++)
                    if(maps[i].picked)
                        break;

                for(j = 0; j < difficulty.length; j++)
                    if(difficulty[j].picked)
                        break;

                listener.SetUpGameSettings(new GameSettings(i,j));
            }

        });

add(mainTitle);
add(mapTitle);
add(difficultyTitle);
add(startButton);

        maps[0].PickImage(0,PickableImage.map_t);
        difficulty[0].PickImage(0,PickableImage.dif_t);
    }

    @Override
    public void OnImagePicked(int id, char type)
    {
        switch(type)
        {
            case PickableImage.map_t:
                for(int i = 0; i < maps.length; i++)
                {
                    if(maps[i].id != id)
                    {
                        maps[i].picked = false;
                        maps[i].panel.setOpaque(false);
                        maps[i].panel.repaint();
                    }
                    else
                    {
                        if(maps[i].panel != null)
                        {
                            maps[i].panel.setBackground(Color.yellow);
                            maps[i].panel.setOpaque(true);
                        }

                    }
                }
                break;


            case PickableImage.dif_t:
                for(int i = 0; i < difficulty.length; i++)
                {
                    if(difficulty[i].id != id)
                    {
                        difficulty[i].panel.setOpaque(false);
                        difficulty[i].picked = false;
                        difficulty[i].panel.repaint();
                    }
                    else
                    {
                        if(difficulty[i].panel != null)
                        {
                            difficulty[i].panel.setBackground(Color.yellow);
                            difficulty[i].panel.setOpaque(true);
                        }

                    }
                }
                break;
        }

    }

}
