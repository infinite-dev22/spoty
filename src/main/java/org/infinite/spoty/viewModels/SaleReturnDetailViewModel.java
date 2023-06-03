package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.SaleReturnDetail;
import org.infinite.spoty.database.models.SaleReturnMaster;

public class SaleReturnDetailViewModel {
    public static final ObservableList<SaleReturnDetail> SaleReturnDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<SaleReturnDetail> SaleReturnDetailsTempList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<SaleReturnMaster> SaleReturn = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty location = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        SaleReturnDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        SaleReturnDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static SaleReturnMaster getSaleReturn() {
        return SaleReturn.get();
    }

    public static void setSaleReturn(SaleReturnMaster SaleReturn) {
        SaleReturnDetailViewModel.SaleReturn.set(SaleReturn);
    }

    public static ObjectProperty<SaleReturnMaster> SaleReturnProperty() {
        return SaleReturn;
    }

    public static int getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        SaleReturnDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        SaleReturnDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        SaleReturnDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getLocation() {
        return location.get();
    }

    public static void setLocation(String location) {
        SaleReturnDetailViewModel.location.set(location);
    }

    public static StringProperty locationProperty() {
        return location;
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setLocation("");
    }

//    public static void addSaleReturnDetails() {
//        SaleReturnDetail SaleReturnDetail = new SaleReturnDetail(getProduct(),
//                getQuantity(),
//                getSerial(),
//                getDescription(),
//                getLocation());
//        SaleReturnDetailsTempList.add(SaleReturnDetail);
//        resetProperties();
//    }

    public static ObservableList<SaleReturnDetail> getSaleReturnDetails() {
        SaleReturnDetailsList.clear();
        SaleReturnDetailsList.addAll(SaleReturnDetailViewModel.getSaleReturnDetails());
        return SaleReturnDetailsList;
    }
}
