package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidsMonitor {

    private List<BoidRunner> boidRunners;
    private final CyclicBarrier barrier;

    public BoidsMonitor(BoidsModel model) {
        boidRunners = new ArrayList<>();
        final var boids = model.getBoids();
        final var numberOfThreads = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(numberOfThreads + 1);
        var chunkSize = boids.size() / numberOfThreads;
        var boidsGroupedInChunks = getBoidsGroupedInChunks(boids, numberOfThreads, chunkSize);
        boidsGroupedInChunks.forEach(boidChunk -> boidRunners.add(new BoidRunner(boidChunk, model, barrier)));

    }

    public synchronized void start() {
        boidRunners.forEach(boidRunner -> Thread.ofPlatform().start(boidRunner));
    }

    public synchronized void update() {
        this.updateVelocity();
        this.updatePosition();
    }

    private ArrayList<List<Boid>> getBoidsGroupedInChunks(final List<Boid> boids, final int numberOfThreads,
            int chunkSize) {
        var boidsGroupedInChunks = new ArrayList<List<Boid>>();
        for (int i = 0; i < numberOfThreads; i++) {
            var start = i * chunkSize;
            var end = (i + 1) * chunkSize;
            if (i == numberOfThreads - 1) {
                end = boids.size();
            }
            boidsGroupedInChunks.add(boids.subList(start, end));
        }
        return boidsGroupedInChunks;
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

}
