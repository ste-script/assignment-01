package pcd.ass01;


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
