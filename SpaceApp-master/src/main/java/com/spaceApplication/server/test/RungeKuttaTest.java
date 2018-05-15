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
        //testFullModel();
        testReducedMass();
        testFullModel1();
    }

    private static void testReducedMass(){
        BareElectrodynamicTether testTether = new BareElectrodynamicTether(0.4, 2000, 0.001, 0, 0.01);
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(testTether,
                6, 2, 500000, 0,  0.0167);

        System.out.println("    testReducedMass           ");
        System.out.println("me = " + testModel.getReducedMass());
    }


    public static void testFullModel1() {
        BareElectrodynamicTether testTether = new BareElectrodynamicTether(0.4, 2000, 0.001, 0, 0.01);
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(testTether,
                6, 2, 500000, 0, 0.00001);

        System.out.println("_____________________________");
        System.out.println("    testFullModel1           ");
        testModel.printInitialState();
        int hours = 30;
        int seconds = hours * 60 * 60;
        seconds = 3600;
        OrbitalElements integrationTestResult = RungeKuttaMethodImpl.integrateWithVariableStep(testModel,
                seconds, 5, 10, 0.01);
        OrbitalElements.printFirstAndLastResults(integrationTestResult);
        printForceResults(testModel, integrationTestResult);

//        OrbitalElements.printResultToConsole(integrationTestResult);
        System.out.println("_____________________________");
    }

    private static void printForceResults(ElectrodynamicTetherSystemModel model, OrbitalElements result){
        System.out.println("\nF");
        System.out.println(model.getFullForce((Double) result.getSemimajorAxis().get(0), (Double)result.getEccentricity().get(0), (Double) result.getTrueAnomaly().get(0)));
        System.out.println(model.getFullForce(result.getLastSemimajor(), result.getLastEccentricity(), result.getLastTrueAnomaly()));

        System.out.println("\nM");
        System.out.println(model.getFullMoment((Double) result.getSemimajorAxis().get(0), (Double)result.getEccentricity().get(0), (Double) result.getTrueAnomaly().get(0)));
        System.out.println(model.getFullMoment(result.getLastSemimajor(), result.getLastEccentricity(), result.getLastTrueAnomaly()));

        System.out.println("\nFt");
        System.out.println(model.getTransversalForce((Double) result.getSemimajorAxis().get(0), (Double)result.getEccentricity().get(0),
                (Double) result.getTrueAnomaly().get(0), (Double) result.getTetherVerticalDeflectionAngle().get(0)));
        System.out.println(model.getTransversalForce(result.getLastSemimajor(), result.getLastEccentricity(),
                result.getLastTrueAnomaly(), result.getLastTetherVerticalDeflectionAngle()));

        System.out.println("\nat");
        System.out.println(model.getTransversalAcceleration((Double) result.getSemimajorAxis().get(0), (Double)result.getEccentricity().get(0),
                (Double) result.getTrueAnomaly().get(0), (Double) result.getTetherVerticalDeflectionAngle().get(0)));
        System.out.println(model.getTransversalAcceleration(result.getLastSemimajor(), result.getLastEccentricity(),
                    result.getLastTrueAnomaly(), result.getLastTetherVerticalDeflectionAngle()));

        System.out.println("\nFs");
        System.out.println(model.getRadialForce((Double) result.getSemimajorAxis().get(0),
                (Double)result.getEccentricity().get(0), (Double) result.getTrueAnomaly().get(0), (Double) result.getTetherVerticalDeflectionAngle().get(0)));
        System.out.println(model.getRadialForce(result.getLastSemimajor(), result.getLastEccentricity(),
                result.getLastTrueAnomaly(), result.getLastTetherVerticalDeflectionAngle()));

        System.out.println("\nas");
        System.out.println(model.getRadialAcceleration((Double) result.getSemimajorAxis().get(0),
                    (Double)result.getEccentricity().get(0), (Double) result.getTrueAnomaly().get(0), (Double) result.getTetherVerticalDeflectionAngle().get(0)));
        System.out.println(model.getRadialAcceleration(result.getLastSemimajor(), result.getLastEccentricity(),
                    result.getLastTrueAnomaly(), result.getLastTetherVerticalDeflectionAngle()));

        System.out.println("\n -Cos(tetta)");
        System.out.println(-Math.cos(result.getLastTetherVerticalDeflectionAngle()));

        System.out.println("\n Sin(tetta)");
        System.out.println(Math.sin(result.getLastTetherVerticalDeflectionAngle()));

        System.out.println("\n Total mass");
        System.out.println(model.getTotalSystemMass());

    }


    public static void testEquations(){
        BareElectrodynamicTether testTether = new BareElectrodynamicTether(1, 1000, 0.001, 0, 0.1);
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(testTether,
                6, 2, 500000, 0, BasicConsts.INITIAL_ECCENTICITY.getValue());

        System.out.println("_____________________________");
        System.out.println("    Model1: test equations           ");
        testModel.printInitialState();

        System.out.println("p(A, ex) = " +
                testModel.getOrbitalParameter(testModel.getInitialSemimajorAxis(), BasicConsts.INITIAL_ECCENTICITY.getValue()));
        System.out.println("_____________________________");
    }
}
