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

package inc.nomard.spoty.core.viewModels.adjustments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentDetail;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentMaster;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentTransaction;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.AdjustmentRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.nomard.spoty.core.values.SharedResources.*;

public class AdjustmentDetailViewModel {
    @Getter
    public static final ObservableList<AdjustmentDetail> adjustmentDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<AdjustmentDetail> adjustmentDetails =
            new SimpleListProperty<>(adjustmentDetailsList);
    private static final LongProperty id = new SimpleLongProperty();
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<AdjustmentMaster> adjustment = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty adjustmentType = new SimpleStringProperty();
    private static final AdjustmentRepositoryImpl adjustmentRepository = new AdjustmentRepositoryImpl();

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

        adjustmentDetailsList.add(adjustmentDetail);
    }

    public static void saveAdjustmentDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        adjustmentDetailsList.forEach(adjustmentDetail -> {
            var task = adjustmentRepository.postDetail(adjustmentDetail);
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

                        ProductViewModel.updateProduct(null, null, null);
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
                        ProductViewModel.updateProduct(null, null, null);

                        updateAdjustmentTransaction(adjustmentDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
                    }
                });
    }

    public static void updateAdjustmentDetail(long index) {
        var adjustmentDetail = adjustmentDetailsList.get((int) index);
        adjustmentDetail.setProduct(getProduct());
        adjustmentDetail.setQuantity(getQuantity());
        adjustmentDetail.setAdjustmentType(getAdjustmentType());
        adjustmentDetailsList.remove(getTempId());
        adjustmentDetailsList.add(getTempId(), adjustmentDetail);
    }

    public static void getAllAdjustmentDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = adjustmentRepository.fetchAllDetail();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<AdjustmentDetail>>() {
            }.getType();
            ArrayList<AdjustmentDetail> adjustmentDetailList = new ArrayList<>();
            try {
                adjustmentDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
            }

            adjustmentDetailsList.clear();
            adjustmentDetailsList.addAll(adjustmentDetailList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAdjustmentDetail(AdjustmentDetail adjustmentDetail) {
        setTempId(getAdjustmentDetails().indexOf(adjustmentDetail));
        setProduct(adjustmentDetail.getProduct());
        setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
        setAdjustmentType(adjustmentDetail.getAdjustmentType());
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();

        var task = adjustmentRepository.searchDetail(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<AdjustmentDetail>>() {
            }.getType();
            ArrayList<AdjustmentDetail> adjustmentDetailList = new ArrayList<>();
            try {
                adjustmentDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (Exception e) {
                SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
            }

            adjustmentDetailsList.clear();
            adjustmentDetailsList.addAll(adjustmentDetailList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateAdjustmentDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        // TODO: Add save multiple on API.
        adjustmentDetailsList.forEach(
                adjustmentDetail -> {
                    var task = adjustmentRepository.putDetail(adjustmentDetail);
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
//        getAllAdjustmentDetails(null);
    }

    public static void removeAdjustmentDetail(long index, int tempIndex) {
        Platform.runLater(() -> adjustmentDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteAdjustmentDetails(LinkedList<Long> indexes,
                                               ParameterlessConsumer onActivity,
                                               ParameterlessConsumer onSuccess,
                                               ParameterlessConsumer onFailed) {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));

        var task = adjustmentRepository.deleteMultipleDetails(findModelList);
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

    private static AdjustmentTransaction getAdjustmentTransaction(long adjustmentIndex) {
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

    private static void createAdjustmentTransaction(AdjustmentDetail adjustmentDetail) {

        AdjustmentTransaction adjustmentTransaction = new AdjustmentTransaction();
        adjustmentTransaction.setAdjustmentDetail(adjustmentDetail);
        adjustmentTransaction.setProduct(adjustmentDetail.getProduct());
        adjustmentTransaction.setAdjustQuantity(adjustmentDetail.getQuantity());
        adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
        adjustmentTransaction.setDate(new Date());

//        adjustmentTransactionDao.create(adjustmentTransaction);
        // TODO: Create adjustment transaction.
    }

    private static void updateAdjustmentTransaction(AdjustmentDetail adjustmentDetail) {
        AdjustmentTransaction adjustmentTransaction =
                getAdjustmentTransaction(adjustmentDetail.getId());
        adjustmentTransaction.setAdjustmentDetail(adjustmentDetail);
        adjustmentTransaction.setProduct(adjustmentDetail.getProduct());
        adjustmentTransaction.setAdjustQuantity(adjustmentDetail.getQuantity());
        adjustmentTransaction.setAdjustmentType(adjustmentDetail.getAdjustmentType());
        adjustmentTransaction.setDate(new Date());

//        adjustmentTransactionDao.update(adjustmentTransaction);
        // TODO: Update adjustment transaction.
    }
}
