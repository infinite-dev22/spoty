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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.UnitOfMeasure;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.UnitsOfMeasureRepositoryImpl;
import org.infinite.spoty.utils.ParameterlessConsumer;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class UOMViewModel {
    public static final ObservableList<UnitOfMeasure> uomsList = FXCollections.observableArrayList();
    public static final ObservableList<UnitOfMeasure> uomComboBoxList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<UnitOfMeasure> unitsOfMeasure =
            new SimpleListProperty<>(uomsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty shortName = new SimpleStringProperty("");
    private static final ObjectProperty<UnitOfMeasure> baseUnit = new SimpleObjectProperty<>();
    private static final StringProperty operator = new SimpleStringProperty("");
    private static final StringProperty operatorValue = new SimpleStringProperty("");
    private static final UnitsOfMeasureRepositoryImpl uomRepository = new UnitsOfMeasureRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        UOMViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        UOMViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getShortName() {
        return shortName.get();
    }

    public static void setShortName(String shortName) {
        UOMViewModel.shortName.set(shortName);
    }

    public static StringProperty shortNameProperty() {
        return shortName;
    }

    public static UnitOfMeasure getBaseUnit() {
        return baseUnit.get();
    }

    public static void setBaseUnit(UnitOfMeasure baseUnit) {
        UOMViewModel.baseUnit.set(baseUnit);
    }

    public static ObjectProperty<UnitOfMeasure> baseUnitProperty() {
        return baseUnit;
    }

    public static String getOperator() {
        return operator.get();
    }

    public static void setOperator(String operator) {
        UOMViewModel.operator.set(operator);
    }

    public static StringProperty operatorProperty() {
        return operator;
    }

    public static double getOperatorValue() {
        return Double.parseDouble((!operatorValue.get().isEmpty()) ? operatorValue.get() : "0");
    }

    public static void setOperatorValue(String operatorValue) {
        UOMViewModel.operatorValue.set(operatorValue);
    }

    public static StringProperty operatorValueProperty() {
        return operatorValue;
    }

    public static ObservableList<UnitOfMeasure> getUnitsOfMeasure() {
        return unitsOfMeasure.get();
    }

    public static void setUnitsOfMeasure(ObservableList<UnitOfMeasure> unitsOfMeasure) {
        UOMViewModel.unitsOfMeasure.set(unitsOfMeasure);
    }

    public static ListProperty<UnitOfMeasure> unitsOfMeasureProperty() {
        return unitsOfMeasure;
    }

    public static void saveUOM(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var uom = UnitOfMeasure.builder()
                .name(getName())
                .shortName(getShortName())
                .baseUnit(getBaseUnit())
                .operator(getOperator())
                .operatorValue(getOperatorValue())
                .build();

        var task = uomRepository.post(uom);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void resetUOMProperties() {
        setId(0);
        setName("");
        setShortName("");
        setBaseUnit(null);
        setOperator("");
        setOperatorValue("");
    }

    public static void getItems(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = uomRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<UnitOfMeasure>>() {
                }.getType();
                ArrayList<UnitOfMeasure> uomList = gson.fromJson(task.get().body(), listType);

                uomsList.clear();
                uomsList.addAll(uomList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, UOMViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = uomRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var uom = gson.fromJson(task.get().body(), UnitOfMeasure.class);

                setId(uom.getId());
                setName(uom.getName());
                setShortName(uom.getShortName());
                setBaseUnit(uom.getBaseUnit());
                setOperator(uom.getOperator());
                setOperatorValue(
                        String.valueOf(uom.getOperatorValue() == 0 ? "" : uom.getOperatorValue()));
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
        var task = uomRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<UnitOfMeasure>>() {
                }.getType();
                ArrayList<UnitOfMeasure> uomList = gson.fromJson(task.get()
                        .body(), listType);

                uomsList.clear();
                uomsList.addAll(uomList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, UOMViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var uom = UnitOfMeasure.builder()
                .id(getId())
                .name(getName())
                .shortName(getShortName())
                .baseUnit(getBaseUnit())
                .operator(getOperator())
                .operatorValue(getOperatorValue())
                .build();

        var task = uomRepository.put(uom);
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

        var task = uomRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
