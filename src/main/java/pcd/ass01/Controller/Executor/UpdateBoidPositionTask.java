package pcd.ass01.Controller.Executor;


import pcd.ass01.Model.Boid;
import pcd.ass01.Model.BoidsModel;

public class UpdateBoidPositionTask extends AbstractBoidTask {

    public UpdateBoidPositionTask(Boid boid, BoidsModel model) {
        super(boid, model);
    }

    @Override
    public Void call() {
        boid.updatePosition(model);
        return null;
    }
}
