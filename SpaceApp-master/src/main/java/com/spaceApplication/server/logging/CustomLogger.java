package com.spaceApplication.server.logging;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by Chris
 */
public class CustomLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;
    static private String testLogFile = "TestingLog.txt";
    static private String testLogHtml = "TestingLog.html";
    static private String LogFile = "Logging.txt";
    static private String LogFileHTML = "Logging.html";

    static public void setupTesting() throws IOException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
//        Logger rootLogger = Logger.getLogger("");
//        Handler[] handlers = rootLogger.getHandlers();
//        if (handlers[0] instanceof ConsoleHandler) {
//            rootLogger.removeHandler(handlers[0]);
//        }

        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler(testLogFile);
        fileHTML = new FileHandler(testLogHtml);

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        // create an HTML formatter
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);
    }

    static public void setupLogging() throws IOException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler(LogFile);
        fileHTML = new FileHandler(LogFileHTML);

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        // create an HTML formatter
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);
    }
}

