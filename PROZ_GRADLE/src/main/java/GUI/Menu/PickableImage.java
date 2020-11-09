package GUI.Menu;

import GUI.ObjectRepresentation.Transform;
import GUI.ObjectRepresentation.UI_Panel;
import Utils.SPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PickableImage extends Transform {

    protected boolean picked;
    protected static MenuEvents menuListener = null;
    protected int id;

    protected final static char map_t = 'M';
    protected final static char dif_t = 'D';

    protected char type;
    protected JPanel panel;

    private final static int sizeX_map = 100;
    private final static int sizeX_dif = 40;

    public PickableImage(String path,char t, int index, JPanel frame, MenuEvents listener)
    {
        type = t;
        id = index;
        picked = false;

        spawnX = frame.getWidth()/4 + index* (200 + sizeX_map);
        if(type == map_t)
            spawnY = 300;
        else
            spawnY = 650;

        panel = new JPanel();

        try{
            LoadImageToJLabel(this,path);
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        frame.add(panel);
        panel.add(this);
        panel.setLayout(null);
        panel.setBounds(325 + id* (200 + sizeX_map),275, sizeX_map + 50, sizeX_map + 50);
        panel.setOpaque(false);

        setBounds(25,25,sizeX_map,sizeX_map);


        if(menuListener == null)
            menuListener = listener;





        if(type == dif_t)
        {
            ResizeIcon(this,sizeX_dif,sizeX_dif);
            panel.setBounds(325 + id* (200 + sizeX_map + 30),650, sizeX_dif + 40, sizeX_dif + 40);
            setBounds(20,20,sizeX_dif,sizeX_dif);
        }


        //EVENTS
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                PickImage(id,type);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.black);
                panel.setOpaque(true);
                panel.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(picked)
                {
                    panel.setBackground(Color.yellow);
                    return;
                }
                panel.setOpaque(false);
                panel.repaint();
            }

        });

    }

    public void PickImage(int id, char type)
    {
        picked = true;
        menuListener.OnImagePicked(id, type);
    }

    @Override
    protected SPair<Integer> getSizeTransform()
    {
        return new SPair<>(sizeX_map,sizeX_map);
    }
}
