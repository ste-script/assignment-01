package pcd.ass01;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidRunner implements Runnable {

    private List<Boid> boidChunk;
    private BoidsModel model;
    private CyclicBarrier velocity;
    private CyclicBarrier position;

    public BoidRunner(List<Boid> boidChunk, BoidsModel model,
            CyclicBarrier velocity,
            CyclicBarrier postition) {
        this.boidChunk = boidChunk;
        this.model = model;
        this.velocity = velocity;
        this.position = postition;
    }

    public void run() {
        while (true) {
            try {
                velocity.await();
                boidChunk.forEach(boid -> boid.updateVelocity(model));

                position.await();
                boidChunk.forEach(boid -> boid.updatePos(model));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
