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

package inc.nomard.spoty.core.viewModels.transfers;

import com.google.gson.*;
import com.google.gson.reflect.*;

import static inc.nomard.spoty.core.values.SharedResources.*;

import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.transfers.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import javafx.beans.property.*;
import javafx.collections.*;

import lombok.extern.java.Log;

@Log
public class TransferDetailViewModel {
    public static final ObservableList<TransferDetail> transferDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
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
    }

    public static void saveTransferDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        transferDetailsList.forEach(transferDetail -> {
            var task = transfersRepository.postDetail(transferDetail);
            if (Objects.nonNull(onActivity)) {
                task.setOnRunning(workerStateEvent -> onActivity.run());
            }
            if (Objects.nonNull(onSuccess)) {
                task.setOnFailed(workerStateEvent -> onSuccess.run());
            }
            if (Objects.nonNull(onFailed)) {
                task.setOnFailed(workerStateEvent -> onFailed.run());
            }
            SpotyThreader.spotyThreadPool(task);
        });

        setProductQuantity();
        transferDetailsList.clear();
    }

    public static void getAllTransferDetails(ParameterlessConsumer onActivity) {
        var task = transfersRepository.fetchAllDetail();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<TransferDetail>>() {
            }.getType();

            try {
                ArrayList<TransferDetail> transferDetailList = gson.fromJson(
                        task.get().body(), listType);

                transferDetailsList.clear();
                transferDetailsList.addAll(transferDetailList);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        SpotyThreader.spotyThreadPool(task);
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

    public static void updateItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var transferDetail = transferDetailsList.get(Math.toIntExact(index));
        transferDetail.setProduct(getProduct());
        transferDetail.setQuantity(getQuantity());
        transferDetail.setSerialNo(getSerial());
        transferDetail.setDescription(getDescription());
        transferDetail.setPrice(getPrice());
        transferDetail.setTotal(getTotal());
        var task = transfersRepository.putDetail(transferDetail);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateTransferDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        // TODO: Add save multiple on API.
        transferDetailsList.forEach(
                transferDetail -> {
                    var task = transfersRepository.putDetail(transferDetail);
                    if (Objects.nonNull(onActivity)) {
                        task.setOnRunning(workerStateEvent -> onActivity.run());
                    }
                    if (Objects.nonNull(onSuccess)) {
                        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
                    }
                    if (Objects.nonNull(onFailed)) {
                        task.setOnFailed(workerStateEvent -> onFailed.run());
                    }
                    SpotyThreader.spotyThreadPool(task);
                });
        updateProductQuantity();
    }

    public static void removeTransferDetail(Long index, int tempIndex) {
        transferDetailsList.remove(tempIndex);
        PENDING_DELETES.add(index);
    }

    public static void deleteTransferDetails(LinkedList<Long> indexes,
                                             ParameterlessConsumer onActivity,
                                             ParameterlessConsumer onSuccess,
                                             ParameterlessConsumer onFailed) {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));

        var task = transfersRepository.deleteMultipleDetails(findModelList);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        SpotyThreader.spotyThreadPool(task);
    }

    private static void setProductQuantity() {
        getTransferDetails()
                .forEach(
                        transferDetails -> {
                            long productDetailQuantity =
                                    transferDetails.getProduct().getQuantity() - transferDetails.getQuantity();

                            ProductViewModel.setQuantity(productDetailQuantity);

                            try {
                                ProductViewModel.updateProduct(null, null, null);
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

                                ProductViewModel.updateProduct(null, null, null);

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

    private static void createTransferTransaction(TransferDetail transferDetails) {
//        Dao<TransferTransaction, Long> transferTransactionDao =
//                DaoManager.createDao(connectionSource, TransferTransaction.class);
//
//        TransferTransaction transferTransaction = new TransferTransaction();
//        transferTransaction.setFromBranch(transferDetails.getTransfer().getFromBranch());
//        transferTransaction.setToBranch(transferDetails.getTransfer().getToBranch());
//        transferTransaction.setTransferDetail(transferDetails);
//        transferTransaction.setProduct(transferDetails.getProduct());
//        transferTransaction.setTransferQuantity(transferDetails.getQuantity());
//        transferTransaction.setCreatedAt(new Date());
//
//        transferTransactionDao.create(transferTransaction);
    }

    private static void updateTransferTransaction(TransferDetail transferDetails) {
//        Dao<TransferTransaction, Long> transferTransactionDao =
//                DaoManager.createDao(connectionSource, TransferTransaction.class);
//
//        TransferTransaction transferTransaction = getTransferTransaction(transferDetails.getId());
//        transferTransaction.setFromBranch(transferDetails.getTransfer().getFromBranch());
//        transferTransaction.setToBranch(transferDetails.getTransfer().getToBranch());
//        transferTransaction.setTransferDetail(transferDetails);
//        transferTransaction.setProduct(transferDetails.getProduct());
//        transferTransaction.setTransferQuantity(transferDetails.getQuantity());
//        transferTransaction.setCreatedAt(new Date());
//
//        transferTransactionDao.update(transferTransaction);
    }
}
