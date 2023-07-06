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
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Currency;

public class CurrencyViewModel {
  private static final LongProperty id = new SimpleLongProperty(0);
  private static final StringProperty code = new SimpleStringProperty("");
  private static final StringProperty name = new SimpleStringProperty("");
  private static final StringProperty symbol = new SimpleStringProperty("");
  public static ObservableList<Currency> currenciesList = FXCollections.observableArrayList();

  public static long getId() {
    return id.get();
  }

  public static void setId(long id) {
    CurrencyViewModel.id.set(id);
  }

  public static LongProperty idProperty() {
    return id;
  }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    CurrencyViewModel.name.set(name);
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static String getSymbol() {
    return symbol.get();
  }

  public static void setSymbol(String symbol) {
    CurrencyViewModel.symbol.set(symbol);
  }

  public static StringProperty symbolProperty() {
    return symbol;
  }

  public static String getCode() {
    return code.get();
  }

  public static void setCode(String code) {
    CurrencyViewModel.code.set(code);
  }

  public static StringProperty codeProperty() {
    return code;
  }

  public static void saveCurrency() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Currency, Long> currencyDao =
                DaoManager.createDao(connectionSource, Currency.class);

            Currency currency = new Currency(getCode(), getName(), getSymbol());
            currencyDao.create(currency);

            clearCurrencyData();
            getCurrencies();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  public static void clearCurrencyData() {
    setId(0);
    setName("");
    setCode("");
    setSymbol("");
  }

  public static void getCurrencies() {
    Task<Void> task =
        new Task<>() {
          @Override
          protected Void call() throws SQLException {
            SQLiteConnection connection = SQLiteConnection.getInstance();
            ConnectionSource connectionSource = connection.getConnection();

            Dao<Currency, Long> currencyDao =
                DaoManager.createDao(connectionSource, Currency.class);
            Platform.runLater(
                () -> {
                  currenciesList.clear();

                  try {
                    currenciesList.addAll(currencyDao.queryForAll());
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

            Dao<Currency, Long> currencyDao =
                DaoManager.createDao(connectionSource, Currency.class);

            Currency currency = currencyDao.queryForId(index);
            setId(currency.getId());
            setSymbol(currency.getSymbol());
            setCode(currency.getCode());
            setName(currency.getName());
            getCurrencies();
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

            Dao<Currency, Long> currencyDao =
                DaoManager.createDao(connectionSource, Currency.class);

            Currency currency = currencyDao.queryForId(index);

            currency.setCode(getCode());
            currency.setName(getName());
            currency.setSymbol(getSymbol());

            currencyDao.update(currency);
            clearCurrencyData();
            getCurrencies();
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

            Dao<Currency, Long> currencyDao =
                DaoManager.createDao(connectionSource, Currency.class);

            currencyDao.deleteById(index);
            getCurrencies();
            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }
}
