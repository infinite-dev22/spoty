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

package inc.normad.spoty.core.viewModels.requisitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import inc.normad.spoty.network_bridge.dtos.Product;
import inc.normad.spoty.network_bridge.dtos.requisitions.RequisitionDetail;
import inc.normad.spoty.network_bridge.dtos.requisitions.RequisitionMaster;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.RequisitionsRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.normad.spoty.core.values.SharedResources.*;

public class RequisitionDetailViewModel {
    @Getter
    public static final ObservableList<RequisitionDetail> requisitionDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<RequisitionDetail> requisitionDetails =
            new SimpleListProperty<>(requisitionDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<RequisitionMaster> requisition = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty description = new SimpleStringProperty();
    private static final RequisitionsRepositoryImpl requisitionsRepository = new RequisitionsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
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
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setRequisition(null);
        setDescription("");
        setQuantity("");
    }

    public static void addRequisitionDetails() {
        var requisitionDetail = RequisitionDetail.builder()
                .product(getProduct())
                .requisition(getRequisition())
                .quantity(Integer.parseInt(getQuantity()))
                .description(getDescription())
                .build();

        requisitionDetailsList.add(requisitionDetail);
    }

    public static void saveRequisitionDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        requisitionDetailsList.forEach(requisitionDetail -> {  // TODO: Add postMultipleDetail().
            var task = requisitionsRepository.postDetail(requisitionDetail);
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
        requisitionDetailsList.clear();
    }

    public static void getAllRequisitionDetails(@Nullable ParameterlessConsumer onActivity) {
        var task = requisitionsRepository.fetchAllDetail();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<RequisitionDetail>>() {
            }.getType();
            ArrayList<RequisitionDetail> requisitionDetailList = new ArrayList<>();
            try {
                requisitionDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
            }

            requisitionDetailsList.clear();
            requisitionDetailsList.addAll(requisitionDetailList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        var task = requisitionsRepository.searchDetail(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<RequisitionDetail>>() {
            }.getType();
            ArrayList<RequisitionDetail> requisitionDetailList = new ArrayList<>();
            try {
                requisitionDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {

                SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
            }

            requisitionDetailsList.clear();
            requisitionDetailsList.addAll(requisitionDetailList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateRequisitionDetail(Long index) {
        var requisitionDetail = RequisitionDetail.builder()
                .id(index)
                .product(getProduct())
                .requisition(getRequisition())
                .quantity(Integer.parseInt(getQuantity()))
                .description(getDescription())
                .build();

        requisitionDetailsList.remove(getTempId());
        requisitionDetailsList.add(getTempId(), requisitionDetail);
    }

    public static void getItem(Long index, int tempIndex,
                               @Nullable ParameterlessConsumer onActivity,
                               @Nullable ParameterlessConsumer onSuccess,
                               @Nullable ParameterlessConsumer onFailed) throws IOException, InterruptedException {
        var findModel = FindModel.builder().id(index).build();
        var task = requisitionsRepository.fetchDetail(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> {
                RequisitionDetail requisitionDetail = new RequisitionDetail();
                try {
                    requisitionDetail = gson.fromJson(task.get().body(), RequisitionDetail.class);
                } catch (InterruptedException | ExecutionException e) {
                    SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
                }

                setTempId(tempIndex);
                setId(requisitionDetail.getId());
                setProduct(requisitionDetail.getProduct());
                setQuantity(String.valueOf(requisitionDetail.getQuantity()));
                setDescription(requisitionDetail.getDescription());

                onSuccess.run();
            });
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateRequisitionDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        // TODO: Add save multiple on API.
        requisitionDetailsList.forEach(
                requisitionDetail -> {
                    var task = requisitionsRepository.putDetail(requisitionDetail);
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

        // getAllRequisitionDetails();
    }

    public static void removeRequisitionDetail(Long index, int tempIndex) {
        Platform.runLater(() -> requisitionDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteRequisitionDetails(@NotNull LinkedList<Long> indexes,
                                                @Nullable ParameterlessConsumer onActivity,
                                                @Nullable ParameterlessConsumer onSuccess,
                                                @Nullable ParameterlessConsumer onFailed) {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));

        var task = requisitionsRepository.deleteMultipleDetails(findModelList);
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
}
