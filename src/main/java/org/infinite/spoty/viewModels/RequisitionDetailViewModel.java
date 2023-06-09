package org.infinite.spoty.viewModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.RequisitionDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.models.RequisitionMaster;

public class RequisitionDetailViewModel {
    public static final ObservableList<RequisitionDetail> requisitionDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<RequisitionDetail> requisitionDetailsTempList = FXCollections.observableArrayList();
    private static final StringProperty id = new SimpleStringProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<RequisitionMaster> requisition = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty description = new SimpleStringProperty();

    public static String getId() {
        return id.get();
    }

    public static void setId(String id) {
        RequisitionDetailViewModel.id.set(id);
    }

    public static StringProperty idProperty() {
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
        setId("");
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
        requisitionDetailsList.addAll(RequisitionDetailDao.fetchRequisitionDetails());
        return requisitionDetailsList;
    }

    public static void updateRequisitionDetail(int index) {
        RequisitionDetail requisitionDetail = new RequisitionDetail(getProduct(),
                getRequisition(),
                Integer.parseInt(getQuantity()),
                getDescription());
        requisitionDetailsTempList.remove(index);
        requisitionDetailsTempList.add(index, requisitionDetail);
        resetProperties();
    }

    public static void getItem(int requisitionDetailID) {
        RequisitionDetail requisitionDetail = RequisitionDetailDao.findRequisitionDetail(requisitionDetailID);
        setId(String.valueOf(requisitionDetail.getId()));
        setProduct(requisitionDetail.getProductDetail());
        setQuantity(String.valueOf(requisitionDetail.getQuantity()));
        setDescription(requisitionDetail.getDescription());
        getRequisitionDetails();
    }

    public static void getItem(RequisitionDetail requisitionDetail, int index) {
        setId("index:" + index + ";");
        setProduct(requisitionDetail.getProductDetail());
        setQuantity(String.valueOf(requisitionDetail.getQuantity()));
        setDescription(requisitionDetail.getDescription());
    }

    public static void updateItem(int requisitionDetailID) {
        RequisitionDetail requisitionDetail = new RequisitionDetail(getProduct(), getRequisition(),
                Integer.parseInt(getQuantity()), getDescription());
        RequisitionDetailDao.updateRequisitionDetail(requisitionDetail, requisitionDetailID);
        getRequisitionDetails();
    }

    public static void removeRequisitionDetail(int index) {
        requisitionDetailsTempList.remove(index);
    }
}
