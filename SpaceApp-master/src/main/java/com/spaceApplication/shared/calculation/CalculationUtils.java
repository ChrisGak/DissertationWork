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

    public static Vector scalarMultiplyAndAddVectors(Vector x, Vector y, double z) {
        if (x.size() != y.size()) {
            throw new VectorSizeCustomException("Вектора должны быть одной размерности");
        }
        Vector result = new Vector(x.size());
        for (int i = 0; i < x.size(); i++) {
            result.add((Double) y.get(i) + z * (Double) x.get(i));
        }
        return result;
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
}
