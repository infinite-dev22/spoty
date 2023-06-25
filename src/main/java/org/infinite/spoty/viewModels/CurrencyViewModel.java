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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.CurrencyDao;
import org.infinite.spoty.database.models.Currency;

public class CurrencyViewModel {
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final StringProperty code = new SimpleStringProperty("");
  private static final StringProperty name = new SimpleStringProperty("");
  private static final StringProperty symbol = new SimpleStringProperty("");
  public static ObservableList<Currency> currenciesList = FXCollections.observableArrayList();

  public static Integer getId() {
    return id.get();
  }

  public static void setId(Integer id) {
    CurrencyViewModel.id.set(id);
  }

  public static IntegerProperty idProperty() {
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
    Currency currency = new Currency(getCode(), getName(), getSymbol());
    CurrencyDao.saveCurrency(currency);
    currenciesList.clear();
    clearCurrencyData();
    getCurrencies();
  }

  public static void clearCurrencyData() {
    setId(0);
    setName("");
    setCode("");
    setSymbol("");
  }

  public static ObservableList<Currency> getCurrencies() {
    currenciesList.clear();
    currenciesList.addAll(CurrencyDao.fetchCurrencies());
    return currenciesList;
  }

  public static void getItem(int currencyID) {
    Currency currency = CurrencyDao.findCurrency(currencyID);
    setId(currency.getId());
    setSymbol(currency.getSymbol());
    setCode(currency.getCode());
    setName(currency.getName());
    getCurrencies();
  }

  public static void updateItem(int currencyID) {
    Currency currency = new Currency(getCode(), getName(), getSymbol());
    CurrencyDao.updateCurrency(currency, currencyID);
    clearCurrencyData();
    getCurrencies();
  }
}
