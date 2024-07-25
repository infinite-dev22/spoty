package inc.nomard.spoty.core.viewModels.hrm.leave;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.leave.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import io.github.palexdev.mfxcore.base.properties.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class LeaveStatusViewModel {
    public static final ObservableList<LeaveStatus> leaveStatusesList = FXCollections.observableArrayList();
    public static final ListProperty<LeaveStatus> leaveStatuses = new SimpleListProperty<>(leaveStatusesList);
    public static final ObservableList<User> usersList = FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<User> employee = new SimpleObjectProperty<>();
    private static final ObjectProperty<Designation> designation = new SimpleObjectProperty<>();
    private static final StringProperty description = new SimpleStringProperty("");
    private static final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    private static final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    private static final StringProperty duration = new SimpleStringProperty("");
    private static final StringProperty leaveType = new SimpleStringProperty();
    private static final StringProperty attachment = new SimpleStringProperty("");
    private static final CharProperty status = new CharProperty();
    private static final LeaveStatusRepositoryImpl leaveStatusRepository = new LeaveStatusRepositoryImpl();
    @Getter
    private static final ObservableList<String> leaveTypeList = FXCollections.observableArrayList(
            "Adoption Leave",
            "Annual Leave",
            "Bereavement Leave",
            "Casual Leave",
            "Compassionate Leave",
            "Extended Leave",
            "Family Leave",
            "Maternal Leave",
            "Military Leave",
            "Paid Leave",
            "Paternal Leave",
            "Personal Leave",
            "Sabbatical Leave",
            "Sick Leave",
            "Study Leave",
            "Unpaid Leave",
            "Vacation Leave",
            "Volunteer Leave",
            "Compensatory off",
            "Extra time off",
            "Jury duty",
            "Public holiday",
            "Religious holidays",
            "Toil");

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

    public static LocalDate getStartDate() {
        return startDate.get();
    }

    public static void setStartDate(LocalDate startDate) {
        LeaveStatusViewModel.startDate.set(startDate);
    }

    public static ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public static LocalDate getEndDate() {
        return startDate.get();
    }

    public static void setEndDate(LocalDate endDate) {
        LeaveStatusViewModel.endDate.set(endDate);
    }

    public static ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public static Duration getDuration() {
        return Duration.between(startDate.get(), endDate.get());
    }

    public static void setDuration(String duration) {
        LeaveStatusViewModel.duration.set(duration);
    }

    public static StringProperty durationProperty() {
        return duration;
    }

    public static String getLeaveType() {
        return leaveType.get();
    }

    public static void setLeaveType(String leaveType) {
        LeaveStatusViewModel.leaveType.set(leaveType);
    }

    public static StringProperty leaveTypeProperty() {
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
        setStartDate(null);
        setEndDate(null);
        setDuration("");
        setLeaveType("null");
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
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Leave status created successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }

    public static void getAllLeaveStatuses(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = leaveStatusRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LeaveStatus>>() {
                    }.getType();
                    ArrayList<LeaveStatus> leaveStatusList = gson.fromJson(response.body(), listType);
                    leaveStatusesList.clear();
                    leaveStatusesList.addAll(leaveStatusList);
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
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
                Platform.runLater(() -> {
                    var leaveStatus = gson.fromJson(response.body(), LeaveStatus.class);
                    setId(leaveStatus.getId());
                    setEmployee(leaveStatus.getEmployee());
                    setDesignation(leaveStatus.getDesignation());
                    setDescription(leaveStatus.getDescription());
                    setStartDate(leaveStatus.getStartDate());
                    setEndDate(leaveStatus.getEndDate());
                    setDuration(leaveStatus.getLocaleDuration());
                    setLeaveType(leaveStatus.getLeaveType());
                    setAttachment(leaveStatus.getAttachment());
                    setStatus(leaveStatus.getStatus());
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
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
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LeaveStatus>>() {
                    }.getType();
                    ArrayList<LeaveStatus> leaveStatusList = gson.fromJson(
                            response.body(), listType);
                    leaveStatusesList.clear();
                    leaveStatusesList.addAll(leaveStatusList);
                    onSuccess.run();
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
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
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Leave status updated successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
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
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Leave status deleted successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, LeaveStatusViewModel.class);
            return null;
        });
    }
}
