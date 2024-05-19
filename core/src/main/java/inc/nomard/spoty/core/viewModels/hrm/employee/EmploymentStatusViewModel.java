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

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;


public class EmploymentStatusViewModel {
    @Getter
    private static final ObservableList<String> colorsList = FXCollections.observableArrayList("Red", "Blue", "Green", "Orange", "Purple", "Brown");
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty color = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    public static ObservableList<EmploymentStatus> employmentStatusesList = FXCollections.observableArrayList();
    private static final ListProperty<EmploymentStatus> employmentStatuses = new SimpleListProperty<>(employmentStatusesList);
    public static ObservableList<EmploymentStatus> employmentStatusesComboBoxList = FXCollections.observableArrayList();
    public static EmploymentStatusesRepositoryImpl employmentStatusesRepository = new EmploymentStatusesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        EmploymentStatusViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        EmploymentStatusViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getColor() {
        return color.get();
    }

    public static void setColor(String color) {
        EmploymentStatusViewModel.color.set(color);
    }

    public static StringProperty colorProperty() {
        return color;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        EmploymentStatusViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static ObservableList<EmploymentStatus> getEmploymentStatuses() {
        return employmentStatuses.get();
    }

    public static void setEmploymentStatuses(ObservableList<EmploymentStatus> employmentStatuses) {
        EmploymentStatusViewModel.employmentStatuses.set(employmentStatuses);
    }

    public static ListProperty<EmploymentStatus> employmentStatusesProperty() {
        return employmentStatuses;
    }

    public static void saveEmploymentStatus(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var employmentStatus =
                EmploymentStatus.builder()
                        .name(getName())
                        .color(getColor())
                        .description(getDescription())
                        .build();

        var task = employmentStatusesRepository.post(employmentStatus);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void clearEmploymentStatusData() {
        setId(0L);
        setName("");
        setDescription("");
    }

    public static void getAllEmploymentStatuses(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = employmentStatusesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<EmploymentStatus>>() {
                }.getType();
                ArrayList<EmploymentStatus> employmentStatusList = gson.fromJson(task.get().body(), listType);

                employmentStatusesList.clear();
                employmentStatusesList.addAll(employmentStatusList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, EmploymentStatusViewModel.class);
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

        var task = employmentStatusesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var employmentStatus = gson.fromJson(task.get().body(), EmploymentStatus.class);

                setId(employmentStatus.getId());
                setName(employmentStatus.getName());
                setDescription(employmentStatus.getDescription());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, EmploymentStatusViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = employmentStatusesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<EmploymentStatus>>() {
                }.getType();
                ArrayList<EmploymentStatus> employmentStatusList = gson.fromJson(task.get().body(), listType);

                employmentStatusesList.clear();
                employmentStatusesList.addAll(employmentStatusList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, EmploymentStatusViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var employmentStatus = EmploymentStatus.builder()
                .id(getId())
                .name(getName())
                .color(getColor())
                .description(getDescription())
                .build();

        var task = employmentStatusesRepository.put(employmentStatus);
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

        var task = employmentStatusesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
