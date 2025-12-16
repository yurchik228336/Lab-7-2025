package threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount();) {
            Function function;
            double left;
            double right;
            double step;
            synchronized (task) {
                function = task.getFunction();
                left = task.getLeft();
                right = task.getRight();
                step = task.getStep();
            }
            if (function == null || step <= 0 || right <= left) {
                Thread.yield();
                continue;
            }
            double result = Functions.integrate(function, left, right, step);
            System.out.println("Result " + left + " " + right + " " + step + " " + result);
            i++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

