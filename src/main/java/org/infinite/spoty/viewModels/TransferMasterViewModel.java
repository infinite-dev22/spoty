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
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.TransferMaster;

public class TransferMasterViewModel {
  public static final ObservableList<TransferMaster> transferMasterList =
      FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty date = new SimpleStringProperty("");
  private static final ObjectProperty<Branch> fromBranch = new SimpleObjectProperty<>(null);
  private static final ObjectProperty<Branch> toBranch = new SimpleObjectProperty<>(null);
  private static final StringProperty totalCost = new SimpleStringProperty("");
  private static final StringProperty status = new SimpleStringProperty("");
  private static final StringProperty note = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    TransferMasterViewModel.id.set(id);
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
    TransferMasterViewModel.date.set(date);
  }

  public static StringProperty dateProperty() {
    return date;
  }

  public static Branch getFromBranch() {
    return fromBranch.get();
  }

  public static void setFromBranch(Branch fromBranch) {
    TransferMasterViewModel.fromBranch.set(fromBranch);
  }

  public static ObjectProperty<Branch> fromBranchProperty() {
    return fromBranch;
  }

  public static Branch getToBranch() {
    return toBranch.get();
  }

  public static void setToBranch(Branch toBranch) {
    TransferMasterViewModel.toBranch.set(toBranch);
  }

  public static ObjectProperty<Branch> toBranchProperty() {
    return toBranch;
  }

  public static String getNote() {
    return note.get();
  }

  public static void setNote(String note) {
    TransferMasterViewModel.note.set(note);
  }

  public static StringProperty noteProperty() {
    return note;
  }

  public static String getStatus() {
    return status.get();
  }

  public static void setStatus(String status) {
    TransferMasterViewModel.status.set(status);
  }

  public static StringProperty statusProperty() {
    return status;
  }

  public static double getTotalCost() {
    return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
  }

  public static void setTotalCost(String totalCost) {
    TransferMasterViewModel.totalCost.set(totalCost);
  }

  public static StringProperty totalCostProperty() {
    return totalCost;
  }

  public static void resetProperties() {
    setId(0);
    setDate("");
    setFromBranch(null);
    setToBranch(null);
    setNote("");
    setStatus("");
    setTotalCost("");
    PENDING_DELETES.clear();
    TransferDetailViewModel.transferDetailsList.clear();
  }

  public static void saveTransferMaster() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<TransferMaster, Long> transferMasterDao =
                DaoManager.createDao(connectionSource, TransferMaster.class);

            TransferMaster transferMaster =
                new TransferMaster(
                    getDate(),
                    getFromBranch(),
                    getToBranch(),
                    getTotalCost(),
                    getStatus(),
                    getNote());

            if (!TransferDetailViewModel.transferDetailsList.isEmpty()) {
              TransferDetailViewModel.transferDetailsList.forEach(
                  transferDetail -> transferDetail.setTransfer(transferMaster));

              transferMaster.setTransferDetails(TransferDetailViewModel.getTransferDetailsList());
            }

            transferMasterDao.create(transferMaster);

            resetProperties();
            getTransferMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getTransferMasters() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<TransferMaster, Long> transferMasterDao =
                DaoManager.createDao(connectionSource, TransferMaster.class);

            Platform.runLater(
                () -> {
                  transferMasterList.clear();

                  try {
                    transferMasterList.addAll(transferMasterDao.queryForAll());
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

  public static void getItem(long transferMasterID) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<TransferMaster, Long> transferMasterDao =
                DaoManager.createDao(connectionSource, TransferMaster.class);

            TransferMaster transferMaster = transferMasterDao.queryForId(transferMasterID);

            setId(transferMaster.getId());
            setDate(transferMaster.getLocaleDate());
            setFromBranch(transferMaster.getFromBranch());
            setToBranch(transferMaster.getToBranch());
            setTotalCost(String.valueOf(transferMaster.getTotal()));
            setNote(transferMaster.getNotes());
            setStatus(transferMaster.getStatus());

            TransferDetailViewModel.transferDetailsList.clear();
            TransferDetailViewModel.transferDetailsList.addAll(transferMaster.getTransferDetails());

            getTransferMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void updateItem(long transferMasterID) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<TransferMaster, Long> transferMasterDao =
                DaoManager.createDao(connectionSource, TransferMaster.class);

            TransferMaster transferMaster = transferMasterDao.queryForId(transferMasterID);
            transferMaster.setDate(getDate());
            transferMaster.setFromBranch(getFromBranch());
            transferMaster.setToBranch(getToBranch());
            transferMaster.setTotal(getTotalCost());
            transferMaster.setStatus(getStatus());
            transferMaster.setNotes(getNote());

            TransferDetailViewModel.deleteTransferDetails(PENDING_DELETES);
            transferMaster.setTransferDetails(TransferDetailViewModel.getTransferDetailsList());

            transferMasterDao.update(transferMaster);

            resetProperties();
            getTransferMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void deleteItem(long transferMasterID) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<TransferMaster, Long> transferMasterDao =
                DaoManager.createDao(connectionSource, TransferMaster.class);

            transferMasterDao.deleteById(transferMasterID);
            getTransferMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static ObservableList<TransferMaster> getTransferMasterList() {
    return transferMasterList;
  }
}
