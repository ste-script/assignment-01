package pcd.ass01.Controller;

import pcd.ass01.Controller.DefaultParallelism.BoidsMultithreaded;
import pcd.ass01.Controller.Executor.BoidsExecutor;
import pcd.ass01.Model.BoidsModel;
import pcd.ass01.View.BoidsView;

import java.util.Optional;

public class BoidsSimulator {

    private final BoidsModel model;

    private Optional<BoidsView> view;
    private ParallelController parallelController;

    private static final int FRAMERATE = 25;
    private int framerate;

    public BoidsSimulator(BoidsModel model, Optional<BoidsView> view) {
        this.model = model;
        this.view = view;

        //setupBoidsMultithreaded();
        setupBoidsExecutor();
    }

    private void setupBoidsMultithreaded() {
        stopSimulation();
        BoidsMultithreaded boidsMultithreaded = new BoidsMultithreaded(model);
        parallelController = boidsMultithreaded;
        view.ifPresent(boidsView -> boidsView.setSimulationStateHandler(boidsMultithreaded));
    }

    private void setupBoidsExecutor() {
        stopSimulation();
        BoidsExecutor boidsExecutor = new BoidsExecutor(model);
        parallelController = boidsExecutor;
        view.ifPresent(boidsView -> boidsView.setSimulationStateHandler(boidsExecutor));
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
     * @param view
     */
    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        parallelController.start();
        while (true) {
            var t0 = System.currentTimeMillis();
            parallelController.update();

            if (view.isPresent()) {
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var framratePeriod = 1000 / FRAMERATE;

                if (dtElapsed < framratePeriod) {
                    try {
                        Thread.sleep(framratePeriod - dtElapsed);
                    } catch (Exception ex) {
                    }
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000 / dtElapsed);
                }
            }
        }
    }
}
