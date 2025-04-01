package pcd.ass01.Controller.VirtualThreads;

import java.util.concurrent.CyclicBarrier;

import pcd.ass01.Model.Boid;
import pcd.ass01.Model.BoidsModel;

public class BoidRunner implements Runnable {

    private Boid boid;
    private BoidsModel model;
    private CyclicBarrier barrier;
    private boolean run = true;

    public BoidRunner(Boid boid, BoidsModel model,
            CyclicBarrier barrier) {
        this.boid = boid;
        this.model = model;
        this.barrier = barrier;
    }

    public void stop() {
        run = false;
    }

    public void run() {
        while (run) {
            try {
                boid.updateVelocity(model);
                barrier.await();
                boid.updatePosition(model);
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