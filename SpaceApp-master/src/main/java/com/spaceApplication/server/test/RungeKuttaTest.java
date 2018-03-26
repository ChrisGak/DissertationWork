package com.spaceApplication.server.test;

import com.spaceApplication.server.export.WriteExcel;
import com.spaceApplication.server.logging.MyLogger;
import com.spaceApplication.server.modeling.differentiation.RungeKuttaMethodImpl;
import com.spaceApplication.server.modeling.differentiation.RungeKuttaMethodResult;
import com.spaceApplication.server.modeling.model.ElectrodynamicTetherSystemModel;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Кристина on 10.02.2016.
 */
public class RungeKuttaTest{
    // use the classname for the logger, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    public static void main(String[] args) {
        try {
            MyLogger.setupTesting();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Accuracy Model: \n");
        System.out.println("Accuracy Model: \n");
        testFullModel1();
        //testFullModel2();
        //testFullModel3();

    }


    public static RungeKuttaMethodImpl testFullModel() {
        boolean isAccurate = true;
        /**
         * double m1, double m2, double L, double H, double tetta in angles, double omega, double eps in angles, double I, boolean isAccurate
         */

        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(20, 40 , 1000, 1000000, 0, 0, 0, 0.0167, 5, isAccurate);

        AccurateRopeSystemModel accurateRope = new AccurateRopeSystemModel(testModel, 0);

        RungeKuttaMethodImpl method = new RungeKuttaMethodImpl();
        /**
         * BaseModel rope, int maxIter, double step, double stepMax, double D
         */
        //method.fullDiffCalc(accurateRope, 100, 10.0, 10.0, 100000);
        RungeKuttaMethodResult.printResultsToConsole(method.getResult());
        System.out.println("_____________________________");
        System.out.println("testFullModel \n");
        System.out.println("_____________________________");
        RungeKuttaMethodResult.printResultsToConsole(method.getResult());

        return method;
    }


    public static void testFullModel1() {
        boolean isAccurate = true;
        /**
         * double m1, double m2, double L, double H, double tetta in angles, double omega, double eps in angles, double I, boolean isAccurate
         */
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(20, 20 , 1000, 1000000, 0.1, 0, 0, 0.0167, 0.5, isAccurate);

        AccurateRopeSystemModel accurateRope = new AccurateRopeSystemModel(testModel, 0);

        RungeKuttaMethodImpl method = new RungeKuttaMethodImpl();
        /**
         * BaseModel rope, int maxIter, double step, double stepMax
         */
        method.fullDiffCalcConstStep(accurateRope, 10000, 10.0, 10.0);
        System.out.println("_____________________________");
        System.out.println("testFullModel1 \n");
        System.out.println("_____________________________");
        //RungeKuttaMethodResult.printResultsToConsole(method.getResult());

        LOGGER.info("testFullModel1 \n");
        LOGGER.info(method.getResult().getA().get(0)+ "\n");
        LOGGER.info(method.getResult().getA().get(method.getResult().getSize()) + "\n");


        WriteExcel test = new WriteExcel();

        test.createFileIfNotExists("reportTest1.xls");
        try {
            test.writeFullInfo(testModel, method.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        System.out.println("Please check the result file under " + test.getInputFile());

    }


    public static void testFullModel2() {
        boolean isAccurate = true;
        /**
         * double m1, double m2, double L, double H, double tetta in angles, double omega, double eps in angles, double I, boolean isAccurate
         */

        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(20, 40 , 1000, 1000000, 0, 0, 0, 0.0167, 5, isAccurate);

        AccurateRopeSystemModel accurateRope = new AccurateRopeSystemModel(testModel, 0);

        RungeKuttaMethodImpl method = new RungeKuttaMethodImpl();
        /**
         * BaseModel rope, int maxIter, double step, double stepMax, double D
         */
        method.fullDiffCalc(accurateRope, 100, 20.0, 20.0, 0.1);
        System.out.println("_____________________________");
        System.out.println("testFullModel2 \n");
        System.out.println("_____________________________");
        RungeKuttaMethodResult.printFirstAndLastResults(method.getResult());

        LOGGER.info("testFullModel2 \n");
    }

    public static void testFullModel3() {
        boolean isAccurate = true;
        /**
         * double m1, double m2, double L, double H, double tetta in angles, double omega, double eps in angles, double I, boolean isAccurate
         */

        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(20, 40 , 1000, 1000000, 45, 0.1, 30, 0.0167, 10, isAccurate);

        AccurateRopeSystemModel accurateRope = new AccurateRopeSystemModel(testModel, 0);

        RungeKuttaMethodImpl method = new RungeKuttaMethodImpl();
        /**
         * BaseModel rope, int maxIter, double step, double stepMax, double D
         */
        method.fullDiffCalc(accurateRope, 1000, 10.0, 20.0, 0.01);
        System.out.println("_____________________________");
        System.out.println("testFullModel3 \n");
        System.out.println("_____________________________");
        RungeKuttaMethodResult.printFirstAndLastResults(method.getResult());

        LOGGER.info("testFullModel3 \n");
    }
}
