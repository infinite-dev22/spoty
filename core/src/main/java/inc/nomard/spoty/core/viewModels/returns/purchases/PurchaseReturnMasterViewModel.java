package inc.nomard.spoty.core.viewModels.returns.purchases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.response.ResponseModel;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.PurchaseReturnMaster;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.PurchaseReturnsRepositoryImpl;
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
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

@Log4j2
public class PurchaseReturnMasterViewModel {
    @Getter
    public static final ObservableList<PurchaseReturnMaster> purchasesList = FXCollections.observableArrayList();
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
    private static final ListProperty<PurchaseReturnMaster> purchases = new SimpleListProperty<>(purchasesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Tax> tax = new SimpleObjectProperty<>();
    private static final ObjectProperty<Discount> discount = new SimpleObjectProperty<>();
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final DoubleProperty amountPaid = new SimpleDoubleProperty(0d);
    private static final DoubleProperty shippingFee = new SimpleDoubleProperty(0d);
    private static final PurchaseReturnsRepositoryImpl purchaseReturnsRepository = new PurchaseReturnsRepositoryImpl();
    private static final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageNumber = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageSize = new SimpleIntegerProperty(50);

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

    public static Double getShippingFee() {
        return shippingFee.get();
    }

    public static void setShippingFee(Double shippingFee) {
        PurchaseReturnMasterViewModel.shippingFee.set(shippingFee);
    }

    public static DoubleProperty shippingFeeProperty() {
        return shippingFee;
    }

    public static Double getAmountPaid() {
        return amountPaid.get();
    }

    public static void setAmountPaid(Double amountPaid) {
        PurchaseReturnMasterViewModel.amountPaid.set(amountPaid);
    }

    public static DoubleProperty amountPaidProperty() {
        return amountPaid;
    }

    public static ObservableList<PurchaseReturnMaster> getPurchaseReturns() {
        return purchases.get();
    }

    public static void setPurchaseReturns(ObservableList<PurchaseReturnMaster> purchases) {
        PurchaseReturnMasterViewModel.purchases.set(purchases);
    }

    public static ListProperty<PurchaseReturnMaster> purchasesProperty() {
        return purchases;
    }

    public static Integer getTotalPages() {
        return totalPages.get();
    }

    public static void setTotalPages(Integer totalPages) {
        PurchaseReturnMasterViewModel.totalPages.set(totalPages);
    }

    public static IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public static Integer getPageNumber() {
        return pageNumber.get();
    }

    public static void setPageNumber(Integer pageNumber) {
        PurchaseReturnMasterViewModel.pageNumber.set(pageNumber);
    }

    public static IntegerProperty pageNumberProperty() {
        return pageNumber;
    }

    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer pageSize) {
        PurchaseReturnMasterViewModel.pageSize.set(pageSize);
    }

    public static IntegerProperty pageSizeProperty() {
        return pageSize;
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
            setShippingFee(0d);
            setAmountPaid(0d);
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
                .amountPaid(getAmountPaid())
                .shippingFee(getShippingFee())
                .purchaseStatus(getStatus())
                .paymentStatus("")
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
                                                   SpotyGotFunctional.MessageConsumer errorMessage, Integer pageNo, Integer pageSize) {
        CompletableFuture<HttpResponse<String>> responseFuture = purchaseReturnsRepository.fetchAll(pageNo, pageSize);
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<ResponseModel<PurchaseReturnMaster>>() {
                    }.getType();
                    ResponseModel<PurchaseReturnMaster> responseModel = gson.fromJson(response.body(), type);
                    setTotalPages(responseModel.getTotalPages());
                    setPageNumber(responseModel.getPageable().getPageNumber());
                    setPageSize(responseModel.getPageable().getPageSize());
                    ArrayList<PurchaseReturnMaster> purchaseList = responseModel.getContent();
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
        var purchaseMaster = PurchaseReturnMaster.builder()
                .id(getId())
                .date(getDate())
                .supplier(getSupplier())
                .tax(getTax())
                .discount(getDiscount())
                .amountPaid(getAmountPaid())
                .shippingFee(getShippingFee())
                .purchaseStatus(getStatus())
                .paymentStatus("")
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
        log.info("STATUS: " + response.statusCode() + " BODY: " + response.body());
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
        log.info("STATUS: " + response.statusCode() + " BODY: " + response.body());
        Platform.runLater(() -> {
            String message = switch (response.statusCode()) {
                case 400 -> "Bad request";
                case 401 -> "Access denied";
                case 403 -> "Forbidden resource";
                case 404 -> "Resource not found";
                case 406 -> "Not acceptable content";
                case 408 -> "Request timeout";
                case 409 -> "Conflicting resources";
                case 422 -> "Unprocessable Content";
                case 429 -> "Too many requests";
                case 500 -> "Server error";
                case 502 -> "Bad gateway";
                case 504 -> "Gateway timeout";
                case 507 -> "Insufficient storage";
                default -> "Client error";
            };
            if (errorMessage != null) {
                errorMessage.showMessage(message);
            }
        });
    }

    private static Void handleException(Throwable throwable, SpotyGotFunctional.MessageConsumer errorMessage) {
        log.error(throwable.getMessage());
        Platform.runLater(() -> {
            SpotyLogger.writeToFile(throwable, PurchaseReturnMasterViewModel.class);
            String message = Connectivity.isConnectedToInternet() ? "An error occurred" : "No Internet Connection";
            if (errorMessage != null) {
                errorMessage.showMessage(message);
            }
        });
        return null;
    }
}
