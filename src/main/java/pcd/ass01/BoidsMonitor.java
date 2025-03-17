package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidsMonitor {

    private List<BoidRunner> boidRunners;
    private CyclicBarrier barrier;

    public BoidsMonitor(BoidsModel model) {
        boidRunners = new ArrayList<>();
        var boids = model.getBoids();
        var numberOfThreads = Runtime.getRuntime().availableProcessors();
        barrier = new CyclicBarrier(numberOfThreads + 1);
        var chunkSize = boids.size() / numberOfThreads;
        for (int i = 0; i < boids.size(); i += chunkSize) {
            var chunk = boids.subList(i, Math.min(i + chunkSize, boids.size()));
            boidRunners.add(new BoidRunner(chunk, model, barrier));
        }

    }

    public synchronized void start() {
        boidRunners.forEach(boidRunner -> Thread.ofPlatform().start(boidRunner));
    }

    private void updateVelocity() {
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePosition() {
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void update() {
        this.updateVelocity();
        this.updatePosition();
    }

}
