package com.spaceApplication.server.modeling.model;

import java.io.Serializable;

/**
 * Created by Chris
 */

public class BareElectrodynamicTether implements Serializable {
    private double mass;
    /**
     * Длина троса,
     * плечо первого и второго тел
     */
    private double length;
    private double diameter;

    public BareElectrodynamicTether(double mass, double length, double diameter) {
        this.mass = mass;
        this.length = length;
        this.diameter = diameter;
    }

    public double getDiameter() {
        return diameter;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getDensity() {
        return mass / length;
    }

    public double getCrossSectionalArea() {
        return Math.PI * diameter * diameter / 4.0;
    }

    /**
     * Плечо силы 1
     *
     * @param nanoSatelliteMass
     * @param mainSatelliteMass
     * @return
     */
    public double getMomentArm1(double nanoSatelliteMass, double mainSatelliteMass) {
        return length * mainSatelliteMass
                / (nanoSatelliteMass + mainSatelliteMass);
    }

    /**
     * Плечо силы 2
     *
     * @param nanoSatelliteMass
     * @param mainSatelliteMass
     * @return
     */
    public double getMomentArm2(double nanoSatelliteMass, double mainSatelliteMass) {
        return length * nanoSatelliteMass
                / (nanoSatelliteMass + mainSatelliteMass);
    }

    /**
     * Плечо силы в точке центра масс
     *
     * @param nanoSatelliteMass
     * @param mainSatelliteMass
     * @return
     */
    public double getMassCenterMomentArm(double nanoSatelliteMass, double mainSatelliteMass) {
        return (length / 2.0) * (mainSatelliteMass - nanoSatelliteMass)
                / (nanoSatelliteMass + mainSatelliteMass);
    }
}
