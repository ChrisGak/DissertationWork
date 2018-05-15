package com.spaceApplication.client.space.model;

import com.spaceApplication.shared.calculation.BasicConsts;
import org.moxieapps.gwt.highcharts.client.Point;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Chris
 */
public class ElectrodynamicTetherSystemModelClient implements Serializable {
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

    public void setInitialHeight(double initialHeight){
        this.initialHeight = initialHeight;
    }
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

    public void setInitialEccentricity(double initialEccentricity){
        this.initialEccentricity = initialEccentricity;
    }
    /**
     * Истинная аномалия Земли
     */
    private double initialTrueAnomalyRadians = 0;
    private Vector startVector;

    /**
     * Integration method params
     */
    private int maxIterations;
    private double integrationStep;
    private double integrationMaxStep;
    private double calculateAccuracy;

    private static double KILO = 1000;

    public ElectrodynamicTetherSystemModelClient() {
    }

    public ElectrodynamicTetherSystemModelClient(BareElectrodynamicTetherClient tether, double mainSatelliteMass, double nanoSatelliteMass,
                                                 double initialHeight, double initialTrueAnomaly, double initialEccentricity,
                                                 int maxIterations, double integrationStep, double integrationMaxStep, double calculateAccuracy) {
        this.tether = tether;
        this.mainSatelliteMass = mainSatelliteMass;
        this.nanoSatelliteMass = nanoSatelliteMass;
        this.initialHeight = initialHeight;
        this.initialEccentricity = initialEccentricity;
        this.initialTrueAnomalyRadians = Math.toRadians(initialTrueAnomaly);
        setInitialPositionParameters(initialHeight);
        this.maxIterations = maxIterations;
        this.integrationStep = integrationStep;
        this.integrationMaxStep = integrationMaxStep;
        this.calculateAccuracy = calculateAccuracy;
    }

    public static ElectrodynamicTetherSystemModelClient createDefaultTetherSystemModel() {
        BareElectrodynamicTetherClient tetherClient = new BareElectrodynamicTetherClient(0.4, 2000, 0.001, 0, 0.01);
        return new ElectrodynamicTetherSystemModelClient(tetherClient, 6, 2, 500000, 0, 0.00001, 100, 5, 10, 0.01);
    }

    public Vector getXNB1(Vector<Double> tetta) {
        Vector xnb1 = new Vector();
        double m1 = getNanoSatelliteMass();
        double m2 = getMainSatelliteMass();
        for (double value : tetta) {
            xnb1.add(m2 * (tether.getLength() / KILO) * Math.sin(value) / (m1 + m2));
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
        double m1 = getNanoSatelliteMass();
        double m2 = getMainSatelliteMass();
        for (double value : tetta) {
            xnb2.add((-m1 / (m1 + m2)) * (tether.getLength()/KILO) * Math.sin(value));
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
        double m1 = getNanoSatelliteMass();
        double m2 = getMainSatelliteMass();
        for (double value : tetta) {
            ynb1.add((-m2 / (m1+m2)) * (tether.getLength()/KILO) * Math.cos(value));
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
        double m1 = getNanoSatelliteMass();
        double m2 = getMainSatelliteMass();
        for (double value : tetta) {
            ynb2.add((m1 / (m1+m2)) * (tether.getLength()/KILO) * Math.cos(value));
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
        double result = semimajorAxis * (1.0 - Math.pow(eccentricity, 2));
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

    public double getNu(double eccentricity, double trueAnomaly) {
        double result = 1.0 + eccentricity * Math.cos(trueAnomaly);
        return result;
    }

    public double getCenterMassOrbit(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getOrbitalParameter(semimajorAxis, eccentricity) / getNu(eccentricity, trueAnomaly);
    }

    public double getMassCenterHeight(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly) - BasicConsts.EARTH_RADIUS.getValue();
    }

    public double getVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return Math.sqrt(BasicConsts.K.getValue() / getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly));
    }

    public double getRelativeVelocity(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getVelocity(semimajorAxis, eccentricity, trueAnomaly)
                - BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getValue()
                * getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly);
    }

    public double getMagneticInduction(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return BasicConsts.MU.getValue() / Math.pow(getCenterMassOrbit(semimajorAxis, eccentricity, trueAnomaly), 3);
    }

    public double getElectricFieldStrength(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getMagneticInduction(semimajorAxis, eccentricity, trueAnomaly)
                * getRelativeVelocity(semimajorAxis, eccentricity, trueAnomaly);
    }

    public double getL0(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return Math.pow(9 * Math.PI
                * BasicConsts.ELECTRON_MASS.getValue()
                * Math.pow(BasicConsts.ALUMINUM_CONDUCTIVITY.getValue(), 2)
                * getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * tether.getCrossSectionalArea()
                / (128.0 * Math.abs(Math.pow(BasicConsts.ELECTON_CHARGE.getValue(), 3))
                * Math.pow(BasicConsts.PLASMA_CONCENTRATION.getValue(), 2)), 1.0 / 3.0);
    }

    public double getElectricCurrent(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return BasicConsts.ALUMINUM_CONDUCTIVITY.getValue()
                * getElectricFieldStrength(semimajorAxis, eccentricity, trueAnomaly)
                * tether.getCrossSectionalArea();
    }

    public double getiC(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return tether.getElectricity() / getElectricCurrent(semimajorAxis, eccentricity, trueAnomaly);
    }

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


    public double getMassCenterRelativePosition() {
        double mA = mainSatelliteMass;
        double mC = nanoSatelliteMass;
        return (1.0 / getTotalSystemMass())
                * (mC + tether.getMass() / 2.0)
                * tether.getLength();
    }

    public double getRelativeLength(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return tether.getLength() / getL0(semimajorAxis, eccentricity, trueAnomaly);
    }


    public double getLC(double semimajorAxis, double eccentricity, double trueAnomaly) {
        return getRelativeLength(semimajorAxis, eccentricity, trueAnomaly) * getL0(semimajorAxis, eccentricity, trueAnomaly);
    }


    public double getReducedMass() {
        return ((mainSatelliteMass * nanoSatelliteMass)
                + ((mainSatelliteMass + nanoSatelliteMass) * tether.getMass()) / 3.0
                + (tether.getMass() * tether.getMass() / 12.0))
                / (mainSatelliteMass + nanoSatelliteMass + tether.getMass());
    }

    public double getInitialTrueAnomalyRadians() {
        return initialTrueAnomalyRadians;
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
