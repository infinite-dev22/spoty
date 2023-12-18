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

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.UnitOfMeasure;


public class UOMViewModel {
    public static final ObservableList<UnitOfMeasure> uomList = FXCollections.observableArrayList();
    public static final ObservableList<UnitOfMeasure> uomComboBoxList =
            FXCollections.observableArrayList();
    private static final ListProperty<UnitOfMeasure> unitsOfMeasure =
            new SimpleListProperty<>(uomList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty shortName = new SimpleStringProperty("");
    private static final ObjectProperty<UnitOfMeasure> baseUnit = new SimpleObjectProperty<>();
    private static final StringProperty operator = new SimpleStringProperty("");
    private static final StringProperty operatorValue = new SimpleStringProperty("");

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

    public static void saveUOM() throws Exception {
//        Dao<UnitOfMeasure, Long> unitOfMeasureDao =
//                DaoManager.createDao(connectionSource, UnitOfMeasure.class);
//
//        UnitOfMeasure unitOfMeasure =
//                new UnitOfMeasure(
//                        getName(), getShortName(), getBaseUnit(), getOperator(), getOperatorValue());
//
//        unitOfMeasureDao.create(unitOfMeasure);

        resetUOMProperties();
        getItems();
    }

    public static void resetUOMProperties() {
        setId(0);
        setName("");
        setShortName("");
        setBaseUnit(null);
        setOperator("");
        setOperatorValue("");
    }

    public static void getItems() throws Exception {
//        Dao<UnitOfMeasure, Long> unitOfMeasureDao =
//                DaoManager.createDao(connectionSource, UnitOfMeasure.class);
//
//        Platform.runLater(
//                () -> {
//                    uomList.clear();
//
//                    try {
//                        uomList.addAll(unitOfMeasureDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, UOMViewModel.class);
//                    }
//                });
    }

    public static void getItem(long index) throws Exception {
//        Dao<UnitOfMeasure, Long> unitOfMeasureDao =
//                DaoManager.createDao(connectionSource, UnitOfMeasure.class);
//
//        UnitOfMeasure uom = unitOfMeasureDao.queryForId(index);
//
//        setId(uom.getId());
//        setName(uom.getName());
//        setShortName(uom.getShortName());
//        setBaseUnit(uom.getBaseUnit());
//        setOperator(uom.getOperator());
//        setOperatorValue(
//                String.valueOf(uom.getOperatorValue() == 0 ? "" : uom.getOperatorValue()));
    }

    public static void updateItem(long index) throws Exception {
//        Dao<UnitOfMeasure, Long> unitOfMeasureDao =
//                DaoManager.createDao(connectionSource, UnitOfMeasure.class);
//
//        UnitOfMeasure uom = unitOfMeasureDao.queryForId(index);
//
//        uom.setName(getName());
//        uom.setShortName(getShortName());
//        uom.setBaseUnit(getBaseUnit());
//        uom.setOperator(getOperator());
//        uom.setOperatorValue(getOperatorValue());
//
//        unitOfMeasureDao.update(uom);

        resetUOMProperties();
        getItems();
    }

    public static void deleteItem(long index) throws Exception {
//        Dao<UnitOfMeasure, Long> unitOfMeasureDao =
//                DaoManager.createDao(connectionSource, UnitOfMeasure.class);
//
//        unitOfMeasureDao.deleteById(index);

        getItems();
    }
}
