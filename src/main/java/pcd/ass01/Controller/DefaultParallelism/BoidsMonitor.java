package pcd.ass01.Controller.DefaultParallelism;

public class BoidsMonitor {
    private final int numberOfThreads;
    private int threadWaiting;
    private long iteration;

    public BoidsMonitor(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.threadWaiting = 0;
        this.iteration = 0;
    }

    public synchronized void await() {
        threadWaiting++;
        if (this.numberOfThreads == this.threadWaiting) {
            iteration++;
            threadWaiting = 0;
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
