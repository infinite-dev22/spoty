package inc.nomard.spoty.core.viewModels.returns.purchases;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class PurchaseReturnMasterViewModel {
    @Getter
    public static final ObservableList<PurchaseReturnMaster> purchaseReturnsList = FXCollections.observableArrayList();
    @Getter
    public static final ObservableList<String> statusesList = FXCollections.observableArrayList("Approved",
            "Pending",
            "Rejected",
            "Returned");
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
    private static final ListProperty<PurchaseReturnMaster> purchaseReturns = new SimpleListProperty<>(purchaseReturnsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Tax> tax = new SimpleObjectProperty<>();
    private static final ObjectProperty<Discount> discount = new SimpleObjectProperty<>();
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final PurchaseReturnsRepositoryImpl purchaseReturnsRepository = new PurchaseReturnsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseReturnMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static LocalDate getDate() {
        return date.get();
    }

    public static void setDate(LocalDate date) {
        PurchaseReturnMasterViewModel.date.set(date);
    }

    public static ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public static Supplier getSupplier() {
        return supplier.get();
    }

    public static void setSupplier(Supplier supplier) {
        PurchaseReturnMasterViewModel.supplier.set(supplier);
    }

    public static ObjectProperty<Supplier> supplierProperty() {
        return supplier;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        PurchaseReturnMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static Tax getTax() {
        return tax.get();
    }

    public static void setTax(Tax tax) {
        PurchaseReturnMasterViewModel.tax.set(tax);
    }

    public static ObjectProperty<Tax> netTaxProperty() {
        return tax;
    }

    public static Discount getDiscount() {
        return discount.get();
    }

    public static void setDiscount(Discount discount) {
        PurchaseReturnMasterViewModel.discount.set(discount);
    }

    public static ObjectProperty<Discount> discountProperty() {
        return discount;
    }

    public static String getNotes() {
        return note.get();
    }

    public static void setNote(String note) {
        PurchaseReturnMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<PurchaseReturnMaster> getPurchaseReturns() {
        return purchaseReturns.get();
    }

    public static void setPurchaseReturns(ObservableList<PurchaseReturnMaster> purchaseReturns) {
        PurchaseReturnMasterViewModel.purchaseReturns.set(purchaseReturns);
    }

    public static ListProperty<PurchaseReturnMaster> purchaseReturnsProperty() {
        return purchaseReturns;
    }

    public static void resetProperties() {
        Platform.runLater(() -> {
            setId(0L);
            setDate(null);
            setSupplier(null);
            setTax(null);
            setDiscount(null);
            setStatus("");
            setNote("");
            PENDING_DELETES.clear();
            PurchaseReturnDetailViewModel.getPurchaseReturnDetails().clear();
        });
    }

    public static void savePurchaseReturnMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                                SpotyGotFunctional.MessageConsumer successMessage,
                                                SpotyGotFunctional.MessageConsumer errorMessage) {
        var purchaseMaster = PurchaseReturnMaster.builder()
                .date(getDate())
                .supplier(getSupplier())
                .tax(getTax())
                .discount(getDiscount())
                .amountPaid(0)
                .total(0)
                .amountDue(0)
                .notes(getNotes())
                .build();
        if (!PurchaseReturnDetailViewModel.getPurchaseReturnDetails().isEmpty()) {
            purchaseMaster.setPurchaseReturnDetails(PurchaseReturnDetailViewModel.getPurchaseReturnDetails());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = purchaseReturnsRepository.post(purchaseMaster);
        responseFuture.thenAccept(response -> handleResponse(response, onSuccess, successMessage, errorMessage))
                .exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void getAllPurchaseReturnMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = purchaseReturnsRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<PurchaseReturnMaster>>() {
                    }.getType();
                    ArrayList<PurchaseReturnMaster> purchaseList = gson.fromJson(response.body(), listType);
                    purchaseReturnsList.clear();
                    purchaseReturnsList.addAll(purchaseList);
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void getPurchaseReturnMaster(Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                               SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = purchaseReturnsRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    var purchaseMaster = gson.fromJson(response.body(), PurchaseReturnMaster.class);
                    setId(purchaseMaster.getId());
                    setNote(purchaseMaster.getNotes());
                    setDate(purchaseMaster.getDate());
                    setSupplier(purchaseMaster.getSupplier());
                    PurchaseReturnDetailViewModel.getPurchaseReturnDetails().clear();
                    PurchaseReturnDetailViewModel.getPurchaseReturnDetails().addAll(purchaseMaster.getPurchaseReturnDetails());
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
        CompletableFuture<HttpResponse<String>> responseFuture = purchaseReturnsRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<PurchaseReturnMaster>>() {
                    }.getType();
                    ArrayList<PurchaseReturnMaster> purchaseList = gson.fromJson(response.body(), listType);
                    purchaseReturnsList.clear();
                    purchaseReturnsList.addAll(purchaseList);
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
        var purchaseMaster = PurchaseReturnMaster.builder()
                .id(getId())
                .date(getDate())
                .supplier(getSupplier())
                .tax(getTax())
                .discount(getDiscount())
                .amountPaid(0)
                .total(0)
                .amountDue(0)
                .notes(getNotes())
                .build();
        if (!PurchaseReturnDetailViewModel.getPurchaseReturnDetails().isEmpty()) {
            purchaseMaster.setPurchaseReturnDetails(PurchaseReturnDetailViewModel.getPurchaseReturnDetails());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = purchaseReturnsRepository.put(purchaseMaster);
        responseFuture.thenAccept(response -> handleResponse(response, onSuccess, successMessage, errorMessage))
                .exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void deleteItem(Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = purchaseReturnsRepository.delete(findModel);
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
            SpotyLogger.writeToFile(throwable, PurchaseReturnMasterViewModel.class);
            String message = Connectivity.isConnectedToInternet() ? "An in-app error occurred" : "No Internet Connection";
            if (errorMessage != null) {
                errorMessage.showMessage(message);
            }
        });
        return null;
    }
}
