package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.spaceApplication.client.space.SpaceAppEntryPoint;
import com.spaceApplication.client.space.controllers.RemoteCalculationControl;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.client.space.ui.components.UIConsts;

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
        systemHeightChart.add(RemoteCalculationControl.createSystemHeightChart(result, tetherSystemModel));
        semimajorAxisChart.add(RemoteCalculationControl.createSemimajorAxisChart(result, tetherSystemModel));
        trueAnomalyChart.add(RemoteCalculationControl.createTrueAnomalyChart(result, tetherSystemModel));
        eccentricityChart.add(RemoteCalculationControl.createEccentricityChart(result, tetherSystemModel));
        deflectionAngleChart.add(RemoteCalculationControl.createDeflectionAngleChart(result, tetherSystemModel));
        angleSpeedChart.add(RemoteCalculationControl.createAngularVelocityChart(result, tetherSystemModel));
        trajectoryChart.add(RemoteCalculationControl.createBigChart(result, tetherSystemModel));

        //SpaceAppEntryPoint.getInstance().getApplicationContainer().getPageSidebarContent().enableExportResultsAnchor(UIConsts.fileName);
    }

    interface GraphResultUiBinder extends UiBinder<Widget, CalculationResultsCtrl> {
    }
}