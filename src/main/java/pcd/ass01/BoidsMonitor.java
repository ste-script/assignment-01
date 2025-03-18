package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidsMonitor {

    private List<BoidRunner> boidRunners;
    private CyclicBarrier barrier;
    private BoidsModel model;
    private int numberOfThreads;

    public BoidsMonitor(BoidsModel model) {
        this.model = model;
        boidRunners = new ArrayList<>();
        createAndAssignBoidRunners();
    }

    public synchronized void start() {
        boidRunners.forEach(boidRunner -> Thread.ofPlatform().start(boidRunner));
    }

    public synchronized void update() {
        this.updateVelocity();
        this.updatePosition();
        this.checkThreadValidity();
    }

    private void calculateNumberOfThreads() {
        numberOfThreads = Math.max(1, Math.min(Runtime.getRuntime().availableProcessors(), model.getBoids().size()));
    }

    private void createAndAssignBoidRunners() {
        calculateNumberOfThreads();
        this.barrier = new CyclicBarrier(numberOfThreads + 1);
        final var boids = model.getBoids();
        var chunkSize = Math.max(1, boids.size() / numberOfThreads);
        var boidsGroupedInChunks = getBoidsGroupedInChunks(boids, numberOfThreads, chunkSize);
        boidsGroupedInChunks.forEach(boidChunk -> boidRunners.add(new BoidRunner(boidChunk, model, barrier)));
    }

    private void checkThreadValidity() {
        try {
            if (model.getNumberOfBoids() != model.getBoids().size()) {
                redistributeBoids();
            }
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteThreads() {
        boidRunners.stream().forEach(BoidRunner::stop);
        boidRunners.clear();
    }

    private synchronized void redistributeBoids() {
        deleteThreads();
        model.setBoids(model.getNumberOfBoids());
        createAndAssignBoidRunners();
        start();
    }

    private ArrayList<List<Boid>> getBoidsGroupedInChunks(final List<Boid> boids, final int numberOfThreads,
            int chunkSize) {
        var boidsGroupedInChunks = new ArrayList<List<Boid>>();
        for (int i = 0; i < numberOfThreads; i++) {
            var start = i * chunkSize;
            var end = Math.min((i + 1) * chunkSize, boids.size());
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