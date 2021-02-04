package gui.menu;

import mvc.exception.CriticalException;
import mvc.exception.ExceptionType;
import gui.menu.event.MenuEvents;
import gui.objectrepresetnation.Transform;
import util.SPair;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PickableImage extends Transform {

    protected boolean picked;
    protected static MenuEvents menuListener = null;
    protected final int id;

    protected final static char mapT = 'M';
    protected final static char difT= 'D';

    protected final char type;
    protected final JPanel panel;

    private final static int sizeXMap = 250;
    private final static int sizeXDif = 40;
    private final static int margin = 20;

    private final static int spawnYm = 300;
    private final static int spawnYd = 800;
    private final static int spawnXP = 120;
    private final static int distance = 200;

    public PickableImage(String path, char t, int index, JPanel frame, MenuEvents listener) throws CriticalException {
        type = t;
        id = index;
        picked = false;

        spawnX = spawnXP + index * (distance + sizeXMap);
        if (type == mapT)
            spawnY = spawnYm;
        else
            spawnY = spawnYd;

        panel = new JPanel();

        try {
            loadImageToJLabel(this, path);
        } catch (IOException e) {
            throw new CriticalException("Error while reading file: " + path, this.getClass().getName(), ExceptionType.LOAD_IMAGE_EXCEPTION);
        }

        frame.add(panel);
        panel.add(this);
        panel.setLayout(null);
        panel.setBounds(spawnX, spawnY, sizeXMap + margin, sizeXMap + margin);
        panel.setOpaque(false);

        setBounds(margin / 2, margin / 2, sizeXMap, sizeXMap);

        menuListener = listener;

        if (type == difT) {
            resizeIcon(this, sizeXDif, sizeXDif);
            panel.setBounds(spawnX + sizeXMap / 2 - sizeXDif / 2, spawnYd, sizeXDif + margin, sizeXDif + margin);
            setBounds(margin / 2, margin / 2, sizeXDif, sizeXDif);
        }


        //EVENTS
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                pickImage(id, type);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(MainMenu.getAntiHighlight());
                panel.setOpaque(true);
                panel.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (picked) {
                    panel.setBackground(MainMenu.getHighlight());
                    return;
                }
                panel.setOpaque(false);
                panel.repaint();
            }

        });

    }

    public void pickImage(int id, char type) {
        picked = true;
        menuListener.onImagePicked(id, type);
    }

    @Override
    protected SPair<Integer> getSizeTransform() {
        return new SPair<>(sizeXMap, sizeXMap);
    }
}
