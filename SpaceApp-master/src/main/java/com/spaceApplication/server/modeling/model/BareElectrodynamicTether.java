package com.spaceApplication.server.modeling.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.spaceApplication.shared.calculation.BasicCalculationOperation;
import com.spaceApplication.shared.calculation.BasicConsts;

/**
 * Created by Chris
 */

public class BareElectrodynamicTether implements IsSerializable {

    private double mass;
    /**
     * Длина троса,
     * плечо первого и второго тел
     */
    private double length;

    public double getDensity(){
        return mass / length;
    }

    private double diameter;
    public double getCrossSectionalArea(){
        return Math.PI * diameter * diameter / 4;
    }
    private double L_p;
    private double L1;
    private double L2;
    public BareElectrodynamicTether() {

    }
    private void setInitialLengthParameters(double l) {
        this.L1 = L * m2 / (m1 + m2);
        this.L2 = L * m1 / (m1 + m2);
        this.L_p = L * (m2 - m1) / (2.0 * (m1 + m2));
    }

    protected double getOm(double A, double ex) {
        return Math.sqrt(BasicConsts.K.getValue() / getP(A, ex));
    }

    protected double getP(double A, double ex) {
        return A * (1.0 - ex * ex);
    }

    protected double getNu(double eps, double ex) {
        return 1.0 + ex * Math.cos(eps);
    }

    /**
     * @param A
     * @param eps
     * @param ex
     * @return Уравнение орбиты центра масс
     */
    protected double getR(double A, double eps, double ex) {
        return getP(A, ex) / getNu(eps, ex);
    }

    /**
     * @param A
     * @param eps
     * @param ex
     * @return Модуль магнитной индукции
     */
    protected double getB(double A, double eps, double ex) {
        return BasicConsts.Mu.getValue() / BasicCalculationOperation.getThirdDegree(getR(A, eps, ex));
    }

    /**
     * Сила Ампера
     * тангенциальное и радиальное направление
     *
     * @param A
     * @param eps
     * @param ex
     * @return
     */
    protected double getF(double A, double eps, double ex) {
        return model.getI() * getB(A, eps, ex) * model.getL();
    }

    protected double getF_s(double A, double eps, double ex, double tetta) {
        double f_s = getF(A, eps, ex) * Math.sin(tetta);
        return f_s;
        //return  getF(A, eps, ex) * Math.sin(tetta);
    }

    protected double getF_t(double A, double eps, double ex, double tetta) {
        double f_t = getF(A, eps, ex) * (-1) * Math.cos(tetta);
        return f_t;
        //return getF(A, eps, ex) * (-1) *Math.cos(tetta);
    }

    /**
     * Тангенциальное и радиальное ускорение
     *
     * @param A
     * @param eps
     * @param ex
     * @param tetta
     * @return
     */
    protected double getA_s(double A, double eps, double ex, double tetta) {
        double a_s = getF_s(A, eps, ex, tetta) / model.getM();
        return a_s;
        //return getF_s(A, eps, ex, tetta) /model.getM();
    }

    protected double getA_t(double A, double eps, double ex, double tetta) {
        double a_t = getF_t(A, eps, ex, tetta) / model.getM();
        return a_t;
        //return getF_t(A, eps, ex, tetta) /model.getM();
    }

    /**
     * Момент силы Ампера
     *
     * @param A
     * @param eps
     * @param ex
     * @return
     */
    protected double getM(double A, double eps, double ex) {
        double M = getF(A, eps, ex) * model.getL_p();
        return M;
    }

    protected double getKoefficient(double A, double ex) {
        double k = Math.sqrt(getP(A, ex) / BasicConsts.K.getValue());
        return k;
        //return Math.sqrt(getP(A, ex) / BasicConsts.K.getValue());
    }

    protected double getSqrKoefficient() {
        double koef = Math.sqrt(BasicConsts.K.getValue() / BasicCalculationOperation.getThirdDegree(model.getP()));
        return koef;
        //return Math.sqrt(BasicConsts.K.getValue()/ BasicCalculationOperation.getThirdDegree(model.getP()));
    }

}
