package gui.menu;

import mvc.event.PassEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EndCommunicate extends JPanel {

    private final JLabel backToMenuButton;

    private final static int width = 300;
    private final static int height = 200;
    private final static Color windowColor = Color.gray;

    private final static int margin = 15;
    private final static int labelY = 30;
    private final static Color infoColor = Color.lightGray;

    public EndCommunicate(float score, PassEvent event, JFrame frame) {
        int spawnX = frame.getWidth() / 2 - width / 2;
        int spawnY = frame.getHeight() / 2 - height / 2;
        this.setLayout(null);
        this.setBounds(spawnX, spawnY, width, height);
        this.setBackground(windowColor);
        this.setOpaque(true);

        String text = "Congratulations your score is:";
        JLabel endText = new JLabel(text, SwingConstants.CENTER);
        endText.setBounds(margin, margin, width - margin * 2, labelY);
        endText.setBackground(infoColor);
        endText.setOpaque(true);

        JLabel finalScore = new JLabel(Float.toString(score), SwingConstants.CENTER);
        finalScore.setBounds(width / 2 - margin * 3, 2 * margin + labelY, margin * 6, labelY);
        finalScore.setBackground(infoColor);
        finalScore.setOpaque(true);

        String placementText = " Your place is :";
        JLabel placement = new JLabel(placementText + 1, SwingConstants.CENTER);
        placement.setBounds(margin, 3 * margin + 2 * labelY, width - margin * 2, labelY);
        placement.setBackground(infoColor);
        placement.setOpaque(true);

        String endButtonText = "Go back to main menu";
        backToMenuButton = new JLabel(endButtonText, SwingConstants.CENTER);
        backToMenuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                event.goToMainMenu();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backToMenuButton.setBackground(Color.white);
                backToMenuButton.setOpaque(true);
                backToMenuButton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backToMenuButton.setBackground(Color.black);
                backToMenuButton.repaint();
            }

        });
        backToMenuButton.setBounds(margin, 4 * margin + 3 * labelY, width - margin * 2, labelY);
        backToMenuButton.setBackground(Color.black);
        backToMenuButton.setOpaque(true);

        this.add(endText);
        this.add(finalScore);
        this.add(placement);
        this.add(backToMenuButton);

        frame.getLayeredPane().add(this);
    }
}
