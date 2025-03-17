package pcd.ass01;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidRunner implements Runnable {

    private List<Boid> boidChunk;
    private BoidsModel model;
    private CyclicBarrier barrier;

    public BoidRunner(List<Boid> boidChunk, BoidsModel model,
            CyclicBarrier barrier) {
        this.boidChunk = boidChunk;
        this.model = model;
        this.barrier = barrier;
    }

    public void run() {
        while (true) {
            try {
                barrier.await();
                boidChunk.forEach(boid -> boid.updateVelocity(model));

                barrier.await();
                boidChunk.forEach(boid -> boid.updatePos(model));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
