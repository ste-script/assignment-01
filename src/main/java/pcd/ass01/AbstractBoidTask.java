package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns;
import java.util.concurrent.Callable;

public abstract class AbstractBoidTask implements Callable<Void> {

    protected Boid boid;
    protected BoidsModel model;

    public AbstractBoidTask(Boid boid, BoidsModel model, BoidPatterns.Pattern pattern) {
        this.boid = boid;
        this.model = model;
        boid.setPattern(pattern);
    }
}
