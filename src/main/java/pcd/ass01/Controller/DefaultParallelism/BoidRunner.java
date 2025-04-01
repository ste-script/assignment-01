package pcd.ass01.Controller.DefaultParallelism;

import pcd.ass01.Model.Boid;
import pcd.ass01.Model.BoidsModel;
import pcd.ass01.View.BoidPattern.BoidPatterns;

import java.util.List;

public class BoidRunner implements Runnable {

    private List<Boid> boidChunk;
    private BoidsModel model;
    private BoidsMonitor barrier;
    private boolean run = true;

    public BoidRunner(List<Boid> boidChunk, BoidsModel model,
                      BoidsMonitor barrier) {
        this.boidChunk = boidChunk;
        this.model = model;
        this.barrier = barrier;
    }


    public void stop() {
        run = false;
    }

    public void run() {
        while (run) {
            try {
                boidChunk.forEach(boid -> boid.updateVelocity(model));
                barrier.await();
                boidChunk.forEach(boid -> boid.updatePosition(model));
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