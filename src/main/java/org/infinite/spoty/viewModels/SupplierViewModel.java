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
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Supplier;

public class SupplierViewModel {
  public static final ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
  public static final ObservableList<Supplier> suppliersComboBoxList = FXCollections.observableArrayList();
  private static final ListProperty<Supplier> suppliers = new SimpleListProperty<>(suppliersList);
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty name = new SimpleStringProperty("");
  private static final StringProperty code = new SimpleStringProperty("");
  private static final StringProperty email = new SimpleStringProperty("");
  private static final StringProperty phone = new SimpleStringProperty("");
  private static final StringProperty city = new SimpleStringProperty("");
  private static final StringProperty address = new SimpleStringProperty("");
  private static final StringProperty taxNumber = new SimpleStringProperty("");
  private static final StringProperty country = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    SupplierViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    SupplierViewModel.name.set(name);
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static String getCode() {
    return code.get();
  }

  public static void setCode(String code) {
    SupplierViewModel.code.set(code);
  }

  public static StringProperty codeProperty() {
    return code;
  }

  public static String getEmail() {
    return email.get();
  }

  public static void setEmail(String email) {
    SupplierViewModel.email.set(email);
  }

  public static StringProperty emailProperty() {
    return email;
  }

  public static String getPhone() {
    return phone.get();
  }

  public static void setPhone(String phone) {
    SupplierViewModel.phone.set(phone);
  }

  public static StringProperty phoneProperty() {
    return phone;
  }

  public static String getCity() {
    return city.get();
  }

  public static void setCity(String city) {
    SupplierViewModel.city.set(city);
  }

  public static StringProperty cityProperty() {
    return city;
  }

  public static String getAddress() {
    return address.get();
  }

  public static void setAddress(String address) {
    SupplierViewModel.address.set(address);
  }

  public static StringProperty addressProperty() {
    return address;
  }

  public static String getTaxNumber() {
    return taxNumber.get();
  }

  public static void setTaxNumber(String taxNumber) {
    SupplierViewModel.taxNumber.set(taxNumber);
  }

  public static StringProperty taxNumberProperty() {
    return taxNumber;
  }

  public static String getCountry() {
    return country.get();
  }

  public static void setCountry(String country) {
    SupplierViewModel.country.set(country);
  }

  public static StringProperty countryProperty() {
    return country;
  }

  public static ObservableList<Supplier> getSuppliers() {
    return suppliers.get();
  }

  public static void setSuppliers(ObservableList<Supplier> suppliers) {
    SupplierViewModel.suppliers.set(suppliers);
  }

  public static ListProperty<Supplier> suppliersProperty() {
    return suppliers;
  }

  public static void resetProperties() {
    setId(0);
    setName("");
    setCode("");
    setEmail("");
    setEmail("");
    setPhone("");
    setCity("");
    setAddress("");
    setTaxNumber("");
    setCountry("");
  }

  public static void saveSupplier() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Supplier, Long> supplierDao =
                DaoManager.createDao(connectionSource, Supplier.class);

            Supplier supplier =
                new Supplier(
                    getName(),
                    getEmail(),
                    getPhone(),
                    getCity(),
                    getAddress(),
                    getTaxNumber(),
                    getCountry());

            supplierDao.create(supplier);

            resetProperties();
            getAllSuppliers();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getAllSuppliers() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Supplier, Long> supplierDao =
                DaoManager.createDao(connectionSource, Supplier.class);

            Platform.runLater(
                () -> {
                  suppliersList.clear();

                  try {
                    suppliersList.addAll(supplierDao.queryForAll());
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

            Dao<Supplier, Long> supplierDao =
                DaoManager.createDao(connectionSource, Supplier.class);

            Supplier supplier = supplierDao.queryForId(index);

            setId(supplier.getId());
            setName(supplier.getName());
            setEmail(supplier.getEmail());
            setPhone(supplier.getPhone());
            setCity(supplier.getCity());
            setCountry(supplier.getCountry());
            setAddress(supplier.getAddress());
            setTaxNumber(supplier.getTaxNumber());

            getAllSuppliers();
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

            Dao<Supplier, Long> supplierDao =
                DaoManager.createDao(connectionSource, Supplier.class);

            Supplier supplier = supplierDao.queryForId(index);

            supplier.setName(getName());
            supplier.setEmail(getEmail());
            supplier.setPhone(getPhone());
            supplier.setTaxNumber(getTaxNumber());
            supplier.setAddress(getAddress());
            supplier.setCity(getCity());
            supplier.setCountry(getCountry());

            supplierDao.update(supplier);
            resetProperties();
            getAllSuppliers();
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

            Dao<Supplier, Long> supplierDao =
                DaoManager.createDao(connectionSource, Supplier.class);

            supplierDao.deleteById(index);
            getAllSuppliers();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static ObservableList<Supplier> getSuppliersList() {
    return suppliersList;
  }

  public static ObservableList<Supplier> getSuppliersComboBoxList() {
    suppliersComboBoxList.clear();
    suppliersComboBoxList.addAll(suppliersList);
    return suppliersComboBoxList;
  }
}
