package inc.nomard.spoty.core.viewModels.hrm.employee;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
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
public class UserViewModel {
    public static final ObservableList<User> usersList = FXCollections.observableArrayList();
    public static final ListProperty<User> users = new SimpleListProperty<>(usersList);
    @Getter
    private static final ObservableList<String> workShiftsList = FXCollections.observableArrayList("Day", "Evening", "Full");
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty firstName = new SimpleStringProperty("");
    private static final StringProperty lastName = new SimpleStringProperty("");
    private static final StringProperty otherName = new SimpleStringProperty("");
    private static final ObjectProperty<Role> role = new SimpleObjectProperty<>(null);
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final BooleanProperty active = new SimpleBooleanProperty(true);
    private static final StringProperty avatar = new SimpleStringProperty("");
    private static final ObjectProperty<LocalDate> dateOfBirth = new SimpleObjectProperty<LocalDate>();
    private static final ObjectProperty<Department> department = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Designation> designation = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<EmploymentStatus> employmentStatus = new SimpleObjectProperty<>(null);
    private static final StringProperty workShift = new SimpleStringProperty("");
    private static final UsersRepositoryImpl usersRepository = new UsersRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        UserViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getFirstName() {
        return firstName.get();
    }

    public static void setFirstName(String firstName) {
        UserViewModel.firstName.set(firstName);
    }

    public static StringProperty firstNameProperty() {
        return firstName;
    }

    public static String getLastName() {
        return lastName.get();
    }

    public static void setLastName(String lastName) {
        UserViewModel.lastName.set(lastName);
    }

    public static StringProperty lastNameProperty() {
        return lastName;
    }

    public static boolean isActive() {
        return active.get();
    }

    public static void setActive(boolean active) {
        UserViewModel.active.set(active);
    }

    public static BooleanProperty activeProperty() {
        return active;
    }

    public static String getOtherName() {
        return otherName.get();
    }

    public static void setOtherName(String otherName) {
        UserViewModel.otherName.set(otherName);
    }

    public static StringProperty userNameProperty() {
        return otherName;
    }

    public static Role getRole() {
        return role.get();
    }

    public static void setRole(Role role) {
        UserViewModel.role.set(role);
    }

    public static ObjectProperty<Role> roleProperty() {
        return role;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        UserViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        UserViewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static ObservableList<User> getUsers() {
        return users.get();
    }

    public static void setUsers(ObservableList<User> users) {
        UserViewModel.users.set(users);
    }

    public static ListProperty<User> usersProperty() {
        return users;
    }

    public static String getAvatar() {
        return avatar.get();
    }

    public static void setAvatar(String avatar) {
        UserViewModel.avatar.set(avatar);
    }

    public static StringProperty avatarProperty() {
        return avatar;
    }

    public static LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public static void setDateOfBirth(LocalDate dateOfBirth) {
        UserViewModel.dateOfBirth.set(dateOfBirth);
    }

    public static ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public static Department getDepartment() {
        return department.get();
    }

    public static void setDepartment(Department department) {
        UserViewModel.department.set(department);
    }

    public static ObjectProperty<Department> departmentProperty() {
        return department;
    }

    public static Designation getDesignation() {
        return designation.get();
    }

    public static void setDesignation(Designation designation) {
        UserViewModel.designation.set(designation);
    }

    public static ObjectProperty<Designation> designationProperty() {
        return designation;
    }

    public static EmploymentStatus getEmploymentStatus() {
        return employmentStatus.get();
    }

    public static void setEmploymentStatus(EmploymentStatus employmentStatus) {
        UserViewModel.employmentStatus.set(employmentStatus);
    }

    public static ObjectProperty<EmploymentStatus> employmentStatusProperty() {
        return employmentStatus;
    }

    public static String getWorkShift() {
        return workShift.get();
    }

    public static void setWorkShift(String workShift) {
        UserViewModel.workShift.set(workShift);
    }

    public static StringProperty workShiftProperty() {
        return workShift;
    }

    public static void resetProperties() {
        setId(0);
        setFirstName("");
        setLastName("");
        setOtherName("");
        setEmail("");
        setPhone("");
        setActive(true);
        setDateOfBirth(null);
        setRole(null);
        setDepartment(null);
        setDesignation(null);
        setEmploymentStatus(null);
        setWorkShift("");
        setAvatar("");
    }

    public static void saveUser(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                SpotyGotFunctional.MessageConsumer successMessage,
                                SpotyGotFunctional.MessageConsumer errorMessage) {
        var user = UserModel.builder()
                .firstName(getFirstName())
                .lastName(getLastName())
                .otherName(getOtherName())
                .phone(getPhone())
                .email(getEmail())
                .dateOfBirth(getDateOfBirth())
                .avatar(getAvatar())
                .active(isActive())
                .role(getRole())
                .department(getDepartment())
                .designation(getDesignation())
                .employmentStatus(getEmploymentStatus())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = usersRepository.post(user);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("User created successfully");
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
            SpotyLogger.writeToFile(throwable, UserViewModel.class);
            return null;
        });
    }

    public static void getAllUsers(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = usersRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<User>>() {
                    }.getType();
                    ArrayList<User> userList = gson.fromJson(response.body(), listType);
                    usersList.clear();
                    usersList.addAll(userList);
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
            SpotyLogger.writeToFile(throwable, UserViewModel.class);
            return null;
        });
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = usersRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var user = gson.fromJson(response.body(), UserModel.class);
                    setId(user.getId());
                    setFirstName(user.getFirstName());
                    setLastName(user.getLastName());
                    setOtherName(user.getOtherName());
                    setActive(user.isActive());
                    setPhone(user.getPhone());
                    setEmail(user.getEmail());
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
            SpotyLogger.writeToFile(throwable, DepartmentViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = usersRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<User>>() {
                    }.getType();
                    ArrayList<User> userList = gson.fromJson(
                            response.body(), listType);
                    usersList.clear();
                    usersList.addAll(userList);
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
            SpotyLogger.writeToFile(throwable, UserViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var user = UserModel.builder()
                .id(getId())
                .firstName(getFirstName())
                .lastName(getLastName())
                .otherName(getOtherName())
                .phone(getPhone())
                .email(getEmail())
                .dateOfBirth(getDateOfBirth())
                .avatar(getAvatar())
                .active(isActive())
                .role(getRole())
                .department(getDepartment())
                .designation(getDesignation())
                .employmentStatus(getEmploymentStatus())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = usersRepository.put(user);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("User updated successfully");
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
            SpotyLogger.writeToFile(throwable, UserViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = usersRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("User deleted successfully");
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
            SpotyLogger.writeToFile(throwable, UserViewModel.class);
            return null;
        });
    }
}
