package pcd.ass01.BoidPattern;

import java.awt.*;

public class ShapeDrawer {
<<<<<<< HEAD
    private static final int scale = 5;

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
        int[] starX = { px, px + 5 * scale, px + 10 * scale, px + 7 * scale, px + 3 * scale, 
                         px, px - 3 * scale, px - 7 * scale, px - 10 * scale, px - 5 * scale };
        int[] starY = { py - 10 * scale, py - 3 * scale, py - 3 * scale, py + 2 * scale, py + 7 * scale, 
                         py + 3 * scale, py + 7 * scale, py + 2 * scale, py - 3 * scale, py - 3 * scale };
        g.fillPolygon(starX, starY, 10);
    }

    public static void drawDiamond(Graphics g, int px, int py) {
        int[] diamondX = { px, px + 1 * scale, px, px - 1 * scale };
        int[] diamondY = { py - 1 * scale, py, py + 1 * scale, py };
        g.fillPolygon(diamondX, diamondY, 4);
=======
    private final static double SCALE = 0.6;
    private final static int BORDER_SIZE = 2;   // best values = 1 or 2

    private final static Color BORDER_COLOR = Color.BLACK;
    private final static double SCALE_FACTOR = 10;

    @FunctionalInterface
    private interface ShapeDrawerFunction {
        void draw(Graphics g, int px, int py, int size);
    }

    private static void drawWithBorder(Graphics g, int px, int py, ShapeDrawerFunction shapeFunction) {
        int size = (int) (SCALE_FACTOR * SCALE);
        int totalBorder = BORDER_SIZE * 2;

        Color oldColor = g.getColor();

        g.setColor(BORDER_COLOR);
        shapeFunction.draw(g, px - BORDER_SIZE, py - BORDER_SIZE, size + totalBorder);
        g.setColor(oldColor);
        shapeFunction.draw(g, px, py, size);
    }

    public static void drawCircle(Graphics g, int px, int py) {
        drawWithBorder(g, px, py, (gr, x, y, s) -> gr.fillOval(x, y, s, s));
    }

    public static void drawSquare(Graphics g, int px, int py) {
        drawWithBorder(g, px, py, (gr, x, y, s) -> gr.fillRect(x, y, s, s));
    }

    public static void drawTriangle(Graphics g, int px, int py) {
        drawWithBorder(g, px, py, (gr, x, y, s) -> {
            int[] xPoints = {x, x + s, x - s};
            int[] yPoints = {y - s, y + s, y + s};
            gr.fillPolygon(xPoints, yPoints, 3);
        });
    }

    public static void drawStar(Graphics g, int px, int py) {
        drawWithBorder(g, px, py, (gr, x, y, s) -> {
            int[] starX = {
                    x, x + s / 2, x + s, x + (s * 7 / 10), x + (s * 3 / 10),
                    x, x - (s * 3 / 10), x - (s * 7 / 10), x - s, x - s / 2
            };
            int[] starY = {
                    y - s, y - (s * 3 / 10), y - (s * 3 / 10), y + (s * 2 / 10), y + (s * 7 / 10),
                    y + (s * 3 / 10), y + (s * 7 / 10), y + (s * 2 / 10), y - (s * 3 / 10), y - (s * 3 / 10)
            };
            gr.fillPolygon(starX, starY, 10);
        });
    }

    public static void drawDiamond(Graphics g, int px, int py) {
        drawWithBorder(g, px, py, (gr, x, y, s) -> {
            int[] diamondX = {x, x + s, x, x - s};
            int[] diamondY = {y - s, y, y + s, y};
            gr.fillPolygon(diamondX, diamondY, 4);
        });
>>>>>>> 4b51fe88519783edbca946e05ddd1fb1bb77e885
    }
}
