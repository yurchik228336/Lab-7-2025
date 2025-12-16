package threads;

import functions.Function;
import functions.Functions;

public class Integrator extends Thread {
    private final Task task;
    private final ReadWriteSemaphore semaphore;

    public Integrator(Task task, ReadWriteSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            if (isInterrupted()) {
                break;
            }
            Function function;
            double left;
            double right;
            double step;
            try {
                semaphore.beginRead();
                function = task.getFunction();
                left = task.getLeft();
                right = task.getRight();
                step = task.getStep();
                semaphore.endRead();
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
            double result = Functions.integrate(function, left, right, step);
            System.out.println("Result " + left + " " + right + " " + step + " " + result);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}

