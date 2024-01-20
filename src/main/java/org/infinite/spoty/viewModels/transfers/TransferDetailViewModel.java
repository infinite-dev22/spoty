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

package org.infinite.spoty.viewModels.transfers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.transfers.TransferDetail;
import org.infinite.spoty.data_source.dtos.transfers.TransferMaster;
import org.infinite.spoty.data_source.dtos.transfers.TransferTransaction;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.repositories.implementations.TransfersRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.infinite.spoty.viewModels.adjustments.AdjustmentDetailViewModel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.*;

public class TransferDetailViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
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
    private static final TransfersRepositoryImpl transfersRepository = new TransfersRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
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

    public static Integer getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
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
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setPrice("");
        setTotal("");
    }

    public static void addTransferDetails() {
        var transferDetail = TransferDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .serialNo(getSerial())
                .description(getDescription())
                .price(getPrice())
                .total(getTotal())
                .build();

        transferDetailsList.add(transferDetail);
        resetProperties();
    }

    public static void saveTransferDetails() {
        transferDetailsList.forEach(transferDetail -> {
            try {
                transfersRepository.postDetail(transferDetail);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        setProductQuantity();
        transferDetailsList.clear();
    }

    public static void getAllTransferDetails() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<TransferDetail>>() {
        }.getType();
        transferDetailsList.clear();

        ArrayList<TransferDetail> transferDetailList = gson.fromJson(
                transfersRepository.fetchAllDetail().body(), listType);
        transferDetailsList.addAll(transferDetailList);
    }

    public static void updateTransferDetail() {
        var transferDetail = TransferDetail.builder()
                .id(getId())
                .product(getProduct())
                .quantity(getQuantity())
                .serialNo(getSerial())
                .description(getDescription())
                .price(getPrice())
                .total(getTotal())
                .build();

        transferDetailsList.remove(getTempId());
        transferDetailsList.add(getTempId(), transferDetail);
        resetProperties();
    }

    public static void getItem(TransferDetail transferDetail) {
        setId(transferDetail.getId());
        setProduct(transferDetail.getProduct());
        setQuantity(String.valueOf(transferDetail.getQuantity()));
        setSerial(transferDetail.getSerialNo());
        setDescription(transferDetail.getDescription());
        setPrice(String.valueOf(transferDetail.getPrice()));
        setTotal(String.valueOf(transferDetail.getTotal()));
    }

    public static void updateItem(Long index) throws IOException, InterruptedException {
        var transferDetail = transferDetailsList.get(Math.toIntExact(index));
        transferDetail.setProduct(getProduct());
        transferDetail.setQuantity(getQuantity());
        transferDetail.setSerialNo(getSerial());
        transferDetail.setDescription(getDescription());
        transferDetail.setPrice(getPrice());
        transferDetail.setTotal(getTotal());
        transfersRepository.putDetail(transferDetail);
        getAllTransferDetails();
    }

    public static void updateTransferDetails() throws IOException, InterruptedException {
        // TODO: Add save multiple on API.
        transferDetailsList.forEach(
                adjustmentDetail -> {
                    try {
                        transfersRepository.putDetail(adjustmentDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
                    }
                });
        updateProductQuantity();
        getAllTransferDetails();
    }

    public static void removeTransferDetail(Long index, int tempIndex) {
        transferDetailsList.remove(tempIndex);
        PENDING_DELETES.add(index);
    }

    public static void deleteTransferDetails(@NotNull LinkedList<Long> indexes) throws IOException, InterruptedException {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));
        transfersRepository.deleteMultipleDetails(findModelList);
    }

    private static void setProductQuantity() {
        getTransferDetails()
                .forEach(
                        transferDetails -> {
                            long productDetailQuantity =
                                    transferDetails.getProduct().getQuantity() - transferDetails.getQuantity();

                            ProductViewModel.setQuantity(productDetailQuantity);

                            try {
                                ProductViewModel.updateProduct();
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

                                Long adjustQuantity = saleTransaction.getTransferQuantity();
                                Long currentProductQuantity = transferDetails.getProduct().getQuantity();
                                long productQuantity =
                                        (currentProductQuantity - adjustQuantity) + transferDetails.getQuantity();

                                ProductViewModel.setQuantity(productQuantity);

                                ProductViewModel.updateProduct();

                                updateTransferTransaction(transferDetails);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, TransferDetailViewModel.class);
                            }
                        });
    }

    private static TransferTransaction getTransferTransaction(Long transferIndex) {
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

    private static void createTransferTransaction(@NotNull TransferDetail transferDetails) {
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

    private static void updateTransferTransaction(@NotNull TransferDetail transferDetails) {
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
