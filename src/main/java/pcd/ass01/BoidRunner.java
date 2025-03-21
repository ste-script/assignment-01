package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidRunner implements Runnable {

    private List<Boid> boidChunk;
    private BoidsModel model;
    private CyclicBarrier barrier;
    private boolean run = true;

    public BoidRunner(List<Boid> boidChunk, BoidsModel model,
            CyclicBarrier barrier, BoidPatterns.Pattern boidPattern) {
        this.boidChunk = boidChunk;
        this.model = model;
        this.barrier = barrier;
        setBoidsPattern(boidPattern);
    }

    private void setBoidsPattern(BoidPatterns.Pattern pattern) {
        this.boidChunk.forEach(boid -> boid.setPattern(pattern));
    }

    public void stop() {
        run = false;
    }

    public void run() {
        while (run) {
            try {
                boidChunk.forEach(boid -> boid.updateVelocity(model));
                barrier.await();
                boidChunk.forEach(boid -> boid.updatePos(model));
                barrier.await();
                // between these two barriers we check if the number of boids has changed
                // and if the thread should continue running
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
