package com.spaceApplication.client.space.controllers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.client.space.ui.components.UIConsts;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;

import static com.spaceApplication.client.space.ui.components.UIConsts.SYSTEM_HEIGHT_TITLE;

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

    public static Widget createSystemHeightChart(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
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

    public static Widget createAngularVelocityChart(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
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

    public static Widget createTrueAnomalyChart(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
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

    public static Widget createEccentricityChart(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
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

    public static Widget createDeflectionAngleChart(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
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

    public static Widget createSemimajorAxisChart(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
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
        downloadHref.setHref(UIConsts.fileName);
        downloadHref.setText("Скачать результаты моделирования в формате Excel-файла");
        downloadImagePanel.add(downloadHref);
    }
}
