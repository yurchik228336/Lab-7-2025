package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;
    private final Random random = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            double base = 1.0 + random.nextDouble() * 9.0;
            double left = 0.1 + random.nextDouble() * 99.9;
            double right = 100.0 + random.nextDouble() * 100.0;
            double step = Math.max(random.nextDouble(), 1e-4);
            synchronized (task) {
                task.setFunction(new Log(base));
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
            }
            System.out.println("Source " + left + " " + right + " " + step);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

