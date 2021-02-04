package gui.objectrepresetnation;

import map.Direction;
import util.SPair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Transform extends JLabel {

    protected static String pathToImageFolder = "/src/img/";
    protected BufferedImage image;
    protected BufferedImage rotated;
    protected boolean rotatedInUse;

    protected int centreX;
    protected int centreY;

    protected int spawnX;
    protected int spawnY;

    protected boolean axis;

    protected int currentAngle; //  N - 0      E - 90      S - 180     W - 270

    public Transform() {
        this.centreX = 0;
        this.centreY = 0;
        this.rotatedInUse = false;
    }

    protected void updateImageCenter() {
        SPair<Integer> pair = getSizeTransform();
        centreX = getX() + (int) Math.floor((float)pair.getX() / 2);
        centreY = getY() - (int) Math.floor((float)pair.getY() / 2);
    }

    protected static void loadImageToJLabel(Transform label, String pathToImage) throws IOException {
        label.image = ImageIO.read(new File(pathToImage));
        SPair<Integer> size = label.getSizeTransform();
        label.setSize(size.getX(), size.getY());
        Image image = label.image.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        size = label.getSizeTransform();
        label.setIcon(new ImageIcon(image));
        label.setBounds(label.spawnX, label.spawnY, size.getX(), size.getY());

    }

    public static void resizeIcon(Transform label, int width, int height) {
        label.setVisible(false);
        label.setSize(width, height);
        Image dim;
        if (label.rotatedInUse)
            dim = label.rotated.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        else
            dim = label.image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setBounds(label.getX(), label.getY(), width, height);
        label.setIcon(new ImageIcon(dim));
        label.repaint();
        label.setVisible(true);
    }

    public static void moveIcon(Transform label, int newPosX, int newPosY) {
        label.setVisible(false);
        label.setBounds(newPosX, newPosY, label.getWidth(), label.getHeight());
        label.repaint();
        label.setVisible(true);
    }

    protected SPair<Integer> getSizeTransform() {
        return new SPair<>();
    }

    protected void setSizeTransform() {
    }


    public void rotateImageToDirection(BufferedImage img, Direction facingDirection) {

        int angle = 0;
        switch (facingDirection) {
            case North:
                angle = 360 - this.currentAngle;
                this.currentAngle = 0;
                break;
            case East:
                angle = 90 - this.currentAngle;
                this.currentAngle = 90;
                break;
            case South:
                angle = 180 - this.currentAngle;
                this.currentAngle = 180;
                break;
            case West:
                angle = 270 - this.currentAngle;
                this.currentAngle = 270;
                break;
        }

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate(((float)(newWidth - w)) / 2, ((float)(newHeight - h)) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, this);
        g2d.dispose();

        this.rotated = rotated;
        this.rotatedInUse = true;

        setVisible(false);
        SPair<Integer> pair = this.getSizeTransform();
        if (Math.abs(angle) == 0) {}
        else if (Math.abs(angle) == 180) {

            Transform.resizeIcon(this, pair.getX(), pair.getY());
            setBounds(getX(), getY(), pair.getX(), pair.getY());

        } else {
            Transform.resizeIcon(this, pair.getY(), pair.getX());
            setBounds(getX(), getY(), pair.getY(), pair.getX());
            setSizeTransform();
        }
        setVisible(true);
        repaint();
    }

    public BufferedImage getRotated() {
        return rotated;
    }

    public static void setPathToImageFolder(String pathToImage) {
        Transform.pathToImageFolder = pathToImage + "/img/";
    }

}
