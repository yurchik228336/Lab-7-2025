package functions.basic;

import functions.Function;

public class Log implements Function {
    private final double base;
    private static final double EPS = Math.ulp(1.0);

    public Log() {
        this.base = Math.E;
    }

    public Log(double base) {
        this.base = base;
    }

    public double getLeftDomainBorder() {
        return 0;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0) return Double.NaN;
        if (Math.abs(x - 1.0) <= EPS) return 0.0;
        return Math.log(x) / Math.log(base);
    }
}
