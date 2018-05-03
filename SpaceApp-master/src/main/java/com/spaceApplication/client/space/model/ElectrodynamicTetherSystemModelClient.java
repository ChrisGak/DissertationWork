package com.spaceApplication.client.space.model;

import com.spaceApplication.shared.calculation.BasicConsts;
import org.moxieapps.gwt.highcharts.client.Point;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Chris
 */
public class ElectrodynamicTetherSystemModelClient implements Serializable {
    private static double defaultDeflectionAngle = 0;
    private static double defaultInitialTrueAnomaly = 0;
    private static double defaultInitialEccentricity = 0.0167;

    private int maxIterations;
    private double integrationStep;
    private double integrationMaxStep;
    private double calculateAccuracy;
    private BareElectrodynamicTetherClient tether;
    /**
     * Массы космических аппаратов
     */
    private double mainSatelliteMass;
    private double nanoSatelliteMass;

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
    private double tetherVerticalDeflectionAngleRadians;
    private double tetherVerticalDeflectionAngleDiff;
    /**
     * Истинная аномалия Земли
     */
    private double initialTrueAnomalyRadians;
    /**
     * Ток короткого замыкания
     */
    private double shortCircuitCurrent = 0.1;
    private Vector startVector;

    public ElectrodynamicTetherSystemModelClient() {
    }

    public ElectrodynamicTetherSystemModelClient(BareElectrodynamicTetherClient tether, double mainSatelliteMass, double nanoSatelliteMass,
                                                 double initialHeight, double tetherVerticalDeflectionAngle, double initialTrueAnomaly, double initialEccentricity,
                                                 int maxIterations, double integrationStep, double integrationMaxStep, double calculateAccuracy) {
        this.tether = tether;
        this.mainSatelliteMass = mainSatelliteMass;
        this.nanoSatelliteMass = nanoSatelliteMass;
        this.initialHeight = initialHeight;
        this.initialEccentricity = initialEccentricity;
        setInitialPositionParameters(initialHeight);
        this.tetherVerticalDeflectionAngleRadians = Math.toRadians(tetherVerticalDeflectionAngle);
        this.initialTrueAnomalyRadians = Math.toRadians(initialTrueAnomaly);
        setStartVector(tetherVerticalDeflectionAngle, tetherVerticalDeflectionAngle, initialTrueAnomaly, initialEccentricity, initialSemimajorAxis);
        this.maxIterations = maxIterations;
        this.integrationStep = integrationStep;
        this.integrationMaxStep = integrationMaxStep;
        this.calculateAccuracy = calculateAccuracy;
    }

    public static double getDefaultDeflectionAngle() {
        return defaultDeflectionAngle;
    }

    public static double getDefaultInitialTrueAnomaly() {
        return defaultInitialTrueAnomaly;
    }

    public static double getDefaultInitialEccentricity() {
        return defaultInitialEccentricity;
    }

    public Vector getXNB1(Vector<Double> tetta) {
        Vector xnb1 = new Vector();
        double m2_e = getMainSatelliteMass();
        for (double value : tetta) {
            xnb1.add(m2_e * tether.getLength() * Math.sin(value));
        }
        return xnb1;
    }

    public Point[] getXNB1Points(Vector tetta) {
        Vector vector = getXNB1(tetta);

        Point[] points = new Point[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            points[i] = new Point((Double) vector.get(i));
        }
        return points;
    }

    public Vector getXNB2(Vector<Double> tetta) {
        Vector xnb2 = new Vector();
        double m1_e = - getNanoSatelliteMass();
        for (double value : tetta) {
            xnb2.add(m1_e * tether.getLength() * Math.sin(value));
        }
        return xnb2;

    }

    public Point[] getXNB2Points(Vector tetta) {
        Vector vector = getXNB2(tetta);

        Point[] points = new Point[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            points[i] = new Point((Double) vector.get(i));
        }
        return points;
    }

    public Vector getYNB1(Vector<Double> tetta) {
        Vector ynb1 = new Vector();
        double m2_e = - getMainSatelliteMass();
        for (double value : tetta) {
            ynb1.add(m2_e * tether.getLength() * Math.cos(value));
        }
        return ynb1;
    }

    public Point[] getYNB1Points(Vector tetta) {
        Vector vector = getYNB1(tetta);

        Point[] points = new Point[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            points[i] = new Point((Double) vector.get(i));
        }
        return points;
    }

    public Vector getYNB2(Vector<Double> tetta) {
        Vector ynb2 = new Vector();
        double m1_e = getNanoSatelliteMass();
        for (double value : tetta) {
            ynb2.add(m1_e * tether.getLength() * Math.cos(value));
        }
        return ynb2;
    }

    public Point[] getYNB2Points(Vector tetta) {
        Vector vector = getYNB2(tetta);

        Point[] points = new Point[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            points[i] = new Point((Double) vector.get(i));
        }
        return points;
    }

    public BareElectrodynamicTetherClient getTether() {
        return tether;
    }

    public double getInitialHeight() {
        return initialHeight;
    }

    public double getInitialEccentricity() {
        return initialEccentricity;
    }

    private void setInitialPositionParameters(double initialHeight) {
        this.R_0 = BasicConsts.EARTH_RADIUS.getValue() + initialHeight;
        this.r_pericentre = R_0;
        this.r_apocentre = r_pericentre * (1 + initialEccentricity) / (1 - initialEccentricity);
        this.initialSemimajorAxis = (r_apocentre + r_pericentre) / 2;
        this.initialOrbitalParameter = initialSemimajorAxis * (1 - Math.pow(initialEccentricity, 2));
    }

    /**
     * p(A, ex)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @return
     */
    public double getOrbitalParameter(double semimajorAxis, double eccentricity) {
        return semimajorAxis * (1 - Math.pow(eccentricity, 2));
    }

    /**
     * k(A,ex)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @return
     */
    public double getK(double semimajorAxis, double eccentricity) {
        return Math.sqrt(getOrbitalParameter(semimajorAxis, eccentricity)
                / BasicConsts.K.getValue());
    }

    public double getNu(double trueAnomaly, double eccentricity) {
        return 1 + eccentricity * Math.cos(trueAnomaly);
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

    public double getMassCenterHeight(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) - BasicConsts.EARTH_RADIUS.getValue();
    }

    public double getVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return Math.sqrt(BasicConsts.K.getValue() / BasicConsts.EARTH_RADIUS.getValue() + getMassCenterHeight(semimajorAxis, eccentricity, trueAnomaly));
    }

    public double getRelativeVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getVelocity(semimajorAxis, eccentricity, trueAnomaly)
                - BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getValue()
                * (BasicConsts.EARTH_RADIUS.getValue() + getMassCenterHeight(semimajorAxis, eccentricity, trueAnomaly));
    }

    /**
     * Добавим ДУ для возмущенной системы для движения центра масс системы в оскулирующих элементах
     */
    //todo check this two elements
    public double getTrueAnomalyDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
//        return Math.sqrt(BasicConsts.K.getValue() / Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3))
//                * Math.pow(getNu(trueAnomaly, eccentricity), 2);
        return (BasicConsts.K.getValue() / Math.pow(getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly), 2)
                + getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle) * Math.cos(trueAnomaly) / eccentricity
                - getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * (1 + getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) / getOrbitalParameter(semimajorAxis, eccentricity))
                * Math.sin(trueAnomaly) / eccentricity)
                * getK(semimajorAxis, eccentricity);
    }

    public double getTrueAnomalySecondDiff(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return -2 * BasicConsts.K.getValue()
                * eccentricity
                * Math.sin(trueAnomaly)
                / Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3);
    }

    public double getDeflectionAngleSecondDiff(double semimajorAxis, double eccentricity, double tetherVerticalDeflectionAngle, double omega, double trueAnomaly) {
        return -3 / 2
                * Math.pow(getTrueAnomalyDiff(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle), 2)
                * Math.pow(getNu(trueAnomaly, eccentricity), -1)
                * Math.sin(2 * tetherVerticalDeflectionAngle)
                - getTrueAnomalySecondDiff(semimajorAxis, eccentricity, trueAnomaly)
                + (getFullMoment(semimajorAxis, eccentricity, tetherVerticalDeflectionAngle)
                / getReducedMass() * Math.pow(tether.getLength(), 2));
    }

    //todo check with the doc
    public double getDeflectionAngleDiff(double deflectionAngleDiff) {
        return deflectionAngleDiff;
    }

    public double getSemimajorAxisDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getK(semimajorAxis, eccentricity)
                * (getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * eccentricity
                * Math.sin(trueAnomaly)
                + getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * getOrbitalParameter(semimajorAxis, eccentricity)
                / getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly))
                * (2 * semimajorAxis / (1 - Math.pow(eccentricity, 2)));
    }

    public double getEccentricityDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getK(semimajorAxis, eccentricity)
                * ((getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * Math.sin(trueAnomaly))
                + (getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * (1 + getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) / getOrbitalParameter(semimajorAxis, eccentricity))
                * Math.cos(trueAnomaly)
                + (eccentricity * getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly)) / getOrbitalParameter(semimajorAxis, eccentricity))
        );
    }

    /**
     * Модуль магнитной индукции
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getMagneticInduction(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return BasicConsts.MU.getValue()
                / Math.pow(BasicConsts.EARTH_RADIUS.getValue()
                + getMassCenterHeight(semimajorAxis, eccentricity, trueAnomaly), 3);
    }

    /**
     * Напряженность электрического поля
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getElectricFieldStrength(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly)
                * getRelativeVelocity(semimajorAxis, eccentricity, trueAnomaly);
    }

    //todo what's this
    public double getL0(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return 1 / Math.pow(9 * Math.PI
                * BasicConsts.ELECTRON_MASS.getValue()
                * BasicConsts.ALUMINUM_CONDUCTIVITY.getValue() * BasicConsts.ALUMINUM_CONDUCTIVITY.getValue()
                * getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * tether.getCrossSectionalArea()
                / (128 * Math.abs(Math.pow(BasicConsts.ELECTON_CHARGE.getValue(), 3))
                * BasicConsts.PLASMA_CONCENTRATION.getValue() * BasicConsts.PLASMA_CONCENTRATION.getValue()), 3);

    }

    /**
     * Напряжение электрического поля
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getElectricFieldVoltage(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * getL0(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * Электрический ток
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getElectricCurrent(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return BasicConsts.ALUMINUM_CONDUCTIVITY.getValue()
                * getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * tether.getCrossSectionalArea();
    }

    /**
     * Вектор производных
     *
     * @param y вектор переменных
     * @return
     */
    public Vector getDiffVector(Vector y) {
        if (y.size() != 6) {
            throw new IndexOutOfBoundsException();
        }
        double tetherVerticalDeflectionAngle = (Double) y.get(0);
        double tetherVerticalDeflectionAngleDiff = (Double) y.get(1);
        double trueAnomaly = (Double) y.get(2);
        double semimajorAxis = (Double) y.get(3);
        double eccentricity = (Double) y.get(4);
        double iteration = (Double) y.get(5);
        Vector vector = new Vector();
        vector.add(0, tetherVerticalDeflectionAngleDiff);
        vector.add(1, getDeflectionAngleSecondDiff(semimajorAxis, eccentricity, tetherVerticalDeflectionAngle, tetherVerticalDeflectionAngleDiff, trueAnomaly));
        vector.add(2, getTrueAnomalyDiff(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle));
        vector.add(3, getSemimajorAxisDiff(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle));
        vector.add(4, getEccentricityDiff(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle));
        vector.add(5, iteration);
        return vector;
    }

    /**
     * Вектор переменных y
     *
     * @return
     */
    public Vector getStartVector() {
        return startVector;
    }

    public void setStartVector(double tetherVerticalDeflectionAngle, double tetherVerticalDeflectionAngleDiff, double trueAnomaly, double eccentricity, double semimajorAxis) {
        startVector = new Vector();
        startVector.add(0, tetherVerticalDeflectionAngle);
        startVector.add(1, tetherVerticalDeflectionAngleDiff);
        startVector.add(2, trueAnomaly);
        startVector.add(3, semimajorAxis);
        startVector.add(4, eccentricity);
        double iteration = 0;
        startVector.add(5, iteration);
    }

    public double getShortCircuitCurrent() {
        return shortCircuitCurrent;
    }

    public void setShortCircuitCurrent(double shortCircuitCurrent) {
        this.shortCircuitCurrent = shortCircuitCurrent;
    }

    /**
     * Безразмерный ток в точке С
     * ic(A,ex,eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getiC(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getShortCircuitCurrent()
                / getElectricCurrent(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * Безразмерный потенциал в точке А
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getPotentialA(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return Math.pow(2 * getiC(semimajorAxis, eccentricity, trueAnomaly)
                - Math.pow(getiC(semimajorAxis, eccentricity, trueAnomaly), 2), 2 / 3);
    }

    public double getNanoSatelliteMass() {
        return nanoSatelliteMass;
    }

    public void setNanoSatelliteMass(double nanoSatelliteMass) {
        this.nanoSatelliteMass = nanoSatelliteMass;
    }

    public double getMainSatelliteMass() {
        return mainSatelliteMass;
    }

    public void setMainSatelliteMass(double mainSatelliteMass) {
        this.mainSatelliteMass = mainSatelliteMass;
    }

    public double getTotalSystemMass() {
        return mainSatelliteMass + nanoSatelliteMass + tether.getMass();
    }

    /**
     * Todo
     * Зависимость длины участка троса от потенциала - интеграл
     */
    public double getS(double semimajorAxis, double eccentricity, double trueAnomaly, double potential) {
        return 0;
    }

    /**
     * Положение центра масс относительно точки А
     * phiA
     *
     * @return
     */
    public double getMassCenterRelativePosition() {
        double mC = mainSatelliteMass;
        double mA = nanoSatelliteMass;
        return (1 / getTotalSystemMass())
                * (mC + tether.getMass() / 2)
                * tether.getLength();
    }

    /**
     * sk(A,ex, eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getRelativeLength(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return tether.getLength() / getL0(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * LB
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getLB(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getS(semimajorAxis, eccentricity, trueAnomaly, 0) * getL0(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * LC
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getLC(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getRelativeLength(semimajorAxis, eccentricity, trueAnomaly) * getL0(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * Сила на участке AB
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getForce1(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return (1 / 2) * getShortCircuitCurrent()
                * getLB(semimajorAxis, eccentricity, trueAnomaly)
                * getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * Сила на участке BC
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getForce2(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getShortCircuitCurrent()
                * (getLC(semimajorAxis, eccentricity, trueAnomaly) - getLB(semimajorAxis, eccentricity, trueAnomaly))
                * getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly);
    }

    public double getFullForce(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getForce1(semimajorAxis, eccentricity, trueAnomaly) + getForce2(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * Момент силы на участке AB
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getMoment1(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getForce1(semimajorAxis, eccentricity, trueAnomaly)
                * ((2 / 3) * getLB(semimajorAxis, eccentricity, trueAnomaly)
                - getMassCenterRelativePosition());
    }

    /**
     * Момент силы на участке BC
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getMoment2(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getForce2(semimajorAxis, eccentricity, trueAnomaly)
                * ((getLB(semimajorAxis, eccentricity, trueAnomaly) + getLC(semimajorAxis, eccentricity, trueAnomaly)) / 2
                - getMassCenterRelativePosition());
    }

    public double getFullMoment(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getMoment1(semimajorAxis, eccentricity, trueAnomaly) + getMoment2(semimajorAxis, eccentricity, trueAnomaly);
    }

    /**
     * Ft
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @param tetherVerticalDeflectionAngle
     * @return
     */
    public double getTransversalForce(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getForce1(semimajorAxis, eccentricity, trueAnomaly)
                * -Math.cos(tetherVerticalDeflectionAngle);
    }

    /**
     * Fs
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @param tetherVerticalDeflectionAngle
     * @return
     */
    public double getRadialForce(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getForce2(semimajorAxis, eccentricity, trueAnomaly)
                * Math.sin(tetherVerticalDeflectionAngle);
    }

    /**
     * Трансверсальное ускорение
     * at
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @param tetherVerticalDeflectionAngle
     * @return
     */
    public double getTransversalAcceleration(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getTransversalForce(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                / getTotalSystemMass();
    }

    /**
     * Радиальное ускорение
     * as
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @param tetherVerticalDeflectionAngle
     * @return
     */
    public double getRadialAcceleration(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getRadialForce(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                / getTotalSystemMass();
    }

    /**
     * Приведенная масса трех тел
     *
     * @return
     */
    public double getReducedMass() {
        return (mainSatelliteMass * tether.getMass()
                + nanoSatelliteMass * tether.getMass()
                + mainSatelliteMass * nanoSatelliteMass)
                / (mainSatelliteMass + nanoSatelliteMass + tether.getMass());

    }

    public double getTetherVerticalDeflectionAngleRadians() {
        return tetherVerticalDeflectionAngleRadians;
    }

    public double getInitialTrueAnomalyRadians() {
        return initialTrueAnomalyRadians;
    }

    public double getTetherVerticalDeflectionAngleDiff() {
        return tetherVerticalDeflectionAngleDiff;
    }

    public void setTetherVerticalDeflectionAngleDiff(double tetherVerticalDeflectionAngleDiff) {
        this.tetherVerticalDeflectionAngleDiff = tetherVerticalDeflectionAngleDiff;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public double getIntegrationStep() {
        return integrationStep;
    }

    public void setIntegrationStep(double integrationStep) {
        this.integrationStep = integrationStep;
    }

    public double getIntegrationMaxStep() {
        return integrationMaxStep;
    }

    public void setIntegrationMaxStep(double integrationMaxStep) {
        this.integrationMaxStep = integrationMaxStep;
    }

    public double getCalculateAccuracy() {
        return calculateAccuracy;
    }

    public void setCalculateAccuracy(double calculateAccuracy) {
        this.calculateAccuracy = calculateAccuracy;
    }
}
