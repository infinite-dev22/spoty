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
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import io.github.palexdev.mfxcore.base.properties.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class PaySlipViewModel {
    public static final ObservableList<PaySlip> paySlipsList = FXCollections.observableArrayList();
    public static final ListProperty<PaySlip> paySlips = new SimpleListProperty<>(paySlipsList);
    public static final ObservableList<User> usersList = FXCollections.observableArrayList();
    public static final ListProperty<User> users = new SimpleListProperty<>(usersList);
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty startDate = new SimpleStringProperty("");
    private static final StringProperty endDate = new SimpleStringProperty("");
    private static final IntegerProperty salariesQuantity = new SimpleIntegerProperty();
    private static final CharProperty status = new CharProperty();
    private static final StringProperty createdOn = new SimpleStringProperty("");
    private static final StringProperty message = new SimpleStringProperty("");
    private static final PaySlipRepositoryImpl paySlipRepository = new PaySlipRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        PaySlipViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Date getStartDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(startDate.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setStartDate(String startDate) {
        PaySlipViewModel.startDate.set(startDate);
    }

    public static StringProperty startDateProperty() {
        return startDate;
    }

    public static Date getEndDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(startDate.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setEndDate(String endDate) {
        PaySlipViewModel.endDate.set(endDate);
    }

    public static StringProperty endDateProperty() {
        return endDate;
    }

    public static Integer getSalariesQuantity() {
        return salariesQuantity.get();
    }

    public static void setSalariesQuantity(Integer salariesQuantity) {
        PaySlipViewModel.salariesQuantity.set(salariesQuantity);
    }

    public static IntegerProperty salariesQuantityProperty() {
        return salariesQuantity;
    }

    public static char getStatus() {
        return status.get();
    }

    public static void setStatus(char status) {
        PaySlipViewModel.status.set(status);
    }

    public static CharProperty statusProperty() {
        return status;
    }

    public static Date getCreatedDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(createdOn.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, RequisitionMasterViewModel.class);
        }
        return null;
    }

    public static void setCreatedDate(String createdOn) {
        PaySlipViewModel.createdOn.set(createdOn);
    }

    public static StringProperty createdDateProperty() {
        return createdOn;
    }

    public static String getMessage() {
        return message.get();
    }

    public static void setMessage(String message) {
        PaySlipViewModel.message.set(message);
    }

    public static StringProperty messageProperty() {
        return message;
    }

    public static ObservableList<PaySlip> getPaySlips() {
        return paySlips.get();
    }

    public static void setPaySlips(ObservableList<PaySlip> paySlips) {
        PaySlipViewModel.paySlips.set(paySlips);
    }

    public static ListProperty<PaySlip> paySlipProperty() {
        return paySlips;
    }

    public static void resetProperties() {
        setId(0);
        setStartDate("");
        setEndDate("");
        setSalariesQuantity(0);
        setStatus('P');
        setCreatedDate("");
        setMessage("");
    }

    public static void savePaySlip(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer successMessage,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        var paySlip = PaySlip.builder()
                .startDate(getStartDate())
                .endDate(getEndDate())
                .salariesQuantity(getSalariesQuantity())
                .status(getStatus())
                .createdOn(getCreatedDate())
                .message(getMessage())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = paySlipRepository.post(paySlip);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Pay slip created successfully");
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
            SpotyLogger.writeToFile(throwable, PaySlipViewModel.class);
            return null;
        });
    }

    public static void getAllPaySlips(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = paySlipRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<PaySlip>>() {
                    }.getType();
                    ArrayList<PaySlip> paySlipList = gson.fromJson(response.body(), listType);
                    paySlipsList.clear();
                    paySlipsList.addAll(paySlipList);
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
            SpotyLogger.writeToFile(throwable, PaySlipViewModel.class);
            return null;
        });
    }

    public static void getItem(Long index,
                               SpotyGotFunctional.ParameterlessConsumer onSuccess,
                               SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = paySlipRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var paySlip = gson.fromJson(response.body(), PaySlip.class);
                    setId(paySlip.getId());
                    setStartDate(paySlip.getLocaleStartDate());
                    setEndDate(paySlip.getLocaleEndDate());
                    setSalariesQuantity(paySlip.getSalariesQuantity());
                    setStatus(paySlip.getStatus());
                    setCreatedDate(paySlip.getLocaleCreatedDate());
                    setMessage(paySlip.getMessage());
                    onSuccess.run();
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
            SpotyLogger.writeToFile(throwable, PaySlipViewModel.class);
            return null;
        });
    }

    public static void searchItem(String search,
                                  SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = paySlipRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<PaySlip>>() {
                    }.getType();
                    ArrayList<PaySlip> paySlipList = gson.fromJson(
                            response.body(), listType);
                    paySlipsList.clear();
                    paySlipsList.addAll(paySlipList);
                    onSuccess.run();
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
            SpotyLogger.writeToFile(throwable, PaySlipViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var paySlip = PaySlip.builder()
                .id(getId())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .salariesQuantity(getSalariesQuantity())
                .status(getStatus())
                .createdOn(getCreatedDate())
                .message(getMessage())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = paySlipRepository.put(paySlip);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Pay slip updated successfully");
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
            SpotyLogger.writeToFile(throwable, PaySlipViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = paySlipRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Pay slip deleted successfully");
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
            SpotyLogger.writeToFile(throwable, PaySlipViewModel.class);
            return null;
        });
    }
}
