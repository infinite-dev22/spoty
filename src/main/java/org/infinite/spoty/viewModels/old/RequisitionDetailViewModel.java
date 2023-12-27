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

package org.infinite.spoty.viewModels.old;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.daos.Product;
import org.infinite.spoty.data_source.daos.requisitions.RequisitionDetail;
import org.infinite.spoty.data_source.daos.requisitions.RequisitionMaster;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;
import static org.infinite.spoty.values.SharedResources.setTempId;

public class RequisitionDetailViewModel {
    @Getter
    public static final ObservableList<RequisitionDetail> requisitionDetailList =
            FXCollections.observableArrayList();
    private static final ListProperty<RequisitionDetail> requisitionDetails =
            new SimpleListProperty<>(requisitionDetailList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<RequisitionMaster> requisition = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty description = new SimpleStringProperty();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        RequisitionDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        RequisitionDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
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

    public static ObservableList<RequisitionDetail> getRequisitionDetails() {
        return requisitionDetails.get();
    }

    public static void setRequisitionDetails(ObservableList<RequisitionDetail> requisitionDetails) {
        RequisitionDetailViewModel.requisitionDetails.set(requisitionDetails);
    }

    public static ListProperty<RequisitionDetail> requisitionDetailsProperty() {
        return requisitionDetails;
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
//        RequisitionDetail requisitionDetail =
//                new RequisitionDetail(
//                        getProduct(),
//                        getRequisition(),
//                        Long.parseLong(getQuantity()),
//                        getDescription());
//
//        Platform.runLater(() -> requisitionDetailList.add(requisitionDetail));

        resetProperties();
    }

    public static void saveRequisitionDetails() throws Exception {
//        Dao<RequisitionDetail, Long> requisitionDetailDao =
//                DaoManager.createDao(connectionSource, RequisitionDetail.class);
//
//        requisitionDetailDao.create(requisitionDetailList);

        requisitionDetailList.clear();
    }

    public static void getAllRequisitionDetails() throws Exception {
//        Dao<RequisitionDetail, Long> requisitionDetailDao =
//                DaoManager.createDao(connectionSource, RequisitionDetail.class);
//
//        requisitionDetailList.clear();
//        requisitionDetailList.addAll(requisitionDetailDao.queryForAll());
    }

    public static void updateRequisitionDetail(long index) throws Exception {
//        Dao<RequisitionDetail, Long> requisitionDetailDao =
//                DaoManager.createDao(connectionSource, RequisitionDetail.class);
//
//        RequisitionDetail requisitionDetail = requisitionDetailDao.queryForId(index);
//        requisitionDetail.setProduct(getProduct());
//        requisitionDetail.setQuantity(Long.parseLong(getQuantity()));
//        requisitionDetail.setDescription(getDescription());
//
//        Platform.runLater(
//                () -> {
//                    requisitionDetailList.remove(getTempId());
//                    requisitionDetailList.add(getTempId(), requisitionDetail);
//                });

        resetProperties();
    }

    public static void getItem(long index, int tempIndex) throws Exception {
//        Dao<RequisitionDetail, Long> requisitionDetailDao =
//                DaoManager.createDao(connectionSource, RequisitionDetail.class);
//
//        RequisitionDetail requisitionDetail = requisitionDetailDao.queryForId(index);
//
//        setTempId(tempIndex);
//        setId(requisitionDetail.getId());
//        setProduct(requisitionDetail.getProduct());
//        setQuantity(String.valueOf(requisitionDetail.getQuantity()));
//        setDescription(requisitionDetail.getDescription());
    }

    public static void updateItem(long index) throws Exception {
//        Dao<RequisitionDetail, Long> requisitionDetailDao =
//                DaoManager.createDao(connectionSource, RequisitionDetail.class);
//
//        RequisitionDetail requisitionDetail = requisitionDetailDao.queryForId(index);
//        requisitionDetail.setProduct(getProduct());
//        requisitionDetail.setQuantity(Long.parseLong(getQuantity()));
//        requisitionDetail.setDescription(getDescription());
//
//        requisitionDetailDao.update(requisitionDetail);

        getAllRequisitionDetails();
    }

    public static void updateRequisitionDetails() throws Exception {
//        Dao<RequisitionDetail, Long> requisitionDetailDao =
//                DaoManager.createDao(connectionSource, RequisitionDetail.class);
//
//        requisitionDetailList.forEach(
//                requisitionDetail -> {
//                    try {
//                        requisitionDetailDao.update(requisitionDetail);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
//                    }
//                });

        getAllRequisitionDetails();
    }

    public static void removeRequisitionDetail(long index, int tempIndex) {
        Platform.runLater(() -> requisitionDetailList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteRequisitionDetails(@NotNull LinkedList<Long> indexes) {
//        indexes.forEach(
//                index -> {
//                    try {
//                        Dao<RequisitionDetail, Long> requisitionDetailDao =
//                                DaoManager.createDao(connectionSource, RequisitionDetail.class);
//
//                        requisitionDetailDao.deleteById(index);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
//                    }
//                });
    }
}
