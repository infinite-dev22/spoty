package org.infinite.spoty.viewModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.AdjustmentDetailDao;
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.models.ProductDetail;

public class AdjustmentDetailViewModel {
    public static final ObservableList<AdjustmentDetail> adjustmentDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<AdjustmentDetail> adjustmentDetailsTempList = FXCollections.observableArrayList();
    private static final StringProperty id = new SimpleStringProperty("");
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<AdjustmentMaster> adjustment = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty adjustmentType = new SimpleStringProperty("");

    public static int getId() {
        return Integer.parseInt(id.get());
    }

    public static void setId(String id) {
        AdjustmentDetailViewModel.id.set(id);
    }

    public static StringProperty idProperty() {
        return id;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        AdjustmentDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static AdjustmentMaster getAdjustment() {
        return adjustment.get();
    }

    public static void setAdjustment(AdjustmentMaster adjustment) {
        AdjustmentDetailViewModel.adjustment.set(adjustment);
    }

    public static ObjectProperty<AdjustmentMaster> adjustmentProperty() {
        return adjustment;
    }

    public static int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        AdjustmentDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getAdjustmentType() {
        return adjustmentType.get();
    }

    public static void setAdjustmentType(String adjustmentType) {
        AdjustmentDetailViewModel.adjustmentType.set(adjustmentType);
    }

    public static StringProperty adjustmentTypeProperty() {
        return adjustmentType;
    }

    public static void resetProperties() {
        setId("");
        setProduct(null);
        setAdjustment(null);
        setAdjustmentType("");
        setQuantity("");
    }

    public static void addAdjustmentDetails() {
        AdjustmentDetail adjustmentDetail = new AdjustmentDetail(getProduct(),
                getQuantity(),
                getAdjustmentType());
        adjustmentDetailsTempList.add(adjustmentDetail);
        resetProperties();
    }

    public static void updateAdjustmentDetail(int index) {
        AdjustmentDetail adjustmentDetail = new AdjustmentDetail(getProduct(),
                getQuantity(),
                getAdjustmentType());
        adjustmentDetailsTempList.remove(index);
        adjustmentDetailsTempList.add(index, adjustmentDetail);
        resetProperties();
    }

    public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
        adjustmentDetailsList.clear();
        adjustmentDetailsList.addAll(AdjustmentDetailDao.getAdjustmentDetail());
        return adjustmentDetailsList;
    }

    public static void getItem(int adjustmentDetailID) {
        AdjustmentDetail adjustmentDetail = AdjustmentDetailDao.findAdjustmentDetail(adjustmentDetailID);
        setId(String.valueOf(adjustmentDetail.getId()));
        setProduct(adjustmentDetail.getProductDetail());
        setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
        setAdjustmentType(adjustmentDetail.getAdjustmentType());
        getAdjustmentDetails();
    }

    public static void getItem(AdjustmentDetail adjustmentDetail, int index) {
        setId("index:" + index + ";");
        setProduct(adjustmentDetail.getProductDetail());
        setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
        setAdjustmentType(adjustmentDetail.getAdjustmentType());
    }

    public static void updateItem(int adjustmentDetailID) {
        AdjustmentDetail adjustmentDetail = new AdjustmentDetail(getProduct(), getQuantity(), getAdjustmentType());
        AdjustmentDetailDao.updateAdjustmentDetail(adjustmentDetail, adjustmentDetailID);
        getAdjustmentDetails();
    }

    public static void removeAdjustmentDetail(int index) {
        adjustmentDetailsTempList.remove(index);
    }
}
