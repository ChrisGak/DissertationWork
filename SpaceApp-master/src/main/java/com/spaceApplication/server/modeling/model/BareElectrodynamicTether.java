package com.spaceApplication.server.modeling.model;

        import java.io.Serializable;

/**
 * Created by Chris
 */

public class BareElectrodynamicTether implements Serializable {
    private double mass;
    private double length;
    private double diameter;
    /**
     * Угол отклонения троса от вертикали
     * tetta
     */
    private double deflectionAngleRadians;
    private double electricity;

    public BareElectrodynamicTether(double mass, double length, double diameter, double deflectionAngle, double electricity) {
        this.mass = mass;
        this.length = length;
        this.diameter = diameter;
        this.deflectionAngleRadians =  Math.toRadians(deflectionAngle);
        this.electricity = electricity;
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

    public double getDeflectionAngleRadians() {
        return deflectionAngleRadians;
    }

    public void setDeflectionAngleRadians(double deflectionAngleRadians) {
        this.deflectionAngleRadians = deflectionAngleRadians;
    }

    public double getDeflectionAngleDegrees(){
        return Math.toDegrees(deflectionAngleRadians);
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }
}
