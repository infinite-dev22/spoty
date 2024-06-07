package inc.nomard.spoty.core.viewModels.purchases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseMaster;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.PurchasesRepositoryImpl;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.utils.connectivity.Connectivity;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.java.Log;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

@Log
public class PurchaseMasterViewModel {

    @Getter
    public static final ObservableList<PurchaseMaster> purchasesList = FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new UnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<PurchaseMaster> purchases = new SimpleListProperty<>(purchasesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final PurchasesRepositoryImpl purchasesRepository = new PurchasesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, PurchaseMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        PurchaseMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Supplier getSupplier() {
        return supplier.get();
    }

    public static void setSupplier(Supplier supplier) {
        PurchaseMasterViewModel.supplier.set(supplier);
    }

    public static ObjectProperty<Supplier> supplierProperty() {
        return supplier;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        PurchaseMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static String getNotes() {
        return note.get();
    }

    public static void setNote(String note) {
        PurchaseMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<PurchaseMaster> getPurchases() {
        return purchases.get();
    }

    public static void setPurchases(ObservableList<PurchaseMaster> purchases) {
        PurchaseMasterViewModel.purchases.set(purchases);
    }

    public static ListProperty<PurchaseMaster> purchasesProperty() {
        return purchases;
    }

    public static void resetProperties() {
        Platform.runLater(() -> {
            setId(0L);
            setDate("");
            setSupplier(null);
            setStatus("");
            setNote("");
            PENDING_DELETES.clear();
            PurchaseDetailViewModel.getPurchaseDetails().clear();
        });
    }

    public static void savePurchaseMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                          SpotyGotFunctional.MessageConsumer successMessage,
                                          SpotyGotFunctional.MessageConsumer errorMessage) {
        var purchaseMaster = PurchaseMaster.builder()
                .date(getDate())
                .supplier(getSupplier())
                .taxRate(0)
                .netTax(0)
                .discount(0)
                .amountPaid(0)
                .total(0)
                .amountDue(0)
                .paymentStatus("")
                .purchaseStatus(getStatus())
                .notes(getNotes())
                .build();
        if (!PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.getPurchaseDetails());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = purchasesRepository.post(purchaseMaster);
        responseFuture.thenAccept(response -> handleResponse(response, onSuccess, successMessage, errorMessage))
                .exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void getAllPurchaseMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                             SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = purchasesRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
                    }.getType();
                    ArrayList<PurchaseMaster> purchaseList = gson.fromJson(response.body(), listType);
                    purchasesList.clear();
                    purchasesList.addAll(purchaseList);
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void getPurchaseMaster(Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = purchasesRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    var purchaseMaster = gson.fromJson(response.body(), PurchaseMaster.class);
                    setId(purchaseMaster.getId());
                    setNote(purchaseMaster.getNotes());
                    setDate(purchaseMaster.getLocaleDate());
                    setSupplier(purchaseMaster.getSupplier());
                    setStatus(purchaseMaster.getPurchaseStatus());
                    PurchaseDetailViewModel.getPurchaseDetails().clear();
                    PurchaseDetailViewModel.getPurchaseDetails().addAll(purchaseMaster.getPurchaseDetails());
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void searchItem(String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = purchasesRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<PurchaseMaster>>() {
                    }.getType();
                    ArrayList<PurchaseMaster> purchaseList = gson.fromJson(response.body(), listType);
                    purchasesList.clear();
                    purchasesList.addAll(purchaseList);
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var purchaseMaster = PurchaseMaster.builder()
                .id(getId())
                .date(getDate())
                .supplier(getSupplier())
                .taxRate(0)
                .netTax(0)
                .discount(0)
                .amountPaid(0)
                .total(0)
                .amountDue(0)
                .paymentStatus("")
                .purchaseStatus(getStatus())
                .notes(getNotes())
                .build();
        if (!PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.getPurchaseDetails());
        }Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class,
                        new UnixEpochDateTypeAdapter())
                .create();
        System.out.println(gson.toJson(purchaseMaster));
        CompletableFuture<HttpResponse<String>> responseFuture = purchasesRepository.put(purchaseMaster);
        responseFuture.thenAccept(response -> handleResponse(response, onSuccess, successMessage, errorMessage))
                .exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void deleteItem(Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = purchasesRepository.delete(findModel);
        responseFuture.thenAccept(response -> handleResponse(response, onSuccess, successMessage, errorMessage))
                .exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    private static void handleResponse(HttpResponse<String> response, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                       SpotyGotFunctional.MessageConsumer successMessage,
                                       SpotyGotFunctional.MessageConsumer errorMessage) {
        Platform.runLater(() -> {
            switch (response.statusCode()) {
                case 200:
                case 201:
                case 204:
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                    if (successMessage != null) {
                        successMessage.showMessage("Operation successful");
                    }
                    break;
                default:
                    handleNon200Status(response, errorMessage);
                    break;
            }
        });
    }

    private static void handleNon200Status(HttpResponse<String> response, SpotyGotFunctional.MessageConsumer errorMessage) {
        Platform.runLater(() -> {
            String message;
            switch (response.statusCode()) {
                case 401:
                    message = "Access denied";
                    break;
                case 404:
                    message = "Resource not found";
                    break;
                case 500:
                default:
                    message = "An error occurred";
                    break;
            }
            if (errorMessage != null) {
                errorMessage.showMessage(message);
            }
        });
    }

    private static Void handleException(Throwable throwable, SpotyGotFunctional.MessageConsumer errorMessage) {
        Platform.runLater(() -> {
            SpotyLogger.writeToFile(throwable, PurchaseMasterViewModel.class);
            String message = Connectivity.isConnectedToInternet() ? "An in-app error occurred" : "No Internet Connection";
            if (errorMessage != null) {
                errorMessage.showMessage(message);
            }
        });
        return null;
    }
}
