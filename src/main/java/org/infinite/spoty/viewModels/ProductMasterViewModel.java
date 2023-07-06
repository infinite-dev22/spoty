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
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.*;

public class ProductMasterViewModel {
  public static final ObservableList<ProductMaster> productMasterList =
      FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty barcodeType = new SimpleStringProperty("");
  private static final StringProperty name = new SimpleStringProperty("");
  private static final DoubleProperty price = new SimpleDoubleProperty(0);
  private static final StringProperty note = new SimpleStringProperty("");
  private static final BooleanProperty notForSale = new SimpleBooleanProperty(false);
  private static final BooleanProperty hasVariants = new SimpleBooleanProperty(false);
  private static final ObjectProperty<ProductCategory> category = new SimpleObjectProperty<>(null);
  private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
  private static final ObjectProperty<Brand> brand = new SimpleObjectProperty<>(null);

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    ProductMasterViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getBarcodeType() {
    return barcodeType.get();
  }

  public static void setBarcodeType(String barcodeType) {
    ProductMasterViewModel.barcodeType.set(barcodeType);
  }

  public static StringProperty barcodeTypeProperty() {
    return barcodeType;
  }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    ProductMasterViewModel.name.set(name);
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static double getPrice() {
    return price.get();
  }

  public static void setPrice(double price) {
    ProductMasterViewModel.price.set(price);
  }

  public static DoubleProperty priceProperty() {
    return price;
  }

  public static String getNote() {
    return note.get();
  }

  public static void setNote(String note) {
    ProductMasterViewModel.note.set(note);
  }

  public static StringProperty noteProperty() {
    return note;
  }

  public static boolean isNotForSale() {
    return notForSale.get();
  }

  public static void setNotForSale(boolean notForSale) {
    ProductMasterViewModel.notForSale.set(notForSale);
  }

  public static BooleanProperty notForSaleProperty() {
    return notForSale;
  }

  public static boolean getHasVariants() {
    return hasVariants.get();
  }

  public static void setHasVariants(boolean hasVariants) {
    ProductMasterViewModel.hasVariants.set(hasVariants);
  }

  public static BooleanProperty hasVariantsProperty() {
    return hasVariants;
  }

  public static Branch getBranch() {
    return branch.get();
  }

  public static void setBranch(Branch branch) {
    ProductMasterViewModel.branch.set(branch);
  }

  public static ObjectProperty<Branch> branchProperty() {
    return branch;
  }

  public static ProductCategory getCategory() {
    return category.get();
  }

  public static void setCategory(ProductCategory category) {
    ProductMasterViewModel.category.set(category);
  }

  public static ObjectProperty<ProductCategory> categoryProperty() {
    return category;
  }

  public static Brand getBrand() {
    return brand.get();
  }

  public static void setBrand(Brand brand) {
    ProductMasterViewModel.brand.set(brand);
  }

  public static ObjectProperty<Brand> brandProperty() {
    return brand;
  }

  public static void resetProperties() {
    setId(0);
    setBarcodeType("");
    setName("");
    setPrice(0);
    setNote("");
    setNotForSale(false);
    setHasVariants(false);
    setCategory(null);
    setBrand(null);
    PENDING_DELETES.clear();
    ProductDetailViewModel.productDetailsList.clear();
  }

  public static void saveProductMaster() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<ProductMaster, Long> productMasterDao =
                DaoManager.createDao(connectionSource, ProductMaster.class);

            ProductMaster productMaster =
                new ProductMaster(
                    getBarcodeType(),
                    getName(),
                    getPrice(),
                    getCategory(),
                    getBrand(),
                    getNote(),
                    isNotForSale(),
                    getHasVariants());

            // Add product master to product details.
            if (!ProductDetailViewModel.productDetailsList.isEmpty()) {
              ProductDetailViewModel.productDetailsList.forEach(
                  productDetail -> productDetail.setProduct(productMaster));
              productMaster.setProductDetails(ProductDetailViewModel.productDetailsList);
            }

            productMasterDao.create(productMaster);

            resetProperties();
            getProductMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getProductMasters() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<ProductMaster, Long> productMasterDao =
                DaoManager.createDao(connectionSource, ProductMaster.class);

            Platform.runLater(
                () -> {
                  productMasterList.clear();

                  try {
                    productMasterList.addAll(productMasterDao.queryForAll());
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

            Dao<ProductMaster, Long> productMasterDao =
                DaoManager.createDao(connectionSource, ProductMaster.class);

            ProductMaster productMaster = productMasterDao.queryForId(index);
            setId(productMaster.getId());
            setBarcodeType(productMaster.getBarcodeType());
            setName(productMaster.getName());
            setCategory(productMaster.getCategory());
            setBrand(productMaster.getBrand());
            setNote(productMaster.getNote());
            setNotForSale(productMaster.isNotForSale());
            setHasVariants(productMaster.hasVariant());
            setBarcodeType(productMaster.getBarcodeType());

            ProductDetailViewModel.productDetailsList.clear();
            ProductDetailViewModel.productDetailsList.addAll(productMaster.getProductDetails());
            getProductMasters();
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

            Dao<ProductMaster, Long> productMasterDao =
                DaoManager.createDao(connectionSource, ProductMaster.class);

            ProductMaster productMaster = productMasterDao.queryForId(index);
            productMaster.setBarcodeType(getBarcodeType());
            productMaster.setName(getName());
            productMaster.setCategory(getCategory());
            productMaster.setBrand(getBrand());
            productMaster.setNote(getNote());
            productMaster.setNotForSale(isNotForSale());
            productMaster.canHaveVariants(getHasVariants());

            ProductDetailViewModel.deleteProductDetails(PENDING_DELETES);
            productMaster.setProductDetails(ProductDetailViewModel.productDetailsList);

            productMasterDao.update(productMaster);

            resetProperties();
            getProductMasters();
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

            Dao<ProductMaster, Long> productMasterDao =
                DaoManager.createDao(connectionSource, ProductMaster.class);

            productMasterDao.deleteById(index);
            getProductMasters();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }
}
