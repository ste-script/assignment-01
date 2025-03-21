package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidRunner implements Runnable {

    private List<Boid> boidChunk;
    private BoidPatterns.Pattern boidPattern;
    private BoidsModel model;
    private CyclicBarrier barrier;
    private boolean run = true;

    public BoidRunner(List<Boid> boidChunk, BoidsModel model,
            CyclicBarrier barrier, BoidPatterns.Pattern boidPattern) {
        this.boidChunk = boidChunk;
        this.model = model;
        this.barrier = barrier;
        this.boidPattern = boidPattern;

        setBoidsPattern();
    }

    private void setBoidsPattern() {
        this.boidChunk.forEach(boid -> boid.setPattern(this.boidPattern));
    }

    public void stop() {
        run = false;
    }

    public void run() {
        while (true && run) {
            try {
                boidChunk.forEach(boid -> boid.updateVelocity(model));
                barrier.await();
                boidChunk.forEach(boid -> boid.updatePos(model));
                barrier.await();
                barrier.await(); // for thread validity check
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
