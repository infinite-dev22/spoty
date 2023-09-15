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
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Customer;

public class CustomerViewModel {
  public static final ObservableList<Customer> customersList = FXCollections.observableArrayList();
  public static final ObservableList<Customer> customersComboBoxList =
      FXCollections.observableArrayList();
  private static final ListProperty<Customer> customers = new SimpleListProperty<>(customersList);
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
    CustomerViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    CustomerViewModel.name.set(name);
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static String getCode() {
    return code.get();
  }

  public static void setCode(String code) {
    CustomerViewModel.code.set(code);
  }

  public static StringProperty codeProperty() {
    return code;
  }

  public static String getEmail() {
    return email.get();
  }

  public static void setEmail(String email) {
    CustomerViewModel.email.set(email);
  }

  public static StringProperty emailProperty() {
    return email;
  }

  public static String getPhone() {
    return phone.get();
  }

  public static void setPhone(String phone) {
    CustomerViewModel.phone.set(phone);
  }

  public static StringProperty phoneProperty() {
    return phone;
  }

  public static String getCity() {
    return city.get();
  }

  public static void setCity(String city) {
    CustomerViewModel.city.set(city);
  }

  public static StringProperty cityProperty() {
    return city;
  }

  public static String getAddress() {
    return address.get();
  }

  public static void setAddress(String address) {
    CustomerViewModel.address.set(address);
  }

  public static StringProperty addressProperty() {
    return address;
  }

  public static String getTaxNumber() {
    return taxNumber.get();
  }

  public static void setTaxNumber(String taxNumber) {
    CustomerViewModel.taxNumber.set(taxNumber);
  }

  public static StringProperty taxNumberProperty() {
    return taxNumber;
  }

  public static String getCountry() {
    return country.get();
  }

  public static void setCountry(String country) {
    CustomerViewModel.country.set(country);
  }

  public static StringProperty countryProperty() {
    return country;
  }

  public static ObservableList<Customer> getCustomers() {
    return customers.get();
  }

  public static void setCustomers(ObservableList<Customer> customers) {
    CustomerViewModel.customers.set(customers);
  }

  public static ListProperty<Customer> customersProperty() {
    return customers;
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

  public static void saveCustomer() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Customer, Long> customerDao =
                DaoManager.createDao(connectionSource, Customer.class);

            Customer customer =
                new Customer(
                    getName(),
                    getEmail(),
                    "+" + getPhone(),
                    getCity(),
                    getAddress(),
                    getTaxNumber(),
                    getCountry());

            customerDao.create(customer);

            return null;
          }
        };

    task.setOnSucceeded(
        event -> {
          resetProperties();
          getAllCustomers();
        });

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void getAllCustomers() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Customer, Long> customerDao =
                DaoManager.createDao(connectionSource, Customer.class);

            Platform.runLater(
                () -> {
                  customersList.clear();

                  try {
                    customersList.addAll(customerDao.queryForAll());
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });

            return null;
          }
        };

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void getItem(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Customer, Long> customerDao =
                DaoManager.createDao(connectionSource, Customer.class);

            Customer customer = customerDao.queryForId(index);

            setId(customer.getId());
            setName(customer.getName());
            setEmail(customer.getEmail());
            setPhone(customer.getPhone());
            setCity(customer.getCity());
            setCountry(customer.getCountry());
            setAddress(customer.getAddress());
            setTaxNumber(customer.getTaxNumber());

            return null;
          }
        };

    task.setOnSucceeded(event -> getAllCustomers());

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void updateItem(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Customer, Long> customerDao =
                DaoManager.createDao(connectionSource, Customer.class);

            Customer customer = customerDao.queryForId(index);

            customer.setName(getName());
            customer.setEmail(getEmail());
            customer.setPhone(getPhone());
            customer.setCity(getCity());
            customer.setAddress(getAddress());
            customer.setTaxNumber(getTaxNumber());
            customer.setCountry(getCountry());

            customerDao.update(customer);

            return null;
          }
        };

    task.setOnSucceeded(
        event -> {
          resetProperties();
          getAllCustomers();
        });

    GlobalActions.spotyThreadPool().execute(task);
  }

  public static void deleteItem(long index) {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Customer, Long> customerDao =
                DaoManager.createDao(connectionSource, Customer.class);

            customerDao.deleteById(index);

            return null;
          }
        };

    task.setOnSucceeded(event -> getAllCustomers());

    GlobalActions.spotyThreadPool().execute(task);
  }
}
