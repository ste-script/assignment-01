package pcd.ass01;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidRunner implements Runnable {

    private List<Boid> boidChunk;
    private BoidsModel model;
    private CyclicBarrier barrier;
    private boolean run = true;

    public BoidRunner(List<Boid> boidChunk, BoidsModel model,
            CyclicBarrier barrier) {
        this.boidChunk = boidChunk;
        this.model = model;
        this.barrier = barrier;
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
