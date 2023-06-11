package org.infinite.spoty.viewModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.StocKInDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.StockInDetail;
import org.infinite.spoty.database.models.StockInMaster;

public class StockInDetailViewModel {
    public static final ObservableList<StockInDetail> stockInDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<StockInDetail> stockInDetailsTempList = FXCollections.observableArrayList();
    private static final StringProperty id = new SimpleStringProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<StockInMaster> stockIn = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty location = new SimpleStringProperty("");

    public static String getId() {
        return id.get();
    }

    public static void setId(String id) {
        StockInDetailViewModel.id.set(id);
    }

    public static StringProperty idProperty() {
        return id;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        StockInDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static StockInMaster getStockIn() {
        return stockIn.get();
    }

    public static void setStockIn(StockInMaster stockIn) {
        StockInDetailViewModel.stockIn.set(stockIn);
    }

    public static ObjectProperty<StockInMaster> stockInProperty() {
        return stockIn;
    }

    public static int getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        StockInDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        StockInDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        StockInDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getLocation() {
        return location.get();
    }

    public static void setLocation(String location) {
        StockInDetailViewModel.location.set(location);
    }

    public static StringProperty locationProperty() {
        return location;
    }

    public static void resetProperties() {
        setId("");
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setLocation("");
    }

    public static void addStockInDetails() {
        StockInDetail stockInDetail = new StockInDetail(getProduct(),
                getQuantity(),
                getSerial(),
                getDescription(),
                getLocation());
        stockInDetailsTempList.add(stockInDetail);
        resetProperties();
    }

    public static ObservableList<StockInDetail> getStockInDetails() {
        stockInDetailsList.clear();
        stockInDetailsList.addAll(StocKInDetailDao.fetchStockInDetails());
        return stockInDetailsList;
    }

    public static void updateStockInDetail(int index) {
        StockInDetail stockInDetail = new StockInDetail(getProduct(),
                getQuantity(),
                getSerial(),
                getDescription(),
                getLocation());
        stockInDetailsTempList.remove(index);
        stockInDetailsTempList.add(index, stockInDetail);
        resetProperties();
    }

    public static void getItem(int stockInDetailID) {
        StockInDetail stockInDetail = StocKInDetailDao.findStockInDetail(stockInDetailID);
        setId(String.valueOf(stockInDetail.getId()));
        setProduct(stockInDetail.getProduct());
        setQuantity(String.valueOf(stockInDetail.getQuantity()));
        setSerial(stockInDetail.getSerialNo());
        setDescription(stockInDetail.getDescription());
        setLocation(stockInDetail.getLocation());
        getStockInDetails();
    }

    public static void getItem(StockInDetail stockInDetail, int index) {
        setId("index:" + index + ";");
        setProduct(stockInDetail.getProduct());
        setQuantity(String.valueOf(stockInDetail.getQuantity()));
        setSerial(stockInDetail.getSerialNo());
        setDescription(stockInDetail.getDescription());
        setLocation(stockInDetail.getLocation());
    }

    public static void updateItem(int stockInDetailID) {
        StockInDetail stockInDetail = new StockInDetail(getProduct(),
                getQuantity(),
                getSerial(),
                getDescription(),
                getLocation());
        StocKInDetailDao.updateStockInDetail(stockInDetail, stockInDetailID);
        getStockInDetails();
    }

    public static void removeStockInDetail(int index) {
        stockInDetailsTempList.remove(index);
    }
}
