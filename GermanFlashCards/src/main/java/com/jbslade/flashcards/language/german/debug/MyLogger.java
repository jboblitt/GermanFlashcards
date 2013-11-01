package com.jbslade.flashcards.language.german.debug;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Justin Boblitt
 * @description All messages I print to the console go through this class. If shouldLog is false in initLogger method, then
 * no Logger is instantiated. Messages are only printed if Logger is non-null.
 * @log 1) 08/19/2013 Initial creation.
 */
public class MyLogger
{
    private static Logger s_logger;

    public static void initLogger(String loggerName, boolean shouldLog)
    {
        if (shouldLog)
            s_logger = Logger.getLogger("logger_" + loggerName);
        else
            s_logger = null;
    }

    public static void logExceptionSevere(String sourceClass, String sourceMethod, String msg,
                                          Throwable thrown)
    {
        requestLogMsg(Level.SEVERE, sourceClass, sourceMethod, msg, thrown);
    }

    public static void logInfo(String sourceClass, String sourceMethod, String msg)
    {
        requestLogMsg(Level.INFO, sourceClass, sourceMethod, msg, null);
    }

    public static void logWarning(String sourceClass, String sourceMethod, String msg)
    {
        requestLogMsg(Level.WARNING, sourceClass, sourceMethod, msg, null);
    }

    public static void logWarning(String sourceClass, String sourceMethod, String msg, Exception e)
    {
        requestLogMsg(Level.WARNING, sourceClass, sourceMethod, msg, e);
    }

    public static void logSevere(String sourceClass, String sourceMethod, String msg)
    {
        requestLogMsg(Level.SEVERE, sourceClass, sourceMethod, msg, null);
    }

    private static void requestLogMsg(Level lvl, String sourceClass, String sourceMethod,
                                      String msg, Throwable thrown)
    {
        String message = (msg != null ? msg : "");
        sourceMethod += "()";
        if (shouldLog())
        {
            if (thrown != null)
                s_logger.logp(lvl, sourceClass, sourceMethod, message, thrown);
            else
                s_logger.logp(lvl, sourceClass, sourceMethod, message);
        }
    }

    private static boolean shouldLog()
    {
        return s_logger != null;
    }

}
