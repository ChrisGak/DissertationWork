package com.spaceApplication.shared.calculation;

import com.spaceApplication.server.modeling.VectorSizeCustomException;

import java.util.Vector;

/**
 * Created by Chris
 */
public class CalculationUtils {
    /**
     * функция для округления и отбрасывания "хвоста"
     */
    public static double r(double value, int k) {
        return (double) Math.round((Math.pow(10, k) * value)) / Math.pow(10, k);
    }


    /**
     * функции, которые получаются из системы
     */
    public static double f(double x, double y, double z) {
        return (Math.cos(3 * x) - 4 * y);
    }

    public static Vector scalarMultiplyAndAddVectors(Vector x, Vector y, double z) {
        if (x.size() != y.size()) {
            throw new RuntimeException("Вектора должны быть одной размерности");
        }
        Vector res = new Vector(x.size());
        for (int i = 0; i < x.size(); i++) {
            res.add(i, (Double) y.get(i) + z * (Double) x.get(i));
        }
        return res;
    }

    public static double getMaxElem(Vector x) {
        if (x.isEmpty()) {
            throw new VectorSizeCustomException("Вектор пуст");
        }
        double max = (Double) x.get(0);
        for (int i = 1; i < x.size(); i++) {
            if ((Double) x.get(i) > max)
                max = (Double) x.get(i);
        }
        return max;
    }

    public static double g(double x, double y, double z) {
        return (z);
    }

    public static double convertDegreesToRadians(double x) {
        return x * Math.PI / 180.0;
    }

    public static double convertRadiansToDegrees(double x) {
        return x * 180.0 / Math.PI;
    }

    /**
     * Obtain an angle from a given number of degrees, minutes and seconds.
     *
     * @param degrees integer number of degrees, positive.
     * @param minutes integer number of minutes, positive only between 0 and 60.
     * @param seconds integer number of seconds, positive only between 0 and 60.
     * @return a new angle whose size in degrees is given by <code>degrees</code>, <code>minutes</code> and
     * <code>seconds</code>.
     * @throws IllegalArgumentException if minutes or seconds are outside the 0-60 range.
     */
    public static double fromDMS(int degrees, int minutes, int seconds) {
        if (minutes < 0 || minutes >= 60) {
            String message = "generic.ArgumentOutOfRange";
            throw new IllegalArgumentException(message);
        }
        if (seconds < 0 || seconds >= 60) {
            String message = "generic.ArgumentOutOfRange";
            throw new IllegalArgumentException(message);
        }

        return Math.signum(degrees) * (Math.abs(degrees) + minutes / 60d + seconds / 3600d);
    }

    public static double fromDMdS(int degrees, double minutes) {
        if (minutes < 0 || minutes >= 60) {
            String message = "generic.ArgumentOutOfRange";
            throw new IllegalArgumentException(message);
        }

        return Math.signum(degrees) * (Math.abs(degrees) + minutes / 60d);
    }

    public static double getP(double A, double ex) {

        return A * (1.0 - Math.pow(ex, 2));
    }

    public static double getNu(double eps, double ex) {

        return 1.0 + ex * Math.cos(eps);
    }

    /**
     * @param A
     * @param eps
     * @param ex
     * @return Уравнение орбиты центра масс
     */
    public static Vector<Double> getRR_RZ(Vector<Double> A, Vector<Double> eps, Vector<Double> ex) {
        Vector<Double> r = new Vector<Double>();

        for (int i = 0; i < A.size(); i++) {
            r.add((getP(A.get(i), ex.get(i)) / getNu(eps.get(i), ex.get(i)) - BasicConsts.EARTH_RADIUS.getValue()));
        }
        return r;
    }
}
