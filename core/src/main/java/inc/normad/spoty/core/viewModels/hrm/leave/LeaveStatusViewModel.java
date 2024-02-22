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

package inc.normad.spoty.core.viewModels.hrm.leave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import inc.normad.spoty.network_bridge.dtos.hrm.employee.Designation;
import inc.normad.spoty.network_bridge.dtos.hrm.employee.User;
import inc.normad.spoty.network_bridge.dtos.hrm.leave.LeaveStatus;
import inc.normad.spoty.network_bridge.dtos.hrm.leave.LeaveType;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.LeaveStatusRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.BankViewModel;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.normad.spoty.core.viewModels.requisitions.RequisitionMasterViewModel;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class LeaveStatusViewModel {
    public static final ObservableList<LeaveStatus> leaveStatusesList = FXCollections.observableArrayList();
    public static final ListProperty<LeaveStatus> leaveStatuses = new SimpleListProperty<>(leaveStatusesList);
    public static final ObservableList<User> usersList = FXCollections.observableArrayList();
    public static final ListProperty<User> users = new SimpleListProperty<>(usersList);
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<User> employee = new SimpleObjectProperty<>();
    private static final ObjectProperty<Designation> designation = new SimpleObjectProperty<>();
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty startDate = new SimpleStringProperty("");
    private static final StringProperty endDate = new SimpleStringProperty("");
    private static final StringProperty duration = new SimpleStringProperty("");
    private static final ObjectProperty<LeaveType> leaveType = new SimpleObjectProperty<>();
    private static final StringProperty attachment = new SimpleStringProperty("");
    private static final LeaveStatusRepositoryImpl leaveStatusRepository = new LeaveStatusRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        LeaveStatusViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static User getEmployee() {
        return employee.get();
    }

    public static void setEmployee(User employee) {
        LeaveStatusViewModel.employee.set(employee);
    }

    public static ObjectProperty<User> employeeProperty() {
        return employee;
    }

    public static Designation getDesignation() {
        return designation.get();
    }

    public static void setDesignation(Designation designation) {
        LeaveStatusViewModel.designation.set(designation);
    }

    public static ObjectProperty<Designation> designationProperty() {
        return designation;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        LeaveStatusViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
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
        LeaveStatusViewModel.startDate.set(startDate);
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
        LeaveStatusViewModel.endDate.set(endDate);
    }

    public static StringProperty endDateProperty() {
        return endDate;
    }

    public static @Nullable Duration getDuration() {
        return Duration.parse(startDate.get());
    }

    public static void setDuration(String duration) {
        LeaveStatusViewModel.duration.set(duration);
    }

    public static StringProperty durationProperty() {
        return duration;
    }

    public static LeaveType getLeaveType() {
        return leaveType.get();
    }

    public static void setLeaveType(LeaveType leaveType) {
        LeaveStatusViewModel.leaveType.set(leaveType);
    }

    public static ObjectProperty<LeaveType> leaveTypeProperty() {
        return leaveType;
    }

    public static String getAttachment() {
        return attachment.get();
    }

    public static void setAttachment(String attachment) {
        LeaveStatusViewModel.attachment.set(attachment);
    }

    public static StringProperty attachmentProperty() {
        return attachment;
    }

    public static ObservableList<LeaveStatus> getLeaveStatuses() {
        return leaveStatuses.get();
    }

    public static void setLeaveStatuses(ObservableList<LeaveStatus> leaveStatuses) {
        LeaveStatusViewModel.leaveStatuses.set(leaveStatuses);
    }

    public static ListProperty<LeaveStatus> leaveStatusProperty() {
        return leaveStatuses;
    }

    public static void resetProperties() {
        setId(0);
        setEmployee(null);
        setDesignation(null);
        setDescription("");
        setStartDate("");
        setEndDate("");
        setDuration("");
        setLeaveType(null);
        setAttachment("");
    }

    public static void saveLeaveStatus(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var leaveStatus = LeaveStatus.builder()
                .employee(getEmployee())
                .designation(getDesignation())
                .description(getDescription())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .duration(getDuration())
                .leaveType(getLeaveType())
                .attachment(getAttachment())  // File to be uploaded.
                .build();

        var task = leaveStatusRepository.post(leaveStatus);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllLeaveStatuses(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var task = leaveStatusRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<LeaveStatus>>() {
                }.getType();
                ArrayList<LeaveStatus> userList = gson.fromJson(task.get().body(), listType);

                leaveStatusesList.clear();
                leaveStatusesList.addAll(userList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, LeaveStatusViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = leaveStatusRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var leaveStatus = gson.fromJson(task.get().body(), LeaveStatus.class);

                setId(leaveStatus.getId());

                setEmployee(leaveStatus.getEmployee());
                setDesignation(leaveStatus.getDesignation());
                setDescription(leaveStatus.getDescription());
                setStartDate(leaveStatus.getLocaleStartDate());
                setEndDate(leaveStatus.getLocaleEndDate());
                setDuration(leaveStatus.getLocaleDuration());
                setLeaveType(leaveStatus.getLeaveType());
                setAttachment(leaveStatus.getAttachment());
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
        var task = leaveStatusRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<LeaveStatus>>() {
                }.getType();
                ArrayList<LeaveStatus> userList = gson.fromJson(task.get().body(), listType);

                leaveStatusesList.clear();
                leaveStatusesList.addAll(userList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, LeaveStatusViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var leaveStatus = LeaveStatus.builder()
                .id(getId())
                .employee(getEmployee())
                .designation(getDesignation())
                .description(getDescription())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .duration(getDuration())
                .leaveType(getLeaveType())
                .attachment(getAttachment())  // File to be uploaded.
                .build();

        var task = leaveStatusRepository.put(leaveStatus);
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

        var task = leaveStatusRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
