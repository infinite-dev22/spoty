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
import org.infinite.spoty.database.dao.SaleReturnDetailDao;
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
        SaleReturnDetailsList.addAll(SaleReturnDetailDao.fetchSaleReturnDetails());
        return SaleReturnDetailsList;
    }
}
