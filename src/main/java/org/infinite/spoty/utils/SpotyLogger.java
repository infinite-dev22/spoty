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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author infinite
 * @version 0.0.2.56
 *
 * <h1>SpotyLogger</h1>
 * Logger to write <b>spoty</b> application logs to file.
 * Contains a single function <code>writeToFile</code> taking two parameters <b>exception</b> and <b>currentClass</b>
 */
public class SpotyLogger {
    static SimpleFormatter formatter = new SimpleFormatter();

    static FileHandler fileHandler;

    /**
     * Write logs to file.
     *
     * @param throwable    exception to be written to log file.
     * @param currentClass class in which the exception is handled.
     */
//    public static <T> void writeToFile(Throwable throwable, @NotNull Class<T> currentClass) {
//        throwable.printStackTrace();
//    }
    public static <T> void writeToFile(Throwable throwable, @NotNull Class<T> currentClass) {
        Logger logger = Logger.getLogger(currentClass.getName());

        Path path;

        try {
            if (System.getProperty("os.name").contains("Windows")) {
                path = Paths.get(System.getProperty("user.home")
                        + "\\AppData\\Local\\ZenmartERP\\sys-log-data\\logs\\stack\\spoty_log.log");
            } else {
                path = Paths.get(System.getProperty("user.home")
                        + "/.config/ZenmartERP/sys-log-data/logs/stack/spoty_log.log");
            }
            if (Objects.isNull(fileHandler)) fileHandler =
                    new FileHandler(
                            path.toString(), 1000, 1, true);

            fileHandler.setFormatter(formatter);

            logger.addHandler(fileHandler);

            logger.log(
                    Level.ALL,
                    throwable.getMessage()
                            + "\n"
                            + Arrays.toString(throwable.getStackTrace())
                            + "\n\n");
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, SpotyLogger.class);
            throw new RuntimeException(e);
        }
    }
}
