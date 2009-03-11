package edu.washington.cs.cse490h.donut.server;

public abstract class AbstractRunAtInterval extends Thread {
    private int interval;

    /**
     * Creates a new RunAtInterval thread. Place the code to be repeated every interval milliseconds
     * in the runClosure() body. Call kill when you want to kill the thread
     * 
     * @param interval
     *            The interval between each time runClosure is called
     */
    public AbstractRunAtInterval(int interval) {
        if (interval <= 0)
            throw new IllegalArgumentException("interval must be > 0");
        this.interval = interval;
    }

    abstract public void runClosure();

    public void kill() {
        interval = 0;
    }

    @Override
    public void run() {
        super.run();
        while (interval != 0) {
            runClosure();
            try {
                sleep(interval);
            } catch (InterruptedException e) {
            }
        }
    }
}
