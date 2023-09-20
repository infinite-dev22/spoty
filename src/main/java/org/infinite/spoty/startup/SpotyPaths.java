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

package org.infinite.spoty.startup;

import org.infinite.spoty.utils.SpotyLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SpotyPaths {
    public static void createPaths() {
        // Create DB Location path on system.
        try {
            Files.createDirectories(
                    Paths.get(
                            System.getProperty("user.home") + "/.config/ZenmartERP/datastores/databases/sqlite"));
            Files.createDirectories(
                    Paths.get(
                            System.getProperty("user.home") + "/.config/ZenmartERP/logs"));
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, SpotyPaths.class);
        }
    }
}
