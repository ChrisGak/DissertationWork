package com.spaceApplication.server.modeling.differentiation;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.spaceApplication.shared.calculation.CalculationUtils;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Chris
 */
public class OrbitalElements implements Serializable {
    private Vector<Double> time;
    private Vector<Double> tetherVerticalDeflectionAngle;
    private Vector<Double> tetherVerticalDeflectionAngleDiff;
    private Vector<Double> trueAnomaly;
    private Vector<Double> semimajorAxis;
    private Vector<Double> eccentricity;
    private Vector<Double> step;
    private Vector<Double> accuracy;
    private Vector<Integer> iteration;
    private int size;

    public OrbitalElements() {
        this.time = new Vector<>();
        this.tetherVerticalDeflectionAngle = new Vector<>();
        this.tetherVerticalDeflectionAngleDiff = new Vector<>();
        this.trueAnomaly = new Vector<>();
        this.semimajorAxis = new Vector<>();
        this.eccentricity = new Vector<>();
        this.step = new Vector<>();
        this.accuracy = new Vector<>();
        this.iteration = new Vector<>();
    }

    /**
     * Мапа векторов расчитанных значений
     * 0 - время
     * 1 - тетта
     * 2 - омега
     * 3 - ипсилон
     * 4 - А полуось орбиты
     * 5 - эксцентриситет орбиты
     * 6 - шаг
     * 7 - значение точности
     * 8 - номер итерации
     */
    public OrbitalElements(Vector time, Vector tetherVerticalDeflectionAngle, Vector tetherVerticalDeflectionAngleDiff,
                           Vector trueAnomaly, Vector semimajorAxis, Vector eccentricity, Vector step, Vector accuracy, Vector iteration) {
        this.time = time;
        this.tetherVerticalDeflectionAngle = tetherVerticalDeflectionAngle;
        this.tetherVerticalDeflectionAngleDiff = tetherVerticalDeflectionAngleDiff;
        this.trueAnomaly = trueAnomaly;
        this.semimajorAxis = semimajorAxis;
        this.eccentricity = eccentricity;
        this.step = step;
        this.accuracy = accuracy;
        this.iteration = iteration;
        size = time.size();
    }

    public static void printResultToConsole(OrbitalElements result) {
        /**
         * Мапа векторов расчитанных значений
         * 0 - время
         * 1 - тетта
         * 2 - омега
         * 3 - ипсилон
         * 4 - А полуось орбиты
         * 5 - эксцентриситет орбиты
         * 6 - шаг
         * 7 - значение точности
         * 8 - номер итерации
         */

        System.out.println("\nTime");
        result.getTime().forEach(z -> System.out.println((double) z));
        System.out.println("\nTetta");
        result.getTetherVerticalDeflectionAngleDegrees().forEach(z
                -> System.out.println(((double) z)));
        System.out.println("\nOmega");
        result.getTetherVerticalDeflectionAngleDiff().forEach(z -> System.out.println((double) z));
        System.out.println("\nEps");
        result.getTrueAnomalyDegrees().forEach(z -> System.out.println(((double) z)));
        System.out.println("\nA");
        result.getSemimajorAxis().forEach(z -> System.out.println((double) z));
        System.out.println("\nEx");
        result.getEccentricity().forEach(z -> System.out.println((double) z));
        System.out.println("\nStep");
        result.getStep().forEach(z -> System.out.println((double) z));
        System.out.println("\nAcuraccy");
        result.getAccuracy().forEach(z -> System.out.println((double) z));
        System.out.println("\nIter");
        result.getIteration().forEach(z -> System.out.println(z));

    }

    public static void printFirstAndLastResults(OrbitalElements result) {
        /**
         * Мапа векторов расчитанных значений
         * 0 - время
         * 1 - тетта
         * 2 - омега
         * 3 - ипсилон
         * 4 - А полуось орбиты
         * 5 - эксцентриситет орбиты
         * 6 - шаг
         * 7 - значение точности
         * 8 - номер итерации
         */
        int size = result.getTime().size() - 1;
        System.out.println("\nTime");
        System.out.println((double) result.getTime().get(0));
        System.out.println((double) result.getTime().get(size));
        System.out.println("\nTetta");
        System.out.println(CalculationUtils.convertRadiansToDegrees((double) result.getTetherVerticalDeflectionAngle().get(0)));
        System.out.println(CalculationUtils.convertRadiansToDegrees((double) result.getTetherVerticalDeflectionAngle().get(size)));
        System.out.println("\nOmega");
        System.out.println((double) result.getTetherVerticalDeflectionAngleDiff().get(0));
        System.out.println((double) result.getTetherVerticalDeflectionAngleDiff().get(size));
        System.out.println("\nEps");
        System.out.println(CalculationUtils.convertRadiansToDegrees((double) result.getTrueAnomaly().get(0)));
        System.out.println(CalculationUtils.convertRadiansToDegrees((double) result.getTrueAnomaly().get(size)));
        System.out.println("\nA");
        System.out.println((double) result.getSemimajorAxis().get(0));
        System.out.println((double) result.getSemimajorAxis().get(size));
        System.out.println("\nEx");
        System.out.println((double) result.getEccentricity().get(0));
        System.out.println((double) result.getEccentricity().get(size));
        System.out.println("\nStep");
        System.out.println((double) result.getStep().get(0));
        System.out.println((double) result.getStep().get(size));
        System.out.println("\nAcuraccy");
        System.out.println((double) result.getAccuracy().get(0));
        System.out.println((double) result.getAccuracy().get(size));
        System.out.println("\nIter");
        System.out.println(result.getIteration().get(0));
        System.out.println(result.getIteration().get(size));

    }

    public static void printResultsWithSignsToConsole(OrbitalElements result, int signes) {
        /**
         * Мапа векторов расчитанных значений
         * 0 - время
         * 1 - тетта
         * 2 - омега
         * 3 - ипсилон
         * 4 - А полуось орбиты
         * 5 - эксцентриситет орбиты
         * 6 - шаг
         * 7 - значение точности
         * 8 - номер итерации
         */

        System.out.println("\nTime");
        result.getTime().forEach(z -> System.out.println(CalculationUtils.r((double) z, signes)));
        System.out.println("\nTetta");
        result.getTetherVerticalDeflectionAngleDegrees().forEach(z -> System.out.println(((double) z)));
        System.out.println("\nOmega");
        result.getTetherVerticalDeflectionAngleDiff().forEach(z -> System.out.println(CalculationUtils.r((double) z, signes)));
        System.out.println("\nEps");
        result.getTrueAnomalyDegrees().forEach(z -> System.out.println(((double) z)));
        System.out.println("\nA");
        result.getSemimajorAxis().forEach(z -> System.out.println(CalculationUtils.r((double) z, signes)));
        System.out.println("\nEx");
        result.getEccentricity().forEach(z -> System.out.println(CalculationUtils.r((double) z, signes)));
        System.out.println("\nStep");
        result.getStep().forEach(z -> System.out.println(CalculationUtils.r((double) z, signes)));
        System.out.println("\nAcuraccy");
        result.getAccuracy().forEach(z -> System.out.println(CalculationUtils.r((double) z, signes)));
        System.out.println("\nIter");
        result.getIteration().forEach(z -> System.out.println(z));

    }

    public int getSize() {
        return time.size();
//        return size;
    }

    public Vector getTime() {
        return time;
    }

    public Vector getTetherVerticalDeflectionAngle() {
        return tetherVerticalDeflectionAngle;
    }

    public Vector getTetherVerticalDeflectionAngleDegrees() {
        Vector degrees = new Vector(tetherVerticalDeflectionAngle.size());
        for (int i = 0; i < tetherVerticalDeflectionAngle.size(); i++) {
            degrees.add(CalculationUtils.convertRadiansToDegrees(tetherVerticalDeflectionAngle.get(i)));
        }
        return degrees;
    }

    public Vector getTetherVerticalDeflectionAngleDiff() {
        return tetherVerticalDeflectionAngleDiff;
    }

    public Vector getTrueAnomaly() {

        return trueAnomaly;
    }

    public Vector getTrueAnomalyDegrees() {
        Vector degrees = new Vector();
        for (int i = 0; i < degrees.size(); i++) {
            degrees.add(CalculationUtils.convertRadiansToDegrees(trueAnomaly.get(i)));
        }
        return degrees;
    }

    public Vector getSemimajorAxis() {
        return semimajorAxis;
    }

    public Vector getEccentricity() {
        return eccentricity;
    }

    public Vector getStep() {
        return step;
    }

    public Vector getAccuracy() {
        return accuracy;
    }

    public Vector getIteration() {
        return iteration;
    }

}
