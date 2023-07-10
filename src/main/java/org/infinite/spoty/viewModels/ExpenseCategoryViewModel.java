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
import org.infinite.spoty.database.models.ExpenseCategory;

public class ExpenseCategoryViewModel {
  public static final ObservableList<ExpenseCategory> categoryList =
      FXCollections.observableArrayList();
  public static final ObservableList<ExpenseCategory> categoryComboBoxList =
      FXCollections.observableArrayList();
  private static final ListProperty<ExpenseCategory> categories = new SimpleListProperty<>(categoryList);
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty name = new SimpleStringProperty("");
  private static final StringProperty description = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    ExpenseCategoryViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    ExpenseCategoryViewModel.name.set(name);
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static String getDescription() {
    return description.get();
  }

  public static void setDescription(String description) {
    ExpenseCategoryViewModel.description.set(description);
  }

  public static StringProperty descriptionProperty() {
    return description;
  }

  public static ObservableList<ExpenseCategory> getCategories() {
    return categories.get();
  }

  public static void setCategories(ObservableList<ExpenseCategory> categories) {
    ExpenseCategoryViewModel.categories.set(categories);
  }

  public static ListProperty<ExpenseCategory> categoriesProperty() {
    return categories;
  }

  public static void resetProperties() {
    setId(0);
    setName("");
    setDescription("");
  }

  public static void saveExpenseCategory() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<ExpenseCategory, Long> expenseCategoryDao =
                DaoManager.createDao(connectionSource, ExpenseCategory.class);

            ExpenseCategory expenseCategory = new ExpenseCategory(getName(), getDescription());

            expenseCategoryDao.create(expenseCategory);

            resetProperties();
            getAllCategories();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getAllCategories() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<ExpenseCategory, Long> expenseCategoryDao =
                DaoManager.createDao(connectionSource, ExpenseCategory.class);

            Platform.runLater(
                () -> {
                  categoryList.clear();

                  try {
                    categoryList.addAll(expenseCategoryDao.queryForAll());
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

            Dao<ExpenseCategory, Long> expenseCategoryDao =
                DaoManager.createDao(connectionSource, ExpenseCategory.class);

            ExpenseCategory expenseCategory = expenseCategoryDao.queryForId(index);
            setId(expenseCategory.getId());
            setName(expenseCategory.getName());
            setDescription(expenseCategory.getDescription());
            getAllCategories();
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

            Dao<ExpenseCategory, Long> expenseCategoryDao =
                DaoManager.createDao(connectionSource, ExpenseCategory.class);

            ExpenseCategory expenseCategory = expenseCategoryDao.queryForId(index);

            expenseCategory.setName(getName());
            expenseCategory.setDescription(getDescription());

            expenseCategoryDao.update(expenseCategory);

            getAllCategories();
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

            Dao<ExpenseCategory, Long> expenseCategoryDao =
                DaoManager.createDao(connectionSource, ExpenseCategory.class);

            expenseCategoryDao.deleteById(index);
            getAllCategories();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static ObservableList<ExpenseCategory> getCategoryComboBoxList() {
    categoryComboBoxList.clear();
    categoryComboBoxList.addAll(categoryList);
    return categoryComboBoxList;
  }

  public static ObservableList<ExpenseCategory> getCategoryList() {
    return categoryList;
  }
}
