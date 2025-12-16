package threads;

import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final ReadWriteSemaphore semaphore;
    private final Random random = new Random();

    public Generator(Task task, ReadWriteSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            if (isInterrupted()) {
                break;
            }
            double base = 1.0 + random.nextDouble() * 9.0;
            double left = 0.1 + random.nextDouble() * 99.9;
            double right = 100.0 + random.nextDouble() * 100.0;
            double step = Math.max(random.nextDouble(), 1e-4);
            try {
                semaphore.beginWrite();
                task.setFunction(new Log(base));
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
                semaphore.endWrite();
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
            System.out.println("Source " + left + " " + right + " " + step);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}

