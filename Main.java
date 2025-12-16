import functions.*;
import functions.basic.Exp;
import functions.basic.Log;
import functions.basic.Cos;
import functions.basic.Sin;
import threads.Task;
import threads.SimpleGenerator;
import threads.SimpleIntegrator;
import threads.Generator;
import threads.Integrator;
import threads.ReadWriteSemaphore;
import java.util.Random;

public class Main {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        testLab7Task1();
        testLab7Task2();
        testLab7Task3();
        task1();
        nonThread();
        simpleThreads();
        complicatedThreads();
    }
    
    private static void testLab7Task1() {
        System.out.println("=== Задание 1: Итераторы ===");
        TabulatedFunction f1 = new ArrayTabulatedFunction(0, 10, 3);
        System.out.println("ArrayTabulatedFunction:");
        for (FunctionPoint p : f1) {
            System.out.println(p);
        }
        
        TabulatedFunction f2 = new LinkedListTabulatedFunction(0, 10, 3);
        System.out.println("LinkedListTabulatedFunction:");
        for (FunctionPoint p : f2) {
            System.out.println(p);
        }
        System.out.println();
    }
    
    private static void testLab7Task2() {
        System.out.println("=== Задание 2: Фабричный метод ===");
        Function f = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        System.out.println();
    }
    
    private static void testLab7Task3() {
        System.out.println("=== Задание 3: Рефлексия ===");
        TabulatedFunction f;
        
        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);
        
        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f.getClass());
        System.out.println(f);
        
        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[] {
            new FunctionPoint(0, 0),
            new FunctionPoint(10, 10)
        });
        System.out.println(f.getClass());
        System.out.println(f);
        
        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);
        System.out.println();
    }

    private static void task1() {
        Exp exp = new Exp();
        double theoretical = Math.E - 1.0;
        double step = 0.5;
        double value = Functions.integrate(exp, 0.0, 1.0, step);
        while (Math.abs(value - theoretical) >= 1e-7) {
            step /= 2.0;
            value = Functions.integrate(exp, 0.0, 1.0, step);
            if (step < 1e-12) {
                break;
            }
        }
        System.out.println("Интеграл exp на [0,1]: " + value + ", шаг: " + step);
        System.out.println("Точное значение: " + theoretical);
    }

    public static void nonThread() {
        Task task = new Task();
        task.setTasksCount(100);
        for (int i = 0; i < task.getTasksCount(); i++) {
            double base = 1.0 + RANDOM.nextDouble() * 9.0;
            double left = 0.1 + RANDOM.nextDouble() * 99.9;
            double right = 100.0 + RANDOM.nextDouble() * 100.0;
            double step = Math.max(RANDOM.nextDouble(), 1e-4);
            task.setFunction(new Log(base));
            task.setLeft(left);
            task.setRight(right);
            task.setStep(step);
            System.out.println("Source " + left + " " + right + " " + step);
            double result = Functions.integrate(task.getFunction(), task.getLeft(), task.getRight(), task.getStep());
            System.out.println("Result " + left + " " + right + " " + step + " " + result);
        }
    }

    public static void simpleThreads() {
        Task task = new Task();
        task.setTasksCount(100);
        Thread generator = new Thread(new SimpleGenerator(task));
        Thread integrator = new Thread(new SimpleIntegrator(task));
        generator.setPriority(Thread.NORM_PRIORITY + 1);
        integrator.setPriority(Thread.NORM_PRIORITY - 1);
        generator.start();
        integrator.start();
        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void complicatedThreads() {
        Task task = new Task();
        task.setTasksCount(100);
        ReadWriteSemaphore semaphore = new ReadWriteSemaphore();
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        generator.start();
        integrator.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        generator.interrupt();
        integrator.interrupt();
        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
