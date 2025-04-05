package pcd.ass01.Controller;

import pcd.ass01.Controller.DefaultParallelism.PlatformThreadBoids;
import pcd.ass01.Controller.Executor.ExecutorBoids;
import pcd.ass01.Controller.Sequential.SequentialBoids;
import pcd.ass01.Controller.VirtualThreads.VirtualThreadBoids;
import pcd.ass01.Model.BoidsModel;
import pcd.ass01.View.BoidsView;

import java.util.Optional;

public class BoidsSimulator {

    private final BoidsModel model;

    private Optional<BoidsView> view;
    private ParallelController parallelController;

    private static final int FRAMERATE = 25;
    private int framerate;

    public BoidsSimulator(BoidsModel model, Optional<BoidsView> view, BoidsSimulatorType type) {
        this.model = model;
        this.view = view;

        if (type == BoidsSimulatorType.PLATFORM_THREADS) {
            setupBoidsMultithreaded();
        } else if (type == BoidsSimulatorType.EXECUTOR) {
            setupBoidsExecutor();
        } else if (type == BoidsSimulatorType.VIRTUAL_THREADS) {
            setupBoidsVirtualThreads();
        } else if (type == BoidsSimulatorType.SEQUENTIAL) {
            setupBoids(new SequentialBoids(model));
        } else {
            throw new IllegalArgumentException("Unknown simulator type: " + type);
        }
    }

    private <T extends SimulationStateHandler & ParallelController> void setupBoids(T executor) {
        stopSimulation();
        parallelController = executor;
        view.ifPresent(boidsView -> boidsView.setSimulationStateHandler(executor));
    }

    private void setupBoidsMultithreaded() {
        setupBoids(new PlatformThreadBoids(model));
    }

    private void setupBoidsExecutor() {
        setupBoids(new ExecutorBoids(model));
    }

    private void setupBoidsVirtualThreads() {
        setupBoids(new VirtualThreadBoids(model));
    }

    private void stopSimulation() {
        if (parallelController != null) {
            view.ifPresent(BoidsView::unsetSimulationStateHandler);
            parallelController.stop();
        }
    }

    /**
     * I guess that this is optional cuz in future we might run the sim
     * without the view to measure performances.
     * 
     * @param view
     */
    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        System.out.println("Starting simulation");
        parallelController.start();
        var iteration = 0;
        final var iterationsToRun = 10;
        while (model.isRunning()) {
            System.err.println("Iteration: " + iteration);
            parallelController.update();
            if (iteration > iterationsToRun) {
                stopSimulation();
                parallelController.update();
            }
            iteration++;
        }
        System.out.println("Simulation finished");
    }
}
