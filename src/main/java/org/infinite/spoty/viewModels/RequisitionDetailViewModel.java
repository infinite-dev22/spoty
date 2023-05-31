package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.models.RequisitionMaster;

public class RequisitionDetailViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<RequisitionMaster> requisition = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty description = new SimpleStringProperty();
    public static final ObservableList<RequisitionDetail> requisitionDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<RequisitionDetail> requisitionDetailsTempList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        RequisitionDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        RequisitionDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static RequisitionMaster getRequisition() {
        return requisition.get();
    }

    public static void setRequisition(RequisitionMaster requisition) {
        RequisitionDetailViewModel.requisition.set(requisition);
    }

    public static ObjectProperty<RequisitionMaster> requisitionProperty() {
        return requisition;
    }

    public static String getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(String quantity) {
        RequisitionDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        RequisitionDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setRequisition(null);
        setDescription("");
        setQuantity("");
    }

    public static void addRequisitionDetails() {
        RequisitionDetail requisitionDetail = new RequisitionDetail(getProduct(),
                getRequisition(),
                Integer.parseInt(getQuantity()),
                getDescription());
        requisitionDetailsTempList.add(requisitionDetail);
        resetProperties();
    }

    public static ObservableList<RequisitionDetail> getRequisitionDetails() {
        requisitionDetailsList.clear();
        requisitionDetailsList.addAll(RequisitionDetailViewModel.getRequisitionDetails());
        return requisitionDetailsList;
    }
}
