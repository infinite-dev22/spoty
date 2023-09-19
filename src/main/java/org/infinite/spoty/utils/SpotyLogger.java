/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

/**
 * @author infinite
 * @version 0.0.2.56
 *
 * <h1>SpotyLogger</h1>
 * Logger to write <b>spoty</b> application logs to file.
 * Contains a single function <code>writeToFile</code> taking two parameters <b>exception</b> and <b>currentClass</b>
 */
public class SpotyLogger {

    /**
     * Write logs to file.
     *
     * @param exception    exception to be written to log file.
     * @param currentClass class in which the exception is handled.
     */
    public static <T> void writeToFile(Exception exception, @NotNull Class<T> currentClass) {
        Logger logger = Logger.getLogger(currentClass.getName());
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(record.getMillis());
                return "<h4>" + record.getLevel() + "</h4> <h6>" + logTime.format(cal.getTime()) + "</h6>"
                        + "Logger Name: " + record.getLoggerName() + "<br/>"
                        + "Thread: " + record.getLongThreadID() + "<br/>"
                        + "Stacktrace: " + record.getMessage() + "<br/><hr/>";
            }
        };
        FileHandler fileHandler;

        try {
            fileHandler =
                    new FileHandler(
                            System.getProperty("user.home")
                                    + "/.config/ZenmartERP/logs/spoty_log.html", true);
            fileHandler.setFormatter(formatter);

            logger.addHandler(fileHandler);

            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, Arrays.toString(exception.getStackTrace()));
            }
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, Arrays.toString(exception.getStackTrace()));
            }
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, Arrays.toString(exception.getStackTrace()));
            }
            if (logger.isLoggable(Level.CONFIG)) {
                logger.log(Level.CONFIG, Arrays.toString(exception.getStackTrace()));
            }
            if (logger.isLoggable(Level.ALL)) {
                logger.log(Level.ALL, Arrays.toString(exception.getStackTrace()));
            }
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, SpotyLogger.class);
            throw new RuntimeException(e);
        }
    }
}
