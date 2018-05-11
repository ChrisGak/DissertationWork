package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spaceApplication.client.consts.SpaceAppConstants;
import com.spaceApplication.client.consts.SpaceAppMessages;
import com.spaceApplication.client.exception.TetherSystemModelValueException;
import com.spaceApplication.client.space.controllers.RemoteCalculationControl;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.client.space.ui.components.Slider;
import com.spaceApplication.client.space.ui.components.UIConsts;
import com.spaceApplication.shared.calculation.BasicConsts;

import static com.spaceApplication.client.space.ui.components.UIConsts.*;

/**
 * Created by Chris
 */
public class ModelParameterCtrlPresenter extends Composite {
    private VerticalPanel dynamicParametersCtrlPanel = new VerticalPanel();
    private VerticalPanel staticParametersCtrlPanel = new VerticalPanel();
    private Button executeCalculationButton;
    private HTMLPanel pageContentInnerPanel;
    private int maxIterations = 1000;
    private double integrationStep = 10;
    private double integrationMaxStep = 10;
    private double integrationAccuracy = 0.001;
    /**
     * UI attributes
     */
    private String textBoxWidth = "90px";
    private int labelWidth = 400, labelHeight = 50;
    private int sliderWidth = 350, sliderHeight = 50;
    private int spacingAttr = 300;
    private String rangeLabelWidth = "70px";

    private ElectrodynamicTetherSystemModelClient tetherSystemModel;
    private AsyncCallback<OrbitalElementsClient> calculationCallback = new AsyncCallback<OrbitalElementsClient>() {
        public void onFailure(Throwable caught) {
            String details = caught.getMessage();
            if (caught instanceof TetherSystemModelValueException) {
                details = "Model has exception:  '" + ((TetherSystemModelValueException) caught).getCause();
            }
            HTML contentHeader = new HTML("<h2 class=" + UIConsts.HEADER_STYLE_NAME + ">" + "Error: " + details + "</h2>");
            pageContentInnerPanel.add(contentHeader);
        }

        @Override
        public void onSuccess(OrbitalElementsClient result) {
            clearVerticalPanel();
            CalculationResultsCtrl calculationResultsCtrl = new CalculationResultsCtrl(result, tetherSystemModel);
            pageContentInnerPanel.add(calculationResultsCtrl);
        }
    };
    ClickHandler executeCalculationClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            clearVerticalPanel();
            RemoteCalculationControl.getInstance().callRemoteCalculation(calculationCallback, tetherSystemModel);
        }
    };
    private SpaceAppConstants constants = GWT.create(SpaceAppConstants.class);
    private SpaceAppMessages messages = GWT.create(SpaceAppMessages.class);

    public ModelParameterCtrlPresenter() {
        tetherSystemModel = ElectrodynamicTetherSystemModelClient.createDefaultTetherSystemModel();

        initTetherModelParamsPanel();
        initTetherSystemModelParamsPanel();
        initCalculationParamsPanel();

        initSystemPositionStaticParamsPanel();
    }

    private static HTML createHeaderBlock(String headerTitle) {
        return new HTML("<div class=\"page-title\"><h3 class=" + UIConsts.BREADCRUMB_HEADER_STYLE_NAME + ">"
                + headerTitle + "</h1></div");
    }

    public AsyncCallback<OrbitalElementsClient> getCalculationCallback() {
        return calculationCallback;
    }

    public ElectrodynamicTetherSystemModelClient getTestTetherSystemModel() {
        return tetherSystemModel;
    }

    public Widget initWidget() {
        pageContentInnerPanel = new HTMLPanel("");
        pageContentInnerPanel.addStyleName(PAGE_INNER_STYLE_NAME);
        dynamicParametersCtrlPanel.addStyleName(COLUMN_LEFT_STYLE_NAME);
        pageContentInnerPanel.add(dynamicParametersCtrlPanel);
        staticParametersCtrlPanel.addStyleName(COLUMN_RIGHT_STYLE_NAME);
        pageContentInnerPanel.add(staticParametersCtrlPanel);
        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        executeCalculationButton = new Button(START_CALCULATION_TITLE);
        buttonWrapper.addStyleName(CLEAR_FLOAT_STYLE_NAME);
        buttonWrapper.add(executeCalculationButton);
        executeCalculationButton.addClickHandler(executeCalculationClickHandler);
        executeCalculationButton.setStyleName(UIConsts.BUTTON_STYLE_NAME);
        pageContentInnerPanel.add(buttonWrapper);
        return pageContentInnerPanel;
    }

    private void initTetherModelParamsPanel() {
        dynamicParametersCtrlPanel.add(createHeaderBlock(TETHER_PARAMS_HEADER_TITLE));

        final Slider tetherMassSlider = new Slider();
        tetherMassSlider.setPixelSize(sliderWidth, sliderHeight);
        tetherMassSlider.setStep(constants.minTetherMass());
        tetherMassSlider.setMin(constants.minTetherMass());
        tetherMassSlider.setMax(constants.maxTetherMass());
        tetherMassSlider.setValue(tetherSystemModel.getTether().getMass());
        Label tetherMassLabel = new Label(TETHER_MASS_TITLE);
        tetherMassLabel.setPixelSize(labelWidth, labelHeight);
        Label tetherMassMinLabel = new Label();
        tetherMassMinLabel.setWidth(rangeLabelWidth);
        Label tetherMassMaxLabel = new Label();
        tetherMassMaxLabel.setWidth(rangeLabelWidth);
        final TextBox tetherMassTextBox = new TextBox();
        tetherMassTextBox.setEnabled(false);
        tetherMassTextBox.setWidth(textBoxWidth);
        tetherMassSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                tetherMassTextBox.setText(String.valueOf(tetherMassSlider.getValue() + _KG));
                tetherSystemModel.getTether().setMass(tetherMassSlider.getValue());
            }
        });
        HorizontalPanel tetherMassPanel = new HorizontalPanel();
        tetherMassPanel.setSpacing(spacingAttr);
        tetherMassPanel.add(tetherMassLabel);
        tetherMassMinLabel.setText(String.valueOf(tetherMassSlider.getMin()));
        tetherMassPanel.add(tetherMassMinLabel);
        tetherMassPanel.add(tetherMassSlider);
        tetherMassMaxLabel.setText(String.valueOf(tetherMassSlider.getMax()));
        tetherMassPanel.add(tetherMassMaxLabel);
        tetherMassPanel.add(tetherMassTextBox);
        tetherMassTextBox.setText(String.valueOf(tetherMassSlider.getValue() + _KG));
        dynamicParametersCtrlPanel.add(tetherMassPanel);

        final Slider tetherLengthSlider = new Slider();
        tetherLengthSlider.setPixelSize(sliderWidth, sliderHeight);
        tetherLengthSlider.setStep(0.1);
        tetherLengthSlider.setMin(constants.minTetherLength());
        tetherLengthSlider.setMax(constants.maxTetherLength());
        tetherLengthSlider.setValue(tetherSystemModel.getTether().getLength());
        tetherLengthSlider.setTitle(TETHER_LENGTH_TITLE);
        final TextBox tetherLengthTextBox = new TextBox();
        tetherLengthTextBox.setEnabled(false);
        tetherLengthTextBox.setWidth(textBoxWidth);
        tetherLengthSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                tetherLengthTextBox.setText(String.valueOf(tetherLengthSlider.getValue() + _KM));
                tetherSystemModel.getTether().setLength(tetherLengthSlider.getValue());
            }
        });
        HorizontalPanel tetherLengthPanel = new HorizontalPanel();
        tetherLengthPanel.setSpacing(spacingAttr);
        Label tetherLengthLabel = new Label(TETHER_LENGTH_TITLE);
        tetherLengthLabel.setPixelSize(labelWidth, labelHeight);
        Label tetherLengthMinLabel = new Label();
        tetherLengthMinLabel.setWidth(rangeLabelWidth);
        Label tetherLengthMaxLabel = new Label();
        tetherLengthMaxLabel.setWidth(rangeLabelWidth);
        tetherLengthPanel.add(tetherLengthLabel);
        tetherLengthMinLabel.setText(String.valueOf(tetherLengthSlider.getMin()));
        tetherLengthPanel.add(tetherLengthMinLabel);
        tetherLengthPanel.add(tetherLengthSlider);
        tetherLengthMaxLabel.setText(String.valueOf(tetherLengthSlider.getMax()));
        tetherLengthPanel.add(tetherLengthMaxLabel);
        tetherLengthPanel.add(tetherLengthTextBox);
        tetherLengthTextBox.setText(String.valueOf(tetherLengthSlider.getValue() + _KM));
        dynamicParametersCtrlPanel.add(tetherLengthPanel);

        final Slider tetherDiameterSlider = new Slider();
        tetherDiameterSlider.setPixelSize(sliderWidth, sliderHeight);
        tetherDiameterSlider.setMin(constants.minTetherDiameter());
        tetherDiameterSlider.setMax(constants.maxTetherDiameter());
        tetherDiameterSlider.setValue(tetherSystemModel.getTether().getDiameter());
        tetherDiameterSlider.setStep(constants.minTetherDiameter());
        tetherDiameterSlider.setTitle(TETHER_DIAMETER_TITLE);
        final TextBox tetherDiameterTextBox = new TextBox();
        tetherDiameterTextBox.setEnabled(false);
        tetherDiameterTextBox.setWidth(textBoxWidth);
        tetherDiameterSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                tetherDiameterTextBox.setText(String.valueOf(tetherDiameterSlider.getValue() + _KM));
                tetherSystemModel.getTether().setDiameter(tetherDiameterSlider.getValue());
            }
        });
        HorizontalPanel tetherDiameterPanel = new HorizontalPanel();
        tetherDiameterPanel.setSpacing(spacingAttr);
        Label tetherDiameterLabel = new Label(TETHER_DIAMETER_TITLE);
        tetherDiameterLabel.setPixelSize(labelWidth, labelHeight);
        Label tetherDiameterMinLabel = new Label();
        tetherDiameterMinLabel.setWidth(rangeLabelWidth);
        Label tetherDiameterMaxLabel = new Label();
        tetherDiameterMaxLabel.setWidth(rangeLabelWidth);
        tetherDiameterPanel.add(tetherDiameterLabel);
        tetherDiameterMinLabel.setText(String.valueOf(tetherDiameterSlider.getMin()));
        tetherDiameterPanel.add(tetherDiameterMinLabel);
        tetherDiameterPanel.add(tetherDiameterSlider);
        tetherDiameterMaxLabel.setText(String.valueOf(tetherDiameterSlider.getMax()));
        tetherDiameterPanel.add(tetherDiameterMaxLabel);
        tetherDiameterPanel.add(tetherDiameterTextBox);
        tetherDiameterTextBox.setText(String.valueOf(tetherDiameterSlider.getValue() + _KM));
        dynamicParametersCtrlPanel.add(tetherDiameterPanel);

        final Slider deflectionAngleSlider = new Slider();
        deflectionAngleSlider.setPixelSize(sliderWidth, sliderHeight);
        deflectionAngleSlider.setTitle(DEFLECTION_ANGLE_TITLE);
        deflectionAngleSlider.setMin(constants.minDeflectionAngle());
        deflectionAngleSlider.setMax(constants.maxDeflectionAngle());
        deflectionAngleSlider.setValue(tetherSystemModel.getTether().getDeflectionAngleDegrees());
        deflectionAngleSlider.setStep(constants.minDeflectionAngle());
        final TextBox deflectionAngleTextBox = new TextBox();
        deflectionAngleTextBox.setEnabled(false);
        deflectionAngleTextBox.setWidth(textBoxWidth);
        deflectionAngleSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                deflectionAngleTextBox.setText(String.valueOf(deflectionAngleSlider.getValue() + _DEG));
                tetherSystemModel.getTether().setDiameter(Math.toRadians(deflectionAngleSlider.getValue()));
            }
        });
        HorizontalPanel deflectionAnglePanel = new HorizontalPanel();
        deflectionAnglePanel.setSpacing(spacingAttr);
        Label deflectionAngleLabel = new Label(DEFLECTION_ANGLE_TITLE);
        deflectionAngleLabel.setPixelSize(labelWidth, labelHeight);
        Label deflectionAngleMinLabel = new Label();
        deflectionAngleMinLabel.setWidth(rangeLabelWidth);
        Label deflectionAngleMaxLabel = new Label();
        deflectionAngleMaxLabel.setWidth(rangeLabelWidth);
        deflectionAnglePanel.add(deflectionAngleLabel);
        deflectionAngleMinLabel.setText(String.valueOf(deflectionAngleSlider.getMin()));
        deflectionAnglePanel.add(deflectionAngleMinLabel);
        deflectionAnglePanel.add(deflectionAngleSlider);
        deflectionAngleMaxLabel.setText(String.valueOf(deflectionAngleSlider.getMax()));
        deflectionAnglePanel.add(deflectionAngleMaxLabel);
        deflectionAngleTextBox.setText(String.valueOf(deflectionAngleSlider.getValue() + _DEG));
        deflectionAnglePanel.add(deflectionAngleTextBox);
        dynamicParametersCtrlPanel.add(deflectionAnglePanel);

        final Slider electricitySlider = new Slider();
        electricitySlider.setPixelSize(sliderWidth, sliderHeight);
        electricitySlider.setTitle(ELECTRICITY_TITLE);
        electricitySlider.setMin(constants.minElectricity());
        electricitySlider.setMax(constants.maxElectricity());
        electricitySlider.setStep(constants.minElectricity());
        electricitySlider.setValue(tetherSystemModel.getTether().getElectricity());
        final TextBox electricityTextBox = new TextBox();
        electricityTextBox.setEnabled(false);
        electricityTextBox.setWidth(textBoxWidth);
        electricitySlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                electricityTextBox.setText(String.valueOf(electricitySlider.getValue()));
                tetherSystemModel.getTether().setElectricity(electricitySlider.getValue());
            }
        });
        HorizontalPanel electricityPanel = new HorizontalPanel();
        electricityPanel.setSpacing(spacingAttr);
        Label electricityLabel = new Label(ELECTRICITY_TITLE);
        electricityLabel.setPixelSize(labelWidth, labelHeight);
        Label electricityMinLabel = new Label();
        electricityMinLabel.setWidth(rangeLabelWidth);
        Label electricityMaxLabel = new Label();
        electricityMaxLabel.setWidth(rangeLabelWidth);
        electricityPanel.add(electricityLabel);
        electricityMinLabel.setText(String.valueOf(electricitySlider.getMin()));
        electricityPanel.add(electricityMinLabel);
        electricityPanel.add(electricitySlider);
        electricityMaxLabel.setText(String.valueOf(electricitySlider.getMax()));
        electricityPanel.add(electricityMaxLabel);
        electricityTextBox.setText(String.valueOf(electricitySlider.getValue()));
        electricityPanel.add(electricityTextBox);
        dynamicParametersCtrlPanel.add(electricityPanel);
    }

    private void initTetherSystemModelParamsPanel() {
        dynamicParametersCtrlPanel.add(createHeaderBlock(TETHER_SYSTEM_PARAMS_HEADER_TITLE));

        final Slider mainSatelliteMassSlider = new Slider();
        mainSatelliteMassSlider.setPixelSize(sliderWidth, sliderHeight);
        mainSatelliteMassSlider.setValue(0.5);
        mainSatelliteMassSlider.setMin(constants.minSatelliteMass());
        mainSatelliteMassSlider.setMax(constants.maxSatelliteMass());
        mainSatelliteMassSlider.setValue(tetherSystemModel.getMainSatelliteMass());
        mainSatelliteMassSlider.setTitle(MAIN_SATELLITE_MASS_TITLE);
        final TextBox mainSatelliteMassTextBox = new TextBox();
        mainSatelliteMassTextBox.setEnabled(false);
        mainSatelliteMassTextBox.setWidth(textBoxWidth);
        mainSatelliteMassSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                mainSatelliteMassTextBox.setText(String.valueOf(mainSatelliteMassSlider.getValue() + _KG));
                tetherSystemModel.setMainSatelliteMass(mainSatelliteMassSlider.getValue());
            }
        });
        HorizontalPanel mainSatelliteMassPanel = new HorizontalPanel();
        mainSatelliteMassPanel.setSpacing(spacingAttr);
        Label mainSatelliteMassLabel = new Label(MAIN_SATELLITE_MASS_TITLE);
        mainSatelliteMassLabel.setPixelSize(labelWidth, labelHeight);
        Label mainSatelliteMinMassLabel = new Label();
        mainSatelliteMinMassLabel.setWidth(rangeLabelWidth);
        Label mainSatelliteMaxMassLabel = new Label();
        mainSatelliteMaxMassLabel.setWidth(rangeLabelWidth);
        mainSatelliteMassPanel.add(mainSatelliteMassLabel);
        mainSatelliteMinMassLabel.setText(String.valueOf(mainSatelliteMassSlider.getMin()));
        mainSatelliteMassPanel.add(mainSatelliteMinMassLabel);
        mainSatelliteMassPanel.add(mainSatelliteMassSlider);
        mainSatelliteMaxMassLabel.setText(String.valueOf(mainSatelliteMassSlider.getMax()));
        mainSatelliteMassPanel.add(mainSatelliteMaxMassLabel);
        mainSatelliteMassTextBox.setText(String.valueOf(mainSatelliteMassSlider.getValue() + _KG));
        mainSatelliteMassPanel.add(mainSatelliteMassTextBox);
        dynamicParametersCtrlPanel.add(mainSatelliteMassPanel);

        final Slider nanosatellineMassSlider = new Slider();
        nanosatellineMassSlider.setPixelSize(sliderWidth, sliderHeight);
        nanosatellineMassSlider.setStep(0.5);
        nanosatellineMassSlider.setMin(constants.minSatelliteMass());
        nanosatellineMassSlider.setMax(constants.maxSatelliteMass());
        nanosatellineMassSlider.setValue(tetherSystemModel.getNanoSatelliteMass());
        final TextBox nanosatellineMassTextBox = new TextBox();
        nanosatellineMassTextBox.setEnabled(false);
        nanosatellineMassTextBox.setWidth(textBoxWidth);
        nanosatellineMassSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                nanosatellineMassTextBox.setText(String.valueOf(nanosatellineMassSlider.getValue() + _KG));
                tetherSystemModel.setNanoSatelliteMass(nanosatellineMassSlider.getValue());
            }
        });
        Label nanosatellineMassLabel = new Label(NANOSATELLITE_MASS_TITLE);
        nanosatellineMassLabel.setPixelSize(labelWidth, labelHeight);
        Label nanosatellineMinMassLabel = new Label();
        nanosatellineMinMassLabel.setWidth(rangeLabelWidth);
        Label nanosatellineMaxMassLabel = new Label();
        nanosatellineMaxMassLabel.setWidth(rangeLabelWidth);
        HorizontalPanel nanosatelliteMassPanel = new HorizontalPanel();
        nanosatelliteMassPanel.setSpacing(spacingAttr);
        nanosatelliteMassPanel.add(nanosatellineMassLabel);
        nanosatellineMinMassLabel.setText(String.valueOf(nanosatellineMassSlider.getMin()));
        nanosatelliteMassPanel.add(nanosatellineMinMassLabel);
        nanosatelliteMassPanel.add(nanosatellineMassSlider);
        nanosatellineMaxMassLabel.setText(String.valueOf(nanosatellineMassSlider.getMax()));
        nanosatelliteMassPanel.add(nanosatellineMaxMassLabel);
        nanosatellineMassTextBox.setText(String.valueOf(nanosatellineMassSlider.getValue() + _KG));
        nanosatelliteMassPanel.add(nanosatellineMassTextBox);
        dynamicParametersCtrlPanel.add(nanosatelliteMassPanel);

        final Slider systemHeightSlider = new Slider();
        systemHeightSlider.setPixelSize(sliderWidth, sliderHeight);
        systemHeightSlider.setTitle(INITIAL_SYSTEM_HEIGHT_TITLE);
        systemHeightSlider.setMin(constants.minSystemHeight());
        systemHeightSlider.setMax(constants.maxSystemHeight());
        systemHeightSlider.setValue(tetherSystemModel.getInitialHeight());
        final TextBox systemHeightTextBox = new TextBox();
        systemHeightTextBox.setEnabled(false);
        systemHeightTextBox.setWidth(textBoxWidth);
        systemHeightSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                systemHeightTextBox.setText(String.valueOf(systemHeightSlider.getValue() + _KM));
                tetherSystemModel.setInitialHeight(systemHeightSlider.getValue());
            }
        });
        HorizontalPanel systemHeightPanel = new HorizontalPanel();
        systemHeightPanel.setSpacing(spacingAttr);
        Label systemHeightLabel = new Label(INITIAL_SYSTEM_HEIGHT_TITLE);
        systemHeightLabel.setPixelSize(labelWidth, labelHeight);
        systemHeightPanel.add(systemHeightLabel);
        Label systemHeightMinLabel = new Label();
        systemHeightMinLabel.setWidth(rangeLabelWidth);
        Label systemHeightMaxLabel = new Label();
        systemHeightMaxLabel.setWidth(rangeLabelWidth);
        systemHeightMinLabel.setText(String.valueOf(systemHeightSlider.getMin()));
        systemHeightPanel.add(systemHeightMinLabel);
        systemHeightPanel.add(systemHeightSlider);
        systemHeightMaxLabel.setText(String.valueOf(systemHeightSlider.getMax()));
        systemHeightPanel.add(systemHeightMaxLabel);
        systemHeightTextBox.setText(String.valueOf(systemHeightSlider.getValue() + _KM));
        systemHeightPanel.add(systemHeightTextBox);
        dynamicParametersCtrlPanel.add(systemHeightPanel);

        final Slider eccentricitySlider = new Slider();
        eccentricitySlider.setPixelSize(sliderWidth, sliderHeight);
        eccentricitySlider.setTitle(ECCENTRICITY_TITLE);
        eccentricitySlider.setMin(constants.minEccentricity());
        eccentricitySlider.setMax(constants.maxEccentricity());
        eccentricitySlider.setValue(tetherSystemModel.getInitialEccentricity());
        eccentricitySlider.setStep(constants.minEccentricity());
        final TextBox eccentricityTextBox = new TextBox();
        eccentricityTextBox.setEnabled(false);
        eccentricityTextBox.setWidth(textBoxWidth);
        eccentricitySlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                eccentricityTextBox.setText(String.valueOf(eccentricitySlider.getValue()));
                tetherSystemModel.setInitialEccentricity(eccentricitySlider.getValue());
            }
        });
        HorizontalPanel eccentricityPanel = new HorizontalPanel();
        eccentricityPanel.setSpacing(spacingAttr);
        Label eccentricityLabel = new Label(ECCENTRICITY_TITLE);
        eccentricityLabel.setPixelSize(labelWidth, labelHeight);
        Label eccentricityMinLabel = new Label();
        eccentricityMinLabel.setWidth(rangeLabelWidth);
        Label eccentricityMaxLabel = new Label();
        eccentricityMaxLabel.setWidth(rangeLabelWidth);
        eccentricityPanel.add(eccentricityLabel);
        eccentricityMinLabel.setText(String.valueOf(eccentricitySlider.getMin()));
        eccentricityPanel.add(eccentricityMinLabel);
        eccentricityPanel.add(eccentricitySlider);
        eccentricityMaxLabel.setText(String.valueOf(eccentricitySlider.getMax()));
        eccentricityPanel.add(eccentricityMaxLabel);
        eccentricityTextBox.setText(String.valueOf(eccentricitySlider.getValue()));
        eccentricityPanel.add(eccentricityTextBox);
        dynamicParametersCtrlPanel.add(eccentricityPanel);
    }

    private void initSystemPositionStaticParamsPanel() {
        staticParametersCtrlPanel.add(createHeaderBlock(FIXED_PARAMS_HEADER_TITLE));
        VerticalPanel systemPositionStaticParamsPanel = new VerticalPanel();
        systemPositionStaticParamsPanel.setSpacing(spacingAttr);

        HorizontalPanel trueAnomalyPanel = new HorizontalPanel();
        trueAnomalyPanel.setSpacing(spacingAttr);
        Label trueAnomalyLabel = new Label(BasicConsts.TRUE_ANOMALY.getDescription());
        trueAnomalyLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        final TextBox trueAnomalyTextBox = new TextBox();
        trueAnomalyTextBox.setEnabled(false);
        trueAnomalyTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        trueAnomalyPanel.add(trueAnomalyLabel);
        trueAnomalyTextBox.setText(String.valueOf(BasicConsts.TRUE_ANOMALY.getValue()));
        trueAnomalyPanel.add(trueAnomalyTextBox);
        staticParametersCtrlPanel.add(trueAnomalyPanel);

        HorizontalPanel earthRadiusPanel = new HorizontalPanel();
        earthRadiusPanel.setSpacing(spacingAttr);
        Label earthRadiusLabel = new Label(BasicConsts.EARTH_RADIUS.getDescription());
        earthRadiusLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        earthRadiusPanel.add(earthRadiusLabel);
        final TextBox earthRadiusTextBox = new TextBox();
        earthRadiusTextBox.setEnabled(false);
        earthRadiusTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        earthRadiusTextBox.setText(String.valueOf(BasicConsts.EARTH_RADIUS.getValue() + BasicConsts.EARTH_RADIUS.getUnitMeasurement()));
        earthRadiusPanel.add(earthRadiusTextBox);
        staticParametersCtrlPanel.add(earthRadiusPanel);

        HorizontalPanel earthRotationAngularVelocityPanel = new HorizontalPanel();
        earthRotationAngularVelocityPanel.setSpacing(spacingAttr);
        Label earthRotationAngularVelocityLabel = new Label(BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getDescription());
        earthRotationAngularVelocityLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        earthRotationAngularVelocityPanel.add(earthRotationAngularVelocityLabel);
        final TextBox earthRotationAngularVelocityTextBox = new TextBox();
        earthRotationAngularVelocityTextBox.setEnabled(false);
        earthRotationAngularVelocityTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        earthRotationAngularVelocityTextBox.setText(String.valueOf(BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getValue() + BasicConsts.EARTH_ROTATION_ANGULAR_VELOCITY.getUnitMeasurement()));
        earthRotationAngularVelocityPanel.add(earthRotationAngularVelocityTextBox);
        staticParametersCtrlPanel.add(earthRotationAngularVelocityPanel);

        HorizontalPanel earthGravitationalConstPanel = new HorizontalPanel();
        earthGravitationalConstPanel.setSpacing(spacingAttr);
        Label earthGravitationalConstLabel = new Label(BasicConsts.K.getDescription());
        earthGravitationalConstLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        earthGravitationalConstPanel.add(earthGravitationalConstLabel);
        final TextBox earthGravitationalConstTextBox = new TextBox();
        earthGravitationalConstTextBox.setEnabled(false);
        earthGravitationalConstTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        earthGravitationalConstTextBox.setText(String.valueOf(BasicConsts.K.getValue() + BasicConsts.K.getUnitMeasurement()));
        earthGravitationalConstPanel.add(earthGravitationalConstTextBox);
        staticParametersCtrlPanel.add(earthGravitationalConstPanel);

        HorizontalPanel dipoleMagneticMomentPanel = new HorizontalPanel();
        dipoleMagneticMomentPanel.setSpacing(spacingAttr);
        Label dipoleMagneticMomentLabel = new Label(BasicConsts.MU.getDescription());
        dipoleMagneticMomentLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        dipoleMagneticMomentPanel.add(dipoleMagneticMomentLabel);
        final TextBox dipoleMagneticMomentTextBox = new TextBox();
        dipoleMagneticMomentTextBox.setEnabled(false);
        dipoleMagneticMomentTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        dipoleMagneticMomentTextBox.setText(String.valueOf(BasicConsts.MU.getValue() + BasicConsts.MU.getUnitMeasurement()));
        dipoleMagneticMomentPanel.add(dipoleMagneticMomentTextBox);
        staticParametersCtrlPanel.add(dipoleMagneticMomentPanel);

        HorizontalPanel electronMassPanel = new HorizontalPanel();
        electronMassPanel.setSpacing(spacingAttr);
        Label electronMassLabel = new Label(BasicConsts.ELECTRON_MASS.getDescription());
        electronMassLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        electronMassPanel.add(electronMassLabel);
        final TextBox electronMassTextBox = new TextBox();
        electronMassTextBox.setEnabled(false);
        electronMassTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        electronMassTextBox.setText(String.valueOf(BasicConsts.ELECTRON_MASS.getValue() + BasicConsts.ELECTRON_MASS.getUnitMeasurement()));
        electronMassPanel.add(electronMassTextBox);
        staticParametersCtrlPanel.add(electronMassPanel);

        HorizontalPanel electronChargePanel = new HorizontalPanel();
        electronChargePanel.setSpacing(spacingAttr);
        Label electronChargeLabel = new Label(BasicConsts.ELECTON_CHARGE.getDescription());
        electronChargeLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        electronChargePanel.add(electronChargeLabel);
        final TextBox electronChargeTextBox = new TextBox();
        electronChargeTextBox.setEnabled(false);
        electronChargeTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        electronChargeTextBox.setText(String.valueOf(BasicConsts.ELECTON_CHARGE.getValue() + BasicConsts.ELECTON_CHARGE.getUnitMeasurement()));
        electronChargePanel.add(electronChargeTextBox);
        staticParametersCtrlPanel.add(electronChargePanel);

        HorizontalPanel plasmaConcentrationPanel = new HorizontalPanel();
        plasmaConcentrationPanel.setSpacing(spacingAttr);
        Label plasmaConcentrationLabel = new Label(BasicConsts.PLASMA_CONCENTRATION.getDescription());
        plasmaConcentrationLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        plasmaConcentrationPanel.add(plasmaConcentrationLabel);
        final TextBox plasmaConcentrationTextBox = new TextBox();
        plasmaConcentrationTextBox.setEnabled(false);
        plasmaConcentrationTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        plasmaConcentrationTextBox.setText(String.valueOf(BasicConsts.PLASMA_CONCENTRATION.getValue() + BasicConsts.PLASMA_CONCENTRATION.getUnitMeasurement()));
        plasmaConcentrationPanel.add(plasmaConcentrationTextBox);
        staticParametersCtrlPanel.add(plasmaConcentrationPanel);

        HorizontalPanel aluminumConductivityPanel = new HorizontalPanel();
        aluminumConductivityPanel.setSpacing(spacingAttr);
        Label aluminumConductivityLabel = new Label(BasicConsts.ALUMINUM_CONDUCTIVITY.getDescription());
        aluminumConductivityLabel.addStyleName(PARAM_LABEL_STYLE_NAME);
        aluminumConductivityPanel.add(aluminumConductivityLabel);
        final TextBox aluminumConductivityTextBox = new TextBox();
        aluminumConductivityTextBox.setEnabled(false);
        aluminumConductivityTextBox.addStyleName(PARAM_TEXT_BOX_STYLE_NAME);
        aluminumConductivityTextBox.setText(String.valueOf(BasicConsts.ALUMINUM_CONDUCTIVITY.getValue() + BasicConsts.ALUMINUM_CONDUCTIVITY.getUnitMeasurement()));
        aluminumConductivityPanel.add(aluminumConductivityTextBox);
        staticParametersCtrlPanel.add(aluminumConductivityPanel);
    }

    private void initCalculationParamsPanel() {
        dynamicParametersCtrlPanel.add(createHeaderBlock(INTEGRATION_PARAMS_HEADER_TITLE));

        final Slider maxCalculationStepSlider = new Slider();
        final Slider calculationStepSlider = new Slider();
        calculationStepSlider.setPixelSize(sliderWidth, sliderHeight);
        calculationStepSlider.setMin(constants.minCalculationStep());
        calculationStepSlider.setMax(constants.maxCalculationStep());
        calculationStepSlider.setValue(tetherSystemModel.getIntegrationStep());
        calculationStepSlider.setTitle(INTEGRATION_STEP_TITLE);
        final TextBox calculationStepSliderTextBox = new TextBox();
        calculationStepSliderTextBox.setEnabled(false);
        calculationStepSliderTextBox.setWidth(textBoxWidth);
        calculationStepSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (calculationStepSlider.getValue() > maxCalculationStepSlider.getValue()) {
                    calculationStepSlider.setValue(maxCalculationStepSlider.getValue());
                }
                calculationStepSliderTextBox.setText(String.valueOf(calculationStepSlider.getValue() + _SEC));
                tetherSystemModel.setIntegrationStep(calculationStepSlider.getValue());
            }
        });
        HorizontalPanel calculationStepPanel = new HorizontalPanel();
        calculationStepPanel.setSpacing(spacingAttr);
        Label calculationStepSliderLabel = new Label(INTEGRATION_STEP_TITLE);
        calculationStepSliderLabel.setPixelSize(labelWidth, labelHeight);
        Label calculationStepSliderMinLabel = new Label();
        calculationStepSliderMinLabel.setWidth(rangeLabelWidth);
        Label calculationStepSliderMaxLabel = new Label();
        calculationStepSliderMaxLabel.setWidth(rangeLabelWidth);
        calculationStepPanel.add(calculationStepSliderLabel);
        calculationStepSliderMinLabel.setText(String.valueOf(calculationStepSlider.getMin()));
        calculationStepPanel.add(calculationStepSliderMinLabel);
        calculationStepPanel.add(calculationStepSlider);
        calculationStepSliderMaxLabel.setText(String.valueOf(calculationStepSlider.getMax()));
        calculationStepPanel.add(calculationStepSliderMaxLabel);
        calculationStepSliderTextBox.setText(String.valueOf(calculationStepSlider.getValue() + _SEC));
        calculationStepPanel.add(calculationStepSliderTextBox);
        dynamicParametersCtrlPanel.add(calculationStepPanel);

        maxCalculationStepSlider.setPixelSize(sliderWidth, sliderHeight);
        maxCalculationStepSlider.setMin(constants.minCalculationMaxStep());
        maxCalculationStepSlider.setMax(constants.maxCalculationMaxStep());
        maxCalculationStepSlider.setValue(tetherSystemModel.getIntegrationMaxStep());
        final TextBox maxCalculationStepTextBox = new TextBox();
        maxCalculationStepTextBox.setEnabled(false);
        maxCalculationStepTextBox.setWidth(textBoxWidth);
        maxCalculationStepSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                maxCalculationStepTextBox.setText(String.valueOf(maxCalculationStepSlider.getValue() + _SEC));
                tetherSystemModel.setIntegrationMaxStep(maxCalculationStepSlider.getValue());
            }
        });
        Label maxCalculationStepLabel = new Label(INTEGRATION_MAX_STEP_TITLE);
        maxCalculationStepLabel.setPixelSize(labelWidth, labelHeight);
        Label maxCalculationStepMinRangeLabel = new Label();
        maxCalculationStepMinRangeLabel.setWidth(rangeLabelWidth);
        Label maxCalculationStepMaxRangeLabel = new Label();
        maxCalculationStepMaxRangeLabel.setWidth(rangeLabelWidth);
        HorizontalPanel maxCalculationStepPanel = new HorizontalPanel();
        maxCalculationStepPanel.setSpacing(spacingAttr);
        maxCalculationStepPanel.add(maxCalculationStepLabel);
        maxCalculationStepMinRangeLabel.setText(String.valueOf(maxCalculationStepSlider.getMin()));
        maxCalculationStepPanel.add(maxCalculationStepMinRangeLabel);
        maxCalculationStepPanel.add(maxCalculationStepSlider);
        maxCalculationStepMaxRangeLabel.setText(String.valueOf(maxCalculationStepSlider.getMax()));
        maxCalculationStepPanel.add(maxCalculationStepMaxRangeLabel);
        maxCalculationStepTextBox.setText(String.valueOf(maxCalculationStepSlider.getValue() + _SEC));
        maxCalculationStepPanel.add(maxCalculationStepTextBox);
        dynamicParametersCtrlPanel.add(maxCalculationStepPanel);

        final Slider maxIterationSlider = new Slider();
        maxIterationSlider.setPixelSize(sliderWidth, sliderHeight);
        maxIterationSlider.setTitle(INTEGRATION_MAX_ITERATIONS_TITLE);
        maxIterationSlider.setMin(constants.minIterations());
        maxIterationSlider.setMax(constants.maxIterations());
        maxIterationSlider.setValue(maxIterations);
        final TextBox maxIterationTextBox = new TextBox();
        maxIterationTextBox.setEnabled(false);
        maxIterationTextBox.setWidth(textBoxWidth);
        maxIterationSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                maxIterationTextBox.setText(String.valueOf(maxIterationSlider.getValue()));
                tetherSystemModel.setMaxIterations((int) maxIterationSlider.getValue());
            }
        });
        HorizontalPanel maxIterationPanel = new HorizontalPanel();
        maxIterationPanel.setSpacing(spacingAttr);
        Label maxIterationLabel = new Label(INTEGRATION_MAX_ITERATIONS_TITLE);
        maxIterationLabel.setPixelSize(labelWidth, labelHeight);
        Label maxIterationMinLabel = new Label();
        maxIterationMinLabel.setWidth(rangeLabelWidth);
        Label maxIterationMaxLabel = new Label();
        maxIterationMaxLabel.setWidth(rangeLabelWidth);
        maxIterationPanel.add(maxIterationLabel);
        maxIterationMinLabel.setText(String.valueOf(maxIterationSlider.getMin()));
        maxIterationPanel.add(maxIterationMinLabel);
        maxIterationPanel.add(maxIterationSlider);
        maxIterationMaxLabel.setText(String.valueOf(maxIterationSlider.getMax()));
        maxIterationPanel.add(maxIterationMaxLabel);
        maxIterationTextBox.setText(String.valueOf(maxIterationSlider.getValue()));
        maxIterationPanel.add(maxIterationTextBox);
        dynamicParametersCtrlPanel.add(maxIterationPanel);

        final Slider calcAccuracySlider = new Slider();
        calcAccuracySlider.setPixelSize(sliderWidth, sliderHeight);
        calcAccuracySlider.setTitle(INTEGRATION_ACCURACY_TITLE);
        calcAccuracySlider.setStep(constants.minIntegrationAccuracy());
        calcAccuracySlider.setMin(constants.minIntegrationAccuracy());
        calcAccuracySlider.setMax(constants.maxIntegrationAccuracy());
        calcAccuracySlider.setValue(integrationAccuracy);
        final TextBox calcAccuracyTextBox = new TextBox();
        calcAccuracyTextBox.setEnabled(false);
        calcAccuracyTextBox.setWidth(textBoxWidth);
        calcAccuracySlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                calcAccuracyTextBox.setText(String.valueOf(calcAccuracySlider.getValue()));
                tetherSystemModel.setCalculateAccuracy(calcAccuracySlider.getValue());
            }
        });
        HorizontalPanel calcAccuracyPanel = new HorizontalPanel();
        calcAccuracyPanel.setSpacing(spacingAttr);
        Label calcAccuracyLabel = new Label(INTEGRATION_ACCURACY_TITLE);
        calcAccuracyLabel.setPixelSize(labelWidth, labelHeight);
        Label calcAccuracyMinLabel = new Label();
        calcAccuracyMinLabel.setWidth(rangeLabelWidth);
        Label calcAccuracyMaxLabel = new Label();
        calcAccuracyMaxLabel.setWidth(rangeLabelWidth);
        calcAccuracyPanel.add(calcAccuracyLabel);
        calcAccuracyMinLabel.setText(String.valueOf(calcAccuracySlider.getMin()));
        calcAccuracyPanel.add(calcAccuracyMinLabel);
        calcAccuracyPanel.add(calcAccuracySlider);
        calcAccuracyMaxLabel.setText(String.valueOf(calcAccuracySlider.getMax()));
        calcAccuracyPanel.add(calcAccuracyMaxLabel);
        calcAccuracyTextBox.setText(String.valueOf(calcAccuracySlider.getValue()));
        calcAccuracyPanel.add(calcAccuracyTextBox);
        dynamicParametersCtrlPanel.add(calcAccuracyPanel);
    }

    public void clearVerticalPanel() {
        pageContentInnerPanel.clear();
    }
}
