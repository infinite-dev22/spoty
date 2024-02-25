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

package inc.normad.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.normad.spoty.network_bridge.dtos.Currency;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.CurrenciesRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class CurrencyViewModel {
    public static final CurrenciesRepositoryImpl currenciesRepository = new CurrenciesRepositoryImpl();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
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

    public static void saveCurrency(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var currency = Currency.builder()
                .code(getCode())
                .name(getName())
                .symbol(getSymbol())
                .build();

        var task = currenciesRepository.post(currency);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void clearCurrencyData() {
        setId(0);
        setName("");
        setCode("");
        setSymbol("");
    }

    public static void getAllCurrencies(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = currenciesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Currency>>() {
                }.getType();
                ArrayList<Currency> currencyList = gson.fromJson(task.get().body(), listType);
                currenciesList.clear();
                currenciesList.addAll(currencyList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, CurrencyViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = currenciesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var currency = gson.fromJson(task.get().body(), Currency.class);

                setCurrency(currency);
                setId(currency.getId());
                setSymbol(currency.getSymbol());
                setCode(currency.getCode());
                setName(currency.getName());
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = currenciesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Currency>>() {
                }.getType();
                ArrayList<Currency> currencyList = gson.fromJson(task.get()
                        .body(), listType);

                currenciesList.clear();
                currenciesList.addAll(currencyList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, CurrencyViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var currency = Currency.builder()
                .id(getId())
                .code(getCode())
                .name(getName())
                .symbol(getSymbol())
                .build();

        var task = currenciesRepository.put(currency);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = currenciesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
