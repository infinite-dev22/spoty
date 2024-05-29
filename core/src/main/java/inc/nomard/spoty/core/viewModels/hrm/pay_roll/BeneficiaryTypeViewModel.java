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

package inc.nomard.spoty.core.viewModels.hrm.pay_roll;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
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


import lombok.extern.slf4j.*;

@Slf4j
public class BeneficiaryTypeViewModel {
    public static final ObservableList<BeneficiaryType> beneficiaryTypesList = FXCollections.observableArrayList();
    public static final ListProperty<BeneficiaryType> beneficiaryTypes = new SimpleListProperty<>(beneficiaryTypesList);
    public static final ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    public static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
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
    private static final ObjectProperty<BeneficiaryType> beneficiaryType = new SimpleObjectProperty<>();
    private static final BeneficiaryTypeRepositoryImpl beneficiaryTypeRepository = new BeneficiaryTypeRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        BeneficiaryTypeViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        BeneficiaryTypeViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getColor() {
        return color.get();
    }

    public static void setColor(String color) {
        BeneficiaryTypeViewModel.color.set(color);
    }

    public static StringProperty colorProperty() {
        return color;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String createdOn) {
        BeneficiaryTypeViewModel.description.set(createdOn);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static BeneficiaryType getBeneficiaryType() {
        return beneficiaryType.get();
    }

    public static void setBeneficiaryType(BeneficiaryType beneficiaryType) {
        BeneficiaryTypeViewModel.beneficiaryType.set(beneficiaryType);
    }

    public static ObjectProperty<BeneficiaryType> beneficiaryTypeProperty() {
        return beneficiaryType;
    }

    public static ObservableList<BeneficiaryType> getBeneficiaryTypes() {
        return beneficiaryTypes.get();
    }

    public static void setBeneficiaryTypes(ListProperty<BeneficiaryType> beneficiaryTypes) {
        BeneficiaryTypeViewModel.beneficiaryTypes.set(beneficiaryTypes);
    }

    public static ListProperty<BeneficiaryType> beneficiaryTypesProperty() {
        return beneficiaryTypes;
    }

    public static void resetProperties() {
        setId(0);
        setName("");
        setColor("");
        setDescription("");
        setBeneficiaryType(null);
    }

    public static void saveBeneficiaryType(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var beneficiaryType = BeneficiaryType.builder()
                .name(getName())
                .color(getColor())
                .description(getDescription())
                .build();

        var task = beneficiaryTypeRepository.post(beneficiaryType);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllBeneficiaryTypes(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = beneficiaryTypeRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<BeneficiaryType>>() {
                }.getType();
                ArrayList<BeneficiaryType> beneficiaryTypeList = gson.fromJson(task.get().body(), listType);

                beneficiaryTypesList.clear();
                beneficiaryTypesList.addAll(beneficiaryTypeList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BeneficiaryTypeViewModel.class);
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

        var task = beneficiaryTypeRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var beneficiaryType = gson.fromJson(task.get().body(), BeneficiaryType.class);

                setId(beneficiaryType.getId());

                setName(beneficiaryType.getName());
                setColor(beneficiaryType.getColor());
                setDescription(beneficiaryType.getDescription());

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
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = beneficiaryTypeRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<BeneficiaryType>>() {
                }.getType();
                ArrayList<BeneficiaryType> beneficiaryTypeList = gson.fromJson(task.get().body(), listType);

                beneficiaryTypesList.clear();
                beneficiaryTypesList.addAll(beneficiaryTypeList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BeneficiaryTypeViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var beneficiaryType = BeneficiaryType.builder()
                .id(getId())
                .name(getName())
                .color(getColor())
                .description(getDescription())
                .build();

        var task = beneficiaryTypeRepository.put(beneficiaryType);
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

        var task = beneficiaryTypeRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
