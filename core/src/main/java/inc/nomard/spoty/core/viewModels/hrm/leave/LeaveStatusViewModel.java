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

package inc.nomard.spoty.core.viewModels.hrm.leave;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.leave.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import io.github.palexdev.mfxcore.base.properties.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class LeaveStatusViewModel {
    public static final ObservableList<LeaveStatus> leaveStatusesList = FXCollections.observableArrayList();
    public static final ListProperty<LeaveStatus> leaveStatuses = new SimpleListProperty<>(leaveStatusesList);
    public static final ObservableList<User> usersList = FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<User> employee = new SimpleObjectProperty<>();
    private static final ObjectProperty<Designation> designation = new SimpleObjectProperty<>();
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty startDate = new SimpleStringProperty("");
    private static final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
    private static final StringProperty endDate = new SimpleStringProperty("");
    private static final ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<>();
    private static final StringProperty duration = new SimpleStringProperty("");
    private static final ObjectProperty<LeaveType> leaveType = new SimpleObjectProperty<>();
    private static final StringProperty attachment = new SimpleStringProperty("");
    private static final CharProperty status = new CharProperty();
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

    public static Date getStartDate() {
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

    public static Property<LocalTime> startTimeProperty() {
        return startTime;
    }

    public static Date getEndDate() {
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

    public static LocalTime getEndTime() {
        return endTime.get();
    }

    public static void setEndTime(LocalTime time) {
        endTime.set(time);
    }

    public static Property<LocalTime> endTimeProperty() {
        return endTime;
    }

    public static LocalTime getStartTime() {
        return startTime.get();
    }

    public static void setStartTime(LocalTime time) {
        startTime.set(time);
    }

    public static Duration getDuration() {
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

    public static char getStatus() {
        return status.get();
    }

    public static void setStatus(char status) {
        LeaveStatusViewModel.status.set(status);
    }

    public static CharProperty statusProperty() {
        return status;
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
        setStartTime(null);
        setEndTime(null);
        setDuration("");
        setLeaveType(null);
        setAttachment("");
        setStatus('P');
    }

    public static void saveLeaveStatus(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                       SpotyGotFunctional.MessageConsumer successMessage,
                                       SpotyGotFunctional.MessageConsumer errorMessage) {
        var leaveStatus = LeaveStatus.builder()
                .employee(getEmployee())
                .designation(getDesignation())
                .description(getDescription())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .startTime(getStartTime())
                .endTime(getEndTime())
                .duration(getDuration())
                .leaveType(getLeaveType())
                .attachment(getAttachment())  // File to be uploaded.
                .status(getStatus())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = leaveStatusRepository.post(leaveStatus);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Leave status created successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }

    public static void getAllLeaveStatuses(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = leaveStatusRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<LeaveStatus>>() {
                }.getType();
                ArrayList<LeaveStatus> leaveStatusList = gson.fromJson(response.body(), listType);
                leaveStatusesList.clear();
                leaveStatusesList.addAll(leaveStatusList);
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = leaveStatusRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                var leaveStatus = gson.fromJson(response.body(), LeaveStatus.class);
                setId(leaveStatus.getId());
                setEmployee(leaveStatus.getEmployee());
                setDesignation(leaveStatus.getDesignation());
                setDescription(leaveStatus.getDescription());
                setStartDate(leaveStatus.getLocaleStartDate());
                setEndDate(leaveStatus.getLocaleEndDate());
                setStartTime(leaveStatus.getStartTime());
                setEndTime(leaveStatus.getEndTime());
                setDuration(leaveStatus.getLocaleDuration());
                setLeaveType(leaveStatus.getLeaveType());
                setAttachment(leaveStatus.getAttachment());
                setStatus(leaveStatus.getStatus());
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = leaveStatusRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<LeaveStatus>>() {
                }.getType();
                ArrayList<LeaveStatus> leaveStatusList = gson.fromJson(
                        response.body(), listType);
                leaveStatusesList.clear();
                leaveStatusesList.addAll(leaveStatusList);
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var leaveStatus = LeaveStatus.builder()
                .id(getId())
                .employee(getEmployee())
                .designation(getDesignation())
                .description(getDescription())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .startTime(getStartTime())
                .endTime(getEndTime())
                .duration(getDuration())
                .leaveType(getLeaveType())
                .attachment(getAttachment())  // File to be uploaded.
                .status(getStatus())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = leaveStatusRepository.put(leaveStatus);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Leave status updated successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = leaveStatusRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Leave status deleted successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }
}
