package com.spaceApplication.shared.calculation;

/**
 * Created by Chris
 */
public enum BasicConsts {
    EARTH_RADIUS(6371.02E3, "Радиус Земли", "м"),
    EARTH_ROTATION_ANGULAR_VELOCITY(7.2921158553E-5, "Угловая скорость вращения Земли", " м/c"),
    INITIAL_ECCENTICITY(0.0167, "Эксцентриситет орбиты", ""),
    TRUE_ANOMALY(0, "Истинная аномалия Земли", ""),
    K(398600E9, "Гравитационная постоянная Земли"," м"),
    MU(8E15, "Магнитный момент земного диполя", " Тл*м^3"),
    ELECTRON_MASS(9.10938356E-31, "Масса электрона", " кг"),
    ELECTON_CHARGE(-1.602176565E-19, "Заряд электрона", " Кл"),
    PLASMA_CONCENTRATION(1E11, "Концентрация плазмы", " м^(-3)"),
    ALUMINUM_CONDUCTIVITY(3.4014E7, "Электропроводимость алюминия", " ом^(-1)*м^(-1)");

    private final double value;   // в системе СИ
    private final String description;
    private final String unitMeasurement;

    BasicConsts(double value, String description, String unitMeasurement) {
        this.value = value;
        this.description = description;
        this.unitMeasurement = unitMeasurement;
    }

    public double getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getUnitMeasurement() {
        return unitMeasurement;
    }
}
