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

package org.infinite.spoty.viewModels.old;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.daos.Currency;


public class CurrencyViewModel {
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty code = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty symbol = new SimpleStringProperty("");
    private static final ObjectProperty<Currency> currency = new SimpleObjectProperty<>();
    public static ObservableList<Currency> currenciesList = FXCollections.observableArrayList();
    public static final ListProperty<Currency> currencies = new SimpleListProperty<>(currenciesList);
    public static ObservableList<Currency> currenciesComboBoxList =
            FXCollections.observableArrayList();

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

    public static ObservableList<Currency> getCurrencies() {
        return currencies.get();
    }

    public static void setCurrencies(ObservableList<Currency> currencies) {
        CurrencyViewModel.currencies.set(currencies);
    }

    public static ListProperty<Currency> currencyProperty() {
        return currencies;
    }

    public static Currency getCurrency() {
        return currency.get();
    }

    public static void setCurrency(Currency currency) {
        CurrencyViewModel.currency.set(currency);
    }

    public static void saveCurrency() throws Exception {
//        Dao<Currency, Long> currencyDao =
//                DaoManager.createDao(connectionSource, Currency.class);
//
//        Currency currency = new Currency(getCode(), getName(), getSymbol());
//        currencyDao.create(currency);

        clearCurrencyData();
        getAllCurrencies();
    }

    public static void clearCurrencyData() {
        setId(0);
        setName("");
        setCode("");
        setSymbol("");
    }

    public static void getAllCurrencies() throws Exception {
//        Dao<Currency, Long> currencyDao =
//                DaoManager.createDao(connectionSource, Currency.class);
//        Platform.runLater(
//                () -> {
//                    currenciesList.clear();
//
//                    try {
//                        currenciesList.addAll(currencyDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, CurrencyViewModel.class);
//                    }
//                });
    }

    public static void getItem(long index) throws Exception {
//        Dao<Currency, Long> currencyDao =
//                DaoManager.createDao(connectionSource, Currency.class);
//
//        Currency currency = currencyDao.queryForId(index);
//        setCurrency(currency);
//        setId(currency.getId());
//        setSymbol(currency.getSymbol());
//        setCode(currency.getCode());
//        setName(currency.getName());
        getAllCurrencies();
    }

    public static void updateItem(long index) throws Exception {
//        SQLiteConnection connection = SQLiteConnection.getInstance();
//        ConnectionSource connectionSource = connection.getConnection();
//
//        Dao<Currency, Long> currencyDao =
//                DaoManager.createDao(connectionSource, Currency.class);
//
//        Currency currency = currencyDao.queryForId(index);
//
//        currency.setCode(getCode());
//        currency.setName(getName());
//        currency.setSymbol(getSymbol());
//
//        currencyDao.update(currency);

        clearCurrencyData();
        getAllCurrencies();
    }

    public static void deleteItem(long index) throws Exception {
//        SQLiteConnection connection = SQLiteConnection.getInstance();
//        ConnectionSource connectionSource = connection.getConnection();
//
//        Dao<Currency, Long> currencyDao =
//                DaoManager.createDao(connectionSource, Currency.class);
//
//        currencyDao.deleteById(index);
        getAllCurrencies();
    }
}
