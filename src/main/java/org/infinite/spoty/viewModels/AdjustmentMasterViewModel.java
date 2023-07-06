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

package org.infinite.spoty.viewModels;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.models.Branch;

public class AdjustmentMasterViewModel {
  public static final ObservableList<AdjustmentMaster> adjustmentMasterList =
      FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty date = new SimpleStringProperty("");
  private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
  private static final StringProperty note = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    AdjustmentMasterViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static Date getDate() {
    try {
      return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setDate(String date) {
    AdjustmentMasterViewModel.date.set(date);
  }

  public static StringProperty dateProperty() {
    return date;
  }

  public static Branch getBranch() {
    return branch.get();
  }

  public static void setBranch(Branch branch) {
    AdjustmentMasterViewModel.branch.set(branch);
  }

  public static ObjectProperty<Branch> branchProperty() {
    return branch;
  }

  public static String getNote() {
    return note.get();
  }

  public static void setNote(String note) {
    AdjustmentMasterViewModel.note.set(note);
  }

  public static StringProperty noteProperty() {
    return note;
  }

  public static void resetProperties() {
    setId(0);
    setDate("");
    setBranch(null);
    setNote("");
    PENDING_DELETES.clear();
    AdjustmentDetailViewModel.adjustmentDetailsList.clear();
  }

  public static void saveAdjustmentMaster() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<AdjustmentMaster, Long> adjustmentMasterDao =
                DaoManager.createDao(connectionSource, AdjustmentMaster.class);

            AdjustmentMaster adjustmentMaster =
                new AdjustmentMaster(getBranch(), getNote(), getDate());
            if (!AdjustmentDetailViewModel.adjustmentDetailsList.isEmpty()) {
              AdjustmentDetailViewModel.adjustmentDetailsList.forEach(
                  adjustmentDetail -> adjustmentDetail.setAdjustment(adjustmentMaster));
              adjustmentMaster.setAdjustmentDetails(
                  AdjustmentDetailViewModel.adjustmentDetailsList);
            }
            adjustmentMasterDao.create(adjustmentMaster);
            resetProperties();
            getAdjustmentMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getAdjustmentMasters() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<AdjustmentMaster, Long> adjustmentMasterDao =
                DaoManager.createDao(connectionSource, AdjustmentMaster.class);

            Platform.runLater(
                () -> {
                  adjustmentMasterList.clear();

                  try {
                    adjustmentMasterList.addAll(adjustmentMasterDao.queryForAll());
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });

            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getItem(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<AdjustmentMaster, Long> adjustmentMasterDao =
                DaoManager.createDao(connectionSource, AdjustmentMaster.class);

            AdjustmentMaster adjustmentMaster = adjustmentMasterDao.queryForId(index);

            setId(adjustmentMaster.getId());
            setBranch(adjustmentMaster.getBranch());
            setNote(adjustmentMaster.getNotes());
            setDate(adjustmentMaster.getLocaleDate());

            AdjustmentDetailViewModel.adjustmentDetailsList.clear();
            AdjustmentDetailViewModel.adjustmentDetailsList.addAll(
                adjustmentMaster.getAdjustmentDetails());

            getAdjustmentMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void updateItem(long index) {

    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<AdjustmentMaster, Long> adjustmentMasterDao =
                DaoManager.createDao(connectionSource, AdjustmentMaster.class);

            AdjustmentMaster adjustmentMaster = adjustmentMasterDao.queryForId(index);
            adjustmentMaster.setBranch(getBranch());
            adjustmentMaster.setNotes(getNote());
            adjustmentMaster.setDate(getDate());
            AdjustmentDetailViewModel.deleteAdjustmentDetails(PENDING_DELETES);
            adjustmentMaster.setAdjustmentDetails(AdjustmentDetailViewModel.adjustmentDetailsList);
            adjustmentMasterDao.update(adjustmentMaster);
            resetProperties();
            getAdjustmentMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void deleteItem(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<AdjustmentMaster, Long> adjustmentMasterDao =
                DaoManager.createDao(connectionSource, AdjustmentMaster.class);

            adjustmentMasterDao.deleteById(index);
            getAdjustmentMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }
}
