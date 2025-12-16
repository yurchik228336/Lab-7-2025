package functions.meta;

import functions.Function;

public class Shift implements Function {
    private final Function source;
    private final double shiftX;
    private final double shiftY;

    public Shift(Function source, double shiftX, double shiftY) {
        this.source = source;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder() {
        return source.getLeftDomainBorder() - shiftX;
    }

    public double getRightDomainBorder() {
        return source.getRightDomainBorder() - shiftX;
    }

    public double getFunctionValue(double x) {
        double value = source.getFunctionValue(x + shiftX);
        if (Double.isNaN(value)) {
            return Double.NaN;
        }
        return value + shiftY;
    }
}
