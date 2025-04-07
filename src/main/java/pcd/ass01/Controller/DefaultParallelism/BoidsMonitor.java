package pcd.ass01.Controller.DefaultParallelism;

public class BoidsMonitor {
    private final int numberOfThreads;
    private int numberOfThreadWating;
    private long iteration;

    public BoidsMonitor(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfThreadWating = 0;
        this.iteration = 0;
    }

    public synchronized void await() {
        numberOfThreadWating++;
        if (this.numberOfThreads == this.numberOfThreadWating) {
            iteration = (iteration + 1) % Long.MAX_VALUE;
            numberOfThreadWating = 0;
            notifyAll();
            return;
        }
        while (true) {
            var iteration = this.iteration;
            try {
                wait();
                if (iteration != this.iteration) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
