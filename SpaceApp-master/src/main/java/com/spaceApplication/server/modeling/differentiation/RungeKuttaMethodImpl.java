package com.spaceApplication.server.modeling.differentiation;

import com.spaceApplication.server.modeling.model.ElectrodynamicTetherSystemModel;
import com.spaceApplication.shared.calculation.CalculationUtils;

import java.util.Vector;

/**
 * Created by Chris
 */
public class RungeKuttaMethodImpl {
    private static int capacity = 6;

    private static double calculateDSMax(double calcAccuracy, Vector DS) {
        if (DS.isEmpty()) {
            throw new RuntimeException("Вектор пуст");
        }
        Vector tempAccuracyAbs = new Vector(DS.size());
        for (int i = 0; i < DS.size(); i++) {
            tempAccuracyAbs.add(Math.abs((double) DS.get(i) / calcAccuracy));
        }

        return CalculationUtils.getMaxElem(tempAccuracyAbs);
    }

    private static Vector calculateDS(double step, Vector k1, Vector k2, Vector k3, Vector k4) {
        Vector DS = new Vector(capacity);
        for (int i = 0; i < k1.size(); i++) {
            DS.add(step * ((double) k1.get(i) - (double) k2.get(i) - (double) k3.get(i) + (double) k4.get(i)));
        }
        return DS;
    }

    private static Vector calculateResY(double step, Vector y, Vector k1, Vector k2, Vector k3, Vector k4) {
        Vector result = new Vector(capacity);
        for (int i = 0; i < k1.size(); i++) {
            result.add((double) y.get(i) + (step / 6.0) * ((double) k1.get(i) + 2.0 * (double) k2.get(i) + 2.0 * (double) k3.get(i) + (double) k4.get(i)));
        }
        return result;
    }

    private static double calculateAccuracy(double step, double DSMax, double stepMax) {
        double step_ = 0;
        if (0.1 <= DSMax && DSMax <= 1) {
            step_ = step;
        } else if (DSMax < 0.1) {
            step_ = step * 2.0;
        } else if (DSMax > 1) {
            step_ = step / 2.0;
        }

        if (step_ > stepMax) {
            return stepMax;
        } else return step_;
    }

    /**
     * Мапа векторов расчитанных значений
     * 0 - время
     * 1 - тетта
     * 2 - омега
     * 3 - ипсилон
     * 4 - А полуось орбиты - extra
     * 5 - эксцентриситет орбиты - extra
     * 6 - шаг
     * 7 - значение точности
     * 8 - номер итерации
     */

    public static OrbitalElements integrateWithVariableStep(ElectrodynamicTetherSystemModel tetherSystemModel,
                                                            int maxIteration, double step, double stepMax, double calcAccuracy) {
        OrbitalElements result;
        Vector k1, k2, k3, k4;
        Vector ys;
        double iteration = 0;
        int i = 0;
        result = new OrbitalElements();
        Vector y = tetherSystemModel.getStartVector();


        while (iteration < maxIteration) {
            iteration += step;

            k1 = tetherSystemModel.getDiffVector(y);
            k2 = tetherSystemModel.getDiffVector(CalculationUtils.scalarMultiplyAndAddVectors(k1, y, step / 2.0));
            k3 = tetherSystemModel.getDiffVector(CalculationUtils.scalarMultiplyAndAddVectors(k2, y, step / 2.0));
            k4 = tetherSystemModel.getDiffVector(CalculationUtils.scalarMultiplyAndAddVectors(k3, y, step));
            /**
             * Return result vector
             */
            ys = calculateResY(step, y, k1, k2, k3, k4);

            y = ys;
            result.getTime().add(iteration);
            result.getTetherVerticalDeflectionAngle().add(y.get(0));
            result.getTetherVerticalDeflectionAngleDiff().add(y.get(1));
            result.getTrueAnomaly().add(y.get(2));
            result.getSemimajorAxis().add(y.get(3));
            result.getEccentricity().add(y.get(4));
            result.getStep().add(step);

            double DSMax = calculateDSMax(calcAccuracy, calculateDS(step, k1, k2, k3, k4));
            result.getAccuracy().add(DSMax);
            result.getIteration().add(i);
            i++;

            step = calculateAccuracy(step, DSMax, stepMax);
        }

        return result;
    }

    public static OrbitalElements integrateWithConstStep(ElectrodynamicTetherSystemModel tetherSystemModel, int maxIter, double step, double calcAccuracy) {
        OrbitalElements result;
        Vector k1, k2, k3, k4;
        Vector ys;
        double t = 0, tt = 0;
        int i = 0;
        result = new OrbitalElements();
        Vector y = tetherSystemModel.getStartVector();
        while (t < maxIter) {
            tt = t + step;

            k1 = tetherSystemModel.getDiffVector(y);
            k2 = tetherSystemModel.getDiffVector(CalculationUtils.scalarMultiplyAndAddVectors(k1, y, step / 2.0));
            k3 = tetherSystemModel.getDiffVector(CalculationUtils.scalarMultiplyAndAddVectors(k2, y, step / 2.0));
            k4 = tetherSystemModel.getDiffVector(CalculationUtils.scalarMultiplyAndAddVectors(k3, y, step));
            /**
             * Return result vector
             */
            ys = calculateResY(step, y, k1, k2, k3, k4);

            t = tt;
            y = (Vector) ys.clone();

            result.getTime().add(i, t);
            result.getTetherVerticalDeflectionAngle().add(i, y.get(0));
            result.getTetherVerticalDeflectionAngleDiff().add(i, y.get(1));
            result.getTrueAnomaly().add(i, y.get(2));
            result.getSemimajorAxis().add(i, y.get(3));
            result.getEccentricity().add(i, y.get(4));
            result.getStep().add(i, step);

            double DSMax = calculateDSMax(calcAccuracy, calculateDS(step, k1, k2, k3, k4));

            result.getAccuracy().add(i, DSMax);
            result.getIteration().add(i, i);
            i++;
        }

        return result;
    }

}
