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

package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.daos.Branch;
import org.infinite.spoty.data_source.daos.Role;
import org.infinite.spoty.data_source.daos.UserProfile;


public class UserViewModel {
    public static final ObservableList<UserProfile> usersList = FXCollections.observableArrayList();
    public static final ListProperty<UserProfile> userProfiles = new SimpleListProperty<>(usersList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty firstName = new SimpleStringProperty("");
    private static final StringProperty lastName = new SimpleStringProperty("");
    private static final StringProperty userName = new SimpleStringProperty("");
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

    public static String getUserName() {
        return userName.get();
    }

    public static void setUserName(String userName) {
        UserViewModel.userName.set(userName);
    }

    public static StringProperty userNameProperty() {
        return userName;
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

    public static ListProperty<UserProfile> usersProperty() {
        return userProfiles;
    }

    public static void resetProperties() {
        setId(0);
        setFirstName("");
        setLastName("");
        setUserName("");
        setRole(null);
        setEmail("");
        setEmail("");
        setPhone("");
        setCity("");
        setAddress("");
        setTaxNumber("");
        setCountry("");
        setActive(true);
    }

    public static void saveUser() throws Exception {
//        Dao<UserProfile, Long> userDao = DaoManager.createDao(connectionSource, UserProfile.class);
//
//        UserProfile user =
//                new UserProfile(
//                        getFirstName(),
//                        getLastName(),
//                        getUserName(),
//                        getRole(),
//                        isActive(),
//                        canAccessAllBranches());
//        user.setPhone(getPhone());
//        user.setEmail(getEmail());
//        user.setBranch(getBranch());
//
//        userDao.create(user);

        resetProperties();
        getAllUserProfiles();
    }

    public static void getAllUserProfiles() throws Exception {
//        Dao<UserProfile, Long> userDao = DaoManager.createDao(connectionSource, UserProfile.class);
//
//        Platform.runLater(
//                () -> {
//                    usersList.clear();
//
//                    try {
//                        usersList.addAll(userDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, UserViewModel.class);
//                    }
//                });
    }

    public static void getItem(long index) throws Exception {
//        Dao<UserProfile, Long> userDao = DaoManager.createDao(connectionSource, UserProfile.class);
//
//        UserProfile user = userDao.queryForId(index);
//
//        Platform.runLater(
//                () -> {
//                    setId(user.getId());
//                    setFirstName(user.getFirstName());
//                    setLastName(user.getLastName());
//                    setUserName(user.getUserName());
//                    setRole(user.getRole());
//                    setActive(user.isActive());
//                    setPhone(user.getPhone());
//                    setEmail(user.getEmail());
//                    setBranch(user.getBranch());
//                    setAccessAllBranches(user.canAccessAllBranches());
//                });

        getAllUserProfiles();
    }

    public static void updateItem(long index) throws Exception {
//        Dao<UserProfile, Long> userDao = DaoManager.createDao(connectionSource, UserProfile.class);
//
//        UserProfile user = userDao.queryForId(index);
//
//        user.setFirstName(getFirstName());
//        user.setLastName(getLastName());
//        user.setUserName(getUserName());
//        user.setRole(getRole());
//        user.setActive(isActive());
//        user.setAccessAllBranches(canAccessAllBranches());
//
//        userDao.update(user);

        resetProperties();
        getAllUserProfiles();
    }

    public static void deleteItem(long index) throws Exception {
//        Dao<UserProfile, Long> userDao = DaoManager.createDao(connectionSource, UserProfile.class);
//
//        userDao.deleteById(index);

        getAllUserProfiles();
    }
}
