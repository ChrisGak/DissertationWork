package com.spaceApplication.server.modeling.model;


import com.spaceApplication.shared.calculation.BasicCalculationOperation;
import com.spaceApplication.shared.calculation.BasicConsts;

import java.io.Serializable;
import java.util.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by Chris
 */
public class ElectrodynamicTetherSystemModel implements Serializable {
    private BareElectrodynamicTether tether;
    /**
     * Массы космических аппаратов и приведенная масса
     */
    private double nanoSatelliteMass;
    private double mainSatelliteMass;
    /**
     * Параметр орбиты
     * p
     */
    private double initialOrbitalParameter;

    /**
     * Высота центра масс системы
     * Hci
     */
    private double initialHeight;
    /**
     * Расстояние центра масс системы до центра Земли
     */
    private double R_0;
    /**
     * Радиусы апоцентра и перицентра
     */
    private double r_apocentre;
    private double r_pericentre;
    /**
     * Большая полуось орбиты
     * A
     */
    private double initialSemimajorAxis;
    /**
     * Эксцентриситет орбиты
     * ex
     */
    private double initialEccentricity;
    /**
     * Угол отклонения троса от вертикали
     * tetta
     */
    private double initialDeflectionAngleRadians;
    /**
     * Истинная аномалия Земли
     */
    private double initialTrueAnomalyRadians;

    private Vector startVector;


    public ElectrodynamicTetherSystemModel(BareElectrodynamicTether tether, double mainSatelliteMass, double nanoSatelliteMass, double initialHeight, double initialDeflectionAngle, double omega, double initialTrueAnomaly, double initialEccentricity double I, boolean isAccurate) {
        this.tether = tether;
        this.mainSatelliteMass = mainSatelliteMass;
        this.nanoSatelliteMass = nanoSatelliteMass;
        this.initialHeight = initialHeight;
        this.initialEccentricity = initialEccentricity;
        setInitialPositionParameters(initialHeight);
        this.initialDeflectionAngleRadians = BasicCalculationOperation.convertDegreesToRadians(initialDeflectionAngle);
        this.initialTrueAnomalyRadians = BasicCalculationOperation.convertDegreesToRadians(initialTrueAnomaly);
    }

    private void setInitialPositionParameters(double initialHeight) {
        this.R_0 = BasicConsts.EARTH_RADIUS.getValue() + initialHeight;
        this.r_pericentre = R_0;
        this.r_apocentre = r_pericentre * (1.0 + initialEccentricity) / (1.0 - initialEccentricity);
        this.initialSemimajorAxis = (r_apocentre + r_pericentre) / 2.0;
        this.initialOrbitalParameter = initialSemimajorAxis * (1.0 - BasicCalculationOperation.getSquare(initialEccentricity));
    }

    public double getOrbitalParameter(double semimajorAxis, double eccentricity) {
        return semimajorAxis * (1.0 - BasicCalculationOperation.getSquare(eccentricity));
    }

    public double getOmega(double semimajorAxis, double eccentricity) {
        return Math.sqrt(BasicConsts.K.getValue() / BasicCalculationOperation.getThirdDegree(getOrbitalParameter(semimajorAxis, eccentricity)));
    }

    public double getNu(double trueAnomaly, double eccentricity) {
        return 1.0 + eccentricity * Math.cos(trueAnomaly);
    }

    /**
     * @param semimajorAxis
     * @param trueAnomaly
     * @param eccentricity
     * @return R(A, ex, eps)
     */
    public double getCenterMassOrbit(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getOrbitalParameter(semimajorAxis, eccentricity) / getNu(trueAnomaly, eccentricity);
    }

    public double getCenterMassHeight(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) - BasicConsts.EARTH_RADIUS.getValue();
    }

    public double getVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return Math.sqrt(BasicConsts.K.getValue() / BasicConsts.EARTH_RADIUS.getValue() + getCenterMassHeight(semimajorAxis, eccentricity, trueAnomaly));
    }

    public double getRelativeVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getVelocity(semimajorAxis, eccentricity, trueAnomaly)
                - BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getValue()
                * (BasicConsts.EARTH_RADIUS.getValue() + getCenterMassHeight(semimajorAxis, eccentricity, trueAnomaly));
    }
    /**
     * Добавим ДУ для возмущенной системы для движения центра масс системы в оскулирующих элементах
     */
    public double getTrueAnomalyDiff(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return Math.sqrt(BasicConsts.K.getValue() / BasicCalculationOperation.getThirdDegree(getOrbitalParameter(semimajorAxis, eccentricity)))
                * BasicCalculationOperation.getSquare(getNu(trueAnomaly, eccentricity));
    }

    public double getOmegat(double A, double ex, double tetta, double omega, double eps) {
        return  -3.0 / 2.0
                * BasicCalculationOperation.getSquare(getTrueAnomalyDiff(A, ex, eps))
                * BasicCalculationOperation.getReverseDegree(getNu(eps, ex))
                * Math.sin(2.0 * tetta) -
                getEpstt(A, ex, eps)
                + getM(A, eps, ex) / (model.getM_e() * BasicCalculationOperation.getSquare(model.getL()));
    }



    protected double getEpst(double A, double ex, double tetta, double eps) {
        return getKoefficient(A, ex) * ((BasicConsts.K.getValue() / BasicCalculationOperation.getSquare(getR(A, eps, ex)))
                + (getA_s(A, eps, ex, tetta) * cos(eps) / ex) - getA_t(A, eps, ex, tetta) * sin(eps) / ex * (1 + getR(A, eps, ex) / getP(A, ex)));
    }

    protected double getAt(double A, double ex, double tetta, double eps) {
        return getKoefficient(A, ex) * ((2.0 * A
                / (1.0 - BasicCalculationOperation.getSquare(ex))) *
                (getA_s(A, eps, ex, tetta) * ex * sin(eps) + getA_t(A, eps, ex, tetta) * getP(A, ex) / getR(A, eps, ex)));
    }

    protected double getExt(double A, double ex, double tetta, double eps) {
        return getKoefficient(A, ex) * ((getA_s(A, eps, ex, tetta) * sin(eps) + getA_t(A, eps, ex, tetta) *
                ((1.0 + getR(A, eps, ex) / getP(A, ex)) * cos(eps) + ex * getR(A, eps, ex) / getP(A, ex))));
    }

    protected double getEpstt(double A, double ex, double eps) {
        //return (-2.0)* BasicConsts.K.getValue() * ex * sin(eps)/ BasicCalculationOperation.getThirdDegree(getP(A, ex));
        return 0;//(-2.0)* BasicConsts.K.getValue() * ex * sin(eps)/ BasicCalculationOperation.getThirdDegree(getP(A, ex));
    }

    /**
     * Вектор производных
     *
     * @param x вектор переменных
     * @return
     */
    public synchronized Vector getDiffVector(Vector x) {
        if (x.size() != 6) {
            throw new RuntimeException("Размер вектора должен быть равен 6.");
        }
        final double tetta = (double) x.get(0);
        final double omega = (double) x.get(1);
        final double eps = (double) x.get(2);
        final double A = (double) x.get(3);
        final double ex = (double) x.get(4);
        final double iter = (double) x.get(5);
        Vector vector = new Vector();
        vector.add(0, omega);
        vector.add(1, getOmegat(A, ex, tetta, omega, eps));
        vector.add(2, getEpst(A, ex, tetta, eps));
        vector.add(3, getAt(A, ex, tetta, eps));
        vector.add(4, getExt(A, ex, tetta, eps));
        vector.add(5, iter);
        return vector;
    }

    public Vector getStartVector() {
        return startVector;
    }

    public void setStartVector(double tetta, double omega, double eps, double ex, double A, double iter) {
        startVector = new Vector();
        startVector.add(0, tetta);
        startVector.add(1, omega);
        startVector.add(2, eps);
        // A
        startVector.add(3, A);
        // ex
        startVector.add(4, ex);
        startVector.add(5, iter);
    }
}
