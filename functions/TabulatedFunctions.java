package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    private TabulatedFunctions() {}
    
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }
    
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }
    
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }
    
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, int pointsCount) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException();
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException();
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, FunctionPoint[] points) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException();
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        if (!(rightX > leftX)) {
            throw new IllegalArgumentException();
        }
        double eps = Math.ulp(1.0);
        if (leftX + eps < f.getLeftDomainBorder() || rightX - eps > f.getRightDomainBorder()) {
            throw new IllegalArgumentException();
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = f.getFunctionValue(x);
        }
        return createTabulatedFunction(leftX, rightX, values);
    }
    
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass, Function f, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException();
        }
        if (!(rightX > leftX)) {
            throw new IllegalArgumentException();
        }
        double eps = Math.ulp(1.0);
        if (leftX + eps < f.getLeftDomainBorder() || rightX - eps > f.getRightDomainBorder()) {
            throw new IllegalArgumentException();
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = f.getFunctionValue(x);
        }
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }

    public static void writeTabulatedFunction(TabulatedFunction f, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.println(f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            pw.println(f.getPointX(i) + " " + f.getPointY(i));
        }
        pw.flush();
    }

    public static void outputTabulatedFunction(TabulatedFunction f, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            dos.writeDouble(f.getPointX(i));
            dos.writeDouble(f.getPointY(i));
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int n = dis.readInt();
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            pts[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(pts);
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.resetSyntax();
        st.wordChars('0', '9');
        st.wordChars('-', '-');
        st.wordChars('+', '+');
        st.wordChars('.', '.');
        st.whitespaceChars(0, ' ');
        int t = st.nextToken();
        if (t != StreamTokenizer.TT_WORD) {
            throw new IOException();
        }
        int n = Integer.parseInt(st.sval);
        if (n < 2) {
            throw new IOException();
        }
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            if (st.nextToken() != StreamTokenizer.TT_WORD) throw new IOException();
            double x = Double.parseDouble(st.sval);
            if (st.nextToken() != StreamTokenizer.TT_WORD) throw new IOException();
            double y = Double.parseDouble(st.sval);
            pts[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(pts);
    }
}