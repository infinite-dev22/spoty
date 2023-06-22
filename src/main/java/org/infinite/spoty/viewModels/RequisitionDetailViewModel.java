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
import org.infinite.spoty.database.dao.RequisitionDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.models.RequisitionMaster;

import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.*;
import static org.infinite.spoty.values.SharedResources.getTempId;

public class RequisitionDetailViewModel {
    public static final ObservableList<RequisitionDetail> requisitionDetailList = FXCollections.observableArrayList();
    public static final ObservableList<RequisitionDetail> requisitionDetailTempList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<RequisitionMaster> requisition = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty description = new SimpleStringProperty();

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
        setTempId(-1);
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
        requisitionDetailTempList.add(requisitionDetail);
        resetProperties();
    }

    public static ObservableList<RequisitionDetail> getRequisitionDetails() {
        requisitionDetailList.clear();
        requisitionDetailList.addAll(RequisitionDetailDao.fetchRequisitionDetails());
        return requisitionDetailList;
    }

    public static void updateRequisitionDetail(int index) {
        RequisitionDetail requisitionDetail = RequisitionDetailDao.findRequisitionDetail(index);
        requisitionDetail.setProductDetail(getProduct());
        requisitionDetail.setQuantity(Integer.parseInt(getQuantity()));
        requisitionDetail.setDescription(getDescription());
        requisitionDetailTempList.remove((int) getTempId());
        requisitionDetailTempList.add(getTempId(), requisitionDetail);
        resetProperties();
    }

    public static void getItem(int index, int tempIndex) {
        RequisitionDetail requisitionDetail = RequisitionDetailDao.findRequisitionDetail(index);
        setTempId(tempIndex);
        setId(requisitionDetail.getId());
        setProduct(requisitionDetail.getProductDetail());
        setQuantity(String.valueOf(requisitionDetail.getQuantity()));
        setDescription(requisitionDetail.getDescription());
    }

    public static void updateItem(int index) {
        RequisitionDetail requisitionDetail = RequisitionDetailDao.findRequisitionDetail(index);
        requisitionDetail.setProductDetail(getProduct());
        requisitionDetail.setQuantity(Integer.parseInt(getQuantity()));
        requisitionDetail.setDescription(getDescription());
        RequisitionDetailDao.updateRequisitionDetail(requisitionDetail, index);
        getRequisitionDetails();
    }

    public static void removeRequisitionDetail(int index, int tempIndex) {
        requisitionDetailTempList.remove(tempIndex);
        PENDING_DELETES.add(index);
    }

    public static void deleteRequisitionDetails(LinkedList<Integer> indexes) {
        indexes.forEach(RequisitionDetailDao::deleteRequisitionDetail);
    }
}
