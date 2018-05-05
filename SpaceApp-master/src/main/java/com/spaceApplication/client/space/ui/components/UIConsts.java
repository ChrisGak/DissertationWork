package com.spaceApplication.client.space.ui.components;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Created by Chris
 */
public class UIConsts {
    /**
     * Style names
     */
    public static final String imageStyle = "img-responsive center-block img_pdng";
    public static final String BUTTON_STYLE_NAME = "btn btn-primary btn-lg";
    public static final String squareButtonStyle = "square-btn btn-primary btn-lg";
    public static final String text_muted = "lead text-muted";
    public static final String info_popup = "info-popup";
    public static final String HEADER_STYLE_NAME = "section-header";
    public static final String BREADCRUMB_HEADER_STYLE_NAME = "breadcrumb-header";

    public static final String ACTIVE_ELEMENT = "active-page";
    public static final String PAGE_CONTENT_SELECTOR = "page-content";
    public static final String PAGE_INNER_STYLE_NAME = "page-inner";
    public static final String SHORT_WIDGET_NUMBER_STYLE_NAME = "stats-number";
    public static final String SHORT_WIDGET_INFO_STYLE_NAME = "stats-info";
    public static final String SHORT_WIDGET_STYLE_NAME = "pull-left";
    /**
     * Id selectors
     */
    public static String APPLICATION_SELECTOR = "application-section";
    public static String STATIC_CONTENT_SELECTOR = "static-section";
    public static String HEADER_CONTENT_SELECTOR = "header-section";
    /**
     * UI components titles
     */
    public static String TEST_EXAMPLE_SELECTOR = "testExample";
    public static String MODELING_BUTTON_SELECTOR = "modeling";
    public static String introParagraphId = "intro_p";
    /**
     * Files names
     */
    public static String REPORT_RESULT_HREF = "report_result.xls";
    /**
     * Titles
     */
    public static String degrees = " градусов";
    public static String START_CALCULATION_TITLE = "Начать вычисление";
    public static String TETHER_MASS_TITLE = "Масса троса";
    public static String TETHER_LENGTH_TITLE = "Длина троса";
    public static String TETHER_DIAMETER_TITLE = "Диаметр троса";
    public static String MAIN_SATELLITE_MASS_TITLE = "Масса космического аппарата";
    public static String NANOSATELLITE_MASS_TITLE = "Масса наноспутника";
    public static String SEMIMAJOR_AXIS_TITLE = "Большая полуось орбиты";
    public static String SYSTEM_HEIGHT_TITLE = "Изменение высоты центра масс над поверхностью Земли";
    public static String INITIAL_SYSTEM_HEIGHT_TITLE = "Высота центра масс системы";
    public static String TRUE_ANOMALY_TITLE = "Истинная аномалия Земли";
    public static String DEFLECTION_ANGLE_TITLE = "Угол отклонения троса от вертикали";
    public static String ECCENTRICITY_TITLE = "Эксцентриситет орбиты";
    public static String ANGULAR_VELOCITY_TITLE = "Угловая скорость";
    public static String ELECTRICITY_TITLE = "Значение электрического тока";
    public static String INTEGRATION_STEP_TITLE = "Шаг интегрирования, c";
    public static String INTEGRATION_MAX_STEP_TITLE = "Максимальный шаг интегрирования, c";
    public static String INTEGRATION_MAX_ITERATIONS_TITLE = "Максимальное число итераций";
    public static String INTEGRATION_ACCURACY_TITLE = "Погрешность интегрирования";

    public static String LAST_FORCE_VALUE_TITLE = "Значение силы Ампера в последний момент времени";
    public static String LAST_MOMENT_VALUE_TITLE = "Момент сил";
    public static String LAST_TRANSVERSAL_ACCELERATION_VALUE_TITLE = "Трансверсальное ускорение";
    public static String LAST_RADIAL_ACCELERATION_VALUE_TITLE = "Радиальное ускорение";

    public static String FIXED_PARAMS_HEADER_TITLE = "Фиксированные параметры тросовой системы";
    public static String TETHER_SYSTEM_PARAMS_HEADER_TITLE = "Параметры тросовой системы" ;
    public static String TETHER_PARAMS_HEADER_TITLE = "Параметры троса";
    public static String INTEGRATION_PARAMS_HEADER_TITLE = "Параметры метода численного интегрирования";

    public static String epsilon = SafeHtmlUtils.htmlEscape("υ, градусов");
    public static String tetta = SafeHtmlUtils.htmlEscape("θ, градусов");
    public static String omega = SafeHtmlUtils.htmlEscape("ω, рад/c");
    /**
     * Math consts
     */
    public static double toKilo = 1000;
    /**
     * Prefixes
     */
    public static String _KG = " кг";
    public static String _KM = " км";
    public static String _SEC = " с";
    public static String _DEG = " °";
    public static String _A = " А";
    public static String _MOMENT = " Нм";
    public static String _ACCELERATION = " м/с²";
}
