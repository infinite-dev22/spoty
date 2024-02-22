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

package org.infinite.spoty.viewModels.hrm.pay_roll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.palexdev.mfxcore.base.properties.CharProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.hrm.employee.User;
import org.infinite.spoty.data_source.dtos.hrm.pay_roll.PaySlip;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.PaySlipRepositoryImpl;
import org.infinite.spoty.utils.ParameterlessConsumer;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.BankViewModel;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.infinite.spoty.viewModels.requisitions.RequisitionMasterViewModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class PaySlipViewModel {
    public static final ObservableList<PaySlip> paySlipsList = FXCollections.observableArrayList();
    public static final ListProperty<PaySlip> paySlips = new SimpleListProperty<>(paySlipsList);
    public static final ObservableList<User> usersList = FXCollections.observableArrayList();
    public static final ListProperty<User> users = new SimpleListProperty<>(usersList);
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty startDate = new SimpleStringProperty("");
    private static final StringProperty endDate = new SimpleStringProperty("");
    private static final IntegerProperty salariesQuantity = new SimpleIntegerProperty();
    private static final CharProperty status = new CharProperty();
    private static final StringProperty createdOn = new SimpleStringProperty("");
    private static final StringProperty message = new SimpleStringProperty("");
    private static final PaySlipRepositoryImpl paySlipRepository = new PaySlipRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        PaySlipViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getStartDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(startDate.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setStartDate(String startDate) {
        PaySlipViewModel.startDate.set(startDate);
    }

    public static StringProperty startDateProperty() {
        return startDate;
    }

    public static @Nullable Date getEndDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(startDate.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setEndDate(String endDate) {
        PaySlipViewModel.endDate.set(endDate);
    }

    public static StringProperty endDateProperty() {
        return endDate;
    }

    public static @NotNull Integer getSalariesQuantity() {
        return salariesQuantity.get();
    }

    public static void setSalariesQuantity(Integer salariesQuantity) {
        PaySlipViewModel.salariesQuantity.set(salariesQuantity);
    }

    public static IntegerProperty salariesQuantityProperty() {
        return salariesQuantity;
    }

    public static char getStatus() {
        return status.get();
    }

    public static void setStatus(char status) {
        PaySlipViewModel.status.set(status);
    }

    public static CharProperty statusProperty() {
        return status;
    }

    public static @Nullable Date getCreatedDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(createdOn.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setCreatedDate(String createdOn) {
        PaySlipViewModel.createdOn.set(createdOn);
    }

    public static StringProperty createdDateProperty() {
        return createdOn;
    }

    public static String getMessage() {
        return message.get();
    }

    public static void setMessage(String message) {
        PaySlipViewModel.message.set(message);
    }

    public static StringProperty messageProperty() {
        return message;
    }

    public static ObservableList<PaySlip> getPaySlipes() {
        return paySlips.get();
    }

    public static void setPaySlipes(ObservableList<PaySlip> paySlips) {
        PaySlipViewModel.paySlips.set(paySlips);
    }

    public static ListProperty<PaySlip> paySlipProperty() {
        return paySlips;
    }

    public static void resetProperties() {
        setId(0);
        setStartDate("");
        setEndDate("");
        setSalariesQuantity(0);
        setStatus('P');
        setCreatedDate("");
        setMessage("");
    }

    public static void savePaySlip(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var paySlip = PaySlip.builder()
                .startDate(getStartDate())
                .endDate(getEndDate())
                .salariesQuantity(getSalariesQuantity())
                .status(getStatus())
                .createdOn(getCreatedDate())
                .message(getMessage())
                .build();

        var task = paySlipRepository.post(paySlip);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllPaySlipes(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = paySlipRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<PaySlip>>() {
                }.getType();
                ArrayList<PaySlip> userList = gson.fromJson(task.get().body(), listType);

                paySlipsList.clear();
                paySlipsList.addAll(userList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, PaySlipViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = paySlipRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var paySlip = gson.fromJson(task.get().body(), PaySlip.class);

                setId(paySlip.getId());

                setStartDate(paySlip.getLocaleStartDate());
                setEndDate(paySlip.getLocaleEndDate());
                setSalariesQuantity(paySlip.getSalariesQuantity());
                setStatus(paySlip.getStatus());
                setCreatedDate(paySlip.getLocaleCreatedDate());
                setMessage(paySlip.getMessage());
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = paySlipRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<PaySlip>>() {
                }.getType();
                ArrayList<PaySlip> userList = gson.fromJson(task.get().body(), listType);

                paySlipsList.clear();
                paySlipsList.addAll(userList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, PaySlipViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var paySlip = PaySlip.builder()
                .id(getId())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .salariesQuantity(getSalariesQuantity())
                .status(getStatus())
                .createdOn(getCreatedDate())
                .message(getMessage())
                .build();

        var task = paySlipRepository.put(paySlip);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = paySlipRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
