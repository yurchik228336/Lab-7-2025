package functions.meta;

import functions.Function;

public class Scale implements Function {
    private final Function source;
    private final double sx;
    private final double sy;

    public Scale(Function source, double sx, double sy) {
        this.source = source;
        this.sx = sx;
        this.sy = sy;
    }

    public double getLeftDomainBorder() {
        if (sx == 0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (sx > 0) {
            return source.getLeftDomainBorder() / sx;
        }
        return source.getRightDomainBorder() / sx;
    }

    public double getRightDomainBorder() {
        if (sx == 0) {
            return Double.POSITIVE_INFINITY;
        }
        if (sx > 0) {
            return source.getRightDomainBorder() / sx;
        }
        return source.getLeftDomainBorder() / sx;
    }

    public double getFunctionValue(double x) {
        double value = source.getFunctionValue(sx * x);
        if (Double.isNaN(value)) {
            return Double.NaN;
        }
        return sy * value;
    }
}
