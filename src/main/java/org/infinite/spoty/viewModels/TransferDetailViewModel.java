package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.TransferDetail;
import org.infinite.spoty.database.models.TransferMaster;

public class TransferDetailViewModel {
    public static final ObservableList<TransferDetail> transferDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<TransferDetail> transferDetailsTempList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<TransferMaster> transfer = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty price = new SimpleStringProperty("");
    private static final StringProperty total = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        TransferDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        TransferDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static TransferMaster getTransfer() {
        return transfer.get();
    }

    public static void setTransfer(TransferMaster transfer) {
        TransferDetailViewModel.transfer.set(transfer);
    }

    public static ObjectProperty<TransferMaster> transferProperty() {
        return transfer;
    }

    public static int getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        TransferDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        TransferDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        TransferDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static double getPrice() {
        return Double.parseDouble(!price.get().isEmpty() ? price.get() : "0");
    }

    public static void setPrice(String price) {
        TransferDetailViewModel.price.set(price);
    }

    public static StringProperty priceProperty() {
        return price;
    }

    public static double getTotal() {
        return Double.parseDouble(!total.get().isEmpty() ? total.get() : "0");
    }

    public static void setTotal(String total) {
        TransferDetailViewModel.total.set(total);
    }

    public static StringProperty totalProperty() {
        return total;
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setPrice("");
        setTotal("");
    }

    public static void addTransferDetails() {
        TransferDetail transferDetail = new TransferDetail(getProduct(),
                getQuantity(),
                getSerial(),
                getDescription(),
                getPrice(),
                getTotal());
        transferDetailsTempList.add(transferDetail);
        resetProperties();
    }

    public static ObservableList<TransferDetail> getTransferDetails() {
        transferDetailsList.clear();
        transferDetailsList.addAll(TransferDetailViewModel.getTransferDetails());
        return transferDetailsList;
    }
}
