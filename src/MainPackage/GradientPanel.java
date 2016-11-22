package MainPackage;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GradientPanel extends javax.swing.JPanel {

    private int direction;
    private Color StartColor;
    private Color EndColor;
    public static final int CENTER_HORIZONTAL = 0;
    public static final int UP_DOWN = 1;
    public static final int CENTER_VERTICAL = 2;
    public static final int LEFT_RIGHT = 3;
    
    public GradientPanel(int direction,Color StartColor,Color EndColor) {
        this.direction = direction;
        this.StartColor = StartColor;
        this.EndColor = EndColor;
    }

    @Override
    public void paintComponent(Graphics g) {
        GradientPaint gp;
        Graphics2D g2;
        switch (direction) {
            case CENTER_HORIZONTAL:
                gp = new GradientPaint(0, 0, StartColor, 0, getHeight() / 2, EndColor, true);
                g2 = (Graphics2D) g;
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight() / 2);
                gp = new GradientPaint(0, getHeight() / 2, EndColor, 0, getHeight(), StartColor, true);
                g2.setPaint(gp);
                g2.fillRect(0, getHeight() / 2, getWidth(), getHeight());
                break;
            case UP_DOWN:
                gp = new GradientPaint(0, 0, StartColor, 0, getHeight(), EndColor, true);
                g2 = (Graphics2D) g;
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                break;
            case CENTER_VERTICAL:
                gp = new GradientPaint(0, 0, StartColor, getWidth()/2, 0, EndColor, true);
                g2 = (Graphics2D) g;
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth()/2, getHeight());
                gp = new GradientPaint(getWidth()/2, 0, EndColor, getWidth(), 0, StartColor, true);
                g2.setPaint(gp);
                g2.fillRect(getWidth()/2, 0, getWidth(), getHeight());
                break;
            case LEFT_RIGHT:
                gp = new GradientPaint(0, 0, StartColor, getWidth(), 0, EndColor, true);
                g2 = (Graphics2D) g;
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                break;
        }
    }
}
