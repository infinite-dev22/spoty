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
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Product;
import org.infinite.spoty.database.models.StockInDetail;
import org.infinite.spoty.database.models.StockInMaster;

public class StockInDetailViewModel {
  public static final ObservableList<StockInDetail> stockInDetailsList =
      FXCollections.observableArrayList();
  private static final ListProperty<StockInDetail> stockInDetails =
      new SimpleListProperty<>(stockInDetailsList);
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<StockInMaster> stockIn = new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty("");
  private static final StringProperty serial = new SimpleStringProperty("");
  private static final StringProperty description = new SimpleStringProperty("");
  private static final StringProperty location = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    StockInDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static Product getProduct() {
    return product.get();
  }

  public static void setProduct(Product product) {
    StockInDetailViewModel.product.set(product);
  }

  public static ObjectProperty<Product> productProperty() {
    return product;
  }

  public static StockInMaster getStockIn() {
    return stockIn.get();
  }

  public static void setStockIn(StockInMaster stockIn) {
    StockInDetailViewModel.stockIn.set(stockIn);
  }

  public static ObjectProperty<StockInMaster> stockInProperty() {
    return stockIn;
  }

  public static long getQuantity() {
    return Long.parseLong(!quantity.get().isEmpty() ? quantity.get() : "0");
  }

  public static void setQuantity(String quantity) {
    StockInDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static String getDescription() {
    return description.get();
  }

  public static void setDescription(String description) {
    StockInDetailViewModel.description.set(description);
  }

  public static StringProperty descriptionProperty() {
    return description;
  }

  public static String getSerial() {
    return serial.get();
  }

  public static void setSerial(String serial) {
    StockInDetailViewModel.serial.set(serial);
  }

  public static StringProperty serialProperty() {
    return serial;
  }

  public static String getLocation() {
    return location.get();
  }

  public static void setLocation(String location) {
    StockInDetailViewModel.location.set(location);
  }

  public static StringProperty locationProperty() {
    return location;
  }

  public static ObservableList<StockInDetail> getStockInDetails() {
    return stockInDetails.get();
  }

  public static void setStockInDetails(ObservableList<StockInDetail> stockInDetails) {
    StockInDetailViewModel.stockInDetails.set(stockInDetails);
  }

  public static ListProperty<StockInDetail> stockInDetailsProperty() {
    return stockInDetails;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setQuantity("");
    setSerial("");
    setDescription("");
    setLocation("");
  }

  public static void addStockInDetails() {
    StockInDetail stockInDetail =
        new StockInDetail(
            getProduct(), getQuantity(), getSerial(), getDescription(), getLocation());

    Platform.runLater(
        () -> {
          stockInDetailsList.add(stockInDetail);
          resetProperties();
        });
  }

  public static void createStockInDetails() throws SQLException {
    SQLiteConnection connection = SQLiteConnection.getInstance();
    ConnectionSource connectionSource = connection.getConnection();

    Dao<StockInDetail, Long> stockInDetailDao =
        DaoManager.createDao(connectionSource, StockInDetail.class);

    stockInDetailDao.create(stockInDetailsList);

    updateProductQuantity();
    Platform.runLater(stockInDetailsList::clear);
  }

  private static void updateProductQuantity() {
    stockInDetailsList.forEach(
        product -> {
          long productDetailQuantity = product.getProduct().getQuantity() + product.getQuantity();
          
          try {
            ProductViewModel.getProduct(product.getId());

            ProductViewModel.setQuantity(productDetailQuantity);

            ProductViewModel.updateProduct(product.getProduct().getId());
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static void getAllStockInDetails() throws SQLException {
    SQLiteConnection connection = SQLiteConnection.getInstance();
    ConnectionSource connectionSource = connection.getConnection();

    Dao<StockInDetail, Long> stockInDetailDao =
        DaoManager.createDao(connectionSource, StockInDetail.class);

    Platform.runLater(
        () -> {
          stockInDetailsList.clear();
          try {
            stockInDetailsList.addAll(stockInDetailDao.queryForAll());
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static void updateStockInDetail(long index) throws SQLException {
    SQLiteConnection connection = SQLiteConnection.getInstance();
    ConnectionSource connectionSource = connection.getConnection();

    Dao<StockInDetail, Long> stockInDetailDao =
        DaoManager.createDao(connectionSource, StockInDetail.class);

    StockInDetail stockInDetail = stockInDetailDao.queryForId(index);

    Platform.runLater(
        () -> {
          stockInDetail.setProduct(getProduct());
          stockInDetail.setQuantity(getQuantity());
          stockInDetail.setSerialNo(getSerial());
          stockInDetail.setDescription(getDescription());
          stockInDetail.setLocation(getLocation());

          stockInDetailsList.remove((int) getTempId());
          stockInDetailsList.add(getTempId(), stockInDetail);

          resetProperties();
        });
  }

  public static void getItem(long index, int tempIndex) throws SQLException {
    SQLiteConnection connection = SQLiteConnection.getInstance();
    ConnectionSource connectionSource = connection.getConnection();

    Dao<StockInDetail, Long> stockInDetailDao =
        DaoManager.createDao(connectionSource, StockInDetail.class);

    StockInDetail stockInDetail = stockInDetailDao.queryForId(index);

    Platform.runLater(
        () -> {
          setTempId(tempIndex);
          setId(stockInDetail.getId());
          setProduct(stockInDetail.getProduct());
          setQuantity(String.valueOf(stockInDetail.getQuantity()));
          setSerial(stockInDetail.getSerialNo());
          setDescription(stockInDetail.getDescription());
          setLocation(stockInDetail.getLocation());
        });
  }

  public static void updateItem(long index) throws SQLException {
    SQLiteConnection connection = SQLiteConnection.getInstance();
    ConnectionSource connectionSource = connection.getConnection();

    Dao<StockInDetail, Long> stockInDetailDao =
        DaoManager.createDao(connectionSource, StockInDetail.class);

    StockInDetail stockInDetail = stockInDetailDao.queryForId(index);

    stockInDetail.setProduct(getProduct());
    stockInDetail.setQuantity(getQuantity());
    stockInDetail.setSerialNo(getSerial());
    stockInDetail.setDescription(getDescription());
    stockInDetail.setLocation(getLocation());

    stockInDetailDao.update(stockInDetail);

    getAllStockInDetails();
  }

  public static void updateStockInDetails() throws SQLException {
    SQLiteConnection connection = SQLiteConnection.getInstance();
    ConnectionSource connectionSource = connection.getConnection();

    Dao<StockInDetail, Long> stockInDetailDao =
        DaoManager.createDao(connectionSource, StockInDetail.class);

    stockInDetailsList.forEach(
        stockInDetail -> {
          try {
            stockInDetailDao.update(stockInDetail);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });

    updateProductQuantity();

    getAllStockInDetails();
  }

  public static void removeStockInDetail(long index, int tempIndex) {
    Platform.runLater(() -> stockInDetailsList.remove(tempIndex));
    PENDING_DELETES.add(index);
  }

  public static void deleteStockInDetails(LinkedList<Long> indexes) {
    indexes.forEach(
        index -> {
          try {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<StockInDetail, Long> stockInDetailDao =
                DaoManager.createDao(connectionSource, StockInDetail.class);

            stockInDetailDao.deleteById(index);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static ObservableList<StockInDetail> getStockInDetailsList() {
    return stockInDetailsList;
  }
}
