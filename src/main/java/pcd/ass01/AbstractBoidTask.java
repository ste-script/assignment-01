package pcd.ass01;

import java.util.concurrent.Callable;

public abstract class AbstractBoidTask implements Callable<Void> {

    protected Boid boid;
    protected BoidsModel model;

    public AbstractBoidTask(Boid boid, BoidsModel model) {
        this.boid = boid;
        this.model = model;
    }
}
