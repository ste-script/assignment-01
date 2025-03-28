package pcd.ass01.Controller;

import pcd.ass01.Controller.Executor.BoidsMonitor;
import pcd.ass01.Model.BoidsModel;
import pcd.ass01.View.BoidsView;

import java.util.Optional;

public class BoidsSimulator {

    private Optional<BoidsView> view;
    private BoidsMonitor monitor;

    private static final int FRAMERATE = 25;
    private int framerate;

    public BoidsSimulator(BoidsModel model) {
        view = Optional.empty();
        monitor = new BoidsMonitor(model);
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        monitor.start();
        while (true) {
            var t0 = System.currentTimeMillis();
            monitor.update();

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
