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

package org.infinite.spoty.viewModels.requisitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.requisitions.RequisitionDetail;
import org.infinite.spoty.data_source.dtos.requisitions.RequisitionMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.RequisitionsRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.*;

public class RequisitionDetailViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    @Getter
    public static final ObservableList<RequisitionDetail> requisitionDetailsList =
            FXCollections.observableArrayList();
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

        Platform.runLater(() -> requisitionDetailsList.add(requisitionDetail));

        resetProperties();
    }

    public static void saveRequisitionDetails() {
        requisitionDetailsList.forEach(requisitionDetail -> {  // TODO: Add postMultipleDetail().
            try {
                requisitionsRepository.postDetail(requisitionDetail);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        requisitionDetailsList.clear();
    }

    public static void getAllRequisitionDetails() {
        Type listType = new TypeToken<ArrayList<RequisitionDetail>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    requisitionDetailsList.clear();

                    try {
                        ArrayList<RequisitionDetail> requisitionDetailList = gson.fromJson(
                                requisitionsRepository.fetchAllDetail().body(), listType);
                        requisitionDetailsList.addAll(requisitionDetailList);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
                    }
                });
    }

    public static void searchItem(String search) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<RequisitionDetail>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    requisitionDetailsList.clear();

                    try {
                        ArrayList<RequisitionDetail> requisitionDetailList = gson.fromJson(
                                requisitionsRepository.searchDetail(searchModel).body(), listType);
                        requisitionDetailsList.addAll(requisitionDetailList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
                    }
                });
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
        resetProperties();
    }

    public static void getItem(Long index, int tempIndex) throws IOException, InterruptedException {
        var findModel = FindModel.builder().id(index).build();
        var requisitionDetail = gson.fromJson(requisitionsRepository.fetchDetail(findModel).body(), RequisitionDetail.class);

        setTempId(tempIndex);
        setId(requisitionDetail.getId());
        setProduct(requisitionDetail.getProduct());
        setQuantity(String.valueOf(requisitionDetail.getQuantity()));
        setDescription(requisitionDetail.getDescription());
    }

    public static void updateRequisitionDetails() {
        requisitionDetailsList.forEach(
                requisitionDetail -> {
                    try {
                        requisitionsRepository.putDetail(requisitionDetail);
                    } catch (IOException | InterruptedException e) {
                        SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
                    }
                });

        getAllRequisitionDetails();
    }

    public static void removeRequisitionDetail(Long index, int tempIndex) {
        Platform.runLater(() -> requisitionDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteRequisitionDetails(@NotNull LinkedList<Long> indexes) throws IOException, InterruptedException {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));
        requisitionsRepository.deleteMultipleDetails(findModelList);
    }
}
