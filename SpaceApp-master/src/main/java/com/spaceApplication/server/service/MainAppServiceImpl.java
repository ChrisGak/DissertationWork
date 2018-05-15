package com.spaceApplication.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.spaceApplication.client.exception.TetherSystemModelValueException;
import com.spaceApplication.client.space.controllers.SpaceApplicationService;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.client.space.ui.components.UIConsts;
import com.spaceApplication.server.export.ExcelWritter;
import com.spaceApplication.server.modeling.differentiation.OrbitalElements;
import com.spaceApplication.server.modeling.differentiation.RungeKuttaMethodImpl;
import com.spaceApplication.server.modeling.model.BareElectrodynamicTether;
import com.spaceApplication.server.modeling.model.ElectrodynamicTetherSystemModel;
import jxl.write.WriteException;

import java.io.IOException;

public class MainAppServiceImpl extends RemoteServiceServlet implements SpaceApplicationService {
    private ExcelWritter test = new ExcelWritter();

    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }


    public OrbitalElementsClient getTestModelIntegrationResult() throws TetherSystemModelValueException {
        BareElectrodynamicTether tether = new BareElectrodynamicTether(0.4, 2000, 0.001, 0, 0.1);
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(tether, 6, 2, 500000, 0, 0.0167);

        OrbitalElements serverResult = RungeKuttaMethodImpl.integrateWithVariableStep(testModel, 100, 10, 10, 0.001);
        /**
         * (Vector time, Vector tetta, Vector omega, Vector eps, Vector A, Vector ex, Vector step, Vector accuracy, Vector iter){
         */
        OrbitalElementsClient clientResult = new OrbitalElementsClient(
                serverResult.getTime(),
                serverResult.getTetherVerticalDeflectionAngleDegrees(),
                serverResult.getTetherVerticalDeflectionAngleDiff(),
                serverResult.getTrueAnomalyDegrees(),
                serverResult.getSemimajorAxis(),
                serverResult.getEccentricity(),
                serverResult.getStep(),
                serverResult.getAccuracy(),
                serverResult.getIteration());

        return clientResult;
    }

    public OrbitalElementsClient getCalculationResult(ElectrodynamicTetherSystemModelClient clientModel) throws TetherSystemModelValueException {
        BareElectrodynamicTether tether = new BareElectrodynamicTether(clientModel.getTether().getMass(), clientModel.getTether().getLength(),
                clientModel.getTether().getDiameter(), clientModel.getTether().getDeflectionAngleDegrees(), clientModel.getTether().getElectricity());
        ElectrodynamicTetherSystemModel systemModel = new ElectrodynamicTetherSystemModel(tether, clientModel.getMainSatelliteMass(),
                clientModel.getNanoSatelliteMass(),
                clientModel.getInitialHeight(), clientModel.getInitialTrueAnomalyRadians(),
                clientModel.getInitialEccentricity());

//        BareElectrodynamicTether tether = new BareElectrodynamicTether(0.4, 2000, 0.001, 0, 0.01);
//        ElectrodynamicTetherSystemModel systemModel = new ElectrodynamicTetherSystemModel(tether,
//                6, 2, 500000, 0, 0.00001);

//        int hours = 30;
//        int seconds = hours * 60 * 60;
//        seconds = 3600;
//        OrbitalElements serverResult = RungeKuttaMethodImpl.integrateWithVariableStep(systemModel,
//                seconds, 5, 10, 0.01);
        OrbitalElements serverResult = RungeKuttaMethodImpl.integrateWithVariableStep(systemModel, clientModel.getMaxIterations(), clientModel.getIntegrationStep(),
                clientModel.getIntegrationMaxStep(), clientModel.getCalculateAccuracy());
        /**
         * (Vector time, Vector tetta, Vector omega, Vector eps, Vector A, Vector ex, Vector step, Vector accuracy, Vector iter){
         */
        OrbitalElementsClient clientResult = new OrbitalElementsClient(
                serverResult.getTime(),
                serverResult.getTetherVerticalDeflectionAngleDegrees(),
                serverResult.getTetherVerticalDeflectionAngleDiff(),
                serverResult.getTrueAnomalyDegrees(),
                serverResult.getSemimajorAxis(),
                serverResult.getEccentricity(),
                serverResult.getStep(),
                serverResult.getAccuracy(),
                serverResult.getIteration());
        clientResult.setForceValue(systemModel.getFullForce(serverResult.getLastSemimajor(), serverResult.getLastEccentricity(), serverResult.getLastTrueAnomaly()));
        clientResult.setMomentValue(systemModel.getFullMoment(serverResult.getLastSemimajor(), serverResult.getLastEccentricity(), serverResult.getLastTrueAnomaly()));
        clientResult.setTransversalAccelertionValue(systemModel.getTransversalAcceleration(serverResult.getLastSemimajor(),
                                serverResult.getLastEccentricity(), serverResult.getLastTrueAnomaly(), serverResult.getLastTetherVerticalDeflectionAngle()));
        clientResult.setRadialAccelerationValue(systemModel.getRadialAcceleration(serverResult.getLastSemimajor(), serverResult.getLastEccentricity(),
                                serverResult.getLastTrueAnomaly(), serverResult.getLastTetherVerticalDeflectionAngle()));

        return clientResult;
    }

    private void printIntegrResultToFile(OrbitalElements result) {
        test.createFileIfNotExists(UIConsts.REPORT_RESULT_HREF);
        try {
            test.writeRungeKuttaResult(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}