package pcd.ass01;

import java.util.List;
import java.util.concurrent.Semaphore;

public class BoidRunner implements Runnable {

    private List<Boid> boidChunk;
    private BoidsModel model;
    private Semaphore canUpdateSemaphore;
    private Semaphore updateDoneSemaphore;

    public BoidRunner(List<Boid> boidChunk, BoidsModel model,
            Semaphore canUpdateSemaphore,
            Semaphore updateDoneSemaphore) {
        this.boidChunk = boidChunk;
        this.model = model;
        this.canUpdateSemaphore = canUpdateSemaphore;
        this.updateDoneSemaphore = updateDoneSemaphore;
    }

    public void run() {
        while (true) {
            try {
                canUpdateSemaphore.acquire();
                boidChunk.forEach(boid -> boid.updateVelocity(model));
                updateDoneSemaphore.release();

                canUpdateSemaphore.acquire();
                boidChunk.forEach(boid -> boid.updatePos(model));
                updateDoneSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
