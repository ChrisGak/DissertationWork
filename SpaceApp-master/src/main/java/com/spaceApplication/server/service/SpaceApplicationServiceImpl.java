package com.spaceApplication.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.spaceApplication.client.exception.SpaceModelException;
import com.spaceApplication.client.space.SpaceApplicationService;
import com.spaceApplication.client.space.html.UIConsts;
import com.spaceApplication.client.space.model.RungeKuttaResult;
import com.spaceApplication.server.export.WriteExcel;
import com.spaceApplication.server.sampleModel.differentiation.RungeKuttaMethodImpl;
import com.spaceApplication.server.sampleModel.model.ElectrodynamicTetherSystemModel;
import jxl.write.WriteException;

import java.io.IOException;

public class SpaceApplicationServiceImpl extends RemoteServiceServlet implements SpaceApplicationService {

    public static boolean IS_DEBUG = true;

    private  WriteExcel test = new WriteExcel();
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }

    @Override
    public String getSpaceModelType() throws SpaceModelException {
        return "Accurate model";
    }

    public RungeKuttaResult getTestCalcResult() throws SpaceModelException{

        boolean isAccurate = true;
        /**
         * double m1, double m2, double L, double H, double tetta, double omega, double eps, boolean isAccurate
         */
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(20, 40 , 1000, 1000000, 0, 0, 0, 0.0167, 5, isAccurate);
        /**
         * ElectrodynamicTetherSystemModel model, double iter, double A, double ex
         */
        AccurateRopeSystemModel accurateRope = new AccurateRopeSystemModel(testModel, 0);

        RungeKuttaMethodImpl method = new RungeKuttaMethodImpl();
        /**
         * BaseModel rope, int maxIter, double step, double stepMax, double D
         */
        method.fullDiffCalc(accurateRope, 100, 10.0, 20.0, 0.01);
       // method.fullDiffCalcConstStep(accurateRope, 100, 10.0, 10.0);
        /**
         * (Vector time, Vector tetta, Vector omega, Vector eps, Vector A, Vector ex, Vector step, Vector accuracy, Vector iter){
         */
        RungeKuttaResult clientResult = new RungeKuttaResult(
                method.getResult().getTime(),
                method.getResult().getConvertedTetta(),
                method.getResult().getOmega(),
                method.getResult().getConvertedEps(),
                method.getResult().getA(),
                method.getResult().getEx(),
                method.getResult().getStep(),
                method.getResult().getAccuracy(),
                method.getResult().getIter());

        return  clientResult;
    }

    public RungeKuttaResult getCalculationResult(com.spaceApplication.client.space.model.CableSystemModel baseModel) throws SpaceModelException {
        boolean isAccurate = true;
        /**
         * double m1, double m2, double L, double H, double tetta, double omega, double eps, boolean isAccurate
         */
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(baseModel.getM1(), baseModel.getM2(), baseModel.getL(),
                baseModel.getH(), baseModel.getTetta(), baseModel.getOmega(), baseModel.getEps(), baseModel.getEx(), baseModel.getI(), isAccurate);

        /**
         * ElectrodynamicTetherSystemModel model, double iter, double A, double ex
         */
        AccurateRopeSystemModel accurateRope = new AccurateRopeSystemModel(testModel, 0);

        RungeKuttaMethodImpl method = new RungeKuttaMethodImpl();
        /**
         * BaseModel rope, int maxIter, double step, double stepMax, double D
         */
        method.fullDiffCalc(accurateRope, baseModel.getMaxIter(), baseModel.getStep(), baseModel.getStepMax(), baseModel.getD());
        /**
         * (Vector time, Vector tetta, Vector omega, Vector eps, Vector A, Vector ex, Vector step, Vector accuracy, Vector iter){
         */
        RungeKuttaResult clientResult = new RungeKuttaResult(
                method.getResult().getTime(),
                method.getResult().getConvertedTetta(),
                method.getResult().getOmega(),
                method.getResult().getConvertedEps(),
                method.getResult().getA(),
                method.getResult().getEx(),
                method.getResult().getStep(),
                method.getResult().getAccuracy(),
                method.getResult().getIter());


        test.createFileIfNotExists(UIConsts.fileName);
        try {
            test.writeRungeKuttaResult(method.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        return  clientResult;
    }

    public RungeKuttaResult testCalculationResult(com.spaceApplication.client.space.model.CableSystemModel baseModel) throws SpaceModelException {
        boolean isAccurate = true;
        /**
         * double m1, double m2, double L, double H, double tetta, double omega, double eps, boolean isAccurate
         */
        ElectrodynamicTetherSystemModel testModel = new ElectrodynamicTetherSystemModel(baseModel.getM1(), baseModel.getM2(), baseModel.getL(),
                baseModel.getH(), baseModel.getTetta(), baseModel.getOmega(), baseModel.getEps(), baseModel.getEx(), baseModel.getI(), isAccurate);

        /**
         * ElectrodynamicTetherSystemModel model, double iter, double A, double ex
         */
        AccurateRopeSystemModel accurateRope = new AccurateRopeSystemModel(testModel, 0);

        RungeKuttaMethodImpl method = new RungeKuttaMethodImpl();
        /**
         * BaseModel rope, int maxIter, double step, double stepMax, double D
         */
        method.fullDiffCalc(accurateRope, baseModel.getMaxIter(), baseModel.getStep(), baseModel.getStepMax(), baseModel.getD());
        /**
         * (Vector time, Vector tetta, Vector omega, Vector eps, Vector A, Vector ex, Vector step, Vector accuracy, Vector iter){
         */
        RungeKuttaResult clientResult = new RungeKuttaResult(
                method.getResult().getTime(),
                method.getResult().getConvertedTetta(),
                method.getResult().getOmega(),
                method.getResult().getConvertedEps(),
                method.getResult().getA(),
                method.getResult().getEx(),
                method.getResult().getStep(),
                method.getResult().getAccuracy(),
                method.getResult().getIter());

        return  clientResult;
    }
}