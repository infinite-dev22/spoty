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
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.*;

public class ProductDetailViewModel {
  public static final ObservableList<ProductDetail> productDetailsList =
      FXCollections.observableArrayList();
  public static final ObservableList<ProductDetail> productDetailsComboBoxList =
      FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final ObjectProperty<ProductMaster> product = new SimpleObjectProperty<>(null);
  private static final ListProperty<Branch> branches = new SimpleListProperty<>(null);
  private static final ObjectProperty<UnitOfMeasure> unit = new SimpleObjectProperty<>(null);
  //    private static final ObjectProperty<UnitOfMeasure> saleUnit = new
  // SimpleObjectProperty<>(null);
  //    private static final ObjectProperty<UnitOfMeasure> productUnit = new
  // SimpleObjectProperty<>(null);
  private static final StringProperty name = new SimpleStringProperty("");
  private static final LongProperty quantity = new SimpleLongProperty(0);
  private static final DoubleProperty cost = new SimpleDoubleProperty(0);
  private static final DoubleProperty price = new SimpleDoubleProperty(0);
  private static final DoubleProperty netTax = new SimpleDoubleProperty(0);
  private static final StringProperty taxType = new SimpleStringProperty("");
  private static final LongProperty stockAlert = new SimpleLongProperty(0);
  private static final StringProperty serial = new SimpleStringProperty("");

  public static Long getId() {
    return id.get();
  }

  public static void setId(long id) {
    ProductDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static ProductMaster getProduct() {
    return product.get();
  }

  public static void setProduct(ProductMaster product) {
    ProductDetailViewModel.product.set(product);
  }

  public static ObjectProperty<ProductMaster> productProperty() {
    return product;
  }

  public static ObservableList<Branch> getBranches() {
    return branches.get();
  }

  public static void setBranches(ObservableList<Branch> branches) {
    ProductDetailViewModel.branches.set(branches);
  }

  public static ListProperty<Branch> branchesProperty() {
    return branches;
  }

  public static UnitOfMeasure getUnit() {
    return unit.get();
  }

  public static void setUnit(UnitOfMeasure unit) {
    ProductDetailViewModel.unit.set(unit);
  }

  public static ObjectProperty<UnitOfMeasure> unitProperty() {
    return unit;
  }

  //    public static UnitOfMeasure getSaleUnit() {
  //        return saleUnit.get();
  //    }
  //
  //    public static void setSaleUnit(UnitOfMeasure saleUnit) {
  //        ProductDetailViewModel.saleUnit.set(saleUnit);
  //    }
  //
  //    public static ObjectProperty<UnitOfMeasure> saleUnitProperty() {
  //        return saleUnit;
  //    }
  //
  //    public static UnitOfMeasure getProductUnit() {
  //        return productUnit.get();
  //    }
  //
  //    public static void setProductUnit(UnitOfMeasure productUnit) {
  //        ProductDetailViewModel.productUnit.set(productUnit);
  //    }
  //
  //    public static ObjectProperty<UnitOfMeasure> productUnitProperty() {
  //        return productUnit;
  //    }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    ProductDetailViewModel.name.set(name != null ? name : "");
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static long getQuantity() {
    return quantity.get();
  }

  public static void setQuantity(long quantity) {
    ProductDetailViewModel.quantity.set(quantity);
  }

  public static LongProperty quantityProperty() {
    return quantity;
  }

  public static double getCost() {
    return cost.get();
  }

  public static void setCost(double cost) {
    ProductDetailViewModel.cost.set(cost);
  }

  public static DoubleProperty costProperty() {
    return cost;
  }

  public static double getPrice() {
    return price.get();
  }

  public static void setPrice(double price) {
    ProductDetailViewModel.price.set(price);
  }

  public static DoubleProperty priceProperty() {
    return price;
  }

  public static double getNetTax() {
    return netTax.get();
  }

  public static void setNetTax(double netTax) {
    ProductDetailViewModel.netTax.set(netTax);
  }

  public static DoubleProperty netTaxProperty() {
    return netTax;
  }

  public static String getTaxType() {
    return taxType.get();
  }

  public static void setTaxType(String taxType) {
    ProductDetailViewModel.taxType.set(taxType);
  }

  public static StringProperty taxTypeProperty() {
    return taxType;
  }

  public static long getStockAlert() {
    return stockAlert.get();
  }

  public static void setStockAlert(long stockAlert) {
    ProductDetailViewModel.stockAlert.set(stockAlert);
  }

  public static LongProperty stockAlertProperty() {
    return stockAlert;
  }

  public static String getSerialNumber() {
    return serial.get();
  }

  public static void setSerial(String serial) {
    ProductDetailViewModel.serial.set(serial);
  }

  public static StringProperty serialProperty() {
    return serial;
  }

  public static void addProductDetail() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            ProductDetail productDetail =
                new ProductDetail(
                    getUnit(),
                    getName(),
                    getQuantity(),
                    getCost(),
                    getPrice(),
                    getNetTax(),
                    getTaxType(),
                    getStockAlert(),
                    getSerialNumber());
            productDetailsList.add(productDetail);

            resetProperties();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void resetProperties() {
    setId(0);
    setTempId(-1);
    setProduct(null);
    setBranches(null);
    setUnit(null);
    setName("");
    setQuantity(0);
    setCost(0);
    setPrice(0);
    setNetTax(0);
    setTaxType("");
    setStockAlert(0);
    setSerial("");
    setTempId(-1);
  }

  public static void getProductDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<ProductDetail, Long> productDetailDao =
                DaoManager.createDao(connectionSource, ProductDetail.class);

            productDetailsList.clear();
            productDetailsList.addAll(productDetailDao.queryForAll());
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void updateProductDetail(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<ProductDetail, Long> productDetailDao =
                DaoManager.createDao(connectionSource, ProductDetail.class);

            ProductDetail productDetail = productDetailDao.queryForId(index);
            productDetail.setUnit(getUnit());
            productDetail.setName(getName());
            productDetail.setSerialNumber(getSerialNumber());

            productDetailsList.remove((int) getTempId());
            productDetailsList.add(getTempId(), productDetail);

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

            Dao<ProductDetail, Long> productDetailDao =
                DaoManager.createDao(connectionSource, ProductDetail.class);

            setTempId(tempIndex);
            ProductDetail productDetail = productDetailDao.queryForId(index);

            setId(productDetail.getId());
            setBranches(FXCollections.observableArrayList(productDetail.getBranch()));
            setUnit(productDetail.getUnit());
            setName(productDetail.getName());
            setQuantity(productDetail.getQuantity());
            setCost(productDetail.getCost());
            setPrice(productDetail.getPrice());
            setNetTax(productDetail.getNetTax());
            setTaxType(productDetail.getTaxType());
            setStockAlert(productDetail.getStockAlert());
            setSerial(productDetail.getSerialNumber());
            setProduct(productDetail.getProduct());
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

            Dao<ProductDetail, Long> productDetailDao =
                DaoManager.createDao(connectionSource, ProductDetail.class);

            ProductDetail productDetail = productDetailDao.queryForId(index);

            productDetail.setUnit(getUnit());
            productDetail.setName(getName());
            productDetail.setQuantity(getQuantity());
            productDetail.setCost(getCost());
            productDetail.setPrice(getPrice());
            productDetail.setNetTax(getNetTax());
            productDetail.setTaxType(getTaxType());
            productDetail.setStockAlert(getStockAlert());
            productDetail.setSerialNumber(getSerialNumber());

            productDetailDao.update(productDetail);

            getProductDetails();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void removeProductDetail(long index, int tempIndex) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            productDetailsList.remove(tempIndex);
            PENDING_DELETES.add(index);
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void deleteProductDetails(List<Long> indexes) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() {
            indexes.forEach(
                index -> {
                  try {
                    SQLiteConnection connection = SQLiteConnection.getInstance();
                    ConnectionSource connectionSource = connection.getConnection();

                    Dao<ProductDetail, Long> productDetailDao =
                        DaoManager.createDao(connectionSource, ProductDetail.class);
                    productDetailDao.deleteById(index);
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

  public static ObservableList<ProductDetail> getProductDetailsComboBoxList() {
    productDetailsComboBoxList.clear();
    productDetailsComboBoxList.addAll(productDetailsList);
    return productDetailsComboBoxList;
  }
}
