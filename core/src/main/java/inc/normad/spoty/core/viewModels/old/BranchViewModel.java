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

package inc.normad.spoty.core.viewModels.old;

import inc.normad.spoty.network_bridge.dtos.Branch;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class BranchViewModel {
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

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
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
//        Dao<Branch, Long> branchDao = DaoManager.createDao(connectionSource, Branch.class);
//
//        Branch branch =
//                new Branch(getName(), getCity(), getPhone(), getEmail(), getTown());
//
//        branchDao.create(branch);

        clearBranchData();
        getAllBranches();
    }

    public static void clearBranchData() {
        setId(0);
        setName("");
        setCity("");
        setPhone("");
        setEmail("");
        setTown("");
    }

    public static void getAllBranches() throws Exception {
//        Dao<Branch, Long> branchDao = DaoManager.createDao(connectionSource, Branch.class);

//        Platform.runLater(
//                () -> {
//                    branchesList.clear();
//
//                    try {
//                        branchesList.addAll(branchDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, BranchViewModel.class);
//                    }
//                });
    }

    public static void getItem(long branchID) throws Exception {
//        Dao<Branch, Long> branchDao = DaoManager.createDao(connectionSource, Branch.class);
//
//        Branch branch = branchDao.queryForId(branchID);
//        setBranch(branch);
//        setId(branch.getId());
//        setName(branch.getName());
//        setEmail(branch.getEmail());
//        setPhone(branch.getPhone());
//        setCity(branch.getCity());
//        setTown(branch.getTown());

        getAllBranches();
    }

    public static void updateItem(long index) throws Exception {
//        Dao<Branch, Long> branchDao = DaoManager.createDao(connectionSource, Branch.class);
//
//        Branch branch = branchDao.queryForId(index);
//
//        branch.setName(getName());
//        branch.setCity(getCity());
//        branch.setPhone(getPhone());
//        branch.setEmail(getEmail());
//        branch.setTown(getTown());
//
//        branchDao.update(branch);

        clearBranchData();
        getAllBranches();
    }

    public static void deleteItem(long index) throws Exception {
//        Dao<Branch, Long> branchDao = DaoManager.createDao(connectionSource, Branch.class);
//
//        branchDao.deleteById(index);
        getAllBranches();
    }
}
