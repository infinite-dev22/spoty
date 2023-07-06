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
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.TransferDetail;
import org.infinite.spoty.database.models.TransferMaster;

public class TransferDetailViewModel {
  public static final ObservableList<TransferDetail> transferDetailsList =
      FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<TransferMaster> transfer = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty("");
  private static final StringProperty serial = new SimpleStringProperty("");
  private static final StringProperty description = new SimpleStringProperty("");
  private static final StringProperty price = new SimpleStringProperty("");
  private static final StringProperty total = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    TransferDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    TransferDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
    return product;
  }

  public static TransferMaster getTransfer() {
    return transfer.get();
  }

  public static void setTransfer(TransferMaster transfer) {
    TransferDetailViewModel.transfer.set(transfer);
  }

  public static ObjectProperty<TransferMaster> transferProperty() {
    return transfer;
  }

  public static long getQuantity() {
    return Long.parseLong(!quantity.get().isEmpty() ? quantity.get() : "0");
  }

  public static void setQuantity(String quantity) {
    TransferDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static String getDescription() {
    return description.get();
  }

  public static void setDescription(String description) {
    TransferDetailViewModel.description.set(description);
  }

  public static StringProperty descriptionProperty() {
    return description;
  }

  public static String getSerial() {
    return serial.get();
  }

  public static void setSerial(String serial) {
    TransferDetailViewModel.serial.set(serial);
  }

  public static StringProperty serialProperty() {
    return serial;
  }

  public static double getPrice() {
    return Double.parseDouble(!price.get().isEmpty() ? price.get() : "0");
  }

  public static void setPrice(String price) {
    TransferDetailViewModel.price.set(price);
  }

  public static StringProperty priceProperty() {
    return price;
  }

  public static double getTotal() {
    return Double.parseDouble(!total.get().isEmpty() ? total.get() : "0");
  }

  public static void setTotal(String total) {
    TransferDetailViewModel.total.set(total);
  }

  public static StringProperty totalProperty() {
    return total;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setQuantity("");
    setSerial("");
    setDescription("");
    setPrice("");
    setTotal("");
  }

  public static void addTransferDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            TransferDetail transferDetail =
                new TransferDetail(
                    getProduct(),
                    getQuantity(),
                    getSerial(),
                    getDescription(),
                    getPrice(),
                    getTotal());

            transferDetailsList.add(transferDetail);
            resetProperties();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getTransferDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<TransferDetail, Long> transferDetailDao =
                DaoManager.createDao(connectionSource, TransferDetail.class);

            transferDetailsList.clear();
            transferDetailsList.addAll(transferDetailDao.queryForAll());
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void updateTransferDetail(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<TransferDetail, Long> transferDetailDao =
                DaoManager.createDao(connectionSource, TransferDetail.class);

            TransferDetail transferDetail = transferDetailDao.queryForId(index);
            transferDetail.setProduct(getProduct());
            transferDetail.setQuantity(getQuantity());
            transferDetail.setSerialNo(getSerial());
            transferDetail.setDescription(getDescription());
            transferDetail.setPrice(getPrice());
            transferDetail.setTotal(getTotal());

            transferDetailsList.remove((int) getTempId());
            transferDetailsList.add(getTempId(), transferDetail);

            resetProperties();
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

            Dao<TransferDetail, Long> transferDetailDao =
                DaoManager.createDao(connectionSource, TransferDetail.class);

            TransferDetail transferDetail = transferDetailDao.queryForId(index);

            setTempId(tempIndex);
            setId(transferDetail.getId());
            setProduct(transferDetail.getProduct());
            setQuantity(String.valueOf(transferDetail.getQuantity()));
            setSerial(transferDetail.getSerialNo());
            setDescription(transferDetail.getDescription());
            setPrice(String.valueOf(transferDetail.getPrice()));
            setTotal(String.valueOf(transferDetail.getTotal()));
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

            Dao<TransferDetail, Long> transferDetailDao =
                DaoManager.createDao(connectionSource, TransferDetail.class);

            TransferDetail transferDetail = transferDetailDao.queryForId(index);
            transferDetail.setProduct(getProduct());
            transferDetail.setQuantity(getQuantity());
            transferDetail.setSerialNo(getSerial());
            transferDetail.setDescription(getDescription());
            transferDetail.setPrice(getPrice());
            transferDetail.setTotal(getTotal());

            transferDetailDao.update(transferDetail);

            getTransferDetails();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void removeTransferDetail(long index, int tempIndex) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            transferDetailsList.remove(tempIndex);
            PENDING_DELETES.add(index);
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void deleteTransferDetails(LinkedList<Long> indexes) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            indexes.forEach(
                index -> {
                  try {
                    SQLiteConnection connection = SQLiteConnection.getInstance();
                    ConnectionSource connectionSource = connection.getConnection();

                    Dao<TransferDetail, Long> transferDetailDao =
                        DaoManager.createDao(connectionSource, TransferDetail.class);

                    transferDetailDao.deleteById(index);
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
}
