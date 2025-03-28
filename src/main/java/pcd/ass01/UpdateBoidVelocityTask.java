package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns.Pattern;

public class UpdateBoidVelocityTask extends AbstractBoidTask {

    public UpdateBoidVelocityTask(Boid boid, BoidsModel model, Pattern pattern) {
        super(boid, model, pattern);
    }

    @Override
    public Void call() {
        boid.updateVelocity(model);
        return null;
    }
}
