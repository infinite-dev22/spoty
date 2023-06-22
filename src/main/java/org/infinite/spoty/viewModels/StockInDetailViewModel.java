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
import org.infinite.spoty.database.dao.StocKInDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.StockInDetail;
import org.infinite.spoty.database.models.StockInMaster;

import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.*;

public class StockInDetailViewModel {
    public static final ObservableList<StockInDetail> stockInDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<StockInDetail> stockInDetailsTempList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<StockInMaster> stockIn = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty location = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        StockInDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
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
        setId(0);
        setTempId(-1);
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
        StockInDetail stockInDetail = StocKInDetailDao.findStockInDetail(index);
        stockInDetail.setProduct(getProduct());
        stockInDetail.setQuantity(getQuantity());
        stockInDetail.setSerialNo(getSerial());
        stockInDetail.setDescription(getDescription());
        stockInDetail.setLocation(getLocation());
        stockInDetailsTempList.remove((int) getTempId());
        stockInDetailsTempList.add(getTempId(), stockInDetail);
        resetProperties();
    }

    public static void getItem(int index, int tempIndex) {
        StockInDetail stockInDetail = StocKInDetailDao.findStockInDetail(index);
        setTempId(tempIndex);
        setId(stockInDetail.getId());
        setProduct(stockInDetail.getProduct());
        setQuantity(String.valueOf(stockInDetail.getQuantity()));
        setSerial(stockInDetail.getSerialNo());
        setDescription(stockInDetail.getDescription());
        setLocation(stockInDetail.getLocation());
    }

    public static void updateItem(int index) {
        StockInDetail stockInDetail = StocKInDetailDao.findStockInDetail(index);
        stockInDetail.setProduct(getProduct());
        stockInDetail.setQuantity(getQuantity());
        stockInDetail.setSerialNo(getSerial());
        stockInDetail.setDescription(getDescription());
        stockInDetail.setLocation(getLocation());
        StocKInDetailDao.updateStockInDetail(stockInDetail, index);
        getStockInDetails();
    }

    public static void removeStockInDetail(int index, int tempIndex) {
        stockInDetailsTempList.remove(tempIndex);
        PENDING_DELETES.add(index);
    }

    public static void deleteStockInDetails(LinkedList<Integer> indexes) {
        indexes.forEach(StocKInDetailDao::deleteStockInDetail);
    }
}
