package com.spaceApplication.server.modeling.model;


import com.spaceApplication.shared.calculation.BasicCalculationOperation;
import com.spaceApplication.shared.calculation.BasicConsts;

import java.io.Serializable;
import java.util.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static javax.swing.text.html.HTML.Tag.I;

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
    private double initialDeflectionAngleRadians;
    /**
     * Истинная аномалия Земли
     */
    private double initialTrueAnomalyRadians;

    /**
     * Ток короткого замыкания
     */
    private double shortCircuitCurrent = 0.1;

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
        this.r_apocentre = r_pericentre * (1 + initialEccentricity) / (1 - initialEccentricity);
        this.initialSemimajorAxis = (r_apocentre + r_pericentre) / 2;
        this.initialOrbitalParameter = initialSemimajorAxis * (1 - Math.pow(initialEccentricity, 2));
    }

    public double getOrbitalParameter(double semimajorAxis, double eccentricity) {
        return semimajorAxis * (1 - Math.pow(eccentricity, 2));
    }

    public double getOmega(double semimajorAxis, double eccentricity) {
        return Math.sqrt(BasicConsts.K.getValue() / Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3));
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
        return Math.sqrt(BasicConsts.K.getValue() / Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3))
                * Math.pow(getNu(trueAnomaly, eccentricity), 2);
    }

    public double getTrueAnomalySecondDiff(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return -2 * BasicConsts.K.getValue()
                * eccentricity
                * Math.sin(trueAnomaly)
                / Math.pow(getOrbitalParameter(semimajorAxis, eccentricity), 3);
    }

    //todo check with the doc
    public double getOmegat(double semimajorAxis, double eccentricity, double deflectionAngle, double omega, double trueAnomaly) {
        return -3 / 2
                * Math.pow(getTrueAnomalyDiff(semimajorAxis, eccentricity, trueAnomaly), 2)
                * Math.pow(getNu(trueAnomaly, eccentricity), -1)
                * Math.sin(2 * deflectionAngle)
                - getTrueAnomalySecondDiff(semimajorAxis, eccentricity, trueAnomaly);
//                + getM(A, eps, ex) / (model.getM_e() * BasicCalculationOperation.getSquare(model.getL()));
    }

    //todo check with the doc
    public double getDeflectionAngleDiff(double omegatetta) {
        return omegatetta;
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
                + getCenterMassHeight(semimajorAxis, eccentricity, trueAnomaly), 3);
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
     *
     * @return
     */
    public double massCenterRelativePosition() {
        double mC = mainSatelliteMass;
        double mA = nanoSatelliteMass;
        return (1 / getTotalSystemMass())
                * (mC + tether.getMass() / 2)
                * tether.getLength();
    }

    public double getRelativeLength(double semimajorAxis, double eccentricity, double trueAnomaly){
        return tether.getLength() / getL0(semimajorAxis, eccentricity, trueAnomaly);
    }

}
