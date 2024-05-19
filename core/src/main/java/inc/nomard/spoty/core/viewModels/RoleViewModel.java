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

package inc.nomard.spoty.core.viewModels;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;


public class RoleViewModel {
    //    private static final Gson gson = new GsonBuilder()
//            .registerTypeAdapter(Date.class,
//                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
//            .create();
    @Getter
    private static final ObservableList<Role> rolesList = FXCollections.observableArrayList();
    private static final ListProperty<Role> roles = new SimpleListProperty<>(rolesList);
    @Getter
    private static final ObservableList<Permission> permissionsList = FXCollections.observableArrayList();
    private static final ListProperty<Permission> permissions = new SimpleListProperty<>(permissionsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty();
    private static final StringProperty label = new SimpleStringProperty();
    private static final StringProperty description = new SimpleStringProperty();
    private static final RolesRepositoryImpl rolesRepository = new RolesRepositoryImpl();

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

    public static String getLabel() {
        return label.get();
    }

    public static void setLabel(String label) {
        RoleViewModel.label.set(label);
    }

    public static StringProperty labelProperty() {
        return label;
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

    public static ObservableList<Role> getRoles() {
        return roles.get();
    }

    public static void setRoles(ObservableList<Role> roles) {
        RoleViewModel.roles.set(roles);
    }

    public static ListProperty<Role> rolesProperty() {
        return roles;
    }

    public static ObservableList<Permission> getPermissions() {
        return permissions.get();
    }

    public static void setPermissions(ObservableList<Permission> permissions) {
        RoleViewModel.permissions.set(permissions);
    }

    public static ListProperty<Permission> permissionsProperty() {
        return permissions;
    }

    public static void saveRole(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var role = Role.builder()
                .name(getName())
                .label(getLabel())
                .description(getDescription())
                .permissions(getPermissions())
                .build();

        if (!RoleViewModel.rolesList.isEmpty()) {
            role.setPermissions(PermissionsViewModel.getPermissionsList());
        }

        System.out.println(new Gson().toJson(role));

        var task = rolesRepository.postRole(role);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void resetRoleProperties() {
        setId(0);
        setName("");
        setDescription("");
    }

    public static void getAllRoles(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = rolesRepository.fetchAllRoles();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<Role>>() {
            }.getType();
            ArrayList<Role> rolesRolesList = new ArrayList<>();
            try {
                rolesRolesList = new Gson().fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, RoleViewModel.class);
            }

            rolesList.clear();
            rolesList.addAll(rolesRolesList);

            if (Objects.nonNull(onSuccess)) {
                onSuccess.run();
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = rolesRepository.fetchRole(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var role = new Gson().fromJson(task.get().body(), Role.class);

                setId(role.getId());
                setName(role.getName());
                setLabel(role.getLabel());
                setDescription(role.getDescription());
                setPermissions(FXCollections.observableArrayList(role.getPermissions()));
                PermissionsViewModel.getPermissionsList().clear();
                PermissionsViewModel.addPermissions(role.getPermissions());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, RoleViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var role = Role.builder()
                .id(getId())
                .name(getName())
                .label(getLabel())
                .description(getDescription())
                .permissions(getPermissions())
                .build();

        if (!PermissionsViewModel.getPermissionsList().isEmpty()) {
            role.setPermissions(PermissionsViewModel.getPermissionsList());
        }
        var task = rolesRepository.putRole(role);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = new FindModel();
        findModel.setId(index);

        var task = rolesRepository.deleteRole(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }
}
