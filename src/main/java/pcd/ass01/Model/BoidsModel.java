package pcd.ass01.Model;

import java.util.ArrayList;
import java.util.List;

public class BoidsModel {

    private final List<Boid> boids;
    private double separationWeight;
    private double alignmentWeight;
    private double cohesionWeight;
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;
    private int numberOfBoids;
    private boolean suspended;
    private boolean running;

    public BoidsModel(int nboids,
            double initialSeparationWeight,
            double initialAlignmentWeight,
            double initialCohesionWeight,
            double width,
            double height,
            double maxSpeed,
            double perceptionRadius,
            double avoidRadius) {
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        this.numberOfBoids = nboids;
        suspended = false;
        running = true;

        boids = new ArrayList<>();
        for (int i = 0; i < nboids; i++) {
            newBoid();
        }
    }

    public synchronized void setNumberOfBoids(int n) {
        numberOfBoids = n;
    }

    public synchronized int getNumberOfBoids() {
        return numberOfBoids;
    }

    public synchronized void suspend() {
        suspended = true;
    }

    public synchronized boolean isSuspended() {
        return suspended;
    }

    public synchronized void resume() {
        suspended = false;
    }

    public synchronized void stop() {
        running = false;
    }

    public synchronized void start() {
        running = true;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setBoids(int nboids) {

        if (nboids > boids.size()) {
            for (int i = 0; i < nboids - boids.size(); i++) {
                newBoid();
            }
        } else if (nboids < boids.size()) {
            deleteBoid(nboids);
        }
    }

    public synchronized List<Boid> getBoids() {
        return List.copyOf(boids);
    }

    public double getMinX() {
        return -width / 2;
    }

    public double getMaxX() {
        return width / 2;
    }

    public double getMinY() {
        return -height / 2;
    }

    public double getMaxY() {
        return height / 2;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public synchronized void setSeparationWeight(double value) {
        this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(double value) {
        this.alignmentWeight = value;
    }

    public synchronized void setCohesionWeight(double value) {
        this.cohesionWeight = value;
    }

    public synchronized double getSeparationWeight() {
        return separationWeight;
    }

    public synchronized double getCohesionWeight() {
        return cohesionWeight;
    }

    public synchronized double getAlignmentWeight() {
        return alignmentWeight;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAvoidRadius() {
        return avoidRadius;
    }

    public double getPerceptionRadius() {
        return perceptionRadius;
    }

    private void newBoid() {
        P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
        V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
        boids.add(new Boid(pos, vel));
    }

    private void deleteBoid(int nboids) {
        var toDelete = boids.size() - nboids;
        if (boids.size() > 0) {
            for (int i = 0; i < toDelete; i++) {
                boids.removeLast();
            }
        }
    }
}
