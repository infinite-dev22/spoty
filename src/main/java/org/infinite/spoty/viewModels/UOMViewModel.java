package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.UnitOfMeasureDao;
import org.infinite.spoty.database.models.UnitOfMeasure;

public class UOMViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty shortName = new SimpleStringProperty("");
    private static final ObjectProperty<UnitOfMeasure> baseUnit = new SimpleObjectProperty<>(null);
    private static final StringProperty operator = new SimpleStringProperty("");
    private static final DoubleProperty operatorValue = new SimpleDoubleProperty(0);

    private static final ObservableList<UnitOfMeasure> uomList = FXCollections.observableArrayList();
    private static final ObservableList<UnitOfMeasure> uomNameList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        UOMViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
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
        return operatorValue.get();
    }

    public static void setOperatorValue(double operatorValue) {
        UOMViewModel.operatorValue.set(operatorValue);
    }

    public static DoubleProperty operatorValueProperty() {
        return operatorValue;
    }

    public static void saveUOM() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure(getName(), getShortName(), getBaseUnit(), getOperator(), getOperatorValue());
        UnitOfMeasureDao.saveUnitOfMeasure(unitOfMeasure);
        uomList.clear();
        resetUOMProperties();
        getItems();
    }

    public static void resetUOMProperties() {
        setId(0);
        setName("");
        setShortName("");
        setBaseUnit(null);
        setOperator("");
        setOperatorValue(0);
    }

    public static ObservableList<UnitOfMeasure> getItems() {
        uomList.addAll(UnitOfMeasureDao.getUnitsOfMeasure());
        return uomList;
    }

//    public static ObservableList<UnitOfMeasure> getItemNames() {
//        uomNameList.addAll(UnitOfMeasureDao.getUnitsOfMeasure());
//        return uomNameList;
//    }
}
