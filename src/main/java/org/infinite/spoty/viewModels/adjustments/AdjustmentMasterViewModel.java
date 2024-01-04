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

package org.infinite.spoty.viewModels.adjustments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.daos.Branch;
import org.infinite.spoty.data_source.daos.adjustments.AdjustmentMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.AdjustmentRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

public class AdjustmentMasterViewModel {
    public static final ObservableList<AdjustmentMaster> adjustmentMastersList =
            FXCollections.observableArrayList();
    private static final ListProperty<AdjustmentMaster> adjustmentMasters =
            new SimpleListProperty<>(adjustmentMastersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty note = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final DoubleProperty totalAmount = new SimpleDoubleProperty();
    private static final AdjustmentRepositoryImpl adjustmentRepository = new AdjustmentRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        AdjustmentMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, AdjustmentMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        AdjustmentMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        AdjustmentMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        AdjustmentMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<AdjustmentMaster> getAdjustmentMasters() {
        return adjustmentMasters.get();
    }

    public static void setAdjustmentMasters(ObservableList<AdjustmentMaster> adjustmentMasters) {
        AdjustmentMasterViewModel.adjustmentMasters.set(adjustmentMasters);
    }

    public static ListProperty<AdjustmentMaster> adjustmentMastersProperty() {
        return adjustmentMasters;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        AdjustmentMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalAmount() {
        return totalAmount.get();
    }

    public static void setTotalAmount(double totalAmount) {
        AdjustmentMasterViewModel.totalAmount.set(totalAmount);
    }

    public static DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0);
                    setDate("");
                    setBranch(null);
                    setNote("");
                    PENDING_DELETES.clear();
                });
    }

    public static void saveAdjustmentMaster() throws IOException, InterruptedException {
        var adjustmentMaster = AdjustmentMaster.builder()
                .branch(getBranch())
                .notes(getNote())
                .date(getDate())
                .build();
        if (!AdjustmentDetailViewModel.getAdjustmentDetailsList().isEmpty()) {
            AdjustmentDetailViewModel.getAdjustmentDetailsList()
                    .forEach(adjustmentDetail -> adjustmentDetail.setAdjustment(adjustmentMaster));

            adjustmentMaster.setAdjustmentDetails(AdjustmentDetailViewModel.getAdjustmentDetailsList());
        }
        adjustmentRepository.postMaster(adjustmentMaster);
        AdjustmentDetailViewModel.saveAdjustmentDetails();
        Platform.runLater(AdjustmentMasterViewModel::resetProperties);
        getAllAdjustmentMasters();
    }

    public static void getAllAdjustmentMasters() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<AdjustmentMaster>>() {
        }.getType();
        adjustmentMastersList.clear();
        ArrayList<AdjustmentMaster> adjustmentMasterList = new Gson().fromJson(
                adjustmentRepository.fetchAllMaster().body(), listType);
        adjustmentMastersList.addAll(adjustmentMasterList);
    }

    public static void getItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();
        findModel.setId(index);
        var response = adjustmentRepository.fetchMaster(findModel).body();
        var adjustmentMaster = new Gson().fromJson(response, AdjustmentMaster.class);

        setId(adjustmentMaster.getId());
        setBranch(adjustmentMaster.getBranch());
        setNote(adjustmentMaster.getNotes());
        setDate(adjustmentMaster.getLocaleDate());
        AdjustmentDetailViewModel.adjustmentDetailsList.clear();
        AdjustmentDetailViewModel.adjustmentDetailsList.addAll(adjustmentMaster.getAdjustmentDetails());
        getAllAdjustmentMasters();
    }

    public static void searchItem(String search) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<AdjustmentMaster>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    adjustmentMastersList.clear();

                    try {
                        ArrayList<AdjustmentMaster> adjustmentMasterList = new Gson().fromJson(
                                adjustmentRepository.searchMaster(searchModel).body(), listType);
                        adjustmentMastersList.addAll(adjustmentMasterList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, AdjustmentMasterViewModel.class);
                    }
                });
    }

    public static void updateItem() throws IOException, InterruptedException {
        var adjustmentMaster = AdjustmentMaster.builder()
                .id(getId())
                .branch(getBranch())
                .notes(getNote())
                .date(getDate())
                .build();

        AdjustmentDetailViewModel.deleteAdjustmentDetails(PENDING_DELETES);

        if (!AdjustmentDetailViewModel.getAdjustmentDetailsList().isEmpty()) {
            AdjustmentDetailViewModel.getAdjustmentDetailsList()
                    .forEach(adjustmentDetail -> adjustmentDetail.setAdjustment(adjustmentMaster));

            adjustmentMaster.setAdjustmentDetails(AdjustmentDetailViewModel.getAdjustmentDetailsList());
        }
        adjustmentRepository.putMaster(adjustmentMaster);
        AdjustmentDetailViewModel.updateAdjustmentDetails();
        Platform.runLater(AdjustmentMasterViewModel::resetProperties);
        getAllAdjustmentMasters();
    }

    public static void deleteItem(Long index) throws IOException, InterruptedException {
        var findModel = new FindModel();

        findModel.setId(index);
        adjustmentRepository.deleteMaster(findModel);
        getAllAdjustmentMasters();
    }
}
