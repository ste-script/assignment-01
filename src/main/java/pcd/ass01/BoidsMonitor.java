package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BoidsMonitor {

    private List<BoidRunner> boidRunners;
    private Semaphore canUpdateSemaphore;
    private Semaphore updateDoneSemaphore;

    public BoidsMonitor(BoidsModel model) {
        boidRunners = new ArrayList<>();
        var nOfCore = Runtime.getRuntime().availableProcessors();
        nOfCore = 8;
        canUpdateSemaphore = new Semaphore(0);
        updateDoneSemaphore = new Semaphore(0);
        var boids = model.getBoids();
        var chunkSize = boids.size() / nOfCore;

        for (int i = 0; i < nOfCore; i++) {
            var start = i * chunkSize;
            var end = (i + 1) * chunkSize;
            var boidChunk = boids.subList(start, end);
            boidRunners.add(new BoidRunner(boidChunk, model,
                    canUpdateSemaphore, updateDoneSemaphore));
        }

    }

    public synchronized void start() {
        boidRunners.forEach(boidRunner -> Thread.ofPlatform().start(boidRunner));
    }

    private void updateVelocity() {
        canUpdateSemaphore.release(boidRunners.size());
        try {
            updateDoneSemaphore.acquire(boidRunners.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updatePosition() {
        canUpdateSemaphore.release(boidRunners.size());
        try {
            updateDoneSemaphore.acquire(boidRunners.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void update() {
        this.updateVelocity();
        this.updatePosition();
    }

}
