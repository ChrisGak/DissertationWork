package com.spaceApplication.server.modeling.model;

import com.spaceApplication.server.modeling.VectorSizeCustomException;
import com.spaceApplication.shared.calculation.BasicConsts;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;

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
     * Угол отклонения троса от вертикали
     * tetta
     */
    private double tetherVerticalDeflectionAngleRadians;
    /**
     * Истинная аномалия Земли
     */
    private double initialTrueAnomalyRadians;
    /**
     * Ток короткого замыкания
     */
    private double shortCircuitCurrent = 0.1;
    private Vector startVector;

    public ElectrodynamicTetherSystemModel(BareElectrodynamicTether tether, double mainSatelliteMass, double nanoSatelliteMass,
                                           double initialHeight, double tetherVerticalDeflectionAngle, double initialTrueAnomaly, double initialEccentricity) {
        this.tether = tether;
        this.mainSatelliteMass = mainSatelliteMass;
        this.nanoSatelliteMass = nanoSatelliteMass;
        this.initialHeight = initialHeight;
        this.initialEccentricity = initialEccentricity;
        setInitialPositionParameters(initialHeight);
        this.tetherVerticalDeflectionAngleRadians = Math.toRadians(tetherVerticalDeflectionAngle);
        this.initialTrueAnomalyRadians = Math.toRadians(initialTrueAnomaly);
        setStartVector(tetherVerticalDeflectionAngle, tetherVerticalDeflectionAngle, initialTrueAnomaly, initialEccentricity, initialSemimajorAxis);
    }

    public double getInitialSemimajorAxis() {
        return initialSemimajorAxis;
    }

    public void printInitialState() {
        System.out.println("_________________________________________");
        System.out.println("    Tether system model initial state:   ");
        System.out.println("    tetherVerticalDeflectionAngle = " + tetherVerticalDeflectionAngleRadians);
        System.out.println("    initialTrueAnomaly = " + initialTrueAnomalyRadians);
        System.out.println("    initialSemimajorAxis = " + initialSemimajorAxis);
        System.out.println("    initialEccentricity = " + initialEccentricity);
        System.out.println("    mainSatelliteMass = " + mainSatelliteMass);
        System.out.println("    nanoSatelliteMass = " + nanoSatelliteMass);
        System.out.println("    tether mass = " + tether.getMass());
        System.out.println("    tether length = " + tether.getLength());
        System.out.println("    tether diameter = " + tether.getDiameter());
        System.out.println("_____________________________");
    }

    public BareElectrodynamicTether getTether() {
        return tether;
    }

    private void setInitialPositionParameters(double initialHeight) {
        this.R_0 = BasicConsts.EARTH_RADIUS.getValue() + initialHeight;
        this.r_pericentre = R_0;
        this.r_apocentre = r_pericentre * (1 + initialEccentricity) / (1 - initialEccentricity);
        this.initialSemimajorAxis = (r_apocentre + r_pericentre) / 2.0;
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
        double result = semimajorAxis * (1 - Math.pow(eccentricity, 2));
        return result;
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

    public double getNu(double trueAnomaly, double eccentricity) {
        double result = 1 + eccentricity * Math.cos(trueAnomaly);
        return result;
    }

    /**
     * @param semimajorAxis
     * @param trueAnomaly
     * @param eccentricity
     * @return R(A, ex, eps)
     */
    public double getCenterMassOrbit(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = getOrbitalParameter(semimajorAxis, eccentricity) / getNu(trueAnomaly, eccentricity);
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
        double result = Math.sqrt(BasicConsts.K.getValue() /
                (BasicConsts.EARTH_RADIUS.getValue() + getMassCenterHeight(semimajorAxis, eccentricity, trueAnomaly)));
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
                - BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getValue()
                * (BasicConsts.EARTH_RADIUS.getValue() + getMassCenterHeight(semimajorAxis, eccentricity, trueAnomaly));
        return result;
    }

    /**
     * Добавим ДУ для возмущенной системы для движения центра масс системы в оскулирующих элементах
     */
    public double getTrueAnomalyDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        double result = (BasicConsts.K.getValue() / Math.pow(getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly), 2)
                + getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle) * Math.cos(trueAnomaly) / eccentricity
                - getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * (1 + getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) / getOrbitalParameter(semimajorAxis, eccentricity))
                * Math.sin(trueAnomaly) / eccentricity)
                * getK(semimajorAxis, eccentricity);
        return result;
    }

    public double getTrueAnomalySecondDiff(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = -2 * BasicConsts.K.getValue()
                * eccentricity
                * Math.sin(trueAnomaly)
                / Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3);
        return result;
    }

    public double getDeflectionAngleSecondDiff(double semimajorAxis, double eccentricity, double tetherVerticalDeflectionAngle, double omega, double trueAnomaly) {
        double result = (-3.0 / 2.0)
                * Math.pow(getTrueAnomalyDiff(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle), 2)
                * Math.pow(getNu(trueAnomaly, eccentricity), -1.0)
                * Math.sin(2 * tetherVerticalDeflectionAngle)
                - getTrueAnomalySecondDiff(semimajorAxis, eccentricity, trueAnomaly)
                + (getFullMoment(semimajorAxis, eccentricity, tetherVerticalDeflectionAngle)
                / (getReducedMass() * Math.pow(tether.getLength(), 2)));
        return result;
    }

    //todo check with the doc
    public double getDeflectionAngleDiff(double deflectionAngleDiff) {
        return deflectionAngleDiff;
    }

    public double getSemimajorAxisDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        double result = getK(semimajorAxis, eccentricity)
                * (getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * eccentricity
                * Math.sin(trueAnomaly)
                + getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                * getOrbitalParameter(semimajorAxis, eccentricity)
                / getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly))
                * (2 * semimajorAxis / (1 - Math.pow(eccentricity, 2)));
        return result;
    }

    public double getEccentricityDiff(double semimajorAxis, double eccentricity, double trueAnomaly, double tetherVerticalDeflectionAngle) {
        double result = getK(semimajorAxis, eccentricity)
                * ((getRadialAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                    * Math.sin(trueAnomaly))
                    + (getTransversalAcceleration(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                    * (1 + getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) / getOrbitalParameter(semimajorAxis, eccentricity))
                    * Math.cos(trueAnomaly)
                    + (eccentricity * getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly)) / getOrbitalParameter(semimajorAxis, eccentricity))
                );
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
                / Math.pow(BasicConsts.EARTH_RADIUS.getValue()
                + getMassCenterHeight(semimajorAxis, eccentricity, trueAnomaly), 3);
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

    //todo what's this
    public double getL0(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = Math.pow(9 * Math.PI
                * BasicConsts.ELECTRON_MASS.getValue()
                * BasicConsts.ALUMINUM_CONDUCTIVITY.getValue() * BasicConsts.ALUMINUM_CONDUCTIVITY.getValue()
                * getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * tether.getCrossSectionalArea()
                / (128 * Math.abs(Math.pow(BasicConsts.ELECTON_CHARGE.getValue(), 3))
                * BasicConsts.PLASMA_CONCENTRATION.getValue() * BasicConsts.PLASMA_CONCENTRATION.getValue()), 1.0 / 3.0);
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

    public void setStartVector(double tetherVerticalDeflectionAngle, double tetherVerticalDeflectionAngleDiff, double trueAnomaly, double eccentricity, double semimajorAxis) {
        startVector = new Vector();
        // tetta
        startVector.add(0, tetherVerticalDeflectionAngle);
        // omega
        startVector.add(1, tetherVerticalDeflectionAngleDiff);
        startVector.add(2, trueAnomaly);
        // A
        startVector.add(3, semimajorAxis);
        // ex
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
     * Переход к безразмерным величинам
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
        double result = getShortCircuitCurrent()
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
        return Math.pow(2 * iC - iC * iC, 2.0 / 3.0);
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
    public double getS(double semimajorAxis, double eccentricity, double trueAnomaly, double potential) throws IllegalArgumentException {
        SimpsonIntegrator integrator = new SimpsonIntegrator();
        double potentialA = getPotentialA(semimajorAxis, eccentricity, trueAnomaly);

        UnivariateFunction function = new UnivariateFunction() {

            public double value(double p) {
                return Math.pow((Math.pow(p, 3.0 / 2.0)
                        - Math.pow(potentialA, 3.0 / 2.0)
                        + 1), -1.0 / 2.0);
            }
        };
        double result = integrator.integrate(64, function, potential, potentialA);
        return result;
    }

    /**
     * Положение центра масс относительно точки А
     * phiA
     *
     * @return
     */
    public double getMassCenterRelativePosition() {
        double mC = nanoSatelliteMass;
        double mA = mainSatelliteMass;
        double result = (1.0 / getTotalSystemMass())
                * (mC + tether.getMass() / 2.0)
                * tether.getLength();
        return result;
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
        double result = tether.getLength() / getL0(semimajorAxis, eccentricity, trueAnomaly);
        return result;
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
        double result = getS(semimajorAxis, eccentricity, trueAnomaly, 0) * getL0(semimajorAxis, eccentricity, trueAnomaly);
        return result;
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
        double result = getRelativeLength(semimajorAxis, eccentricity, trueAnomaly) * getL0(semimajorAxis, eccentricity, trueAnomaly);
        return result;
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
        double result = (1.0 / 2.0) * getShortCircuitCurrent()
                * getLB(semimajorAxis, eccentricity, trueAnomaly)
                * getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly);
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
        double result = getShortCircuitCurrent()
                * (getLC(semimajorAxis, eccentricity, trueAnomaly) - getLB(semimajorAxis, eccentricity, trueAnomaly))
                * getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly);
        return result;
    }

    public double getFullForce(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = getForce1(semimajorAxis, eccentricity, trueAnomaly) + getForce2(semimajorAxis, eccentricity, trueAnomaly);
        return result;
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
        double result = getForce1(semimajorAxis, eccentricity, trueAnomaly)
                * ((2.0 / 3.0) * getLB(semimajorAxis, eccentricity, trueAnomaly)
                - getMassCenterRelativePosition());
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
        double result = getForce2(semimajorAxis, eccentricity, trueAnomaly)
                * ((getLB(semimajorAxis, eccentricity, trueAnomaly) + getLC(semimajorAxis, eccentricity, trueAnomaly)) / 2.0
                - getMassCenterRelativePosition());
        return result;
    }

    public double getFullMoment(double semimajorAxis, double eccentricity, double trueAnomaly) {
        double result = getMoment1(semimajorAxis, eccentricity, trueAnomaly) + getMoment2(semimajorAxis, eccentricity, trueAnomaly);
        return result;
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
        double result = getFullForce(semimajorAxis, eccentricity, trueAnomaly)
                * -Math.cos(tetherVerticalDeflectionAngle);
        return result;
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
        double result = getFullForce(semimajorAxis, eccentricity, trueAnomaly)
                * Math.sin(tetherVerticalDeflectionAngle);
        return result;
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
        double result = getTransversalForce(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                / getTotalSystemMass();
        return result;
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
        double result = getRadialForce(semimajorAxis, eccentricity, trueAnomaly, tetherVerticalDeflectionAngle)
                / getTotalSystemMass();
        return result;
    }

    /**
     * Приведенная масса трех тел
     *
     * @return
     */
    public double getReducedMass() {
        double result = ((mainSatelliteMass * nanoSatelliteMass)
                + ((mainSatelliteMass + nanoSatelliteMass) * tether.getMass()) / 3
                +(tether.getMass() * tether.getMass() / 12))
                / (mainSatelliteMass + nanoSatelliteMass + tether.getMass());
        return result;

    }

    public double getInitialHeight() {
        return initialHeight;
    }

    public double getInitialOrbitalParameter() {
        return initialOrbitalParameter;
    }
}
