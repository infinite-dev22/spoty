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
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Role;
import org.infinite.spoty.database.models.User;

public class UserViewModel {
  public static final ObservableList<User> usersList = FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty firstName = new SimpleStringProperty("");
  private static final StringProperty lastName = new SimpleStringProperty("");
  private static final StringProperty userName = new SimpleStringProperty("");
  private static final ObjectProperty<Role> role = new SimpleObjectProperty<>(null);
  private static final StringProperty email = new SimpleStringProperty("");
  private static final StringProperty phone = new SimpleStringProperty("");
  private static final StringProperty city = new SimpleStringProperty("");
  private static final StringProperty address = new SimpleStringProperty("");
  private static final StringProperty taxNumber = new SimpleStringProperty("");
  private static final StringProperty country = new SimpleStringProperty("");
  private static final BooleanProperty active = new SimpleBooleanProperty(true);
  private static final BooleanProperty accessAllBranches = new SimpleBooleanProperty(false);

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    UserViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getFirstName() {
    return firstName.get();
  }

  public static void setFirstName(String firstName) {
    UserViewModel.firstName.set(firstName);
  }

  public static StringProperty firstNameProperty() {
    return firstName;
  }

  public static String getLastName() {
    return lastName.get();
  }

  public static void setLastName(String lastName) {
    UserViewModel.lastName.set(lastName);
  }

  public static StringProperty lastNameProperty() {
    return lastName;
  }

  public static boolean isActive() {
    return active.get();
  }

  public static void setActive(boolean active) {
    UserViewModel.active.set(active);
  }

  public static BooleanProperty activeProperty() {
    return active;
  }

  public static boolean canAccessAllBranches() {
    return accessAllBranches.get();
  }

  public static void setAccessAllBranches(boolean accessAllBranches) {
    UserViewModel.accessAllBranches.set(accessAllBranches);
  }

  public static BooleanProperty accessAllBranchesProperty() {
    return accessAllBranches;
  }

  public static String getUserName() {
    return userName.get();
  }

  public static void setUserName(String userName) {
    UserViewModel.userName.set(userName);
  }

  public static StringProperty userNameProperty() {
    return userName;
  }

  public static Role getRole() {
    return role.get();
  }

  public static void setRole(Role role) {
    UserViewModel.role.set(role);
  }

  public static ObjectProperty<Role> roleProperty() {
    return role;
  }

  public static String getEmail() {
    return email.get();
  }

  public static void setEmail(String email) {
    UserViewModel.email.set(email);
  }

  public static StringProperty emailProperty() {
    return email;
  }

  public static String getPhone() {
    return phone.get();
  }

  public static void setPhone(String phone) {
    UserViewModel.phone.set(phone);
  }

  public static StringProperty phoneProperty() {
    return phone;
  }

  public static String getCity() {
    return city.get();
  }

  public static void setCity(String city) {
    UserViewModel.city.set(city);
  }

  public static StringProperty cityProperty() {
    return city;
  }

  public static String getAddress() {
    return address.get();
  }

  public static void setAddress(String address) {
    UserViewModel.address.set(address);
  }

  public static StringProperty addressProperty() {
    return address;
  }

  public static String getTaxNumber() {
    return taxNumber.get();
  }

  public static void setTaxNumber(String taxNumber) {
    UserViewModel.taxNumber.set(taxNumber);
  }

  public static StringProperty taxNumberProperty() {
    return taxNumber;
  }

  public static String getCountry() {
    return country.get();
  }

  public static void setCountry(String country) {
    UserViewModel.country.set(country);
  }

  public static StringProperty countryProperty() {
    return country;
  }

  public static void resetProperties() {
    setId(0);
    setFirstName("");
    setLastName("");
    setUserName("");
    setRole(null);
    setEmail("");
    setEmail("");
    setPhone("");
    setCity("");
    setAddress("");
    setTaxNumber("");
    setCountry("");
    setActive(true);
  }

  public static void saveUser() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<User, Long> userDao = DaoManager.createDao(connectionSource, User.class);

            User user =
                new User(
                    getFirstName(),
                    getLastName(),
                    getUserName(),
                    getRole(),
                    isActive(),
                    canAccessAllBranches());

            userDao.create(user);

            resetProperties();
            getUsers();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getUsers() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<User, Long> userDao = DaoManager.createDao(connectionSource, User.class);

            usersList.clear();
            usersList.addAll(userDao.queryForAll());
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

            Dao<User, Long> userDao = DaoManager.createDao(connectionSource, User.class);

            User user = userDao.queryForId(index);

            setId(user.getId());
            setFirstName(user.getFirstName());
            setLastName(user.getLastName());
            setUserName(user.getUserName());
            setRole(user.getRole());
            setActive(user.isActive());
            setAccessAllBranches(user.canAccessAllBranches());

            getUsers();
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

            Dao<User, Long> userDao = DaoManager.createDao(connectionSource, User.class);

            User user = userDao.queryForId(index);

            user.setFirstName(getFirstName());
            user.setLastName(getLastName());
            user.setUserName(getUserName());
            user.setRole(getRole());
            user.setActive(isActive());
            user.setAccessAllBranches(canAccessAllBranches());

            userDao.update(user);

            resetProperties();
            getUsers();
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

            Dao<User, Long> userDao = DaoManager.createDao(connectionSource, User.class);

            userDao.deleteById(index);
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }
}
