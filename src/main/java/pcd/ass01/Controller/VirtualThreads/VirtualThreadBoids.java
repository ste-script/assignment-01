package pcd.ass01.Controller.VirtualThreads;

import java.util.ArrayList;
import java.util.List;

import pcd.ass01.Controller.ParallelController;
import pcd.ass01.Controller.SimulationStateHandler;
import pcd.ass01.Model.BoidsModel;
import pcd.ass01.View.BoidPattern.BoidPatterns;
import java.util.concurrent.CyclicBarrier;

public class VirtualThreadBoids implements ParallelController, SimulationStateHandler {
    private List<BoidRunner> boidRunners;
    private CyclicBarrier barrier;
    private BoidsModel model;
    private BoidPatterns boidPatterns = new BoidPatterns();

    public VirtualThreadBoids(BoidsModel model) {
        this.model = model;
        boidRunners = new ArrayList<>();
        createAndAssignBoidRunners();
    }

    public synchronized void start() {
        model.start();
        boidRunners.forEach(boidRunner -> Thread.ofVirtual().start(boidRunner));
    }

    public synchronized void update() {
        if (model.isSuspended() || !model.isRunning()) {
            return;
        }
        this.updateVelocity();
        this.updatePosition();
        this.checkThreadValidity();
    }

    public synchronized void stop() {
        model.setBoids(0);
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

    private void createAndAssignBoidRunners() {
        final var boids = model.getBoids();
        this.barrier = new CyclicBarrier(boids.size() + 1);
        // assigning patterns to each BoidRunner
        this.boidPatterns.resetPatterns();
        boids.forEach((boid) -> {
            boidRunners.add(new BoidRunner(boid, model, barrier));
        });
    }

    private void checkThreadValidity() {
        try {
            if (model.getNumberOfBoids() != model.getBoids().size()) {
                redistributeBoids();
            } else {
                barrier.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteThreads() {
        boidRunners.stream().forEach(BoidRunner::stop);
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boidRunners.clear();
    }

    private synchronized void redistributeBoids() {
        deleteThreads();
        model.setBoids(model.getNumberOfBoids());
        createAndAssignBoidRunners();
        start();
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
