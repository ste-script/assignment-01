package pcd.ass01.BoidPattern;

import java.awt.*;

public class ShapeDrawer {
    private static final int scale = 5;

    // TODO finish scale based size
    public static void drawCircle(Graphics g, int px, int py) {
        g.fillOval(px, py, 1 * scale, 1 * scale);
    }

    public static void drawSquare(Graphics g, int px, int py) {
        g.fillRect(px, py, 1 * scale, 1 * scale);
    }

    public static void drawTriangle(Graphics g, int px, int py) {
        int[] xPoints = { px, px + 1 * scale, px - 1 * scale };
        int[] yPoints = { py - 1 * scale, py + 1 * scale, py + 1 * scale };
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public static void drawStar(Graphics g, int px, int py) {
        int[] starX = { px, px + 5, px + 10, px + 7, px + 3, px, px - 3, px - 7, px - 10, px - 5 };
        int[] starY = { py - 10, py - 3, py - 3, py + 2, py + 7, py + 3, py + 7, py + 2, py - 3, py - 3 };
        g.fillPolygon(starX, starY, 10);
    }

    public static void drawDiamond(Graphics g, int px, int py) {
        int[] diamondX = { px, px + 1 * scale, px, px - 1 * scale };
        int[] diamondY = { py - 1 * scale, py, py + 1 * scale, py };
        g.fillPolygon(diamondX, diamondY, 4);
    }
}
