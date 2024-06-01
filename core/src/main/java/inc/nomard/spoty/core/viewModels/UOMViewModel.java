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
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class UOMViewModel {
    @Getter
    public static final ObservableList<String> operatorList = FXCollections.observableArrayList("Multiply(*)", "Divide(/)");
    public static final ObservableList<UnitOfMeasure> uomsList = FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<UnitOfMeasure> unitsOfMeasure =
            new SimpleListProperty<>(uomsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty shortName = new SimpleStringProperty("");
    private static final ObjectProperty<UnitOfMeasure> baseUnit = new SimpleObjectProperty<>();
    private static final StringProperty operator = new SimpleStringProperty("");
    private static final StringProperty operatorValue = new SimpleStringProperty("");
    private static final UnitsOfMeasureRepositoryImpl uomRepository = new UnitsOfMeasureRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        UOMViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        UOMViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getShortName() {
        return shortName.get();
    }

    public static void setShortName(String shortName) {
        UOMViewModel.shortName.set(shortName);
    }

    public static StringProperty shortNameProperty() {
        return shortName;
    }

    public static UnitOfMeasure getBaseUnit() {
        return baseUnit.get();
    }

    public static void setBaseUnit(UnitOfMeasure baseUnit) {
        UOMViewModel.baseUnit.set(baseUnit);
    }

    public static ObjectProperty<UnitOfMeasure> baseUnitProperty() {
        return baseUnit;
    }

    public static String getOperator() {
        if (Objects.equals(operator.get(), "Multiply(*)")) {
            return "*";
        }
        if (Objects.equals(operator.get(), "Divide(/)")) {
            return "/";
        }
        return "*";
    }

    public static void setOperator(String operator) {
        UOMViewModel.operator.set(operator);
    }

    public static StringProperty operatorProperty() {
        return operator;
    }

    public static double getOperatorValue() {
        return Double.parseDouble((!operatorValue.get().isEmpty()) ? operatorValue.get() : "1.00");
    }

    public static void setOperatorValue(String operatorValue) {
        UOMViewModel.operatorValue.set(operatorValue);
    }

    public static StringProperty operatorValueProperty() {
        return operatorValue;
    }

    public static ObservableList<UnitOfMeasure> getUnitsOfMeasure() {
        return unitsOfMeasure.get();
    }

    public static void setUnitsOfMeasure(ObservableList<UnitOfMeasure> unitsOfMeasure) {
        UOMViewModel.unitsOfMeasure.set(unitsOfMeasure);
    }

    public static ListProperty<UnitOfMeasure> unitsOfMeasureProperty() {
        return unitsOfMeasure;
    }

    public static void saveUOM(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                               SpotyGotFunctional.MessageConsumer successMessage,
                               SpotyGotFunctional.MessageConsumer errorMessage) {
        var uom = UnitOfMeasure.builder()
                .name(getName())
                .shortName(getShortName())
                .baseUnit(getBaseUnit())
                .operator(getOperator())
                .operatorValue(getOperatorValue())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = uomRepository.post(uom);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Unity Of Measure created successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, UOMViewModel.class);
            return null;
        });
    }

    public static void resetUOMProperties() {
        setId(0);
        setName("");
        setShortName("");
        setBaseUnit(null);
        setOperator("");
        setOperatorValue("");
    }

    public static void getAllUOMs(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = uomRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<UnitOfMeasure>>() {
                }.getType();
                ArrayList<UnitOfMeasure> uomList = gson.fromJson(response.body(), listType);
                uomsList.clear();
                uomsList.addAll(uomList);
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, UOMViewModel.class);
            return null;
        });
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = uomRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                var uom = gson.fromJson(response.body(), UnitOfMeasure.class);
                setId(uom.getId());
                setName(uom.getName());
                setShortName(uom.getShortName());
                setBaseUnit(uom.getBaseUnit());
                setOperator(uom.getOperator());
                setOperatorValue(
                        String.valueOf(uom.getOperatorValue() == 0 ? "" : uom.getOperatorValue()));
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, UOMViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = uomRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<UnitOfMeasure>>() {
                }.getType();
                ArrayList<UnitOfMeasure> uomList = gson.fromJson(
                        response.body(), listType);
                uomsList.clear();
                uomsList.addAll(uomList);
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, UOMViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var uom = UnitOfMeasure.builder()
                .id(getId())
                .name(getName())
                .shortName(getShortName())
                .baseUnit(getBaseUnit())
                .operator(getOperator())
                .operatorValue(getOperatorValue())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = uomRepository.put(uom);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Unity Of Measure updated successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, UOMViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = uomRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Unity Of Measure deleted successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, UOMViewModel.class);
            return null;
        });
    }
}
