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
import com.spaceApplication.client.space.model.BareElectrodynamicTetherClient;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.client.space.ui.components.Slider;
import com.spaceApplication.client.space.ui.components.UIConsts;

import static com.spaceApplication.client.space.ui.components.UIConsts.*;

/**
 * Created by Chris
 */
public class ModelParameterCtrlPresenter extends Composite {
    private VerticalPanel dynamicParametersCtrlPanel = new VerticalPanel();
    private VerticalPanel staticParametersCtrlPanel = new VerticalPanel();
    private Button executeCalculationButton;

    private Slider tetherMassSlider, tetherLengthSlider, tetherDiameterSlider;
    private TextBox tetherMassTextBox, tetherLengthTextBox, tetherDiameterTextBox;
    private Label tetherMassLabel, tetherLengthLabel, tetherDiameterLabel;
    private Label tetherMassMinLabel, tetherMassMaxLabel, tetherLengthMinLabel, tetherLengthMaxLabel, tetherDiameterMinLabel, tetherDiameterMaxLabel;

    private Slider mainSatelliteMassSlider, nanosatellineMassSlider, systemHeightSlider;
    private TextBox mainSatelliteMassTextBox, nanosatellineMassTextBox, systemHeightTextBox;
    private Label mainSatelliteMassLabel, nanosatellineMassLabel, systemHeightLabel;
    private Label mainSatelliteMinMassLabel, mainSatelliteMaxMassLabel, nanosatellineMinMassLabel, nanosatellineMaxMassLabel, systemHeightMinLabel, systemHeightMaxLabel;

    private Slider calculationStepSlider, maxCalculationStepSlider, maxIterationSlider, calcAccuracySlider;
    private TextBox calculationStepSliderTextBox, maxCalculationStepTextBox, maxIterationTextBox, calcAccuracyTextBox;
    private Label calculationStepSliderLabel, maxCalculationStepLabel, maxIterationLabel, calcAccuracyLabel;
    private Label calculationStepSliderMinLabel, calculationStepSliderMaxLabel, maxCalculationStepMinRangeLabel, maxCalculationStepMaxRangeLabel,
            maxIterationMinLabel, maxIterationMaxLabel, calcAccuracyMinLabel, calcAccuracyMaxLabel;

    private TextBox deflectionAngleTextBox, initialTrueAnomalyTextBox, initialEccentricityTextBox;
    private Label deflectionAngleLabel, initialTrueAnomalyLabel, initialEccentricityLabel;

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
    private int sliderWidth = 450, sliderHeight = 50;
    private int spacingAttr = 300;
    private String rangeLabelWidth = "50px";

    private double toKilo = 1000;
    private BareElectrodynamicTetherClient testTether;
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
//            pageContentInnerPanel.add(RemoteCalculationControl.createAllResultPlots(result, tetherSystemModel));
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
        initTextBoxes();
        initLabels();
        initSliders();

        testTether = new BareElectrodynamicTetherClient(0.4, 2000, 0.001);
        tetherSystemModel = new ElectrodynamicTetherSystemModelClient(testTether, 6, 2, 500000,
                ElectrodynamicTetherSystemModelClient.getDefaultDeflectionAngle(), ElectrodynamicTetherSystemModelClient.getDefaultInitialTrueAnomaly(),
                ElectrodynamicTetherSystemModelClient.getDefaultInitialEccentricity(), maxIterations, integrationStep, integrationMaxStep, integrationAccuracy);

        initTetherModelParamsPanel();
        initTetherSystemModelParamsPanel();
        initCalculationParamsPanel();

        printSystemPositionStaticParamsPanel();
    }

    public AsyncCallback<OrbitalElementsClient> getCalculationCallback() {
        return calculationCallback;
    }

    public ElectrodynamicTetherSystemModelClient getTestTetherSystemModel() {
        return tetherSystemModel;
    }

    public Widget initWidget() {
        pageContentInnerPanel = new HTMLPanel("");
        pageContentInnerPanel.addStyleName(PAGE_INNER_SELECTOR);
        pageContentInnerPanel.add(dynamicParametersCtrlPanel);
        pageContentInnerPanel.add(staticParametersCtrlPanel);
        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        executeCalculationButton = new Button("Начать вычисление");
        buttonWrapper.add(executeCalculationButton);
        executeCalculationButton.addClickHandler(executeCalculationClickHandler);
        executeCalculationButton.setStyleName(UIConsts.BUTTON_STYLE_NAME);
        pageContentInnerPanel.add(buttonWrapper);
        return pageContentInnerPanel;
    }

    private void initTetherModelParamsPanel() {
        HTML contentHeader = new HTML("<div class=\"page-title\"><h3 class=" + UIConsts.BREADCRUMB_HEADER_STYLE_NAME + ">" + "Параметры троса" + "</h1></div");
        dynamicParametersCtrlPanel.add(contentHeader);

        tetherMassSlider.setMin(0.1);
        tetherMassSlider.setMax(2.0);
        tetherMassSlider.setValue(0.4);
        tetherMassLabel.setText("Масса троса");
        tetherMassSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                tetherMassTextBox.setText(String.valueOf(tetherMassSlider.getValue() + _KG));
                testTether.setMass(tetherMassSlider.getValue());
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

        tetherLengthSlider.setMin(0.5);
        tetherLengthSlider.setMax(3.0);
        tetherLengthSlider.setValue(2.0);
        tetherLengthSlider.setTitle("Длина троса");
        tetherLengthSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                tetherLengthTextBox.setText(String.valueOf(tetherLengthSlider.getValue() + _KM));
                testTether.setLength(tetherLengthSlider.getValue());
            }
        });
        HorizontalPanel tetherLengthPanel = new HorizontalPanel();
        tetherLengthPanel.setSpacing(spacingAttr);
        tetherLengthPanel.add(tetherLengthLabel);
        tetherLengthMinLabel.setText(String.valueOf(tetherLengthSlider.getMin()));
        tetherLengthPanel.add(tetherLengthMinLabel);
        tetherLengthPanel.add(tetherLengthSlider);
        tetherLengthMaxLabel.setText(String.valueOf(tetherLengthSlider.getMax()));
        tetherLengthPanel.add(tetherLengthMaxLabel);
        tetherLengthPanel.add(tetherLengthTextBox);
        tetherMassTextBox.setText(String.valueOf(tetherLengthSlider.getValue() + _KM));
        dynamicParametersCtrlPanel.add(tetherLengthPanel);

        tetherDiameterSlider.setMin(0.001);
        tetherDiameterSlider.setMax(0.005);
        tetherDiameterSlider.setValue(0.001);
        tetherDiameterSlider.setTitle("Диаметр троса");
        tetherDiameterSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                tetherDiameterTextBox.setText(String.valueOf(tetherDiameterSlider.getValue() + _KM));
                testTether.setDiameter(tetherDiameterSlider.getValue());
            }
        });
        HorizontalPanel tetherDiameterPanel = new HorizontalPanel();
        tetherDiameterPanel.setSpacing(spacingAttr);
        tetherDiameterPanel.add(tetherDiameterLabel);
        tetherDiameterMinLabel.setText(String.valueOf(tetherDiameterSlider.getMin()));
        tetherDiameterPanel.add(tetherDiameterMinLabel);
        tetherDiameterPanel.add(tetherDiameterSlider);
        tetherDiameterMaxLabel.setText(String.valueOf(tetherDiameterSlider.getMax()));
        tetherDiameterPanel.add(tetherDiameterMaxLabel);
        tetherDiameterPanel.add(tetherDiameterTextBox);
        tetherMassTextBox.setText(String.valueOf(tetherDiameterSlider.getValue() + _KM));
        dynamicParametersCtrlPanel.add(tetherDiameterPanel);
    }

    private void initTetherSystemModelParamsPanel() {
        HTML contentHeader = new HTML("<div class=\"page-title\"><h3 class=" + UIConsts.BREADCRUMB_HEADER_STYLE_NAME + ">" + "Параметры тросовой системы" + "</h1></div");
        dynamicParametersCtrlPanel.add(contentHeader);

        mainSatelliteMassSlider.setMin(2.0);
        mainSatelliteMassSlider.setMax(10.0);
        mainSatelliteMassSlider.setValue(6.0);
        mainSatelliteMassSlider.setTitle("Масса космического аппарата");
        mainSatelliteMassSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                mainSatelliteMassTextBox.setText(String.valueOf(mainSatelliteMassSlider.getValue() + _KG));
            }
        });
        HorizontalPanel mainSatelliteMassPanel = new HorizontalPanel();
        mainSatelliteMassPanel.setSpacing(spacingAttr);
        mainSatelliteMassPanel.add(mainSatelliteMassLabel);
        mainSatelliteMinMassLabel.setText(String.valueOf(mainSatelliteMassSlider.getMin()));
        mainSatelliteMassPanel.add(mainSatelliteMinMassLabel);
        mainSatelliteMassPanel.add(mainSatelliteMassSlider);
        mainSatelliteMaxMassLabel.setText(String.valueOf(mainSatelliteMassSlider.getMax()));
        mainSatelliteMassPanel.add(mainSatelliteMaxMassLabel);
        mainSatelliteMassTextBox.setText(String.valueOf(mainSatelliteMassSlider.getValue() + _KG));
        mainSatelliteMassPanel.add(mainSatelliteMassTextBox);
        dynamicParametersCtrlPanel.add(mainSatelliteMassPanel);

        nanosatellineMassSlider.setMin(2.0);
        nanosatellineMassSlider.setMax(10.0);
        nanosatellineMassSlider.setValue(2.0);
        nanosatellineMassSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                nanosatellineMassTextBox.setText(String.valueOf(nanosatellineMassSlider.getValue() + _KG));
            }
        });
        nanosatellineMassLabel.setTitle("Масса наноспутника");
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

        systemHeightSlider.setTitle("Высота центра масс системы");
        systemHeightSlider.setMin(500.0);
        systemHeightSlider.setMax(1000.0);
        systemHeightSlider.setValue(500.0);
        systemHeightSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                systemHeightTextBox.setText(String.valueOf(systemHeightSlider.getValue() + _KM));
            }
        });
        HorizontalPanel systemHeightPanel = new HorizontalPanel();
        systemHeightPanel.setSpacing(spacingAttr);
        systemHeightPanel.add(systemHeightLabel);
        systemHeightMinLabel.setText(String.valueOf(systemHeightSlider.getMin()));
        systemHeightPanel.add(systemHeightMinLabel);
        systemHeightPanel.add(systemHeightSlider);
        systemHeightMaxLabel.setText(String.valueOf(systemHeightSlider.getMax()));
        systemHeightPanel.add(systemHeightMaxLabel);
        systemHeightTextBox.setText(String.valueOf(systemHeightSlider.getValue() + _KM));
        systemHeightPanel.add(systemHeightTextBox);
        dynamicParametersCtrlPanel.add(systemHeightPanel);
    }

    private void printSystemPositionStaticParamsPanel() {
        HTML contentHeader = new HTML("<div class=\"page-title\"><h3 class=" + UIConsts.BREADCRUMB_HEADER_STYLE_NAME + ">" + "Фиксированные параметры тросовой системы" + "</h1></div");
        staticParametersCtrlPanel.add(contentHeader);
        VerticalPanel systemPositionStaticParamsPanel = new VerticalPanel();
        systemPositionStaticParamsPanel.setSpacing(spacingAttr);

        deflectionAngleLabel.setText(UIConsts.DEFLECTION_ANGLE_TITLE);
        deflectionAngleTextBox.setText(String.valueOf(ElectrodynamicTetherSystemModelClient.getDefaultDeflectionAngle()));
        systemPositionStaticParamsPanel.add(deflectionAngleLabel);
        systemPositionStaticParamsPanel.add(deflectionAngleTextBox);

        initialTrueAnomalyLabel.setText(UIConsts.TRUE_ANOMALY_TITLE);
        initialTrueAnomalyTextBox.setText(String.valueOf(ElectrodynamicTetherSystemModelClient.getDefaultInitialTrueAnomaly()));
        systemPositionStaticParamsPanel.add(initialTrueAnomalyLabel);
        systemPositionStaticParamsPanel.add(initialTrueAnomalyTextBox);

        initialEccentricityLabel.setText(UIConsts.ECCENTRICITY_TITLE);
        initialEccentricityTextBox.setText(String.valueOf(ElectrodynamicTetherSystemModelClient.getDefaultInitialEccentricity()));
        systemPositionStaticParamsPanel.add(initialEccentricityLabel);
        systemPositionStaticParamsPanel.add(initialEccentricityTextBox);

        staticParametersCtrlPanel.add(systemPositionStaticParamsPanel);
    }

    private void initCalculationParamsPanel() {
        HTML contentHeader = new HTML("<div class=\"page-title\"><h3 class=" + UIConsts.BREADCRUMB_HEADER_STYLE_NAME + ">" + "Параметры метода численного интегрирования" + "</h1></div");
        dynamicParametersCtrlPanel.add(contentHeader);

        calculationStepSlider.setMin(5.0);
        calculationStepSlider.setMax(10.0);
        calculationStepSlider.setValue(integrationStep);
        calculationStepSlider.setTitle("Шаг интегрирования");
        calculationStepSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                calculationStepSliderTextBox.setText(String.valueOf(calculationStepSlider.getValue() + _SEC));
                tetherSystemModel.setIntegrationStep(calculationStepSlider.getValue());
            }
        });
        HorizontalPanel calculationStepPanel = new HorizontalPanel();
        calculationStepPanel.setSpacing(spacingAttr);
        calculationStepPanel.add(calculationStepSliderLabel);
        calculationStepSliderMinLabel.setText(String.valueOf(calculationStepSlider.getMin()));
        calculationStepPanel.add(calculationStepSliderMinLabel);
        calculationStepPanel.add(calculationStepSlider);
        calculationStepSliderMaxLabel.setText(String.valueOf(calculationStepSlider.getMax()));
        calculationStepPanel.add(calculationStepSliderMaxLabel);
        calculationStepSliderTextBox.setText(String.valueOf(calculationStepSlider.getValue() + _SEC));
        calculationStepPanel.add(calculationStepSliderTextBox);
        dynamicParametersCtrlPanel.add(calculationStepPanel);

        maxCalculationStepSlider.setMin(5.0);
        maxCalculationStepSlider.setMax(10.0);
        maxCalculationStepSlider.setValue(integrationMaxStep);
        maxCalculationStepSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                maxCalculationStepTextBox.setText(String.valueOf(maxCalculationStepSlider.getValue() + _SEC));
                tetherSystemModel.setIntegrationMaxStep(maxCalculationStepSlider.getValue());
            }
        });
        maxCalculationStepLabel.setTitle("Максимальный шаг интегрирования");
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

        maxIterationSlider.setTitle("Максимальное число итераций");
        maxIterationSlider.setMin(50.0);
        maxIterationSlider.setMax(maxIterations);
        maxIterationSlider.setValue(maxIterations);
        maxIterationSlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                maxIterationTextBox.setText(String.valueOf(maxIterationSlider.getValue()));
                tetherSystemModel.setMaxIterations((int) maxIterationSlider.getValue());
            }
        });
        HorizontalPanel maxIterationPanel = new HorizontalPanel();
        maxIterationPanel.setSpacing(spacingAttr);
        maxIterationPanel.add(maxIterationLabel);
        maxIterationMinLabel.setText(String.valueOf(maxIterationSlider.getMin()));
        maxIterationPanel.add(maxIterationMinLabel);
        maxIterationPanel.add(maxIterationSlider);
        maxIterationMaxLabel.setText(String.valueOf(maxIterationSlider.getMax()));
        maxIterationPanel.add(maxIterationMaxLabel);
        maxIterationTextBox.setText(String.valueOf(maxIterationSlider.getValue()));
        maxIterationPanel.add(maxIterationTextBox);
        dynamicParametersCtrlPanel.add(maxIterationPanel);

        calcAccuracySlider.setTitle("Точность вычислений");
        calcAccuracySlider.setMin(0.001);
        calcAccuracySlider.setMax(0.1);
        calcAccuracySlider.setValue(integrationAccuracy);
        calcAccuracySlider.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                calcAccuracyTextBox.setText(String.valueOf(calcAccuracySlider.getValue()));
                tetherSystemModel.setCalculateAccuracy(calcAccuracySlider.getValue());
            }
        });
        HorizontalPanel calcAccuracyPanel = new HorizontalPanel();
        calcAccuracyPanel.setSpacing(spacingAttr);
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


    private void initLabels() {
        tetherMassLabel = new Label("Масса троса");
        tetherLengthLabel = new Label("Длина троса");
        tetherDiameterLabel = new Label("Диаметр троса");
        tetherMassMinLabel = new Label();
        tetherMassMaxLabel = new Label();
        tetherLengthMinLabel = new Label();
        tetherLengthMaxLabel = new Label();
        tetherDiameterMinLabel = new Label();
        tetherDiameterMaxLabel = new Label();

        mainSatelliteMassLabel = new Label("Масса космического аппарата");
        nanosatellineMassLabel = new Label("Масса наноспутника");
        mainSatelliteMinMassLabel = new Label();
        mainSatelliteMaxMassLabel = new Label();
        nanosatellineMinMassLabel = new Label();
        nanosatellineMaxMassLabel = new Label();
        systemHeightLabel = new Label("Высота центра масс системы");
        systemHeightMinLabel = new Label();
        systemHeightMaxLabel = new Label();

        calculationStepSliderLabel = new Label("Начальный шаг интегрирования, c");
        maxCalculationStepLabel = new Label("Максимальный шаг интегрирования, c");
        maxIterationLabel = new Label("Максимальное число итераций");
        calcAccuracyLabel = new Label("Погрешность интегрирования");
        calculationStepSliderMinLabel = new Label();
        calculationStepSliderMaxLabel = new Label();
        maxCalculationStepMinRangeLabel = new Label();
        maxCalculationStepMaxRangeLabel = new Label();
        maxIterationMinLabel = new Label();
        maxIterationMaxLabel = new Label();
        calcAccuracyMinLabel = new Label();
        calcAccuracyMaxLabel = new Label();
        deflectionAngleLabel = new Label("Угол отклонения троса от вертикали");
        initialTrueAnomalyLabel = new Label("Истинная аномалия Земли");
        initialEccentricityLabel = new Label("Эксцентриситет");

        tetherMassMinLabel.setWidth(rangeLabelWidth);
        tetherMassMaxLabel.setWidth(rangeLabelWidth);
        tetherLengthMinLabel.setWidth(rangeLabelWidth);
        tetherLengthMaxLabel.setWidth(rangeLabelWidth);
        tetherDiameterMinLabel.setWidth(rangeLabelWidth);
        tetherDiameterMaxLabel.setWidth(rangeLabelWidth);
        calculationStepSliderMinLabel.setWidth(rangeLabelWidth);
        calculationStepSliderMaxLabel.setWidth(rangeLabelWidth);
        maxCalculationStepMinRangeLabel.setWidth(rangeLabelWidth);
        maxCalculationStepMaxRangeLabel.setWidth(rangeLabelWidth);
        maxIterationMinLabel.setWidth(rangeLabelWidth);
        maxIterationMaxLabel.setWidth(rangeLabelWidth);
        calcAccuracyMinLabel.setWidth(rangeLabelWidth);
        calcAccuracyMaxLabel.setWidth(rangeLabelWidth);
        mainSatelliteMinMassLabel.setWidth(rangeLabelWidth);
        mainSatelliteMaxMassLabel.setWidth(rangeLabelWidth);
        nanosatellineMinMassLabel.setWidth(rangeLabelWidth);
        nanosatellineMaxMassLabel.setWidth(rangeLabelWidth);
        systemHeightMinLabel.setWidth(rangeLabelWidth);
        systemHeightMaxLabel.setWidth(rangeLabelWidth);

        tetherMassLabel.setPixelSize(labelWidth, labelHeight);
        tetherLengthLabel.setPixelSize(labelWidth, labelHeight);
        tetherDiameterLabel.setPixelSize(labelWidth, labelHeight);
        systemHeightLabel.setPixelSize(labelWidth, labelHeight);
        mainSatelliteMassLabel.setPixelSize(labelWidth, labelHeight);
        nanosatellineMassLabel.setPixelSize(labelWidth, labelHeight);
        calculationStepSliderLabel.setPixelSize(labelWidth, labelHeight);
        maxCalculationStepLabel.setPixelSize(labelWidth, labelHeight);
        maxIterationLabel.setPixelSize(labelWidth, labelHeight);
        calcAccuracyLabel.setPixelSize(labelWidth, labelHeight);
        deflectionAngleLabel.setPixelSize(labelWidth, labelHeight);
        initialTrueAnomalyLabel.setPixelSize(labelWidth, labelHeight);
        initialEccentricityLabel.setPixelSize(labelWidth, labelHeight);
    }

    private void initTextBoxes() {
        tetherMassTextBox = new TextBox();
        tetherLengthTextBox = new TextBox();
        tetherDiameterTextBox = new TextBox();
        mainSatelliteMassTextBox = new TextBox();
        nanosatellineMassTextBox = new TextBox();
        systemHeightTextBox = new TextBox();
        calculationStepSliderTextBox = new TextBox();
        maxCalculationStepTextBox = new TextBox();
        maxIterationTextBox = new TextBox();
        calcAccuracyTextBox = new TextBox();
        deflectionAngleTextBox = new TextBox();
        initialTrueAnomalyTextBox = new TextBox();
        initialEccentricityTextBox = new TextBox();

        tetherMassTextBox.setEnabled(false);
        tetherLengthTextBox.setEnabled(false);
        tetherDiameterTextBox.setEnabled(false);
        mainSatelliteMassTextBox.setEnabled(false);
        nanosatellineMassTextBox.setEnabled(false);
        systemHeightTextBox.setEnabled(false);
        calculationStepSliderTextBox.setEnabled(false);
        maxCalculationStepTextBox.setEnabled(false);
        maxIterationTextBox.setEnabled(false);
        calcAccuracyTextBox.setEnabled(false);
        deflectionAngleTextBox.setEnabled(false);
        initialTrueAnomalyTextBox.setEnabled(false);
        initialEccentricityTextBox.setEnabled(false);

        tetherMassTextBox.setWidth(textBoxWidth);
        tetherLengthTextBox.setWidth(textBoxWidth);
        tetherDiameterTextBox.setWidth(textBoxWidth);
        mainSatelliteMassTextBox.setWidth(textBoxWidth);
        nanosatellineMassTextBox.setWidth(textBoxWidth);
        systemHeightTextBox.setWidth(textBoxWidth);
        calculationStepSliderTextBox.setWidth(textBoxWidth);
        maxCalculationStepTextBox.setWidth(textBoxWidth);
        maxIterationTextBox.setWidth(textBoxWidth);
        calcAccuracyTextBox.setWidth(textBoxWidth);
        deflectionAngleTextBox.setWidth(textBoxWidth);
        initialTrueAnomalyTextBox.setWidth(textBoxWidth);
        initialEccentricityTextBox.setWidth(textBoxWidth);
    }

    private void initSliders() {
        tetherMassSlider = new Slider();
        tetherLengthSlider = new Slider();
        tetherDiameterSlider = new Slider();
        mainSatelliteMassSlider = new Slider();
        nanosatellineMassSlider = new Slider();
        systemHeightSlider = new Slider();
        calculationStepSlider = new Slider();
        maxCalculationStepSlider = new Slider();
        maxIterationSlider = new Slider();
        calcAccuracySlider = new Slider();

        mainSatelliteMassSlider.setPixelSize(sliderWidth, sliderHeight);
        nanosatellineMassSlider.setPixelSize(sliderWidth, sliderHeight);
        systemHeightSlider.setPixelSize(sliderWidth, sliderHeight);
        tetherMassSlider.setPixelSize(sliderWidth, sliderHeight);
        tetherLengthSlider.setPixelSize(sliderWidth, sliderHeight);
        tetherDiameterSlider.setPixelSize(sliderWidth, sliderHeight);
        calculationStepSlider.setPixelSize(sliderWidth, sliderHeight);
        maxCalculationStepSlider.setPixelSize(sliderWidth, sliderHeight);
        maxIterationSlider.setPixelSize(sliderWidth, sliderHeight);
        calcAccuracySlider.setPixelSize(sliderWidth, sliderHeight);
    }

    public void clearVerticalPanel() {
        pageContentInnerPanel.clear();
    }
}
