package inc.nomard.spoty.core.viewModels.stock_ins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.response.ResponseModel;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.StockInMaster;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.StockInsRepositoryImpl;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.utils.connectivity.Connectivity;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

@Slf4j
public class StockInMasterViewModel {
    @Getter
    public static final ObservableList<StockInMaster> stockInsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();
    private static final ListProperty<StockInMaster> stockIns =
            new SimpleListProperty<>(stockInsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final StockInsRepositoryImpl stockInsRepository = new StockInsRepositoryImpl();
    private static final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageNumber = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageSize = new SimpleIntegerProperty(50);

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        StockInMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        StockInMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        StockInMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        StockInMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        StockInMasterViewModel.totalCost.set(totalCost);
    }

    public static StringProperty totalCostProperty() {
        return totalCost;
    }

    public static ObservableList<StockInMaster> getStockIns() {
        return stockIns.get();
    }

    public static void setStockIns(ObservableList<StockInMaster> stockIns) {
        StockInMasterViewModel.stockIns.set(stockIns);
    }

    public static ListProperty<StockInMaster> stockInsProperty() {
        return stockIns;
    }

    public static Integer getTotalPages() {
        return totalPages.get();
    }

    public static void setTotalPages(Integer totalPages) {
        StockInMasterViewModel.totalPages.set(totalPages);
    }

    public static IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public static Integer getPageNumber() {
        return pageNumber.get();
    }

    public static void setPageNumber(Integer pageNumber) {
        StockInMasterViewModel.pageNumber.set(pageNumber);
    }

    public static IntegerProperty pageNumberProperty() {
        return pageNumber;
    }

    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer pageSize) {
        StockInMasterViewModel.pageSize.set(pageSize);
    }

    public static IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setBranch(null);
                    setNote("");
                    setStatus("");
                    setTotalCost("");
                    PENDING_DELETES.clear();
                    StockInDetailViewModel.stockInDetailsList.clear();
                });
    }

    public static void saveStockInMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer successMessage,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        var stockInMaster = StockInMaster.builder()
                .notes(getNote())
                .build();
        if (!StockInDetailViewModel.stockInDetailsList.isEmpty()) {
            stockInMaster.setStockInDetails(StockInDetailViewModel.getStockInDetailsList());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = stockInsRepository.post(stockInMaster);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Stock in created successfully");
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
            SpotyLogger.writeToFile(throwable, StockInMasterViewModel.class);
            return null;
        });
    }

    public static void getAllStockInMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer errorMessage, Integer pageNo, Integer pageSize) {
        CompletableFuture<HttpResponse<String>> responseFuture = stockInsRepository.fetchAll(pageNo, pageSize);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type type = new TypeToken<ResponseModel<StockInMaster>>() {
                    }.getType();
                    ResponseModel<StockInMaster> responseModel = gson.fromJson(response.body(), type);
                    setTotalPages(responseModel.getTotalPages());
                    setPageNumber(responseModel.getPageable().getPageNumber());
                    setPageSize(responseModel.getPageable().getPageSize());
                    ArrayList<StockInMaster> stockInList = responseModel.getContent();
                    stockInsList.clear();
                    stockInsList.addAll(stockInList);
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
            SpotyLogger.writeToFile(throwable, StockInMasterViewModel.class);
            return null;
        });
    }

    public static void getStockInMaster(Long index,
                                        SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = stockInsRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    StockInMaster stockInMaster = gson.fromJson(response.body(), StockInMaster.class);
                    setId(stockInMaster.getId());
                    setNote(stockInMaster.getNotes());
                    StockInDetailViewModel.stockInDetailsList.clear();
                    StockInDetailViewModel.stockInDetailsList.addAll(stockInMaster.getStockInDetails());
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
            SpotyLogger.writeToFile(throwable, StockInMasterViewModel.class);
            return null;
        });
    }

    public static void searchStockInMaster(String search,
                                           SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = stockInsRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<StockInMaster>>() {
                    }.getType();
                    ArrayList<StockInMaster> stockInList = gson.fromJson(
                            response.body(), listType);
                    stockInsList.clear();
                    stockInsList.addAll(stockInList);
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
            SpotyLogger.writeToFile(throwable, StockInMaster.class);
            return null;
        });
    }

    public static void updateStockInMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer successMessage,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        var stockInMaster = StockInMaster.builder()
                .id(getId())
                .notes(getNote())
                .build();
        if (!StockInDetailViewModel.stockInDetailsList.isEmpty()) {
            stockInMaster.setStockInDetails(StockInDetailViewModel.stockInDetailsList);
        }
        CompletableFuture<HttpResponse<String>> responseFuture = stockInsRepository.put(stockInMaster);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Stock in updated successfully");
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
            SpotyLogger.writeToFile(throwable, StockInMasterViewModel.class);
            return null;
        });
    }

    public static void deleteStockInMaster(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = stockInsRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Stock in deleted successfully");
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
            SpotyLogger.writeToFile(throwable, StockInMasterViewModel.class);
            return null;
        });
    }
}
