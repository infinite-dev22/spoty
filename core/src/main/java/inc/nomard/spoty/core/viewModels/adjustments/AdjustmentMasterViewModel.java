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

package inc.nomard.spoty.core.viewModels.adjustments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentMaster;
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
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

public class AdjustmentMasterViewModel {
    public static final ObservableList<AdjustmentMaster> adjustmentMastersList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
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

    public static void saveAdjustmentMaster(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var adjustmentMaster = AdjustmentMaster.builder()
                .notes(getNote())
                .date(getDate())
                .build();

        if (!AdjustmentDetailViewModel.getAdjustmentDetailsList().isEmpty()) {
            AdjustmentDetailViewModel.getAdjustmentDetailsList()
                    .forEach(adjustmentDetail -> adjustmentDetail.setAdjustment(adjustmentMaster));

            adjustmentMaster.setAdjustmentDetails(AdjustmentDetailViewModel.getAdjustmentDetailsList());
        }

        var task = adjustmentRepository.postMaster(adjustmentMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            AdjustmentDetailViewModel.saveAdjustmentDetails(onActivity, null, onFailed);
            onSuccess.run();
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllAdjustmentMasters(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var task = adjustmentRepository.fetchAllMaster();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<AdjustmentMaster>>() {
            }.getType();
            ArrayList<AdjustmentMaster> adjustmentMasterList = new ArrayList<>();
            try {
                adjustmentMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, AdjustmentMasterViewModel.class);
            }

            adjustmentMastersList.clear();
            adjustmentMastersList.addAll(adjustmentMasterList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAdjustmentMaster(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();
        var task = adjustmentRepository.fetchMaster(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            AdjustmentMaster adjustmentMaster = new AdjustmentMaster();
            try {
                adjustmentMaster = gson.fromJson(task.get().body(), AdjustmentMaster.class);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, AdjustmentMasterViewModel.class);
            }

            setId(adjustmentMaster.getId());
            setNote(adjustmentMaster.getNotes());
            setDate(adjustmentMaster.getLocaleDate());
            AdjustmentDetailViewModel.adjustmentDetailsList.clear();
            AdjustmentDetailViewModel.adjustmentDetailsList.addAll(adjustmentMaster.getAdjustmentDetails());

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = adjustmentRepository.searchMaster(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<AdjustmentMaster>>() {
            }.getType();
            ArrayList<AdjustmentMaster> adjustmentMasterList = new ArrayList<>();
            try {
                adjustmentMasterList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, AdjustmentMasterViewModel.class);
            }

            adjustmentMastersList.clear();
            adjustmentMastersList.addAll(adjustmentMasterList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var adjustmentMaster = AdjustmentMaster.builder()
                .id(getId())
                .notes(getNote())
                .date(getDate())
                .build();

        if (!PENDING_DELETES.isEmpty()) {
            AdjustmentDetailViewModel.deleteAdjustmentDetails(PENDING_DELETES, onActivity, null, onFailed);
        }

        if (!AdjustmentDetailViewModel.getAdjustmentDetailsList().isEmpty()) {
            AdjustmentDetailViewModel.getAdjustmentDetailsList()
                    .forEach(adjustmentDetail -> adjustmentDetail.setAdjustment(adjustmentMaster));

            adjustmentMaster.setAdjustmentDetails(AdjustmentDetailViewModel.getAdjustmentDetailsList());
        }

        var task = adjustmentRepository.putMaster(adjustmentMaster);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> AdjustmentDetailViewModel.updateAdjustmentDetails(onActivity, onSuccess, onFailed));
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = adjustmentRepository.deleteMaster(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
