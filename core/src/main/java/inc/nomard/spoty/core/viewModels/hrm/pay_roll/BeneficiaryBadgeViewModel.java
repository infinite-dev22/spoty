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
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class BeneficiaryBadgeViewModel {
    public static final ObservableList<BeneficiaryBadge> beneficiaryBadgesList = FXCollections.observableArrayList();
    public static final ListProperty<BeneficiaryBadge> beneficiaryBadges = new SimpleListProperty<>(beneficiaryBadgesList);
    public static final ObservableList<BeneficiaryType> beneficiaryTypesList = FXCollections.observableArrayList();
    public static final ListProperty<BeneficiaryType> beneficiaryTypes = new SimpleListProperty<>(beneficiaryTypesList);
    public static final ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    public static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
    @Getter
    private static final ObservableList<String> colorsList = FXCollections.observableArrayList("Red", "Blue", "Green", "Orange", "Purple", "Brown");
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
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

    public static void saveBeneficiaryBadge(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer successMessage,
                                            SpotyGotFunctional.MessageConsumer errorMessage) {
        var beneficiaryBadge = BeneficiaryBadge.builder()
                .name(getName())
                .beneficiaryType(getBeneficiaryType())
                .color(getColor())
                .description(getDescription())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = beneficiaryBadgeRepository.post(beneficiaryBadge);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Beneficiary badge created successfully");
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
            SpotyLogger.writeToFile(throwable, BeneficiaryBadgeViewModel.class);
            return null;
        });
    }

    public static void getAllBeneficiaryBadges(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                               SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = beneficiaryBadgeRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<BeneficiaryBadge>>() {
                    }.getType();
                    ArrayList<BeneficiaryBadge> beneficiaryBadgeList = gson.fromJson(response.body(), listType);
                    beneficiaryBadgesList.clear();
                    beneficiaryBadgesList.addAll(beneficiaryBadgeList);
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
            SpotyLogger.writeToFile(throwable, BeneficiaryBadgeViewModel.class);
            return null;
        });
    }

    public static void getItem(Long index,
                               SpotyGotFunctional.ParameterlessConsumer onSuccess,
                               SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = beneficiaryBadgeRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var beneficiaryBadge = gson.fromJson(response.body(), BeneficiaryBadge.class);
                    setId(beneficiaryBadge.getId());
                    setName(beneficiaryBadge.getName());
                    setBeneficiaryType(beneficiaryBadge.getBeneficiaryType());
                    setColor(beneficiaryBadge.getColor());
                    setDescription(beneficiaryBadge.getDescription());
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
            SpotyLogger.writeToFile(throwable, DepartmentViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = beneficiaryBadgeRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<BeneficiaryBadge>>() {
                    }.getType();
                    ArrayList<BeneficiaryBadge> beneficiaryBadgeList = gson.fromJson(
                            response.body(), listType);
                    beneficiaryBadgesList.clear();
                    beneficiaryBadgesList.addAll(beneficiaryBadgeList);
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
            SpotyLogger.writeToFile(throwable, BeneficiaryBadgeViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var beneficiaryBadge = BeneficiaryBadge.builder()
                .id(getId())
                .name(getName())
                .beneficiaryType(getBeneficiaryType())
                .color(getColor())
                .description(getDescription())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = beneficiaryBadgeRepository.put(beneficiaryBadge);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Beneficiary badge updated successfully");
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
            SpotyLogger.writeToFile(throwable, BeneficiaryBadgeViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = beneficiaryBadgeRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Beneficiary badge deleted successfully");
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
            SpotyLogger.writeToFile(throwable, BeneficiaryBadgeViewModel.class);
            return null;
        });
    }
}
