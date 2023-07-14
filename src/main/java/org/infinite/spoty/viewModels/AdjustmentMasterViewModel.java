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
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.models.Branch;

public class AdjustmentMasterViewModel {
  public static final ObservableList<AdjustmentMaster> adjustmentMasterList =
      FXCollections.observableArrayList();
  private static final ListProperty<AdjustmentMaster> adjustmentMasters =
      new SimpleListProperty<>(adjustmentMasterList);
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

  public static ObservableList<AdjustmentMaster> getAdjustmentMasters() {
    return adjustmentMasters.get();
  }

  public static void setAdjustmentMasters(ObservableList<AdjustmentMaster> adjustmentMasters) {
    AdjustmentMasterViewModel.adjustmentMasters.set(adjustmentMasters);
  }

  public static ListProperty<AdjustmentMaster> adjustmentMastersProperty() {
    return adjustmentMasters;
  }

  public static void resetProperties() {
    Platform.runLater(
        () -> {
          setId(0);
          setDate("");
          setBranch(null);
          setNote("");
          PENDING_DELETES.clear();
        });
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

            if (!AdjustmentDetailViewModel.getAdjustmentDetailsList().isEmpty()) {
              AdjustmentDetailViewModel.getAdjustmentDetailsList()
                  .forEach(adjustmentDetail -> adjustmentDetail.setAdjustment(adjustmentMaster));

              adjustmentMaster.setAdjustmentDetails(
                  AdjustmentDetailViewModel.getAdjustmentDetailsList());
            }

            adjustmentMasterDao.create(adjustmentMaster);
            AdjustmentDetailViewModel.saveAdjustmentDetails();

            return null;
          }
        };

    task.setOnSucceeded(
        event -> {
          resetProperties();
          getAllAdjustmentMasters();
        });

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void getAllAdjustmentMasters() {
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

    GlobalActions.spotyThreadPool().execute(task);
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

            Platform.runLater(
                () -> {
                  AdjustmentDetailViewModel.adjustmentDetailsList.clear();
                  AdjustmentDetailViewModel.adjustmentDetailsList.addAll(
                      adjustmentMaster.getAdjustmentDetails());
                });

            getAllAdjustmentMasters();
            return null;
          }
        };

    task.setOnSucceeded(event -> getAllAdjustmentMasters());

    GlobalActions.spotyThreadPool().execute(task);
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

            adjustmentMaster.setAdjustmentDetails(
                AdjustmentDetailViewModel.getAdjustmentDetailsList());

            adjustmentMasterDao.update(adjustmentMaster);
            AdjustmentDetailViewModel.updateAdjustmentDetails();
            return null;
          }
        };

    task.setOnSucceeded(
        event -> {
          resetProperties();
          getAllAdjustmentMasters();
        });

    GlobalActions.spotyThreadPool().execute(task);
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
            return null;
          }
        };

    task.setOnSucceeded(event -> getAllAdjustmentMasters());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static ObservableList<AdjustmentMaster> getAdjustmentMasterList() {
    return adjustmentMasterList;
  }
}
