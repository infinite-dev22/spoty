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
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.database.models.UnitOfMeasure;

public class SaleDetailViewModel {
  public static final ObservableList<SaleDetail> saleDetailList =
      FXCollections.observableArrayList();
  private static final ListProperty<SaleDetail> saleDetails =
      new SimpleListProperty<>(saleDetailList);
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty ref = new SimpleStringProperty();
  private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
  private static final StringProperty serial = new SimpleStringProperty();
  private static final StringProperty price = new SimpleStringProperty();
  private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>();
  private static final StringProperty netTax = new SimpleStringProperty();
  private static final StringProperty taxType = new SimpleStringProperty();
  private static final StringProperty discount = new SimpleStringProperty();
  private static final StringProperty discountType = new SimpleStringProperty();
  private static final StringProperty total = new SimpleStringProperty();
  private static final StringProperty quantity = new SimpleStringProperty();

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    SaleDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getRef() {
    return ref.get();
  }

  public static void setRef(String ref) {
    SaleDetailViewModel.ref.set(ref);
  }

  public static StringProperty refProperty() {
    return ref;
  }

  public static ProductDetail getProduct() {
    return product.get();
  }

  public static void setProduct(ProductDetail product) {
    SaleDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductDetail> productProperty() {
    return product;
  }

  public static String getSerial() {
    return serial.get();
  }

  public static void setSerial(String serial) {
    SaleDetailViewModel.serial.set(serial);
  }

  public static StringProperty serialProperty() {
    return serial;
  }

  public static String getPrice() {
    return price.get();
  }

  public static void setPrice(String price) {
    SaleDetailViewModel.price.set(price);
  }

  public static StringProperty priceProperty() {
    return price;
  }

  public static UnitOfMeasure getSaleUnit() {
    return saleUnit.get();
  }

  public static void setSaleUnit(UnitOfMeasure saleUnit) {
    SaleDetailViewModel.saleUnit.set(saleUnit);
  }

  public static ObjectProperty<UnitOfMeasure> saleUnitProperty() {
    return saleUnit;
  }

  public static String getNetTax() {
    return netTax.get();
  }

  public static void setNetTax(String netTax) {
    SaleDetailViewModel.netTax.set(netTax);
  }

  public static StringProperty netTaxProperty() {
    return netTax;
  }

  public static String getTaxType() {
    return taxType.get();
  }

  public static void setTaxType(String taxType) {
    SaleDetailViewModel.taxType.set(taxType);
  }

  public static StringProperty taxTypeProperty() {
    return taxType;
  }

  public static String getDiscount() {
    return discount.get();
  }

  public static void setDiscount(String discount) {
    SaleDetailViewModel.discount.set(discount);
  }

  public static StringProperty discountProperty() {
    return discount;
  }

  public static String getDiscountType() {
    return discountType.get();
  }

  public static void setDiscountType(String discountType) {
    SaleDetailViewModel.discountType.set(discountType);
  }

  public static StringProperty discountTypeProperty() {
    return discountType;
  }

  public static String getTotal() {
    return total.get();
  }

  public static void setTotal(String total) {
    SaleDetailViewModel.total.set(total);
  }

  public static StringProperty totalProperty() {
    return total;
  }

  public static String getQuantity() {
    return quantity.get();
  }

  public static void setQuantity(String quantity) {
    SaleDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static ObservableList<SaleDetail> getSaleDetails() {
    return saleDetails.get();
  }

  public static void setSaleDetails(ObservableList<SaleDetail> saleDetails) {
    SaleDetailViewModel.saleDetails.set(saleDetails);
  }

  public static ListProperty<SaleDetail> saleDetailsProperty() {
    return saleDetails;
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setSerial("");
    setSaleUnit(null);
    setNetTax("");
    setTaxType("");
    setDiscount("");
    setQuantity("");
    setDiscountType("");
  }

  public static void addSaleDetail() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            SaleDetail saleDetail =
                new SaleDetail(
                    getProduct(),
                    Long.parseLong(getQuantity()),
                    getSerial(),
                    Double.parseDouble(getNetTax()),
                    getTaxType(),
                    Double.parseDouble(getDiscount()),
                    getDiscountType());

            saleDetailList.add(saleDetail);
            resetProperties();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void updateSaleDetail(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<SaleDetail, Long> saleDetailDao =
                DaoManager.createDao(connectionSource, SaleDetail.class);

            SaleDetail saleDetail = saleDetailDao.queryForId(index);
            saleDetail.setProduct(getProduct());
            saleDetail.setQuantity(Long.parseLong(getQuantity()));
            saleDetail.setSerialNumber(getSerial());
            saleDetail.setNetTax(Double.parseDouble(getNetTax()));
            saleDetail.setTaxType(getTaxType());
            saleDetail.setDiscount(Double.parseDouble(getDiscount()));
            saleDetail.setDiscountType(getDiscountType());

            saleDetailList.remove((int) getTempId());
            saleDetailList.add(getTempId(), saleDetail);

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

            Dao<SaleDetail, Long> saleDetailDao =
                DaoManager.createDao(connectionSource, SaleDetail.class);

            SaleDetail saleDetail = saleDetailDao.queryForId(index);

            setTempId(tempIndex);
            setId(saleDetail.getId());
            setProduct(saleDetail.getProduct());
            setSerial(saleDetail.getSerialNumber());
            setNetTax(String.valueOf(saleDetail.getNetTax()));
            setTaxType(saleDetail.getTaxType());
            setDiscount(String.valueOf(saleDetail.getDiscount()));
            setDiscountType(saleDetail.getDiscountType());
            setQuantity(String.valueOf(saleDetail.getQuantity()));
            setProduct(saleDetail.getProduct());
            setTotal(String.valueOf(saleDetail.getTotal()));
            setQuantity(String.valueOf(saleDetail.getQuantity()));
            setPrice(String.valueOf(saleDetail.getPrice()));
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

            Dao<SaleDetail, Long> saleDetailDao =
                DaoManager.createDao(connectionSource, SaleDetail.class);

            SaleDetail saleDetail = saleDetailDao.queryForId(index);
            saleDetail.setProduct(getProduct());
            saleDetail.setQuantity(Long.parseLong(getQuantity()));
            saleDetail.setSerialNumber(getSerial());
            saleDetail.setNetTax(Double.parseDouble(getNetTax()));
            saleDetail.setTaxType(getTaxType());
            saleDetail.setDiscount(Double.parseDouble(getDiscount()));
            saleDetail.setDiscountType(getDiscountType());

            saleDetailDao.update(saleDetail);

            getAllSaleDetails();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getAllSaleDetails() throws SQLException {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<SaleDetail, Long> saleDetailDao =
                DaoManager.createDao(connectionSource, SaleDetail.class);

            saleDetailList.clear();
            saleDetailList.addAll(saleDetailDao.queryForAll());
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void removeSaleDetail(long index, int tempIndex) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            saleDetailList.remove(tempIndex);
            PENDING_DELETES.add(index);
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void deleteSaleDetails(LinkedList<Long> indexes) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            indexes.forEach(
                index -> {
                  try {
                    SQLiteConnection connection = SQLiteConnection.getInstance();
                    ConnectionSource connectionSource = connection.getConnection();

                    Dao<SaleDetail, Long> saleDetailDao =
                        DaoManager.createDao(connectionSource, SaleDetail.class);

                    saleDetailDao.deleteById(index);
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

  public static ObservableList<SaleDetail> getSaleDetailList() {
    return saleDetailList;
  }
}
