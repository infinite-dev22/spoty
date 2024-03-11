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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.BeneficiaryBadge;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.BeneficiaryType;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.BeneficiaryBadgeRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class BeneficiaryBadgeViewModel {
    public static final ObservableList<BeneficiaryBadge> beneficiaryBadgesList = FXCollections.observableArrayList();
    public static final ListProperty<BeneficiaryBadge> beneficiaryBadges = new SimpleListProperty<>(beneficiaryBadgesList);
    public static final ObservableList<BeneficiaryType> beneficiaryTypesList = FXCollections.observableArrayList();
    public static final ListProperty<BeneficiaryType> beneficiaryTypes = new SimpleListProperty<>(beneficiaryTypesList);
    public static final ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    public static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty color = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final ObjectProperty<BeneficiaryType> beneficiaryType = new SimpleObjectProperty<>();
    private static final BeneficiaryBadgeRepositoryImpl beneficiaryBadgeRepository = new BeneficiaryBadgeRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        BeneficiaryBadgeViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
            return name.get();
    }

    public static void setName(String name) {
        BeneficiaryBadgeViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getColor() {
            return color.get();
    }

    public static void setColor(String color) {
        BeneficiaryBadgeViewModel.color.set(color);
    }

    public static StringProperty colorProperty() {
        return color;
    }

    public static String getDescription() {
            return description.get();
    }

    public static void setDescription(String createdOn) {
        BeneficiaryBadgeViewModel.description.set(createdOn);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static BeneficiaryType getBeneficiaryType() {
        return beneficiaryType.get();
    }

    public static void setBeneficiaryType(BeneficiaryType beneficiaryType) {
        BeneficiaryBadgeViewModel.beneficiaryType.set(beneficiaryType);
    }

    public static ObjectProperty<BeneficiaryType> beneficiaryTypeProperty() {
        return beneficiaryType;
    }

    public static ObservableList<BeneficiaryBadge> getBeneficiaryBadges() {
        return beneficiaryBadges.get();
    }

    public static void setBeneficiaryBadges(ObservableList<BeneficiaryBadge> beneficiaryBadges) {
        BeneficiaryBadgeViewModel.beneficiaryBadges.set(beneficiaryBadges);
    }

    public static ListProperty<BeneficiaryBadge> beneficiaryBadgeProperty() {
        return beneficiaryBadges;
    }

    public static void resetProperties() {
        setId(0);
        setName("");
        setColor("");
        setDescription("");
        setBeneficiaryType(null);
    }

    public static void saveBeneficiaryBadge(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var beneficiaryBadge = BeneficiaryBadge.builder()
                .name(getName())
                .beneficiaryType(getBeneficiaryType())
                .color(getColor())
                .description(getDescription())
                .build();

        var task = beneficiaryBadgeRepository.post(beneficiaryBadge);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getAllBeneficiaryBadges(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var task = beneficiaryBadgeRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<BeneficiaryBadge>>() {
                }.getType();
                ArrayList<BeneficiaryBadge> beneficiaryBadgeList = gson.fromJson(task.get().body(), listType);

                beneficiaryBadgesList.clear();
                beneficiaryBadgesList.addAll(beneficiaryBadgeList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BeneficiaryBadgeViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = beneficiaryBadgeRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var beneficiaryBadge = gson.fromJson(task.get().body(), BeneficiaryBadge.class);

                setId(beneficiaryBadge.getId());

                setName(beneficiaryBadge.getName());
                setBeneficiaryType(beneficiaryBadge.getBeneficiaryType());
                setColor(beneficiaryBadge.getColor());
                setDescription(beneficiaryBadge.getDescription());

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
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = beneficiaryBadgeRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<BeneficiaryBadge>>() {
                }.getType();
                ArrayList<BeneficiaryBadge> beneficiaryBadgeList = gson.fromJson(task.get().body(), listType);

                beneficiaryBadgesList.clear();
                beneficiaryBadgesList.addAll(beneficiaryBadgeList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BeneficiaryBadgeViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var beneficiaryBadge = BeneficiaryBadge.builder()
                .id(getId())
                .name(getName())
                .beneficiaryType(getBeneficiaryType())
                .color(getColor())
                .description(getDescription())
                .build();

        var task = beneficiaryBadgeRepository.put(beneficiaryBadge);
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

        var task = beneficiaryBadgeRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
