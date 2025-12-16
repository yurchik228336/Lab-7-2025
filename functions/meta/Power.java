package functions.meta;

import functions.Function;

public class Power implements Function {
    private final Function base;
    private final double power;

    public Power(Function base, double power) {
        this.base = base;
        this.power = power;
    }

    public double getLeftDomainBorder() {
        return base.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return base.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        double value = base.getFunctionValue(x);
        if (Double.isNaN(value)) {
            return Double.NaN;
        }
        return Math.pow(value, power);
    }
}
