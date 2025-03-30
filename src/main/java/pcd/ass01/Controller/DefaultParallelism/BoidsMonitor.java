package pcd.ass01.Controller.DefaultParallelism;

public class BoidsMonitor {
    private final int numberOfThreads;
    private int threadWaiting;

    public BoidsMonitor(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.threadWaiting = 0;
    }

    public synchronized void await() {
        threadWaiting++;
        if (this.numberOfThreads - this.threadWaiting == 0) {
            notifyAll();
        }
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadWaiting--;
    }
}
