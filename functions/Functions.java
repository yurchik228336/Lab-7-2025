package functions;

import functions.meta.*;

public class Functions {
    private Functions() {}
    
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static double integrate(Function f, double left, double right, double step) {
        if (f == null || step <= 0 || right <= left) {
            throw new IllegalArgumentException();
        }
        double eps = Math.ulp(1.0);
        if (left + eps < f.getLeftDomainBorder() || right - eps > f.getRightDomainBorder()) {
            throw new IllegalArgumentException();
        }
        double x = left;
        double prev = f.getFunctionValue(x);
        if (Double.isNaN(prev)) {
            throw new IllegalArgumentException();
        }
        double sum = 0.0;
        while (x < right) {
            double nextX = Math.min(x + step, right);
            double nextVal = f.getFunctionValue(nextX);
            if (Double.isNaN(nextVal)) {
                throw new IllegalArgumentException();
            }
            sum += (nextX - x) * (prev + nextVal) * 0.5;
            x = nextX;
            prev = nextVal;
        }
        return sum;
    }
}
