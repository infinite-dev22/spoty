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

package inc.nomard.spoty.utils;

import lombok.extern.java.Log;

import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Log
public class SpotyLogger {
    static SimpleFormatter formatter = new SimpleFormatter();

    static FileHandler fileHandler;

    /**
     * Write logs to file.
     *
     * @param throwable    exception to be written to log file.
     * @param currentClass class in which the exception is handled.
     */
//    public static <T> void writeToFile(Throwable throwable, Class<T> currentClass) {
//        throwable.printStackTrace();
//    }


    // Doesn.t actually write errors to file
    public static <T> void writeToFile(Throwable throwable, Class<T> currentClass) {
        Logger logger = Logger.getLogger(currentClass.getName());

        Path path;
        throw new RuntimeException(throwable);

//        try {
//            if (System.getProperty("os.name").contains("Windows")) {
//                path = Paths.get(System.getProperty("user.home")
//                        + "\\AppData\\Local\\OpenSaleERP\\sys-log-data\\logs\\stack\\spoty_log.log");
//            } else {
//                path = Paths.get(System.getProperty("user.home")
//                        + "/.config/OpenSaleERP/sys-log-data/logs/stack/spoty_log.log");
//            }
//            if (Objects.isNull(fileHandler)) fileHandler =
//                    new FileHandler(
//                            path.toString(), 1000, 1, true);
//
//            fileHandler.setFormatter(formatter);
//
//            logger.addHandler(fileHandler);
//
//            logger.log(
//                    Level.ALL,
//                    throwable.getMessage()
//                            + "\n"
//                            + Arrays.toString(throwable.getStackTrace())
//                            + "\n\n");
//        } catch (IOException e) {
//            SpotyLogger.writeToFile(e, SpotyLogger.class);
//            throw new RuntimeException(e);
//        }
    }
}
