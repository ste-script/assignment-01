package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BoidsMonitor {

    private ExecutorService boidRunners;
    private BoidsModel model;
    private int numberOfThreads;
    private BoidPatterns boidPatterns = new BoidPatterns();
    private boolean startMode;

    public BoidsMonitor(BoidsModel model) {
        this.model = model;
        startMode = BoidsSimulation.getPatternBased();
    }

    public synchronized void start() {
        calculateNumberOfThreads();
        boidRunners = Executors.newFixedThreadPool(numberOfThreads);
    }

    public synchronized void update() {
        if (model.isSuspended()) {
            return;
        }
        this.updateVelocity();
        this.updatePosition();
        this.checkThreadValidity();
        this.checkModeChanged();
    }

    private void checkModeChanged() {
        if (startMode != BoidsSimulation.getPatternBased()) {
            startMode = BoidsSimulation.getPatternBased();
            redistributeBoids();
        }
    }

    private void calculateNumberOfThreads() {
        var numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors() + 1;
        if (BoidsSimulation.getPatternBased()) {
            numberOfAvailableProcessors = BoidsSimulation.THREAD_COUNT;
        }
        numberOfThreads = Math.max(1, Math.min(numberOfAvailableProcessors, model.getBoids().size()));
    }

    private void checkThreadValidity() {
        try {
            if (model.getNumberOfBoids() != model.getBoids().size()) {
                redistributeBoids();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void redistributeBoids() {
        boidRunners.shutdown();
        try {
            boidRunners.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.setBoids(model.getNumberOfBoids());
        start();
    }

    private ArrayList<List<Boid>> getBoidsGroupedInChunks() {
        var boids = model.getBoids();
        var boidsGroupedInChunks = new ArrayList<List<Boid>>();
        var chunkSize = Math.max(1, boids.size() / numberOfThreads);

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
        var futures = new ArrayList<Future<Void>>();
        getBoidsGroupedInChunks().forEach((boidChunk) -> {
            futures.add(boidRunners.submit(new UpdateBoidVelocityTask(boidChunk, model, getAssignedPattern())));
        });
        waitForFutures(futures);
    }

    private void updatePosition() {
        var futures = new ArrayList<Future<Void>>();
        getBoidsGroupedInChunks().forEach((boidChunk) -> {
            futures.add(boidRunners.submit(new UpdateBoidPositionTask(boidChunk, model, getAssignedPattern())));
        });
        waitForFutures(futures);
    }

    private void waitForFutures(List<Future<Void>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private BoidPatterns.Pattern getAssignedPattern() {
        BoidPatterns.Pattern assignedPattern = BoidsSimulation.DEFAULT_PATTERN;
        if (BoidsSimulation.getPatternBased()) {
            assignedPattern = this.boidPatterns.getNextPattern();
        }
        return assignedPattern;
    }
}