package com.spaceApplication.client.space.controllers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
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
    //private static ElectrodynamicTetherSystemModel defaultTestModel = new ElectrodynamicTetherSystemModel(20, 40 , 1000, 1000000, 0, 0, 0, 0.0167, 0.5,  1000, 10.0, 20.0, 0.01);
    private static ElectrodynamicTetherSystemModelClient defaultTestModel = new ElectrodynamicTetherSystemModelClient(20, 20, 1000, 1000000, 0.1, 0, 0, 0.0167, 0.5, 1000, 10.0, 20.0, 0.01);
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

    public static Widget createAllResultPlots(OrbitalElementsClient results, ElectrodynamicTetherSystemModelClient model) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        VerticalPanel verticalPanel = new VerticalPanel();
        //verticalPanel.add(createResultCharts(results));
        verticalPanel.add(createBigChart(results, model));

        horizontalPanel.add(verticalPanel);
        horizontalPanel.add(downloadImagePanel);

        return horizontalPanel;
    }

    public static Widget createAllResultPlotsTest(OrbitalElementsClient results) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        VerticalPanel inputDataVPanel = new VerticalPanel();
        HTML h1 = new HTML("<h1 class=" + UIConsts.HEADER_STYLE_NAME + ">" + "Параметры тросовой системы" + "</h1>");
        inputDataVPanel.add(h1);

        inputDataVPanel.add(new Label("Масса первого космического аппарата " + defaultTestModel.getM1() + " кг"));
        inputDataVPanel.add(new Label("Масса второго космического аппарата " + defaultTestModel.getM2() + " кг"));
        inputDataVPanel.add(new Label("Длина троса " + defaultTestModel.getL() / 1000 + " км"));
        inputDataVPanel.add(new Label("Высота центра масс " + defaultTestModel.getH() / 1000 + " км"));

        HTML h2 = new HTML("<h1 class=" + UIConsts.HEADER_STYLE_NAME + ">" + "Начальные параметры" + "</h1>");
        inputDataVPanel.add(h2);

        inputDataVPanel.add(new Label("Угол отклонения троса от вертикали " + defaultTestModel.getTetta() + " градусов"));
        inputDataVPanel.add(new Label("Истинная аномалия Земли " + defaultTestModel.getTetta() + " градусов"));
        inputDataVPanel.add(new Label("Угловая скорость " + defaultTestModel.getOmega() + " рад/c"));
        inputDataVPanel.add(new Label("Эксцентриситет " + defaultTestModel.getEx()));
        inputDataVPanel.add(new Label("Значение тока " + defaultTestModel.getI() + " А"));

        HTML h3 = new HTML("<h1 class=" + UIConsts.HEADER_STYLE_NAME + ">" + "Параметры моделирования" + "</h1>");
        inputDataVPanel.add(h3);

        inputDataVPanel.add(new Label("Время интегрирования " + defaultTestModel.getMaxIter() + " с"));
        inputDataVPanel.add(new Label("Начальный шаг интегрирования " + defaultTestModel.getStep() + " с"));
        inputDataVPanel.add(new Label("Максимальный шаг " + defaultTestModel.getStepMax() + " с"));
        inputDataVPanel.add(new Label("Погрешность интегрирования " + defaultTestModel.getCalcAccuracy()));

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(inputDataVPanel);
        //verticalPanel.add(createResultCharts(results));
        verticalPanel.add(createBigChart(results, defaultTestModel));

        horizontalPanel.add(verticalPanel);
        horizontalPanel.add(downloadImagePanel);

        return horizontalPanel;
    }

    public static Widget createResultPlots(OrbitalElementsClient results) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        VerticalPanel verticalPanel = new VerticalPanel();
        //verticalPanel.add(createResultCharts(results));

        horizontalPanel.add(verticalPanel);
        horizontalPanel.add(downloadImagePanel);

        return horizontalPanel;
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

    public void callRPCTest(AsyncCallback<OrbitalElementsClient> calculationCallback) {
        if (spaceApplicationServiceAsync == null)
            spaceApplicationServiceAsync = GWT.create(SpaceApplicationService.class);


        spaceApplicationServiceAsync.getCalculationResult(defaultTestModel, calculationCallback);
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
