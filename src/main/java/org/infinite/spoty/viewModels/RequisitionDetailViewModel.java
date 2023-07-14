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

import static org.infinite.spoty.values.SharedResources.*;
import static org.infinite.spoty.values.SharedResources.getTempId;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.models.RequisitionMaster;

public class RequisitionDetailViewModel {
  public static final ObservableList<RequisitionDetail> requisitionDetailList =
      FXCollections.observableArrayList();
  private static final ListProperty<RequisitionDetail> requisitionDetails =
      new SimpleListProperty<>(requisitionDetailList);
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<RequisitionMaster> requisition = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty();
  private static final StringProperty description = new SimpleStringProperty();

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    RequisitionDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    RequisitionDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
    return product;
  }

  public static RequisitionMaster getRequisition() {
    return requisition.get();
  }

  public static void setRequisition(RequisitionMaster requisition) {
    RequisitionDetailViewModel.requisition.set(requisition);
  }

  public static ObjectProperty<RequisitionMaster> requisitionProperty() {
    return requisition;
  }

  public static String getQuantity() {
    return quantity.get();
  }

  public static void setQuantity(String quantity) {
    RequisitionDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static String getDescription() {
    return description.get();
  }

  public static void setDescription(String description) {
    RequisitionDetailViewModel.description.set(description);
  }

  public static StringProperty descriptionProperty() {
    return description;
  }

  public static ObservableList<RequisitionDetail> getRequisitionDetails() {
    return requisitionDetails.get();
  }

  public static void setRequisitionDetails(ObservableList<RequisitionDetail> requisitionDetails) {
    RequisitionDetailViewModel.requisitionDetails.set(requisitionDetails);
  }

  public static ListProperty<RequisitionDetail> requisitionDetailsProperty() {
    return requisitionDetails;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setRequisition(null);
    setDescription("");
    setQuantity("");
  }

  public static void addRequisitionDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            RequisitionDetail requisitionDetail =
                new RequisitionDetail(
                    getProduct(),
                    getRequisition(),
                    Long.parseLong(getQuantity()),
                    getDescription());

            Platform.runLater(() -> requisitionDetailList.add(requisitionDetail));

            return null;
          }
        };

    task.setOnSucceeded(event -> resetProperties());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void saveRequisitionDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<RequisitionDetail, Long> requisitionDetailDao =
                DaoManager.createDao(connectionSource, RequisitionDetail.class);

            requisitionDetailDao.create(requisitionDetailList);

            return null;
          }
        };

    task.setOnSucceeded(event -> requisitionDetailList.clear());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void getAllRequisitionDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<RequisitionDetail, Long> requisitionDetailDao =
                DaoManager.createDao(connectionSource, RequisitionDetail.class);

            requisitionDetailList.clear();
            requisitionDetailList.addAll(requisitionDetailDao.queryForAll());
            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void updateRequisitionDetail(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<RequisitionDetail, Long> requisitionDetailDao =
                DaoManager.createDao(connectionSource, RequisitionDetail.class);

            RequisitionDetail requisitionDetail = requisitionDetailDao.queryForId(index);
            requisitionDetail.setProduct(getProduct());
            requisitionDetail.setQuantity(Long.parseLong(getQuantity()));
            requisitionDetail.setDescription(getDescription());

            Platform.runLater(
                () -> {
                  requisitionDetailList.remove((int) getTempId());
                  requisitionDetailList.add(getTempId(), requisitionDetail);
                });

            return null;
          }
        };

    task.setOnSucceeded(event -> resetProperties());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void getItem(long index, int tempIndex) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<RequisitionDetail, Long> requisitionDetailDao =
                DaoManager.createDao(connectionSource, RequisitionDetail.class);

            RequisitionDetail requisitionDetail = requisitionDetailDao.queryForId(index);

            setTempId(tempIndex);
            setId(requisitionDetail.getId());
            setProduct(requisitionDetail.getProduct());
            setQuantity(String.valueOf(requisitionDetail.getQuantity()));
            setDescription(requisitionDetail.getDescription());

            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void updateItem(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<RequisitionDetail, Long> requisitionDetailDao =
                DaoManager.createDao(connectionSource, RequisitionDetail.class);

            RequisitionDetail requisitionDetail = requisitionDetailDao.queryForId(index);
            requisitionDetail.setProduct(getProduct());
            requisitionDetail.setQuantity(Long.parseLong(getQuantity()));
            requisitionDetail.setDescription(getDescription());

            requisitionDetailDao.update(requisitionDetail);

            return null;
          }
        };

    task.setOnSucceeded(event -> getAllRequisitionDetails());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void updateRequisitionDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<RequisitionDetail, Long> requisitionDetailDao =
                DaoManager.createDao(connectionSource, RequisitionDetail.class);

            requisitionDetailList.forEach(
                requisitionDetail -> {
                  try {
                    requisitionDetailDao.update(requisitionDetail);
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });

            return null;
          }
        };

    task.setOnSucceeded(event -> getAllRequisitionDetails());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void removeRequisitionDetail(long index, int tempIndex) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            Platform.runLater(() -> requisitionDetailList.remove(tempIndex));
            PENDING_DELETES.add(index);
            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void deleteRequisitionDetails(LinkedList<Long> indexes) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            indexes.forEach(
                index -> {
                  try {
                    SQLiteConnection connection = SQLiteConnection.getInstance();
                    ConnectionSource connectionSource = connection.getConnection();

                    Dao<RequisitionDetail, Long> requisitionDetailDao =
                        DaoManager.createDao(connectionSource, RequisitionDetail.class);

                    requisitionDetailDao.deleteById(index);
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });
            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static ObservableList<RequisitionDetail> getRequisitionDetailList() {
    return requisitionDetailList;
  }
}
