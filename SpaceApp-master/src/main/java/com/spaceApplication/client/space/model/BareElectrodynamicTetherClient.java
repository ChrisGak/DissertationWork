package com.spaceApplication.client.space.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Chris
 */
public class BareElectrodynamicTetherClient implements IsSerializable {
    private double mass;
    private double length;
    private double diameter;
    /**
     * Угол отклонения троса от вертикали
     * tetta
     */
    private double deflectionAngleRadians;
    private double electricity;

    public BareElectrodynamicTetherClient() {
    }

    public double getDiameter(){
        return diameter;
    }

    public void setDiameter(double diameter){
        this.diameter = diameter;
    }

    public BareElectrodynamicTetherClient(double mass, double length, double diameter, double deflectionAngle, double electricity) {
        this.mass = mass;
        this.length = length;
        this.diameter = diameter;
        this.deflectionAngleRadians =  Math.toRadians(deflectionAngle);
        this.electricity = electricity;
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

    public double getLengthKM() {
        return length / 1000.0;
    }

    public double getDensity() {
        return mass / length;
    }

    public double getCrossSectionalArea() {
        return Math.PI * diameter * diameter / 4.0;
    }

    public double getDeflectionAngleRadians() {
        return deflectionAngleRadians;
    }

    public double getDeflectionAngleDegrees() {
        return Math.toDegrees(deflectionAngleRadians);
    }

    public void setDeflectionAngleRadians(double deflectionAngleRadians) {
        this.deflectionAngleRadians = deflectionAngleRadians;
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }
}
