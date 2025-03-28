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

    public BoidsMonitor(BoidsModel model) {
        this.model = model;
        this.boidPatterns = new BoidPatterns();
        calculateNumberOfThreads();
    }
    
    public synchronized void start() {
        boidRunners = Executors.newFixedThreadPool(numberOfThreads);
    }

    public synchronized void update() {
        if (model.isSuspended()) {
            return;
        }
        if (!model.isRunning()) {
            handleStop();
            return;
        }
        this.updateVelocity();
        this.updatePosition();
        this.checkNumberOfBoidsValidity();
    }

    private void handleStop() {
        boidRunners.shutdown();
        try {
            boidRunners.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Error while shutting down the boid runners");
        }
        model.setBoids(0);
    }

    private void calculateNumberOfThreads() {
        numberOfThreads = detectNumberOfThreadsUsage();
    }

    private int detectNumberOfThreadsUsage() {
        final var numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors() + 1;
        return Math.max(1, Math.min(numberOfAvailableProcessors, model.getBoids().size()));
    }

    private void checkNumberOfBoidsValidity() {
        if (model.getNumberOfBoids() != model.getBoids().size()) {
            model.setBoids(model.getNumberOfBoids());
        }
    }

    private void updateVelocity() {
        var futures = new ArrayList<Future<Void>>();
        model.getBoids().forEach((boid) -> {
            futures.add(boidRunners.submit(new UpdateBoidVelocityTask(boid, model)));
        });
        this.boidPatterns.resetPatterns();
        waitForFutures(futures);
    }

    private void updatePosition() {
        var futures = new ArrayList<Future<Void>>();
        model.getBoids().forEach((boid) -> {
            futures.add(boidRunners.submit(new UpdateBoidPositionTask(boid, model)));
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
}