package pcd.ass01.Controller.Executor;

import pcd.ass01.Model.Boid;
import pcd.ass01.Model.BoidsModel;

public class UpdateBoidVelocityTask extends AbstractBoidTask {

    public UpdateBoidVelocityTask(Boid boid, BoidsModel model) {
        super(boid, model);
    }

    @Override
    public Void call() {
        boid.updateVelocity(model);
        return null;
    }
}
