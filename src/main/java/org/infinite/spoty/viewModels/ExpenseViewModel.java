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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Expense;
import org.infinite.spoty.database.models.ExpenseCategory;

public class ExpenseViewModel {
  public static final ObservableList<Expense> expenseList = FXCollections.observableArrayList();
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty date = new SimpleStringProperty("");
  private static final StringProperty reference = new SimpleStringProperty("");
  private static final StringProperty name = new SimpleStringProperty("");
  private static final StringProperty amount = new SimpleStringProperty("");
  private static final ObjectProperty<ExpenseCategory> category = new SimpleObjectProperty<>(null);
  private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
  private static final StringProperty details = new SimpleStringProperty("");

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    ExpenseViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static Date getDate() {
    try {
      return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static void setDate(String date) {
    ExpenseViewModel.date.set(date);
  }

  public static StringProperty dateProperty() {
    return date;
  }

  public static String getReference() {
    return reference.get();
  }

  public static void setReference(String reference) {
    ExpenseViewModel.reference.set(reference);
  }

  public static StringProperty referenceProperty() {
    return reference;
  }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    ExpenseViewModel.name.set(name);
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static double getAmount() {
    return Double.parseDouble(!amount.get().isEmpty() ? amount.get() : "0");
  }

  public static void setAmount(double amount) {
    ExpenseViewModel.amount.set(amount > 0 ? Double.toString(amount) : "");
  }

  public static StringProperty amountProperty() {
    return amount;
  }

  public static ExpenseCategory getCategory() {
    return category.get();
  }

  public static void setCategory(ExpenseCategory category) {
    ExpenseViewModel.category.set(category);
  }

  public static ObjectProperty<ExpenseCategory> categoryProperty() {
    return category;
  }

  public static Branch getBranch() {
    return branch.get();
  }

  public static void setBranch(Branch branch) {
    ExpenseViewModel.branch.set(branch);
  }

  public static ObjectProperty<Branch> branchProperty() {
    return branch;
  }

  public static String getDetails() {
    return details.get();
  }

  public static void setDetails(String details) {
    ExpenseViewModel.details.set(details);
  }

  public static StringProperty detailsProperty() {
    return details;
  }

  public static void resetProperties() {
    setId(0);
    setDate("");
    setReference("");
    setName("");
    setAmount(0);
    setCategory(null);
    setBranch(null);
    setDetails("");
  }

  public static void saveExpense() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Expense, Long> expenseDao = DaoManager.createDao(connectionSource, Expense.class);

            Expense expense =
                new Expense(
                    getDate(), getName(), getCategory(), getBranch(), getDetails(), getAmount());

            expenseDao.create(expense);

            getExpenses();
            resetProperties();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void getExpenses() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Expense, Long> expenseDao = DaoManager.createDao(connectionSource, Expense.class);

            Platform.runLater(
                () -> {
                  expenseList.clear();

                  try {
                    expenseList.addAll(expenseDao.queryForAll());
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

            Dao<Expense, Long> expenseDao = DaoManager.createDao(connectionSource, Expense.class);

            Expense expense = expenseDao.queryForId(index);

            setId(expense.getId());
            setDate(expense.getLocaleDate());
            setName(expense.getName());
            setBranch(expense.getBranch());
            setCategory(expense.getExpenseCategory());
            setAmount(expense.getAmount());
            setDetails(expense.getDetails());

            getExpenses();
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

            Dao<Expense, Long> expenseDao = DaoManager.createDao(connectionSource, Expense.class);

            Expense expense = expenseDao.queryForId(index);

            expense.setDate(getDate());
            expense.setName(getName());
            expense.setCategory(getCategory());
            expense.setBranch(getBranch());
            expense.setDetails(getDetails());
            expense.setAmount(getAmount());

            expenseDao.update(expense);

            getExpenses();
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

            Dao<Expense, Long> expenseDao = DaoManager.createDao(connectionSource, Expense.class);

            expenseDao.deleteById(index);
            getExpenses();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }
}
