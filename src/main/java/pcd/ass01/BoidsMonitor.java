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
    private BoidPatterns boidPatterns;
    private boolean startMode;

    public BoidsMonitor(BoidsModel model) {
        this.model = model;
        this.boidPatterns = new BoidPatterns();
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
        numberOfThreads = detectNumberOfThreadsUsage();
    }

    private int detectNumberOfThreadsUsage() {
        var numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors() + 1;
        if (BoidsSimulation.getPatternBased()) {
            numberOfAvailableProcessors = BoidsSimulation.THREAD_COUNT;
        }
        return Math.max(1, Math.min(numberOfAvailableProcessors, model.getBoids().size()));
    }

    private void checkThreadValidity() {
        if (model.getNumberOfBoids() != model.getBoids().size()) {
            redistributeBoids();
        }
    }

    private synchronized void redistributeBoids() {
        model.setBoids(model.getNumberOfBoids());
        if (numberOfThreads == detectNumberOfThreadsUsage()) {
            return;
        }
        boidRunners.shutdown();
        try {
            boidRunners.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        start();
    }

    private void updateVelocity() {
        var futures = new ArrayList<Future<Void>>();
        model.getBoids().forEach((boid) -> {
            futures.add(boidRunners.submit(new UpdateBoidVelocityTask(boid, model, getAssignedPattern())));
        });
        this.boidPatterns.resetPatterns();
        waitForFutures(futures);
    }

    private void updatePosition() {
        var futures = new ArrayList<Future<Void>>();
        model.getBoids().forEach((boid) -> {
            futures.add(boidRunners.submit(new UpdateBoidPositionTask(boid, model, getAssignedPattern())));
        });
        this.boidPatterns.resetPatterns();
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