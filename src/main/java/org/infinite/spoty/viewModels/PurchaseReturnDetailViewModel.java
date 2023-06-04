package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.PurchaseReturnDetail;
import org.infinite.spoty.database.models.PurchaseReturnMaster;

public class PurchaseReturnDetailViewModel {
    public static final ObservableList<PurchaseReturnDetail> purchaseReturnDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<PurchaseReturnDetail> purchaseReturnDetailsTempList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<PurchaseReturnMaster> purchaseReturn = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty location = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        PurchaseReturnDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        PurchaseReturnDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static PurchaseReturnMaster getPurchaseReturn() {
        return purchaseReturn.get();
    }

    public static void setPurchaseReturn(PurchaseReturnMaster purchaseReturn) {
        PurchaseReturnDetailViewModel.purchaseReturn.set(purchaseReturn);
    }

    public static ObjectProperty<PurchaseReturnMaster> purchaseReturnProperty() {
        return purchaseReturn;
    }

    public static int getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        PurchaseReturnDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        PurchaseReturnDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        PurchaseReturnDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getLocation() {
        return location.get();
    }

    public static void setLocation(String location) {
        PurchaseReturnDetailViewModel.location.set(location);
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

//    public static void addPurchaseReturnDetails() {
//        PurchaseReturnDetail purchaseReturnDetail = new PurchaseReturnDetail(getProduct(),
//                getQuantity(),
//                getSerial(),
//                getDescription(),
//                getLocation());
//        PurchaseReturnDetailsTempList.add(purchaseReturnDetail);
//        resetProperties();
//    }

    public static ObservableList<PurchaseReturnDetail> getPurchaseReturnDetails() {
        purchaseReturnDetailsList.clear();
        purchaseReturnDetailsList.addAll(PurchaseReturnDetailViewModel.getPurchaseReturnDetails());
        return purchaseReturnDetailsList;
    }
}