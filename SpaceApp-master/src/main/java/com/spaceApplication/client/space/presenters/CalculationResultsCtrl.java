package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.spaceApplication.client.space.controllers.RemoteCalculationControl;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;

/**
 * Created by Chris
 */
public class CalculationResultsCtrl extends Composite {
    private static GraphResultUiBinder ourUiBinder = GWT.create(GraphResultUiBinder.class);
    @UiField
    HTMLPanel systemHeightChart;
    @UiField
    HTMLPanel semimajorAxisChart;
    @UiField
    HTMLPanel trueAnomalyChart;
    @UiField
    HTMLPanel eccentricityChart;
    @UiField
    HTMLPanel deflectionAngleChart;
    @UiField
    HTMLPanel trajectoryChart;
    @UiField
    HTMLPanel angleSpeedChart;
    private OrbitalElementsClient result;
    private ElectrodynamicTetherSystemModelClient tetherSystemModel;

    public CalculationResultsCtrl(OrbitalElementsClient result, ElectrodynamicTetherSystemModelClient tetherSystemModel) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.result = result;
        this.tetherSystemModel = tetherSystemModel;
        initWidget();
    }

    private void initWidget() {
        systemHeightChart.add(RemoteCalculationControl.getInstance().createSemimajorAxisChart(result, tetherSystemModel));
        semimajorAxisChart.add(RemoteCalculationControl.getInstance().createSemimajorAxisChart(result, tetherSystemModel));
        trueAnomalyChart.add(RemoteCalculationControl.getInstance().createTrueAnomalyChart(result, tetherSystemModel));
        eccentricityChart.add(RemoteCalculationControl.getInstance().createEccentricityChart(result, tetherSystemModel));
        deflectionAngleChart.add(RemoteCalculationControl.getInstance().createEccentricityChart(result, tetherSystemModel));
        angleSpeedChart.add(RemoteCalculationControl.getInstance().createAngularVelocityChart(result, tetherSystemModel));
        trajectoryChart.add(RemoteCalculationControl.getInstance().createBigChart(result, tetherSystemModel));
    }

    interface GraphResultUiBinder extends UiBinder<Widget, CalculationResultsCtrl> {
    }
}