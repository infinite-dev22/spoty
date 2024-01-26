/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in AdjustmentDetailViewModel c is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of AdjustmentDetailViewModel c is prohibid.
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
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.adjustments.AdjustmentDetail;
import org.infinite.spoty.data_source.dtos.adjustments.AdjustmentMaster;
import org.infinite.spoty.data_source.dtos.adjustments.AdjustmentTransaction;
import org.infinite.spoty.utils.SpotyLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.*;

public class AdjustmentDetailViewModel {
    @Getter
    public static final ObservableList<AdjustmentDetail> adjustmentDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<AdjustmentDetail> adjustmentDetails =
            new SimpleListProperty<>(adjustmentDetailsList);
    private static final LongProperty id = new SimpleLongProperty();
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<AdjustmentMaster> adjustment = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty adjustmentType = new SimpleStringProperty();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        AdjustmentDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        AdjustmentDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
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

    public static long getQuantity() {
        return Long.parseLong(quantity.get());
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

    public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
        return adjustmentDetails.get();
    }

    public static void setAdjustmentDetails(ObservableList<AdjustmentDetail> adjustmentDetails) {
        AdjustmentDetailViewModel.adjustmentDetails.set(adjustmentDetails);
    }

    public static ListProperty<AdjustmentDetail> adjustmentDetailsProperty() {
        return adjustmentDetails;
    }

    public static void resetProperties() {
        setId(0);
        setTempId(-1);
        setProduct(null);
        setAdjustment(null);
        setAdjustmentType("");
        setQuantity("");
    }

    public static void addAdjustmentDetails() {
        var adjustmentDetail = AdjustmentDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .adjustmentType(getAdjustmentType())
                .build();

        Platform.runLater(
                () -> {
                    adjustmentDetailsList.add(adjustmentDetail);
                    resetProperties();
                });
    }

    public static void saveAdjustmentDetails() throws Exception {
        // TODO: Save adjustment detail list.
        setProductQuantity();

        Platform.runLater(adjustmentDetailsList::clear);
    }

    private static void setProductQuantity() {
        adjustmentDetailsList.forEach(
                adjustmentDetail -> {
                    long productQuantity = 0;

                    try {

                        if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("INCREMENT")) {
                            productQuantity =
                                    adjustmentDetail.getProduct().getQuantity() + adjustmentDetail.getQuantity();
                        } else if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("DECREMENT")) {
                            productQuantity =
                                    adjustmentDetail.getProduct().getQuantity() - adjustmentDetail.getQuantity();
                        }

                        createAdjustmentTransaction(adjustmentDetail);

                        ProductViewModel.setQuantity(productQuantity);

                        ProductViewModel.updateProductQuantity(adjustmentDetail.getProduct().getId());
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
                    }
                });
    }

    private static void updateProductQuantity() {
        adjustmentDetailsList.forEach(
                adjustmentDetail -> {
                    long productQuantity = 0;
                    long currentProductQuantity = adjustmentDetail.getProduct().getQuantity();

                    try {
                        AdjustmentTransaction adjustmentTransaction =
                                getAdjustmentTransaction(adjustmentDetail.getId());

                        long adjustQuantity = adjustmentTransaction.getAdjustQuantity();

                        if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("INCREMENT")) {
                            productQuantity =
                                    (currentProductQuantity - adjustQuantity) + adjustmentDetail.getQuantity();

                            adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
                        } else if (adjustmentDetail.getAdjustmentType().equalsIgnoreCase("DECREMENT")) {
                            productQuantity =
                                    (currentProductQuantity + adjustQuantity) - adjustmentDetail.getQuantity();

                            adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
                        }

                        ProductViewModel.setQuantity(productQuantity);
                        ProductViewModel.updateProductQuantity(adjustmentDetail.getProduct().getId());

                        updateAdjustmentTransaction(adjustmentDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
                    }
                });
    }

    public static void updateAdjustmentDetail(long index) {
        AdjustmentDetail adjustmentDetail = adjustmentDetailsList.get((int) index);
        adjustmentDetail.setProduct(getProduct());
        adjustmentDetail.setQuantity(getQuantity());
        adjustmentDetail.setAdjustmentType(getAdjustmentType());

        Platform.runLater(
                () -> {
                    adjustmentDetailsList.remove(getTempId());
                    adjustmentDetailsList.add(getTempId(), adjustmentDetail);

                    resetProperties();
                });
    }

    public static void getAllAdjustmentDetails() throws Exception {
//        Platform.runLater(
//                () -> {
//                    adjustmentDetailsList.clear();
//
//                    try {
//                         TODO: Fetch all adjustment details.
//                         adjustmentDetailsList.addAll(adjustmentDetailDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
//                    }
//                });
    }

    public static void getAdjustmentDetail(AdjustmentDetail adjustmentDetail) throws Exception {
        Platform.runLater(
                () -> {
                    setTempId(getAdjustmentDetails().indexOf(adjustmentDetail));
                    setProduct(adjustmentDetail.getProduct());
                    setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
                    setAdjustmentType(adjustmentDetail.getAdjustmentType());
                });
    }

    public static void updateAdjustmentDetails() throws Exception {
//        adjustmentDetailsList.forEach(
//                adjustmentDetail -> {
//                    try {
//                         TODO: Update adjustment details.
//                         adjustmentDetailDao.update(adjustmentDetail);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
//                    }
//                });

        updateProductQuantity();

        getAllAdjustmentDetails();
    }

    public static void removeAdjustmentDetail(long index, int tempIndex) {
        Platform.runLater(() -> adjustmentDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteAdjustmentDetails(@NotNull LinkedList<Long> indexes) {
//        indexes.forEach(
//                index -> {
//                    try {
//                         TODO: Delete adjustment details.
//                         adjustmentDetailDao.deleteById(index);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
//                    }
//                });
    }

    private static AdjustmentTransaction getAdjustmentTransaction(long adjustmentIndex)
            throws Exception {
//        PreparedQuery<AdjustmentTransaction> preparedQuery =
//                adjustmentTransactionDao
//                        .queryBuilder()
//                        .where()
//                        .eq("adjustment_detail_id", adjustmentIndex)
//                        .prepare();

        // TODO: Query for adjustment transaction by adjustment detail id.

//        return adjustmentTransactionDao.queryForFirst(preparedQuery);
        return new AdjustmentTransaction();
    }

    private static void createAdjustmentTransaction(@NotNull AdjustmentDetail adjustmentDetail)
            throws Exception {

        AdjustmentTransaction adjustmentTransaction = new AdjustmentTransaction();
        adjustmentTransaction.setBranch(adjustmentDetail.getAdjustment().getBranch());
        adjustmentTransaction.setAdjustmentDetail(adjustmentDetail);
        adjustmentTransaction.setProduct(adjustmentDetail.getProduct());
        adjustmentTransaction.setAdjustQuantity(adjustmentDetail.getQuantity());
        adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
        adjustmentTransaction.setDate(new Date());

//        adjustmentTransactionDao.create(adjustmentTransaction);
        // TODO: Create adjustment transaction.
    }

    private static void updateAdjustmentTransaction(@NotNull AdjustmentDetail adjustmentDetail)
            throws Exception {
        AdjustmentTransaction adjustmentTransaction =
                getAdjustmentTransaction(adjustmentDetail.getId());
        adjustmentTransaction.setBranch(adjustmentDetail.getAdjustment().getBranch());
        adjustmentTransaction.setAdjustmentDetail(adjustmentDetail);
        adjustmentTransaction.setProduct(adjustmentDetail.getProduct());
        adjustmentTransaction.setAdjustQuantity(adjustmentDetail.getQuantity());
        adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
        adjustmentTransaction.setDate(new Date());

//        adjustmentTransactionDao.update(adjustmentTransaction);
        // TODO: Update adjustment transaction.
    }
}