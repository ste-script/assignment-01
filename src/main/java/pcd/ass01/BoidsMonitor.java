package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BoidsMonitor {

    private List<BoidRunner> boidRunners;
    private Semaphore updateVelocitySemaphore;
    private Semaphore updatePositonSemaphore;
    private Semaphore updateDoneSemaphore;

    public BoidsMonitor(BoidsModel model) {
        boidRunners = new ArrayList<>();
        updateVelocitySemaphore = new Semaphore(0);
        updatePositonSemaphore = new Semaphore(0);
        updateDoneSemaphore = new Semaphore(0);
        for (Boid boid : model.getBoids()) {
            boidRunners.add(
                    new BoidRunner(boid, model, updatePositonSemaphore, updateVelocitySemaphore, updateDoneSemaphore));
        }
    }

    public synchronized void start() {
        boidRunners.forEach(boidRunner -> Thread.ofVirtual().start(boidRunner));
    }

    public synchronized void updateVelocity() {
        updateVelocitySemaphore.release(boidRunners.size());
        try {
            updateDoneSemaphore.acquire(boidRunners.size());
            updateVelocitySemaphore.acquire(boidRunners.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updatePosition() {
        updatePositonSemaphore.release(boidRunners.size());
        try {
            updateDoneSemaphore.acquire(boidRunners.size());
            updatePositonSemaphore.acquire(boidRunners.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
