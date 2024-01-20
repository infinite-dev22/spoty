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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.auth.ProtectedGlobals;
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.BranchesRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


public class BranchViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty title = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty town = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>();
    public static ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    private static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
    public static ObservableList<Branch> branchesComboBoxList = FXCollections.observableArrayList();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        BranchViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        BranchViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        BranchViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        BranchViewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static String getTown() {
        return town.get();
    }

    public static void setTown(String town) {
        BranchViewModel.town.set(town);
    }

    public static StringProperty townProperty() {
        return town;
    }

    public static String getCity() {
        return city.get();
    }

    public static void setCity(String city) {
        BranchViewModel.city.set(city);
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static String getTitle() {
        return title.get();
    }

    public static void setTitle(String title) {
        BranchViewModel.title.set(title);
    }

    public static StringProperty titleProperty() {
        return title;
    }

    public static ObservableList<Branch> getBranches() {
        return branches.get();
    }

    public static void setBranches(ObservableList<Branch> branches) {
        BranchViewModel.branches.set(branches);
    }

    public static ListProperty<Branch> branchesProperty() {
        return branches;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        BranchViewModel.branch.set(branch);
    }

    public static void saveBranch() throws Exception {
        var branchesRepository = new BranchesRepositoryImpl();

        var branch =
                Branch.builder()
                        .name(getName())
                        .city(getCity())
                        .phone(getPhone())
                        .email(getEmail())
                        .town(getTown())
                        .build();

        var response = branchesRepository.post(branch);
        System.out.println(response.body());
        System.out.println("Token: " + ProtectedGlobals.authToken);

        clearBranchData();
        getAllBranches();
    }

    public static void clearBranchData() {
        setId(0L);
        setName("");
        setCity("");
        setPhone("");
        setEmail("");
        setTown("");
    }

    public static void getAllBranches() {
        var branchesRepository = new BranchesRepositoryImpl();
        Type listType = new TypeToken<ArrayList<Branch>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    branchesList.clear();

                    try {
                        ArrayList<Branch> branchList = gson.fromJson(branchesRepository.fetchAll().body(), listType);
                        branchesList.addAll(branchList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, BranchViewModel.class);
                    }
                });
    }

    public static void getItem(Long branchID) throws Exception {
        var branchesRepository = new BranchesRepositoryImpl();
        var findModel = new FindModel();
        findModel.setId(branchID);
        var response = branchesRepository.fetch(findModel).body();
        var branch = gson.fromJson(response, Branch.class);

        setBranch(branch);
        setId(branch.getId());
        setName(branch.getName());
        setEmail(branch.getEmail());
        setPhone(branch.getPhone());
        setCity(branch.getCity());
        setTown(branch.getTown());

        getAllBranches();
    }

    public static void searchItem(String search) throws Exception {
        var branchesRepository = new BranchesRepositoryImpl();
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<Branch>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    branchesList.clear();

                    try {
                        ArrayList<Branch> branchList = gson.fromJson(branchesRepository.search(searchModel).body(), listType);
                        branchesList.addAll(branchList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, BranchViewModel.class);
                    }
                });

    }

    public static void updateItem() throws IOException, InterruptedException {
        var branchesRepository = new BranchesRepositoryImpl();

        var branch = Branch.builder()
                .id(getId())
                .name(getName())
                .city(getCity())
                .phone(getPhone())
                .email(getEmail())
                .town(getTown())
                .build();

        branchesRepository.put(branch);
        clearBranchData();
        getAllBranches();
    }

    public static void deleteItem(Long branchID) throws IOException, InterruptedException {
        var branchesRepository = new BranchesRepositoryImpl();
        var findModel = new FindModel();

        findModel.setId(branchID);
        branchesRepository.delete(findModel);
        getAllBranches();
    }
}
