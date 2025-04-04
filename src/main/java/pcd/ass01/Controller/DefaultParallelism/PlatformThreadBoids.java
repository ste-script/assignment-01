package pcd.ass01.Controller.DefaultParallelism;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import pcd.ass01.Controller.ParallelController;
import pcd.ass01.Controller.SimulationStateHandler;
import pcd.ass01.Model.Boid;
import pcd.ass01.Model.BoidsModel;

public class PlatformThreadBoids implements ParallelController, SimulationStateHandler {
    private List<BoidRunner> boidRunners;
    private CyclicBarrier barrier;
    private BoidsModel model;
    private int numberOfThreads;
    private List<Thread> threads;

    public PlatformThreadBoids(BoidsModel model) {
        this.model = model;
        boidRunners = new ArrayList<>();
        createAndAssignBoidRunners();
        threads = new ArrayList<>();
    }

    public synchronized void start() {
        model.start();
        boidRunners.forEach(boidRunner -> threads.add(new Thread(boidRunner)));
        threads.forEach(Thread::start);
    }

    public synchronized void update() {
        this.updateVelocity();
        this.updatePosition();
        this.checkThreadValidity();
    }

    public synchronized void stop() {
        model.stop();
    }

    @Override
    public void resume() {
        model.resume();
    }

    @Override
    public void suspend() {
        model.suspend();
    }

    private void calculateNumberOfThreads() {
        var numberOfAvailableProcessors = 1; // Runtime.getRuntime().availableProcessors() + 1;
        numberOfThreads = Math.max(1, Math.min(numberOfAvailableProcessors, model.getBoids().size()));
    }

    private void createAndAssignBoidRunners() {
        calculateNumberOfThreads();
        System.out.println("Number of threads: " + numberOfThreads);
        this.barrier = new CyclicBarrier(numberOfThreads + 1);
        final var boids = model.getBoids();
        var chunkSize = Math.max(1, boids.size() / numberOfThreads);
        var boidsGroupedInChunks = getBoidsGroupedInChunks(boids, numberOfThreads, chunkSize);

        // assigning patterns to each BoidRunner
        boidsGroupedInChunks.forEach((boidChunk) -> {
            boidRunners.add(new BoidRunner(boidChunk, model, barrier));
        });
    }

    private void checkThreadValidity() {
        if (!model.isRunning()) {
            System.out.println("Stopping threads");
            boidRunners.forEach(BoidRunner::stop);
            threads.clear();
            boidRunners.clear();
        }
        try {
            System.out.println("Thread validity check");
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            System.out.println("MAIN is updating velocity");
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePosition() {
        try {
            System.out.println("MAIN is updating position");
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
