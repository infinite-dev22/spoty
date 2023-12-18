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

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.transfers.TransferDetail;
import org.infinite.spoty.data_source.dtos.transfers.TransferMaster;
import org.infinite.spoty.data_source.dtos.transfers.TransferTransaction;
import org.infinite.spoty.utils.SpotyLogger;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;
import static org.infinite.spoty.values.SharedResources.setTempId;

public class TransferDetailViewModel {
    public static final ObservableList<TransferDetail> transferDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<TransferDetail> transferDetails =
            new SimpleListProperty<>(transferDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<TransferMaster> transfer = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty price = new SimpleStringProperty("");
    private static final StringProperty total = new SimpleStringProperty("");

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        TransferDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        TransferDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
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

    public static long getQuantity() {
        return Long.parseLong(!quantity.get().isEmpty() ? quantity.get() : "0");
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

    public static ObservableList<TransferDetail> getTransferDetails() {
        return transferDetails.get();
    }

    public static void setTransferDetails(ObservableList<TransferDetail> transferDetails) {
        TransferDetailViewModel.transferDetails.set(transferDetails);
    }

    public static ListProperty<TransferDetail> transferDetailsProperty() {
        return transferDetails;
    }

    public static void resetProperties() {
        setId(0);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setPrice("");
        setTotal("");
    }

    public static void addTransferDetails() {
//        TransferDetail transferDetail =
//                new TransferDetail(
//                        getProduct(), getQuantity(), getSerial(), getDescription(), getPrice(), getTotal());
//
//        Platform.runLater(
//                () -> {
//                    transferDetailsList.add(transferDetail);
//
//                    resetProperties();
//                });
    }

    public static void saveTransferDetails() throws Exception {
//        Dao<TransferDetail, Long> transferDetailDao =
//                DaoManager.createDao(connectionSource, TransferDetail.class);
//
//        transferDetailDao.create(transferDetailsList);

        setProductQuantity();

        Platform.runLater(transferDetailsList::clear);
    }

    public static void getAllTransferDetails() throws Exception {
//        Dao<TransferDetail, Long> transferDetailDao =
//                DaoManager.createDao(connectionSource, TransferDetail.class);
//
//        Platform.runLater(
//                () -> {
//                    transferDetailsList.clear();
//                    try {
//                        transferDetailsList.addAll(transferDetailDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, TransferDetailViewModel.class);
//                    }
//                });
    }

    public static void updateTransferDetail(long index) throws Exception {
//        Dao<TransferDetail, Long> transferDetailDao =
//                DaoManager.createDao(connectionSource, TransferDetail.class);
//
//        TransferDetail transferDetail = transferDetailDao.queryForId(index);
//        transferDetail.setProduct(getProduct());
//        transferDetail.setQuantity(getQuantity());
//        transferDetail.setSerialNo(getSerial());
//        transferDetail.setDescription(getDescription());
//        transferDetail.setPrice(getPrice());
//        transferDetail.setTotal(getTotal());
//
//        Platform.runLater(
//                () -> {
//                    transferDetailsList.remove(getTempId());
//                    transferDetailsList.add(getTempId(), transferDetail);
//
//                    resetProperties();
//                });
    }

    public static void getItem(long index, int tempIndex) throws Exception {
//        Dao<TransferDetail, Long> transferDetailDao =
//                DaoManager.createDao(connectionSource, TransferDetail.class);
//
//        TransferDetail transferDetail = transferDetailDao.queryForId(index);
//
//        Platform.runLater(
//                () -> {
//                    setTempId(tempIndex);
//                    setId(transferDetail.getId());
//                    setProduct(transferDetail.getProduct());
//                    setQuantity(String.valueOf(transferDetail.getQuantity()));
//                    setSerial(transferDetail.getSerialNo());
//                    setDescription(transferDetail.getDescription());
//                    setPrice(String.valueOf(transferDetail.getPrice()));
//                    setTotal(String.valueOf(transferDetail.getTotal()));
//                });
    }

    public static void updateItem(long index) throws Exception {
//        Dao<TransferDetail, Long> transferDetailDao =
//                DaoManager.createDao(connectionSource, TransferDetail.class);
//
//        TransferDetail transferDetail = transferDetailDao.queryForId(index);
//        transferDetail.setProduct(getProduct());
//        transferDetail.setQuantity(getQuantity());
//        transferDetail.setSerialNo(getSerial());
//        transferDetail.setDescription(getDescription());
//        transferDetail.setPrice(getPrice());
//        transferDetail.setTotal(getTotal());
//
//        transferDetailDao.update(transferDetail);

        getAllTransferDetails();
    }

    public static void updateTransferDetails() throws Exception {
//        Dao<TransferDetail, Long> transferDetailDao =
//                DaoManager.createDao(connectionSource, TransferDetail.class);
//
//        transferDetailsList.forEach(
//                transferDetail -> {
//                    try {
//                        transferDetailDao.update(transferDetail);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, TransferDetailViewModel.class);
//                    }
//                });

        updateProductQuantity();

        getAllTransferDetails();
    }

    public static void removeTransferDetail(long index, int tempIndex) {
        Platform.runLater(() -> transferDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteTransferDetails(@NotNull LinkedList<Long> indexes) {
//        indexes.forEach(
//                index -> {
//                    try {
//                        Dao<TransferDetail, Long> transferDetailDao =
//                                DaoManager.createDao(connectionSource, TransferDetail.class);
//
//                        transferDetailDao.deleteById(index);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, TransferDetailViewModel.class);
//                    }
//                });
    }

    private static void setProductQuantity() {
        getTransferDetails()
                .forEach(
                        transferDetails -> {
                            long productDetailQuantity =
                                    transferDetails.getProduct().getQuantity() - transferDetails.getQuantity();

                            ProductViewModel.setQuantity(productDetailQuantity);

                            try {
                                ProductViewModel.updateProductQuantity(transferDetails.getProduct().getId());
                                createTransferTransaction(transferDetails);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, TransferDetailViewModel.class);
                            }
                        });
    }

    private static void updateProductQuantity() {
        getTransferDetails()
                .forEach(
                        transferDetails -> {
                            try {
                                TransferTransaction saleTransaction =
                                        getTransferTransaction(transferDetails.getId());

                                long adjustQuantity = saleTransaction.getTransferQuantity();
                                long currentProductQuantity = transferDetails.getProduct().getQuantity();
                                long productQuantity =
                                        (currentProductQuantity - adjustQuantity) + transferDetails.getQuantity();

                                ProductViewModel.setQuantity(productQuantity);

                                ProductViewModel.updateProductQuantity(transferDetails.getProduct().getId());

                                updateTransferTransaction(transferDetails);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, TransferDetailViewModel.class);
                            }
                        });
    }

    private static TransferTransaction getTransferTransaction(long transferIndex)
            throws Exception {
//        Dao<TransferTransaction, Long> transferTransactionDao =
//                DaoManager.createDao(connectionSource, TransferTransaction.class);
//
//        PreparedQuery<TransferTransaction> preparedQuery =
//                transferTransactionDao
//                        .queryBuilder()
//                        .where()
//                        .eq("stock_in_detail_id", transferIndex)
//                        .prepare();
//
//        return transferTransactionDao.queryForFirst(preparedQuery);
        return new TransferTransaction();
    }

    private static void createTransferTransaction(@NotNull TransferDetail transferDetails)
            throws Exception {
//        Dao<TransferTransaction, Long> transferTransactionDao =
//                DaoManager.createDao(connectionSource, TransferTransaction.class);
//
//        TransferTransaction transferTransaction = new TransferTransaction();
//        transferTransaction.setFromBranch(transferDetails.getTransfer().getFromBranch());
//        transferTransaction.setToBranch(transferDetails.getTransfer().getToBranch());
//        transferTransaction.setTransferDetail(transferDetails);
//        transferTransaction.setProduct(transferDetails.getProduct());
//        transferTransaction.setTransferQuantity(transferDetails.getQuantity());
//        transferTransaction.setDate(new Date());
//
//        transferTransactionDao.create(transferTransaction);
    }

    private static void updateTransferTransaction(@NotNull TransferDetail transferDetails)
            throws Exception {
//        Dao<TransferTransaction, Long> transferTransactionDao =
//                DaoManager.createDao(connectionSource, TransferTransaction.class);
//
//        TransferTransaction transferTransaction = getTransferTransaction(transferDetails.getId());
//        transferTransaction.setFromBranch(transferDetails.getTransfer().getFromBranch());
//        transferTransaction.setToBranch(transferDetails.getTransfer().getToBranch());
//        transferTransaction.setTransferDetail(transferDetails);
//        transferTransaction.setProduct(transferDetails.getProduct());
//        transferTransaction.setTransferQuantity(transferDetails.getQuantity());
//        transferTransaction.setDate(new Date());
//
//        transferTransactionDao.update(transferTransaction);
    }
}
