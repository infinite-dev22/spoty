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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Permission;
import org.infinite.spoty.database.models.Role;
import org.infinite.spoty.database.models.RolePermission;

public class RoleViewModel {
    private static final ObservableList<Role> rolesList = FXCollections.observableArrayList();
    private static final ListProperty<Role> roles = new SimpleListProperty<>(rolesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty();
    private static final StringProperty description = new SimpleStringProperty();
    private static final PreparedQuery<Permission> permissionsForRoleQuery = null;
    private static final SQLiteConnection connection = SQLiteConnection.getInstance();
    private static final ConnectionSource connectionSource = connection.getConnection();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        RoleViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        RoleViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        RoleViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static void saveRole() throws SQLException {
        Dao<Role, Long> roleDao = DaoManager.createDao(connectionSource, Role.class);
        Dao<RolePermission, Long> rolePermissionDao =
                DaoManager.createDao(connectionSource, RolePermission.class);

        Role role = new Role(getName(), getDescription());

        roleDao.create(role);

        Platform.runLater(
                () ->
                        PermissionsViewModel.getPermissionsList()
                                .forEach(
                                        permission -> {
                                            try {
                                                rolePermissionDao.create(new RolePermission(role, permission));
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }));

        resetRoleProperties();
        PermissionsViewModel.getPermissionsList().clear();
        getAllRoles();
    }

    public static void resetRoleProperties() {
        setId(0);
        setName("");
        setDescription("");
        PermissionsViewModel.resetPermissionsProperties();
    }

    public static ObservableList<Role> getRolesList() {
        return rolesList;
    }

    public static ObservableList<Role> getRoles() {
        return roles.get();
    }

    public static void setRoles(ObservableList<Role> roles) {
        RoleViewModel.roles.set(roles);
    }

    public static ListProperty<Role> rolesProperty() {
        return roles;
    }

    public static void getAllRoles() throws SQLException {
        Dao<Role, Long> roleDao = DaoManager.createDao(connectionSource, Role.class);

        Platform.runLater(
                () -> {
                    rolesList.clear();

                    try {
                        rolesList.addAll(roleDao.queryForAll());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static void getItem(long index) throws SQLException {
        Dao<Role, Long> roleDao = DaoManager.createDao(connectionSource, Role.class);

        Role role = roleDao.queryForId(index);

        setId(role.getId());
        setName(role.getName());
        setDescription(role.getDescription());

        getAllRoles();
    }

    public static void updateItem(long index) throws SQLException {
        Dao<Role, Long> roleDao = DaoManager.createDao(connectionSource, Role.class);

        Role role = roleDao.queryForId(index);

        role.setName(getName());
        role.setDescription(getDescription());

        roleDao.update(role);


        resetRoleProperties();
        getAllRoles();
    }

    public static void deleteItem(long index) throws SQLException {
        Dao<Role, Long> roleDao = DaoManager.createDao(connectionSource, Role.class);

        roleDao.deleteById(index);

        getAllRoles();
    }
}
