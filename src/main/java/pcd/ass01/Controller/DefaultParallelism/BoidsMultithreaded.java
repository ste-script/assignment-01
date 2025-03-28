package pcd.ass01.Controller.DefaultParallelism;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import pcd.ass01.BoidsSimulation;
import pcd.ass01.Controller.ParallelController;
import pcd.ass01.Controller.SimulationStateHandler;
import pcd.ass01.Model.Boid;
import pcd.ass01.Model.BoidsModel;
import pcd.ass01.View.BoidPattern.BoidPatterns;

public class BoidsMultithreaded implements ParallelController, SimulationStateHandler {
    private List<BoidRunner> boidRunners;
    private CyclicBarrier barrier;
    private BoidsModel model;
    private int numberOfThreads;
    private BoidPatterns boidPatterns = new BoidPatterns();
    private boolean startMode;

    public BoidsMultithreaded(BoidsModel model) {
        this.model = model;
        boidRunners = new ArrayList<>();
        createAndAssignBoidRunners();
        startMode = false;
    }

    public synchronized void start() {
        boidRunners.forEach(boidRunner -> Thread.ofPlatform().start(boidRunner));
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

    public synchronized void stop() {
        deleteThreads();
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

    private void checkModeChanged() {
        /*if (startMode != BoidsSimulation.getPatternBased()) {
            startMode = BoidsSimulation.getPatternBased();
            redistributeBoids();
        }*/
    }

    private void calculateNumberOfThreads() {
        var numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors() + 1 ;
        /*if (BoidsSimulation.getPatternBased()) {
            numberOfAvailableProcessors = BoidsSimulation.THREAD_COUNT;
        }*/
        numberOfThreads = Math.max(1, Math.min(numberOfAvailableProcessors, model.getBoids().size()));
    }

    private void createAndAssignBoidRunners() {
        calculateNumberOfThreads();
        this.barrier = new CyclicBarrier(numberOfThreads + 1);
        final var boids = model.getBoids();
        var chunkSize = Math.max(1, boids.size() / numberOfThreads);
        var boidsGroupedInChunks = getBoidsGroupedInChunks(boids, numberOfThreads, chunkSize);

        // assigning patterns to each BoidRunner
        this.boidPatterns.resetPatterns();
        boidsGroupedInChunks.forEach((boidChunk) -> {
            BoidPatterns.Pattern assignedPattern = BoidsSimulation.DEFAULT_PATTERN;
            /*if (BoidsSimulation.getPatternBased()) {
                assignedPattern = this.boidPatterns.getNextPattern();
            }*/
            boidRunners.add(new BoidRunner(boidChunk, model, barrier, assignedPattern));
        });
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
