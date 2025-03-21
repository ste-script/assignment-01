package pcd.ass01.BoidPattern;

import java.awt.*;

public class ShapeDrawer {
    public static void drawCircle(Graphics g, int px, int py) {
        g.fillOval(px, py, 10, 10);
    }

    public static void drawSquare(Graphics g, int px, int py) {
        g.fillRect(px, py, 10, 10);
    }

    public static void drawTriangle(Graphics g, int px, int py) {
        int[] xPoints = {px, px + 10, px - 10};
        int[] yPoints = {py - 10, py + 10, py + 10};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public static void drawStar(Graphics g, int px, int py) {
        int[] starX = {px, px + 5, px + 10, px + 7, px + 3, px, px - 3, px - 7, px - 10, px - 5};
        int[] starY = {py - 10, py - 3, py - 3, py + 2, py + 7, py + 3, py + 7, py + 2, py - 3, py - 3};
        g.fillPolygon(starX, starY, 10);
    }

    public static void drawDiamond(Graphics g, int px, int py) {
        int[] diamondX = {px, px + 10, px, px - 10};
        int[] diamondY = {py - 10, py, py + 10, py};
        g.fillPolygon(diamondX, diamondY, 4);
    }
}
