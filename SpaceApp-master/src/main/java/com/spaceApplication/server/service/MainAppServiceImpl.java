package com.spaceApplication.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.spaceApplication.client.exception.TetherSystemModelValueException;
import com.spaceApplication.client.space.SpaceApplicationService;
import com.spaceApplication.client.space.html.UIConsts;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
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
        BareElectrodynamicTether tether = new BareElectrodynamicTether(0.4, 2000, 0.001);
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(tether, 6, 2, 500000, 0, 0, 0.0167);

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
        BareElectrodynamicTether tether = new BareElectrodynamicTether(clientModel.getTether().getMass(), clientModel.getTether().getLength(), clientModel.getTether().getDiameter());
        ElectrodynamicTetherSystemModel systemModel = new ElectrodynamicTetherSystemModel(tether, clientModel.getMainSatelliteMass(), clientModel.getNanoSatelliteMass(),
                clientModel.getInitialHeight(), clientModel.getTetherVerticalDeflectionAngleRadians(), clientModel.getInitialTrueAnomalyRadians(), clientModel.getInitialEccentricity());

        OrbitalElements serverResult = RungeKuttaMethodImpl.integrateWithVariableStep(systemModel, clientModel.getMaxIter(), clientModel.getStep(), clientModel.getStepMax(), clientModel.getCalcAccuracy());
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

    private void printIntegrResultToFile(OrbitalElements result) {
        test.createFileIfNotExists(UIConsts.fileName);
        try {
            test.writeRungeKuttaResult(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}