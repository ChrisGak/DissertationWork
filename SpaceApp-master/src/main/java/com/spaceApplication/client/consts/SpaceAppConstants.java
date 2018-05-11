package com.spaceApplication.client.consts;

import com.google.gwt.i18n.client.Constants;

/**
 * Created by Chris
 */
public interface SpaceAppConstants extends Constants {
    @DefaultStringValue("Создать модель")
    String modelCreation();

    @DefaultStringValue("Ввод начальных данных")
    String modelCreationTitle();

    @DefaultStringValue("Загрузить модель")
    String modelDownloading();

    @DefaultStringValue("Загрузить модель из xls-файла")
    String modelDownloadingTitle();

    @DefaultStringValue("Сохраненные результаты")
    String saved();

    @DefaultDoubleValue(2)
    double minSatelliteMass();

    @DefaultDoubleValue(10)
    double maxSatelliteMass();

    @DefaultDoubleValue(0.1)
    double minTetherMass();

    @DefaultDoubleValue(2)
    double maxTetherMass();

    @DefaultDoubleValue(0.5)
    double minTetherLength();

    @DefaultDoubleValue(3)
    double maxTetherLength();

    @DefaultDoubleValue(0.001)
    double minTetherDiameter();

    @DefaultDoubleValue(0.005)
    double maxTetherDiameter();

    @DefaultDoubleValue(0)
    double minDeflectionAngle();

    @DefaultDoubleValue(45)
    double maxDeflectionAngle();

    @DefaultDoubleValue(0.01)
    double minElectricity();

    @DefaultDoubleValue(0.1)
    double maxElectricity();

    @DefaultDoubleValue(500.0)
    double minSystemHeight();

    @DefaultDoubleValue(1000.0)
    double maxSystemHeight();

    @DefaultDoubleValue(0.00001)
    double minEccentricity();

    @DefaultDoubleValue(0.1)
    double maxEccentricity();

    @DefaultDoubleValue(5.0)
    double minCalculationStep();

    @DefaultDoubleValue(10.0)
    double maxCalculationStep();

    @DefaultDoubleValue(5.0)
    double minCalculationMaxStep();

    @DefaultDoubleValue(10.0)
    double maxCalculationMaxStep();

    @DefaultDoubleValue(50.0)
    double minIterations();

    @DefaultDoubleValue(1000.0)
    double maxIterations();

    @DefaultDoubleValue(0.001)
    double minIntegrationAccuracy();

    @DefaultDoubleValue(0.1)
    double maxIntegrationAccuracy();
}
