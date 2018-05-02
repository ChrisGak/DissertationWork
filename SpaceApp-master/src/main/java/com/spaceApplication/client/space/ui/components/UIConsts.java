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
    public static final String introH2Id = "intro_h2";
    public static final String ACTIVE_ELEMENT = "active-page";
    public static final String PAGE_CONTENT_SELECTOR = "page-content";
    public static final String PAGE_INNER_SELECTOR = "page-inner";
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
    public static String fileName = "report_result.xls";
    /**
     * Titles
     */
    public static String degrees = " градусов";
    public static String SEMIMAJOR_AXIS_TITLE = "Большая полуось орбиты";
    public static String SYSTEM_HEIGHT_TITLE = "Изменение высоты центра масс над поверхностью Земли";
    public static String TRUE_ANOMALY_TITLE = "Истинная аномалия Земли";
    public static String DEFLECTION_ANGLE_TITLE = "Угол отклонения троса от вертикали";
    public static String ECCENTRICITY_TITLE = "Эксцентриситет";
    public static String ANGULAR_VELOCITY_TITLE = "Угловая скорость";
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
}
