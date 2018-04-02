package com.spaceApplication.server.test;

import com.spaceApplication.server.export.ExcelWritter;
import com.spaceApplication.server.logging.CustomLogger;
import com.spaceApplication.server.modeling.differentiation.RungeKuttaMethodImpl;
import com.spaceApplication.server.modeling.differentiation.OrbitalElements;
import com.spaceApplication.server.modeling.model.BareElectrodynamicTether;
import com.spaceApplication.server.modeling.model.ElectrodynamicTetherSystemModel;
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
                100, 5, 10, 0.001);
        OrbitalElements.printFirstAndLastResults(integrationTestResult);
        System.out.println("_____________________________");
    }
}
