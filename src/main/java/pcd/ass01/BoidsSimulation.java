package pcd.ass01;

import pcd.ass01.BoidPattern.BoidPatterns;
import pcd.ass01.BoidPattern.ShapeType;

import java.awt.*;

public class BoidsSimulation {

	final static int N_BOIDS = 1500;

	final static double SEPARATION_WEIGHT = 1.0;
	final static double ALIGNMENT_WEIGHT = 1.0;
	final static double COHESION_WEIGHT = 1.0;

	final static int ENVIRONMENT_WIDTH = 1000;
	final static int ENVIRONMENT_HEIGHT = 1000;
	static final double MAX_SPEED = 4.0;
	static final double PERCEPTION_RADIUS = 50.0;
	static final double AVOID_RADIUS = 20.0;

	final static int SCREEN_WIDTH = 800;
	final static int SCREEN_HEIGHT = 800;

	final static BoidPatterns.Pattern DEFAULT_PATTERN = new BoidPatterns.Pattern(Color.BLUE, ShapeType.CIRCLE);
	/**
	 * These two variables handle the test mode:
	 * - THREAD_COUNT works only if PATTERN_BASED is set to true
	 * - THREAD_COUNT must a number smaller than the total amount of patterns
	 */
	final static boolean PATTERN_BASED = true;
	final static int THREAD_COUNT = 10;

	public static void main(String[] args) {
		var model = new BoidsModel(
				N_BOIDS,
				SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
				ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
				MAX_SPEED,
				PERCEPTION_RADIUS,
				AVOID_RADIUS);
		var sim = new BoidsSimulator(model);
		var view = new BoidsView(model, SCREEN_WIDTH, SCREEN_HEIGHT);
		sim.attachView(view);
		sim.runSimulation();
	}
}
