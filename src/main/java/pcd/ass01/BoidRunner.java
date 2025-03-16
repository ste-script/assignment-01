package pcd.ass01;

import java.util.concurrent.Semaphore;

public class BoidRunner implements Runnable {

    private Boid boid;
    private Semaphore updatePositonSemaphore;
    private Semaphore updateVelocitySemaphore;
    private Semaphore updateDoneSemaphore;
    private BoidsModel model;

    public BoidRunner(Boid boid, BoidsModel model, Semaphore updatePositonSemaphore,
            Semaphore updateVelocitySemaphore, Semaphore updateDoneSemaphore) {
        this.boid = boid;
        this.model = model;
        this.updatePositonSemaphore = updatePositonSemaphore;
        this.updateVelocitySemaphore = updateVelocitySemaphore;
        this.updateDoneSemaphore = updateDoneSemaphore;
    }

    public void run() {
        while (true) {
            try {
                updateVelocitySemaphore.acquire();
                boid.updateVelocity(model);
                updateVelocitySemaphore.release();

                // Signal all updated velocities
                updateDoneSemaphore.release();

                updatePositonSemaphore.acquire();
                boid.updatePos(model);
                updatePositonSemaphore.release();
                updateDoneSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
