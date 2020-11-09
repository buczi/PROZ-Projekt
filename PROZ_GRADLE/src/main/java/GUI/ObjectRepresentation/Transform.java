package GUI.ObjectRepresentation;

import Map.Direction;
import Utils.SPair;
import mvc.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Transform extends JLabel {

    protected static String pathToImageFolder = "/src/img/";///home/maciek/Desktop/PROZ_PRO/
    protected BufferedImage image;
    protected BufferedImage rotated;
    protected boolean rotated_IN_USE;

    protected int centreX;
    protected int centreY;

    protected int spawnX;
    protected int spawnY;

    protected Layer layer;
    protected boolean axis;

    protected int currentAngle; //  N - 0      E - 90      S - 180     W - 270

    public Transform()
    {
        this.centreX = 0;
        this.centreY = 0;
        this.rotated_IN_USE = false;
    }

    protected void UpdateImageCenter()
    {
        SPair<Integer>pair = getSizeTransform();
        centreX = getX() + (int)Math.floor(pair.getX_()/2);
        centreY = getY() - (int)Math.floor(pair.getY_()/2);
    }

    protected static void LoadImageToJLabel(Transform label, String pathToImage) throws IOException
    {
        label.image = ImageIO.read(new File(pathToImage));
        SPair<Integer>size = label.getSizeTransform();
        label.setSize(size.getX_(),size.getY_());
        Image image = label.image.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        size = label.getSizeTransform();
        label.setIcon(new ImageIcon(image));
        label.setBounds(label.spawnX,label.spawnY,size.getX_(),size.getY_());

    }

    public static void ResizeIcon(Transform label, int width, int height)
    {
        label.setVisible(false);
        label.setSize(width,height);
        Image dim = null;
        if(label.rotated_IN_USE)
            dim = label.rotated.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        else
            dim = label.image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setBounds(label.getX(), label.getY(), width, height);
        label.setIcon(new ImageIcon(dim));
        label.repaint();
        label.setVisible(true);
    }

    public static void MoveIcon(Transform label, int newPosX, int newPosY)
    {
        label.setVisible(false);
        label.setBounds(newPosX,newPosY,label.getWidth(), label.getHeight());
        label.repaint();
        label.setVisible(true);
    }

    public static BufferedImage LoadImage(String path)
    {
        BufferedImage image = null;
        try{
            image = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return image;
    }


    protected SPair<Integer> getSizeTransform()
    {
        return new SPair<>();
    }
    protected void setSizeTransform(){};



    public  BufferedImage rotateImageToDirection(BufferedImage img, Direction facingDirection) {

        int angle = 0;
        switch (facingDirection)
        {
            case North: angle = 360 - this.currentAngle;
                this.currentAngle = 0;
                break;
            case East: angle = 90 - this.currentAngle;
                this.currentAngle = 90;
                break;
            case South: angle = 180 - this.currentAngle;
                this.currentAngle = 180;
                break;
            case West: angle = 270 - this.currentAngle;
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
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, this);
        g2d.dispose();

        this.rotated = rotated;
        this.rotated_IN_USE = true;

        setVisible(false);
        SPair<Integer>pair = this.getSizeTransform();
        if(Math.abs(angle) == 0);
        else if(Math.abs(angle) == 180)
        {

            Transform.ResizeIcon(this, pair.getX_(),pair.getY_());
            setBounds(getX(),getY(),pair.getX_(),pair.getY_());

        }
        else
        {
            Transform.ResizeIcon(this, pair.getY_(),pair.getX_());
            setBounds(getX(),getY(),pair.getY_(),pair.getX_());
            setSizeTransform();
        }
        setVisible(true);
        repaint();
        return rotated;
    }

    public BufferedImage getRotated() {
        return rotated;
    }

    public BufferedImage getImage(){return  image;}

    public static void setPathToImageFolder(String pathToImageFolder) {
        String temp = Transform.pathToImageFolder;
        //Transform.pathToImageFolder = pathToImageFolder + temp;
        Transform.pathToImageFolder = Controller.absolutePath +"/img/";
    }
}
