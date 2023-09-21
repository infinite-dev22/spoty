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

package org.infinite.spoty.database.connection;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;
import org.infinite.spoty.utils.SpotyLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class SQLiteConnection {
  private static SQLiteConnection instance;

  public static SQLiteConnection getInstance() {
    if (Objects.equals(instance, null)) instance = new SQLiteConnection();
    return instance;
  }

  /**
   * Get connection to local sqlite database.
   *
   * @return JdbcConnectionSource else null
   */
  public ConnectionSource getConnection() {
    Logger.setGlobalLogLevel(Level.OFF);
    // Create the database if not exists or connect it exists.
    if (System.getProperty("os.name").contains("Linux")) {
      try {
        return new JdbcConnectionSource(
            "jdbc:sqlite:"
                + System.getProperty("user.home")
                + "/.config/ZenmartERP/datastores/databases/sqlite/database.sqlite3");
      } catch (Exception e) {
        SpotyLogger.writeToFile(e, this.getClass());
      }
    }

    if (System.getProperty("os.name").contains("mac")) {
      try {
        return new JdbcConnectionSource(
            "jdbc:sqlite:"
                + System.getProperty("user.home")
                + "/.config/ZenmartERP/datastores/databases/sqlite/database.sqlite3");
      } catch (Exception e) {
        SpotyLogger.writeToFile(e, this.getClass());
      }
    }

    if (System.getProperty("os.name").contains("Windows")) {
      try {
        return new JdbcConnectionSource(
            "jdbc:sqlite:"
                + System.getenv("APPDATA")
                + "/.config/ZenmartERP/datastores/databases/sqlite/database.sqlite3");
      } catch (Exception e) {
        SpotyLogger.writeToFile(e, this.getClass());
      }
    }
    return null;
  }
}
