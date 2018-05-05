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
    HTMLPanel forceValue;
    @UiField
    HTMLPanel momentValue;
    @UiField
    HTMLPanel transversalAccelerationValue;
    @UiField
    HTMLPanel radialAccelerationValue;
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
        forceValue.add(RemoteCalculationControl.createForceValueWidget(result));
        momentValue.add(RemoteCalculationControl.createMomentValueWidget(result));
        transversalAccelerationValue.add(RemoteCalculationControl.createTransversalAccelerationValueWidget(result));
        radialAccelerationValue.add(RemoteCalculationControl.createRadialAccelerationValueWidget(result));

        systemHeightChart.add(RemoteCalculationControl.createSystemHeightChart(result));
        semimajorAxisChart.add(RemoteCalculationControl.createSemimajorAxisChart(result));
        trueAnomalyChart.add(RemoteCalculationControl.createTrueAnomalyChart(result));
        eccentricityChart.add(RemoteCalculationControl.createEccentricityChart(result));
        deflectionAngleChart.add(RemoteCalculationControl.createDeflectionAngleChart(result));
        angleSpeedChart.add(RemoteCalculationControl.createAngularVelocityChart(result));
        trajectoryChart.add(RemoteCalculationControl.createBigChart(result, tetherSystemModel));

        SpaceAppEntryPoint.getInstance().getApplicationContainer().getPageSidebarContent().enableExportResultsAnchor(UIConsts.REPORT_RESULT_HREF);
    }

    interface GraphResultUiBinder extends UiBinder<Widget, CalculationResultsCtrl> {
    }
}