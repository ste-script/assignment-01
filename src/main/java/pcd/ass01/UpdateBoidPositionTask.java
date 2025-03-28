package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns.Pattern;

public class UpdateBoidPositionTask extends AbstractBoidTask {

    public UpdateBoidPositionTask(Boid boid, BoidsModel model, Pattern pattern) {
        super(boid, model, pattern);
    }

    @Override
    public Void call() {
        boid.updatePosition(model);
        return null;
    }
}
