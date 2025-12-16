package threads;

public class ReadWriteSemaphore {
    private boolean full = false;

    public synchronized void beginWrite() throws InterruptedException {
        while (full) {
            wait();
        }
    }

    public synchronized void endWrite() {
        full = true;
        notifyAll();
    }

    public synchronized void beginRead() throws InterruptedException {
        while (!full) {
            wait();
        }
    }

    public synchronized void endRead() {
        full = false;
        notifyAll();
    }
}

