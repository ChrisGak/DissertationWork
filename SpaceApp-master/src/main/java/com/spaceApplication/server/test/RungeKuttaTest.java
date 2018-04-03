package com.spaceApplication.server.test;

import com.spaceApplication.server.export.ExcelWritter;
import com.spaceApplication.server.logging.CustomLogger;
import com.spaceApplication.server.modeling.differentiation.RungeKuttaMethodImpl;
import com.spaceApplication.server.modeling.differentiation.OrbitalElements;
import com.spaceApplication.server.modeling.model.BareElectrodynamicTether;
import com.spaceApplication.server.modeling.model.ElectrodynamicTetherSystemModel;
import com.spaceApplication.shared.calculation.BasicConsts;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Chris
 */
public class RungeKuttaTest {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        try {
            CustomLogger.setupTesting();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Тестирование результатов интегрирования: \n");
        testFullModel();
    }


    public static void testFullModel() {
        BareElectrodynamicTether testTether = new BareElectrodynamicTether(0.4, 2000, 0.001);
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(testTether,
                6, 2, 500000, 0, 0, 0.0167);

        System.out.println("_____________________________");
        System.out.println("    testFullModel1           ");
        testModel.printInitialState();
        OrbitalElements integrationTestResult = RungeKuttaMethodImpl.integrateWithVariableStep(testModel,
                50, 5, 10, 0.001);
//        OrbitalElements.printFirstAndLastResults(integrationTestResult);
        OrbitalElements.printResultToConsole(integrationTestResult);
        System.out.println("_____________________________");
    }


    public static void testEquations(){
        BareElectrodynamicTether testTether = new BareElectrodynamicTether(0.4, 2000, 0.001);
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(testTether,
                6, 2, 500000, 0, 0, BasicConsts.INITIAL_ECCENTICITY.getValue());

        System.out.println("_____________________________");
        System.out.println("    Model1: test equations           ");
        testModel.printInitialState();

        System.out.println("p(A, ex) = " +
                testModel.getOrbitalParameter(testModel.getInitialSemimajorAxis(), BasicConsts.INITIAL_ECCENTICITY.getValue()));
        System.out.println("_____________________________");
    }
}
