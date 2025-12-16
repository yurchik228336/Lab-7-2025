package functions.meta;

import functions.Function;

public class Sum implements Function {
    private final Function a;
    private final Function b;

    public Sum(Function a, Function b) {
        this.a = a;
        this.b = b;
    }

    public double getLeftDomainBorder() {
        return Math.max(a.getLeftDomainBorder(), b.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        return Math.min(a.getRightDomainBorder(), b.getRightDomainBorder());
    }

    public double getFunctionValue(double x) {
        double va = a.getFunctionValue(x);
        double vb = b.getFunctionValue(x);
        if (Double.isNaN(va) || Double.isNaN(vb)) {
            return Double.NaN;
        }
        return va + vb;
    }
}
