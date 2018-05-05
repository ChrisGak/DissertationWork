package com.spaceApplication.client.space.controllers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.client.space.ui.components.UIConsts;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;

import static com.spaceApplication.client.space.ui.components.UIConsts.*;

/**
 * Created by Chris
 */
public class RemoteCalculationControl {
    private static VerticalPanel downloadImagePanel;
    private static RemoteCalculationControl instance;
    // create an instance of the service proxy class by calling GWT.create(Class).
    private SpaceApplicationServiceAsync spaceApplicationServiceAsync = GWT.create(SpaceApplicationService.class);


    protected RemoteCalculationControl() {
        spaceApplicationServiceAsync = GWT.create(SpaceApplicationService.class);
        initFileDownloadAnchor();
    }

    public static RemoteCalculationControl getInstance() {
        if (instance == null) {
            instance = new RemoteCalculationControl();
        }
        return instance;
    }

    public static Widget createBigChart(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
        VerticalPanel contentPanel = new VerticalPanel();
        Series XNB1, XNB2, YNB1, YNB2;

        Chart chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText("Траектория")
                .setLegend(new Legend()
                        .setAlign(Legend.Align.RIGHT)
                        .setBackgroundColor("#CCCCCC")
                        .setShadow(true)
                );

        XNB1 = chart.createSeries().setName("XNB1").setPoints(model.getXNB1Points(results.getTetta()));
        XNB2 = chart.createSeries().setName("XNB2").setPoints(model.getXNB2Points(results.getTetta()));
        YNB1 = chart.createSeries().setName("YNB1").setPoints(model.getYNB1Points(results.getTetta()));
        YNB2 = chart.createSeries().setName("YNB2").setPoints(model.getYNB2Points(results.getTetta()));
        chart.addSeries(XNB1);
        chart.addSeries(XNB2);
        chart.addSeries(YNB1);
        chart.addSeries(YNB2);
        chart.getYAxis().setAxisTitleText("N");
        chart.setShowAxes(true);
        contentPanel.add(chart);

        return contentPanel;
    }

    public static Widget createForceValueWidget(OrbitalElementsClient results){
        double fullForceValue = results.getForceValue();
        HTMLPanel infoElement = new HTMLPanel(LAST_FORCE_VALUE_TITLE);
        infoElement.addStyleName(SHORT_WIDGET_INFO_STYLE_NAME);
        HTMLPanel spanNumberElement = new HTMLPanel(fullForceValue + _A);
        spanNumberElement.addStyleName(SHORT_WIDGET_NUMBER_STYLE_NAME);
        HTMLPanel wrapperPanel = new HTMLPanel("");
        wrapperPanel.addStyleName(SHORT_WIDGET_STYLE_NAME);
        wrapperPanel.add(spanNumberElement);
        wrapperPanel.add(infoElement);
        return wrapperPanel;
    }

    public static Widget createMomentValueWidget(OrbitalElementsClient results){
        double momentValue = results.getMomentValue();
        HTMLPanel infoElement = new HTMLPanel(LAST_MOMENT_VALUE_TITLE);
        infoElement.addStyleName(SHORT_WIDGET_INFO_STYLE_NAME);
        HTMLPanel spanNumberElement = new HTMLPanel(momentValue + _MOMENT);
        spanNumberElement.addStyleName(SHORT_WIDGET_NUMBER_STYLE_NAME);
        HTMLPanel wrapperPanel = new HTMLPanel("");
        wrapperPanel.addStyleName(SHORT_WIDGET_STYLE_NAME);
        wrapperPanel.add(spanNumberElement);
        wrapperPanel.add(infoElement);
        return wrapperPanel;
    }

    public static Widget createTransversalAccelerationValueWidget(OrbitalElementsClient results){
        double transversalAccelerationValue = results.getTransversalAccelertionValue();
        HTMLPanel infoElement = new HTMLPanel(LAST_TRANSVERSAL_ACCELERATION_VALUE_TITLE);
        infoElement.addStyleName(SHORT_WIDGET_INFO_STYLE_NAME);
        HTMLPanel spanNumberElement = new HTMLPanel(transversalAccelerationValue + _ACCELERATION);
        spanNumberElement.addStyleName(SHORT_WIDGET_NUMBER_STYLE_NAME);
        HTMLPanel wrapperPanel = new HTMLPanel("");
        wrapperPanel.addStyleName(SHORT_WIDGET_STYLE_NAME);
        wrapperPanel.add(spanNumberElement);
        wrapperPanel.add(infoElement);
        return wrapperPanel;
    }

    public static Widget createRadialAccelerationValueWidget(OrbitalElementsClient results){
        double radialAccelerationValue = results.getRadialAccelerationValue();
        HTMLPanel infoElement = new HTMLPanel(LAST_RADIAL_ACCELERATION_VALUE_TITLE);
        infoElement.addStyleName(SHORT_WIDGET_INFO_STYLE_NAME);
        HTMLPanel spanNumberElement = new HTMLPanel(radialAccelerationValue + _ACCELERATION);
        spanNumberElement.addStyleName(SHORT_WIDGET_NUMBER_STYLE_NAME);
        HTMLPanel wrapperPanel = new HTMLPanel("");
        wrapperPanel.addStyleName(SHORT_WIDGET_STYLE_NAME);
        wrapperPanel.add(spanNumberElement);
        wrapperPanel.add(infoElement);
        return wrapperPanel;
    }

    public static Widget createSystemHeightChart(OrbitalElementsClient results) {
        Chart chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText(SYSTEM_HEIGHT_TITLE)
                .setLegend(new Legend()
                        .setAlign(Legend.Align.RIGHT)
                        .setBackgroundColor("#CCCCCC")
                        .setShadow(true)
                );
        Series pointsSeries = chart.createSeries().setName("R, км").setPoints(results.getHeightPoints());
        chart.addSeries(pointsSeries);
        chart.getYAxis().setAxisTitleText("N");
        return chart;
    }

    public static Widget createAngularVelocityChart(OrbitalElementsClient results) {
        Chart chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText(UIConsts.ANGULAR_VELOCITY_TITLE)
                .setLegend(new Legend()
                        .setAlign(Legend.Align.RIGHT)
                        .setBackgroundColor("#CCCCCC")
                        .setShadow(true)
                );

        Series pointsSeries = chart.createSeries().setName(UIConsts.omega).setPoints(results.getOmegaPoints());
        chart.addSeries(pointsSeries);
        chart.getYAxis().setAxisTitleText("N");
        return chart;
    }

    public static Widget createTrueAnomalyChart(OrbitalElementsClient results) {
        Chart chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText(UIConsts.TRUE_ANOMALY_TITLE)
                .setLegend(new Legend()
                        .setAlign(Legend.Align.RIGHT)
                        .setBackgroundColor("#CCCCCC")
                        .setShadow(true)
                );

        Series pointsSeries = chart.createSeries().setName(UIConsts.epsilon).setPoints(results.getEpsPoints());
        chart.addSeries(pointsSeries);
        chart.getYAxis().setAxisTitleText("N");
        return chart;
    }

    public static Widget createEccentricityChart(OrbitalElementsClient results) {
        Chart chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText(UIConsts.ECCENTRICITY_TITLE)
                .setLegend(new Legend()
                        .setAlign(Legend.Align.RIGHT)
                        .setBackgroundColor("#CCCCCC")
                        .setShadow(true)
                );

        Series pointsSeries = chart.createSeries().setName("e").setPoints(results.getExPoints());
        chart.addSeries(pointsSeries);
        chart.getYAxis().setAxisTitleText("N");
        return chart;
    }

    public static Widget createDeflectionAngleChart(OrbitalElementsClient results) {
        Chart chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText(UIConsts.DEFLECTION_ANGLE_TITLE)
                .setLegend(new Legend()
                        .setAlign(Legend.Align.RIGHT)
                        .setBackgroundColor("#CCCCCC")
                        .setShadow(true)
                );

        Series pointsSeries = chart.createSeries().setName(UIConsts.tetta).setPoints(results.getTettaPoints());
        chart.addSeries(pointsSeries);
        chart.getYAxis().setAxisTitleText("N");
        return chart;
    }

    public static Widget createSemimajorAxisChart(OrbitalElementsClient results) {
        Chart chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText(UIConsts.SEMIMAJOR_AXIS_TITLE)
                .setLegend(new Legend()
                        .setAlign(Legend.Align.RIGHT)
                        .setBackgroundColor("#CCCCCC")
                        .setShadow(true)
                );

        Series pointsSeries = chart.createSeries().setName("A, км").setPoints(results.getAPoints());
        chart.addSeries(pointsSeries);
        chart.getYAxis().setAxisTitleText("N");
        return chart;
    }

    public void callRPCTest(ElectrodynamicTetherSystemModelClient model, AsyncCallback<OrbitalElementsClient> calculationCallback) {
        if (spaceApplicationServiceAsync == null)
            spaceApplicationServiceAsync = GWT.create(SpaceApplicationService.class);


        spaceApplicationServiceAsync.getCalculationResult(model, calculationCallback);
    }

    public void callRemoteCalculation(AsyncCallback<OrbitalElementsClient> calculationCallback, ElectrodynamicTetherSystemModelClient model) {
        if (spaceApplicationServiceAsync == null)
            spaceApplicationServiceAsync = GWT.create(SpaceApplicationService.class);

        spaceApplicationServiceAsync.getCalculationResult(model, calculationCallback);
    }

    private void initFileDownloadAnchor() {
        downloadImagePanel = new VerticalPanel();
        Image downloadImage = new Image("images/box.jpg");
        downloadImage.setStyleName(UIConsts.imageStyle);
        downloadImage.setTitle("Скачать результаты моделирования");
        downloadImagePanel.add(downloadImage);
        Anchor downloadHref = new Anchor();
//        downloadHref.setHref(UIConsts.fileName);
        downloadHref.setText("Скачать результаты моделирования в формате Excel-файла");
        downloadImagePanel.add(downloadHref);
    }
}
