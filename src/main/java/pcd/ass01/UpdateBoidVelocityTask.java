package pcd.ass01;

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
