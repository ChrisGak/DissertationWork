package com.spaceApplication.server.modeling.model;

import com.spaceApplication.server.modeling.VectorSizeCustomException;
import com.spaceApplication.shared.calculation.BasicConsts;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Chris
 */
public class ElectrodynamicTetherSystemModel implements Serializable {
    private BareElectrodynamicTether tether;
    /**
     * Массы космических аппаратов
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
     * Истинная аномалия Земли
     */
    private double initialTrueAnomalyRadians;
    private Vector startVector;

    public ElectrodynamicTetherSystemModel(BareElectrodynamicTether tether, double mainSatelliteMass, double nanoSatelliteMass,
                                           double initialHeight, double initialTrueAnomaly, double initialEccentricity) {
        this.tether = tether;
        this.mainSatelliteMass = mainSatelliteMass;
        this.nanoSatelliteMass = nanoSatelliteMass;
        this.initialHeight = initialHeight;
        this.initialTrueAnomalyRadians = Math.toRadians(initialTrueAnomaly);
        this.initialEccentricity = initialEccentricity;
        setInitialPositionParameters(initialHeight);
        //todo check this out
        double tetherVerticalDeflectionAngleDiff = 0;
        setStartVector(tether.getDeflectionAngleRadians(), tetherVerticalDeflectionAngleDiff, initialTrueAnomaly, initialSemimajorAxis, initialEccentricity);
    }

    public double getInitialSemimajorAxisKM() {
        return (initialSemimajorAxis - BasicConsts.EARTH_RADIUS.getValue()) / 1000.0;
    }

    public double getInitialTrueAnomalyRadians() {
        return initialTrueAnomalyRadians;
    }

    public double getInitialTrueAnomalyDegree() {
        return Math.toDegrees(initialTrueAnomalyRadians);
    }

    public double getInitialSemimajorAxis() {
        return initialSemimajorAxis;
    }

    public void printInitialState() {
        System.out.println("_________________________________________");
        System.out.println("    Tether system model initial state:   ");
        System.out.println("    MainSatelliteMass, kg = " + mainSatelliteMass);
        System.out.println("    NanoSatelliteMass, kg = " + nanoSatelliteMass);
        System.out.println("    Tether mass, kg = " + tether.getMass());
        System.out.println("    Tether length, m = " + tether.getLength());
        System.out.println("    Tether diameter, m = " + tether.getDiameter());
        System.out.println("    TetherVerticalDeflectionAngle, ° = " + tether.getDeflectionAngleDegrees());
        System.out.println("    Tether electricity, I = " + tether.getElectricity());
        System.out.println("    InitialTrueAnomaly, ° = " + getInitialTrueAnomalyDegree());
        System.out.println("    InitialSemimajorAxis, m = " + getInitialSemimajorAxis());
        System.out.println("    InitialEccentricity = " + initialEccentricity);
        System.out.println("_____________________________");
    }

    public BareElectrodynamicTether getTether() {
        return tether;
    }

    private void setInitialPositionParameters(double initialHeight) {
        this.R_0 = BasicConsts.EARTH_RADIUS.getValue() + initialHeight;
        this.r_pericentre = R_0;
        this.r_apocentre = r_pericentre * (1.0 + initialEccentricity) / (1.0 - initialEccentricity);
        this.initialSemimajorAxis = (r_apocentre + r_pericentre) / 2.0;
        setInitialPositionParameter(initialSemimajorAxis, initialEccentricity);
    }

    private void setInitialPositionParameter(double semimajorAxis, double eccentricity) {
        this.initialOrbitalParameter = semimajorAxis * (1.0 - Math.pow(eccentricity, 2));
    }

    /**
     * p(A, ex)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @return
     */
    public double getOrbitalParameter(double semimajorAxis, double eccentricity) {
        return semimajorAxis * (1.0 - Math.pow(eccentricity, 2));
    }

    /**
     * k(A,ex)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @return
     */
    public double getK(double semimajorAxis, double eccentricity) {
        double result = Math.sqrt(getOrbitalParameter(semimajorAxis, eccentricity)
                / BasicConsts.K.getValue());
        return result;
    }

    /**
     * nu(ex, eps)
     *
     * @param trueAnomaly
     * @param eccentricity
     * @return
     */
    public double getNu(double eccentricity, double trueAnomaly) {
        double result = 1.0 + eccentricity * Math.cos(trueAnomaly);
        return result;
    }

    /**
     * @param semimajorAxis
     * @param trueAnomaly
     * @param eccentricity
     * @return R(A, ex, eps)
     */
    public double getCenterMassOrbit(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = getOrbitalParameter(semimajorAxis, eccentricity)
                / getNu(eccentricity, trueAnomaly);
        return result;
    }

    /**
     * H(A, ex, eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getMassCenterHeight(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) - BasicConsts.EARTH_RADIUS.getValue();
        return result;
    }

    /**
     * V0(A,ex,eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = Math.sqrt(BasicConsts.K.getValue()
                / getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly));
        return result;
    }

    /**
     * Vr(A, ex, eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getRelativeVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = getVelocity(semimajorAxis, eccentricity, trueAnomaly)
                - BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getValue() * getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly);
        return result;
    }

    /**
     * Модуль магнитной индукции
     * B(A,ex,eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getMagneticInduction(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = BasicConsts.MU.getValue()
                / Math.pow(getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly), 3);
        return result;
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
        double result = getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly)
                * getRelativeVelocity(semimajorAxis, eccentricity, trueAnomaly);
        return result;
    }

    public double getL0(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = Math.pow(9 * Math.PI
                * BasicConsts.ELECTRON_MASS.getValue()
                * Math.pow(BasicConsts.ALUMINUM_CONDUCTIVITY.getValue(), 2)
                * getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * tether.getCrossSectionalArea()
                / (128.0 * Math.abs(Math.pow(BasicConsts.ELECTON_CHARGE.getValue(), 3))
                * Math.pow(BasicConsts.PLASMA_CONCENTRATION.getValue(), 2)), 1.0 / 3.0);
        return result;
    }

    /**
     * Напряжение электрического поля
     * V0(A,ex,eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getElectricFieldVoltage(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * getL0(semimajorAxis, eccentricity, trueAnomaly);
        return result;
    }

    /**
     * Электрический ток
     * I0(A,ex,eps)
     *
     * @param semimajorAxis
     * @param eccentricity
     * @param trueAnomaly
     * @return
     */
    public double getElectricCurrent(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = BasicConsts.ALUMINUM_CONDUCTIVITY.getValue()
                * getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * tether.getCrossSectionalArea();
        return result;
    }

    public void setStartVector(double tetherVerticalDeflectionAngle, double tetherVerticalDeflectionAngleDiff, double trueAnomaly, double semimajorAxis, double eccentricity) {
        startVector = new Vector();
        startVector.add(0, tetherVerticalDeflectionAngle);
        startVector.add(1, tetherVerticalDeflectionAngleDiff);
        startVector.add(2, trueAnomaly);
        startVector.add(3, semimajorAxis);
        startVector.add(4, eccentricity);
        double iteration = 0;
        startVector.add(5, iteration);
    }

    /**
     * Вектор производных
     *
     * @param y вектор переменных
     * @return
     */
    public Vector getDiffVector(Vector y) {
        if (y.size() != 6) {
            throw new VectorSizeCustomException();
        }
        double tetherVerticalDeflectionAngle = (double) y.get(0);
        double tetherVerticalDeflectionAngleDiff = (double) y.get(1);
        double trueAnomaly = (double) y.get(2);
        double semimajorAxis = (double) y.get(3);
        double eccentricity = (double) y.get(4);
        double iteration = (double) y.get(5);
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

    /**
     * Переход к характерным величинам величинам, чтобы избавиться от размерностей
     */
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
        double result = tether.getElectricity()
                / getElectricCurrent(semimajorAxis, eccentricity, trueAnomaly);
        return result;
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
        double iC = getiC(semimajorAxis, eccentricity, trueAnomaly);
        return Math.pow(2.0 * iC - iC * iC, 2.0 / 3.0);
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
     * Зависимость длины участка троса от потенциала - интеграл
     */
    public double getS(double semimajorAxis, double eccentricity, double trueAnomaly, double potential) throws IllegalArgumentException {
        IterativeLegendreGaussIntegrator iterativeLegendreGaussIntegrator = new IterativeLegendreGaussIntegrator(5, 1.0e-13, 1.0e-13, 2, 15);
        double potentialA = getPotentialA(semimajorAxis, eccentricity, trueAnomaly);
        double degree = 1.5;
        double fullDegree = -0.5;

        UnivariateFunction function = new UnivariateFunction() {

            public double value(double p) {
                return Math.pow((Math.pow(p, degree) - Math.pow(potentialA, degree) + 1.0), fullDegree);
            }
        };
        double result = iterativeLegendreGaussIntegrator.integrate(10000, function, potential, potentialA);
        return result;
    }

    /**
     * Положение центра масс относительно точки А
     * phiA
     *
     * @return
     */
    public double getMassCenterRelativePosition() {
        double mA = mainSatelliteMass;
        double mC = nanoSatelliteMass;
        return (1.0 / getTotalSystemMass())
                * (mC + tether.getMass() / 2.0)
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
        double minPotential = 0;
        return getS(semimajorAxis, eccentricity, trueAnomaly, minPotential) * getL0(semimajorAxis, eccentricity, trueAnomaly);
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
        double result1 = 0.5 * tether.getElectricity();
        double result2 = getLB(semimajorAxis, eccentricity, trueAnomaly) * getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly);
        double result = result1 * result2;
        return result;
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
        return tether.getElectricity()
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
        double result1 = getForce1(semimajorAxis, eccentricity, trueAnomaly);
        double result2 = (2.0 / 3.0) * getLB(semimajorAxis, eccentricity, trueAnomaly);
        double result3 = getMassCenterRelativePosition();
        double result = result1 * (result2 - result3);
        return result;
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
        double result1 = getForce2(semimajorAxis, eccentricity, trueAnomaly);
        double result2 = (getLB(semimajorAxis, eccentricity, trueAnomaly) + getLC(semimajorAxis, eccentricity, trueAnomaly)) / 2.0;
        double result3 = getMassCenterRelativePosition();
        double result = result1 * (result2 - result3);
        return result;
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
        return getFullForce(semimajorAxis, eccentricity, trueAnomaly)
                * (-1) * Math.cos(tetherVerticalDeflectionAngle);
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
        return getFullForce(semimajorAxis, eccentricity, trueAnomaly)
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
        return ((mainSatelliteMass * nanoSatelliteMass)
                + ((mainSatelliteMass + nanoSatelliteMass) * tether.getMass()) / 3.0
                + (tether.getMass() * tether.getMass() / 12.0))
                / (mainSatelliteMass + nanoSatelliteMass + tether.getMass());
    }

    public double getInitialHeight() {
        return initialHeight;
    }

    public double getInitialOrbitalParameter() {
        return initialOrbitalParameter;
    }

    /**
     * Добавим ДУ для возмущенной системы для движения центра масс системы в оскулирующих элементах
     */
    public double getTrueAnomalyDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return (BasicConsts.K.getValue() / Math.pow(getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly), 2)
                + getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle) * Math.cos(trueAnomaly) / eccentricity
                - getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * (1.0 + getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) / getOrbitalParameter(semimajorAxis, eccentricity))
                * Math.sin(trueAnomaly) / eccentricity)
                * getK(semimajorAxis, eccentricity);
    }

    public double getTrueAnomalyDiff_(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result1 = Math.sqrt(BasicConsts.K.getValue() / Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3));
        double result2 = Math.pow(getNu(eccentricity, trueAnomaly), 2);
        double result = result1 * result2;
        return result;
    }

    public double getTrueAnomalySecondDiff(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result1 = -2.0 * BasicConsts.K.getValue();
        double result2 = eccentricity * Math.sin(trueAnomaly);
        double result3 = Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3);
        double result = result1 * result2 / result3;
        return result;
    }

    public double getDeflectionAngleSecondDiff(double semimajorAxis, double eccentricity, double tetherVerticalDeflectionAngle, double omega, double trueAnomaly) {
        double result1 = -1.5 * Math.pow(getTrueAnomalyDiff_(semimajorAxis, eccentricity, trueAnomaly), 2)
                * Math.pow(getNu(eccentricity, trueAnomaly), -1.0) * Math.sin(2.0 * tetherVerticalDeflectionAngle);
        double result2 = getTrueAnomalySecondDiff(semimajorAxis, eccentricity, trueAnomaly);
        double result3 = getFullMoment(semimajorAxis, eccentricity, tetherVerticalDeflectionAngle) / (getReducedMass() * Math.pow(tether.getLength(), 2));
        double result = result1 - result2 + result3;
        return result;
    }

    public double getSemimajorAxisDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getK(semimajorAxis, eccentricity)
                * (getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * eccentricity
                * Math.sin(trueAnomaly)
                + getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * getOrbitalParameter(semimajorAxis, eccentricity)
                / getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly))
                * (2 * semimajorAxis / (1.0 - Math.pow(eccentricity, 2)));
    }

    public double getEccentricityDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        return getK(semimajorAxis, eccentricity)
                * ((getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * Math.sin(trueAnomaly))
                + (getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * ((1.0 + getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) / getOrbitalParameter(semimajorAxis, eccentricity)) * Math.cos(trueAnomaly)
                + (eccentricity * getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) / getOrbitalParameter(semimajorAxis, eccentricity))))
        );
    }
}
