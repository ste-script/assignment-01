package pcd.ass01;

import javax.swing.*;
import java.awt.*;

public class BoidsPanel extends JPanel {

	private BoidsView view; 
	private BoidsModel model;
    private int framerate;

    public BoidsPanel(BoidsView view, BoidsModel model) {
    	this.model = model;
    	this.view = view;
    }

    public void setFrameRate(int framerate) {
    	this.framerate = framerate;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        
        var w = view.getWidth();
        var h = view.getHeight();
        var envWidth = model.getWidth();
        var xScale = w/envWidth;
        // var envHeight = model.getHeight();
        // var yScale = h/envHeight;

        var boids = model.getBoids();

        for (Boid boid : boids) {
            g.setColor(boid.getColor());
            var x = boid.getPos().x();
            var y = boid.getPos().y();
            int px = (int)(w/2 + x*xScale);
            int py = (int)(h/2 - y*xScale);

            switch (boid.getShape()) {
                case CIRCLE:
                    g.fillOval(px, py, 10, 10); // Disegna un ovale
                    break;
                case SQUARE:
                    g.fillRect(px, py, 10, 10); // Disegna un quadrato
                    break;
                case TRIANGLE:
                    int[] xPoints = {px, px + 10, px - 10};
                    int[] yPoints = {py - 10, py + 10, py + 10};
                    g.fillPolygon(xPoints, yPoints, 3); // Disegna un triangolo
                    break;
                case STAR:
                    int[] starX = {px, px + 5, px + 10, px + 7, px + 3, px, px - 3, px - 7, px - 10, px - 5};
                    int[] starY = {py - 10, py - 3, py - 3, py + 2, py + 7, py + 3, py + 7, py + 2, py - 3, py - 3};
                    g.fillPolygon(starX, starY, 10); // Disegna una stella
                    break;
                case DIAMOND:
                    int[] diamondX = {px, px + 10, px, px - 10}; // Vertici del rombo
                    int[] diamondY = {py - 10, py, py + 10, py}; // Vertici del rombo
                    g.fillPolygon(diamondX, diamondY, 4); // Disegna un rombo
                    break;
                default:
                    break;
            }
        }
        
        g.setColor(Color.BLACK);
        g.drawString("Num. Boids: " + boids.size(), 10, 25);
        g.drawString("Framerate: " + framerate, 10, 40);
   }
}
