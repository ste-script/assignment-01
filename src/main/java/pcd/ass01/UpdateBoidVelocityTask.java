package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns;

import java.util.List;
import java.util.concurrent.Callable;

public class UpdateBoidVelocityTask implements Callable<Void> {

    private List<Boid> boidChunk;
    private BoidsModel model;

    public UpdateBoidVelocityTask(List<Boid> boidChunk, BoidsModel model, BoidPatterns.Pattern boidPattern) {
        this.boidChunk = boidChunk;
        this.model = model;
        setBoidsPattern(boidPattern);
    }

    private void setBoidsPattern(BoidPatterns.Pattern pattern) {
        this.boidChunk.forEach(boid -> boid.setPattern(pattern));
    }

    @Override
    public Void call() {
        boidChunk.forEach(boid -> boid.updateVelocity(model));
        return null;
    }

}
