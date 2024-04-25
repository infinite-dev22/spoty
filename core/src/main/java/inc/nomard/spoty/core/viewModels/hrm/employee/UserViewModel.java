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

package inc.nomard.spoty.core.viewModels.hrm.employee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Role;
import inc.nomard.spoty.network_bridge.dtos.UserProfile;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.User;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.UserProfilesRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class UserViewModel {
    public static final ObservableList<UserProfile> userProfilesList = FXCollections.observableArrayList();
    public static final ListProperty<UserProfile> userProfiles = new SimpleListProperty<>(userProfilesList);
    public static final ObservableList<User> usersList = FXCollections.observableArrayList();
    public static final ListProperty<User> users = new SimpleListProperty<>(usersList);
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty firstName = new SimpleStringProperty("");
    private static final StringProperty lastName = new SimpleStringProperty("");
    private static final StringProperty otherName = new SimpleStringProperty("");
    private static final ObjectProperty<Role> role = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final StringProperty address = new SimpleStringProperty("");
    private static final StringProperty taxNumber = new SimpleStringProperty("");
    private static final StringProperty country = new SimpleStringProperty("");
    private static final BooleanProperty active = new SimpleBooleanProperty(true);
    private static final BooleanProperty accessAllBranches = new SimpleBooleanProperty(false);
    private static final StringProperty avatar = new SimpleStringProperty("");
    private static final UserProfilesRepositoryImpl userProfilesRepository = new UserProfilesRepositoryImpl();

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

    public static boolean canAccessAllBranches() {
        return accessAllBranches.get();
    }

    public static void setAccessAllBranches(boolean accessAllBranches) {
        UserViewModel.accessAllBranches.set(accessAllBranches);
    }

    public static BooleanProperty accessAllBranchesProperty() {
        return accessAllBranches;
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

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        UserViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
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

    public static String getCity() {
        return city.get();
    }

    public static void setCity(String city) {
        UserViewModel.city.set(city);
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static String getAddress() {
        return address.get();
    }

    public static void setAddress(String address) {
        UserViewModel.address.set(address);
    }

    public static StringProperty addressProperty() {
        return address;
    }

    public static String getTaxNumber() {
        return taxNumber.get();
    }

    public static void setTaxNumber(String taxNumber) {
        UserViewModel.taxNumber.set(taxNumber);
    }

    public static StringProperty taxNumberProperty() {
        return taxNumber;
    }

    public static String getCountry() {
        return country.get();
    }

    public static void setCountry(String country) {
        UserViewModel.country.set(country);
    }

    public static StringProperty countryProperty() {
        return country;
    }

    public static ObservableList<UserProfile> getUserProfiles() {
        return userProfiles.get();
    }

    public static void setUserProfiles(ObservableList<UserProfile> userProfiles) {
        UserViewModel.userProfiles.set(userProfiles);
    }

    public static ListProperty<UserProfile> userProfilesProperty() {
        return userProfiles;
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

    public static void resetProperties() {
        setId(0);
        setFirstName("");
        setLastName("");
        setOtherName("");
        setEmail("");
        setPhone("");
        setCity("");
        setAddress("");
        setCountry("");
    }

    public static void saveUser(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var userProfile = UserProfile.builder()
                .firstName(getFirstName())
                .lastName(getLastName())
                .otherName(getOtherName())
                .phone(getPhone())
                .email(getEmail())
                .avatar(getAvatar())
                .build();

        var task = userProfilesRepository.post(userProfile);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllUserProfiles(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = userProfilesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<UserProfile>>() {
                }.getType();
                ArrayList<UserProfile> userList = gson.fromJson(task.get().body(), listType);

                userProfilesList.clear();
                userProfilesList.addAll(userList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, UserViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = userProfilesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var userProfile = gson.fromJson(task.get().body(), UserProfile.class);

                setId(userProfile.getId());
                setFirstName(userProfile.getFirstName());
                setLastName(userProfile.getLastName());
                setOtherName(userProfile.getOtherName());
                setActive(userProfile.isActive());
                setPhone(userProfile.getPhone());
                setEmail(userProfile.getEmail());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = userProfilesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<UserProfile>>() {
                }.getType();
                ArrayList<UserProfile> userList = gson.fromJson(task.get().body(), listType);

                userProfilesList.clear();
                userProfilesList.addAll(userList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, UserViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var userProfile = UserProfile.builder()
                .id(getId())
                .firstName(getFirstName())
                .lastName(getLastName())
                .otherName(getOtherName())
                .phone(getPhone())
                .email(getEmail())
                .avatar(getAvatar())
                .build();

        var task = userProfilesRepository.put(userProfile);
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

        var task = userProfilesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
