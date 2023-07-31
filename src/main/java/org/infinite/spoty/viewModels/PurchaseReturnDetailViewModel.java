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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Product;
import org.infinite.spoty.database.models.PurchaseReturnDetail;
import org.infinite.spoty.database.models.PurchaseReturnMaster;

public class PurchaseReturnDetailViewModel {
  public static final ObservableList<PurchaseReturnDetail> purchaseReturnDetailsList =
      FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty();
  private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
  private static final ObjectProperty<PurchaseReturnMaster> purchaseReturn =
      new SimpleObjectProperty<>();
  private static final StringProperty quantity = new SimpleStringProperty("");
  private static final StringProperty serial = new SimpleStringProperty("");
  private static final StringProperty description = new SimpleStringProperty("");
  private static final StringProperty location = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    PurchaseReturnDetailViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static Product getProduct() {
    return product.get();
  }

  public static void setProduct(Product product) {
    PurchaseReturnDetailViewModel.product.set(product);
  }

  public static ObjectProperty<Product> productProperty() {
    return product;
  }

  public static PurchaseReturnMaster getPurchaseReturn() {
    return purchaseReturn.get();
  }

  public static void setPurchaseReturn(PurchaseReturnMaster purchaseReturn) {
    PurchaseReturnDetailViewModel.purchaseReturn.set(purchaseReturn);
  }

  public static ObjectProperty<PurchaseReturnMaster> purchaseReturnProperty() {
    return purchaseReturn;
  }

  public static long getQuantity() {
    return Long.parseLong(!quantity.get().isEmpty() ? quantity.get() : "0");
  }

  public static void setQuantity(String quantity) {
    PurchaseReturnDetailViewModel.quantity.set(quantity);
  }

  public static StringProperty quantityProperty() {
    return quantity;
  }

  public static String getDescription() {
    return description.get();
  }

  public static void setDescription(String description) {
    PurchaseReturnDetailViewModel.description.set(description);
  }

  public static StringProperty descriptionProperty() {
    return description;
  }

  public static String getSerial() {
    return serial.get();
  }

  public static void setSerial(String serial) {
    PurchaseReturnDetailViewModel.serial.set(serial);
  }

  public static StringProperty serialProperty() {
    return serial;
  }

  public static String getLocation() {
    return location.get();
  }

  public static void setLocation(String location) {
    PurchaseReturnDetailViewModel.location.set(location);
  }

  public static StringProperty locationProperty() {
    return location;
  }

  public static void resetProperties() {
    setId(0);
    setProduct(null);
    setQuantity("");
    setSerial("");
    setDescription("");
    setLocation("");
  }

  public static void getPurchaseReturnDetails() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<PurchaseReturnDetail, Long> purchaseReturnDetailDao =
                DaoManager.createDao(connectionSource, PurchaseReturnDetail.class);

            purchaseReturnDetailsList.clear();
            purchaseReturnDetailsList.addAll(purchaseReturnDetailDao.queryForAll());
            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static ObservableList<PurchaseReturnDetail> getPurchaseReturnDetailsList() {
    return purchaseReturnDetailsList;
  }
}
