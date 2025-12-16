package functions;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionPoint[] points;
    private int pointsCount;
    private static final double EPS = Math.ulp(1.0);
    
    public ArrayTabulatedFunction() {
    }
    
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        if (!(rightX > leftX)) {
            throw new IllegalArgumentException();
        }
        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount * 2];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }
    
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException();
        }
        if (!(rightX > leftX)) {
            throw new IllegalArgumentException();
        }
        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount * 2];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] sourcePoints) {
        if (sourcePoints == null || sourcePoints.length < 2) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < sourcePoints.length; i++) {
            if (sourcePoints[i - 1].getX() >= sourcePoints[i].getX()) {
                throw new IllegalArgumentException();
            }
        }
        this.pointsCount = sourcePoints.length;
        this.points = new FunctionPoint[Math.max(4, pointsCount * 2)];
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(sourcePoints[i]);
        }
    }
    
    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }
    
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(x - points[i].getX()) <= EPS) {
                return points[i].getY();
            }
        }
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            if (x >= x1 && x <= x2) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                if (Double.isNaN(y1) || Double.isNaN(y2)) {
                    return Double.NaN;
                }
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }
    
    public int getPointsCount() {
        return pointsCount;
    }
    
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        return new FunctionPoint(points[index]);
    }
    
    public void setPoint(int index, FunctionPoint point) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        double newX = point.getX();
        if (index > 0 && newX - EPS <= points[index - 1].getX()) {
            throw new IllegalArgumentException();
        }
        if (index < pointsCount - 1 && newX + EPS >= points[index + 1].getX()) {
            throw new IllegalArgumentException();
        }
        points[index] = new FunctionPoint(point);
    }
    
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        return points[index].getX();
    }
    
    public void setPointX(int index, double x) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        if (index > 0 && x - EPS <= points[index - 1].getX()) {
            throw new IllegalArgumentException();
        }
        if (index < pointsCount - 1 && x + EPS >= points[index + 1].getX()) {
            throw new IllegalArgumentException();
        }
        points[index].setX(x);
    }
    
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        return points[index].getY();
    }
    
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        points[index].setY(y);
    }
    
    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException();
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException();
        }
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }
    
    public void addPoint(FunctionPoint point) {
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - point.getX()) <= EPS) {
                throw new IllegalArgumentException();
            }
        }
        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }
        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }
        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeObject(points[i]);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount * 2];
        for (int i = 0; i < pointsCount; i++) {
            points[i] = (FunctionPoint) in.readObject();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(points[i].toString());
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TabulatedFunction)) {
            return false;
        }
        TabulatedFunction other = (TabulatedFunction) o;
        if (pointsCount != other.getPointsCount()) {
            return false;
        }
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction arrOther = (ArrayTabulatedFunction) o;
            for (int i = 0; i < pointsCount; i++) {
                if (!points[i].equals(arrOther.points[i])) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < pointsCount; i++) {
                if (!points[i].equals(other.getPoint(i))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        return hash;
    }
    
    @Override
    public Object clone() {
        FunctionPoint[] clonedPoints = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            clonedPoints[i] = (FunctionPoint) points[i].clone();
        }
        return new ArrayTabulatedFunction(clonedPoints);
    }
    
    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int currentIndex = 0;
            
            @Override
            public boolean hasNext() {
                return currentIndex < pointsCount;
            }
            
            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return new FunctionPoint(points[currentIndex++]);
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }
        
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
        
        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }
}
