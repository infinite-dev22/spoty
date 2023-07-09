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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.LinkedList;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.models.ProductDetail;

public class AdjustmentDetailViewModel {
  public static final ObservableList<AdjustmentDetail> adjustmentDetailsList =
      FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<AdjustmentMaster> adjustment = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty("");
  private static final StringProperty adjustmentType = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    AdjustmentDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    AdjustmentDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
    return product;
  }

  public static AdjustmentMaster getAdjustment() {
    return adjustment.get();
  }

  public static void setAdjustment(AdjustmentMaster adjustment) {
    AdjustmentDetailViewModel.adjustment.set(adjustment);
  }

  public static ObjectProperty<AdjustmentMaster> adjustmentProperty() {
    return adjustment;
  }

  public static long getQuantity() {
    return Long.parseLong(quantity.get());
  }

  public static void setQuantity(String quantity) {
    AdjustmentDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static String getAdjustmentType() {
    return adjustmentType.get();
  }

  public static void setAdjustmentType(String adjustmentType) {
    AdjustmentDetailViewModel.adjustmentType.set(adjustmentType);
  }

  public static StringProperty adjustmentTypeProperty() {
    return adjustmentType;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setAdjustment(null);
    setAdjustmentType("");
    setQuantity("");
  }

  public static void addAdjustmentDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            AdjustmentDetail adjustmentDetail =
                new AdjustmentDetail(getProduct(), getQuantity(), getAdjustmentType());

            adjustmentDetailsList.add(adjustmentDetail);
            resetProperties();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void updateAdjustmentDetail(int index) {
    Task<Void> task =
            new Task<>() {
              @Override
              protected Void call() {
                AdjustmentDetail adjustmentDetail = adjustmentDetailsList.get(index);
                adjustmentDetail.setProduct(getProduct());
                adjustmentDetail.setQuantity(getQuantity());
                adjustmentDetail.setAdjustmentType(getAdjustmentType());
                resetProperties();
                return null;
              }
            };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getAdjustmentDetails() {
    Task<Void> task =
            new Task<>() {
              @Override
              protected Void call() throws SQLException {
                SQLiteConnection connection = SQLiteConnection.getInstance();
                ConnectionSource connectionSource = connection.getConnection();

                Dao<AdjustmentDetail, Long> adjustmentDetailDao =
                        DaoManager.createDao(connectionSource, AdjustmentDetail.class);

                adjustmentDetailsList.clear();
                adjustmentDetailsList.addAll(adjustmentDetailDao.queryForAll());
                return null;
              }
            };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getItem(long index, int tempIndex) {
    Task<Void> task =
            new Task<>() {
              @Override
              protected Void call() throws SQLException {
                SQLiteConnection connection = SQLiteConnection.getInstance();
                ConnectionSource connectionSource = connection.getConnection();

                Dao<AdjustmentDetail, Long> adjustmentDetailDao =
                        DaoManager.createDao(connectionSource, AdjustmentDetail.class);

                AdjustmentDetail adjustmentDetail = adjustmentDetailDao.queryForId(index);
                setTempId(tempIndex);
                setProduct(adjustmentDetail.getProduct());
                setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
                setAdjustmentType(adjustmentDetail.getAdjustmentType());
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

                Dao<AdjustmentDetail, Long> adjustmentDetailDao =
                        DaoManager.createDao(connectionSource, AdjustmentDetail.class);

                AdjustmentDetail adjustmentDetail = adjustmentDetailDao.queryForId(index);
                adjustmentDetail.setProduct(getProduct());
                adjustmentDetail.setQuantity(getQuantity());
                adjustmentDetail.setAdjustmentType(getAdjustmentType());
                adjustmentDetailDao.update(adjustmentDetail);
                getAdjustmentDetails();
                return null;
              }
            };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void removeAdjustmentDetail(long index, int tempIndex) {
    Task<Void> task =
            new Task<>() {
              @Override
              protected Void call() {
                adjustmentDetailsList.remove(tempIndex);
                PENDING_DELETES.add(index);
                return null;
              }
            };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void deleteAdjustmentDetails(LinkedList<Long> indexes) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            indexes.forEach(
                index -> {
                  try {
                    SQLiteConnection connection = SQLiteConnection.getInstance();
                    ConnectionSource connectionSource = connection.getConnection();

                    Dao<AdjustmentDetail, Long> adjustmentDetailDao =
                        DaoManager.createDao(connectionSource, AdjustmentDetail.class);

                    adjustmentDetailDao.deleteById(index);
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

  public static ObservableList<AdjustmentDetail> getAdjustmentDetailsList() {
    return adjustmentDetailsList;
  }
}
