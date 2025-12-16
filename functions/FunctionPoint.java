package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;
    private static final double EPS = Math.ulp(1.0);
    
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    
    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionPoint)) {
            return false;
        }
        FunctionPoint point = (FunctionPoint) o;
        return Math.abs(point.x - x) < EPS && Math.abs(point.y - y) < EPS;
    }
    
    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        int xHash = (int) (xBits ^ (xBits >>> 32));
        int yHash = (int) (yBits ^ (yBits >>> 32));
        return xHash ^ yHash;
    }
    
    @Override
    public Object clone() {
        return new FunctionPoint(this);
    }
}
