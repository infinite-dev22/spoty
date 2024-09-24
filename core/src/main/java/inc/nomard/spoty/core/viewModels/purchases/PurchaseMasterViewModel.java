package inc.nomard.spoty.core.viewModels.purchases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseMaster;
import inc.nomard.spoty.network_bridge.dtos.response.ResponseModel;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.PurchasesRepositoryImpl;
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
import lombok.extern.java.Log;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

@Log
public class PurchaseMasterViewModel {
    @Getter
    public static final ObservableList<PurchaseMaster> purchasesList = FXCollections.observableArrayList();
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
    private static final ListProperty<PurchaseMaster> purchases = new SimpleListProperty<>(purchasesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private static final ObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Tax> tax = new SimpleObjectProperty<>();
    private static final ObjectProperty<Discount> discount = new SimpleObjectProperty<>();
    private static final StringProperty purchaseStatus = new SimpleStringProperty("");
    private static final StringProperty paymentStatus = new SimpleStringProperty("");
    private static final StringProperty approvalStatus = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final DoubleProperty amountPaid = new SimpleDoubleProperty(0d);
    private static final DoubleProperty amountDue = new SimpleDoubleProperty(0d);
    private static final DoubleProperty shippingFee = new SimpleDoubleProperty(0d);
    private static final PurchasesRepositoryImpl purchasesRepository = new PurchasesRepositoryImpl();
    private static final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageNumber = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageSize = new SimpleIntegerProperty(50);

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static LocalDate getDate() {
        return date.get();
    }

    public static void setDate(LocalDate date) {
        PurchaseMasterViewModel.date.set(date);
    }

    public static ObjectProperty<LocalDate> dateProperty() {
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

    public static String getPurchaseStatus() {
        return purchaseStatus.get();
    }

    public static void setPurchaseStatus(String purchaseStatus) {
        PurchaseMasterViewModel.purchaseStatus.set(purchaseStatus);
    }

    public static StringProperty purchaseStatusProperty() {
        return purchaseStatus;
    }

    public static String getPaymentStatus() {
        return paymentStatus.get();
    }

    public static void setPaymentStatus(String paymentStatus) {
        PurchaseMasterViewModel.paymentStatus.set(paymentStatus);
    }

    public static StringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    public static String getApprovalStatus() {
        return approvalStatus.get();
    }

    public static void setApprovalStatus(String approvalStatus) {
        PurchaseMasterViewModel.approvalStatus.set(approvalStatus);
    }

    public static StringProperty approvalStatusProperty() {
        return approvalStatus;
    }

    public static Tax getTax() {
        return tax.get();
    }

    public static void setTax(Tax tax) {
        PurchaseMasterViewModel.tax.set(tax);
    }

    public static ObjectProperty<Tax> netTaxProperty() {
        return tax;
    }

    public static Discount getDiscount() {
        return discount.get();
    }

    public static void setDiscount(Discount discount) {
        PurchaseMasterViewModel.discount.set(discount);
    }

    public static ObjectProperty<Discount> discountProperty() {
        return discount;
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

    public static Double getShippingFee() {
        return shippingFee.get();
    }

    public static void setShippingFee(Double shippingFee) {
        PurchaseMasterViewModel.shippingFee.set(shippingFee);
    }

    public static DoubleProperty shippingFeeProperty() {
        return shippingFee;
    }

    public static Double getAmountPaid() {
        return amountPaid.get();
    }

    public static void setAmountPaid(Double amountPaid) {
        PurchaseMasterViewModel.amountPaid.set(amountPaid);
    }

    public static DoubleProperty amountPaidProperty() {
        return amountPaid;
    }

    public static Double getAmountDue() {
        return amountDue.get();
    }

    public static void setAmountDue(Double amountDue) {
        PurchaseMasterViewModel.amountDue.set(amountDue);
    }

    public static DoubleProperty amountDueProperty() {
        return amountDue;
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

    public static Integer getTotalPages() {
        return totalPages.get();
    }

    public static void setTotalPages(Integer totalPages) {
        PurchaseMasterViewModel.totalPages.set(totalPages);
    }

    public static IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public static Integer getPageNumber() {
        return pageNumber.get();
    }

    public static void setPageNumber(Integer pageNumber) {
        PurchaseMasterViewModel.pageNumber.set(pageNumber);
    }

    public static IntegerProperty pageNumberProperty() {
        return pageNumber;
    }

    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer pageSize) {
        PurchaseMasterViewModel.pageSize.set(pageSize);
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
            setPurchaseStatus("");
            setNote("");
            setShippingFee(0d);
            setAmountPaid(0d);
            setAmountDue(0d);
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
                .tax(getTax())
                .discount(getDiscount())
                .amountPaid(getAmountPaid())
                .shippingFee(getShippingFee())
                .purchaseStatus(getPurchaseStatus())
                .paymentStatus("")
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
                                             SpotyGotFunctional.MessageConsumer errorMessage, Integer pageNo, Integer pageSize) {
        CompletableFuture<HttpResponse<String>> responseFuture = purchasesRepository.fetchAll(pageNo, pageSize);
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<ResponseModel<PurchaseMaster>>() {
                    }.getType();
                    ResponseModel<PurchaseMaster> responseModel = gson.fromJson(response.body(), type);
                    setTotalPages(responseModel.getTotalPages());
                    setPageNumber(responseModel.getPageable().getPageNumber());
                    setPageSize(responseModel.getPageable().getPageSize());
                    ArrayList<PurchaseMaster> purchaseList = responseModel.getContent();
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
                    setDate(purchaseMaster.getDate());
                    setSupplier(purchaseMaster.getSupplier());
                    setTax(purchaseMaster.getTax());
                    setDiscount(purchaseMaster.getDiscount());
                    setPurchaseStatus(purchaseMaster.getPurchaseStatus());
                    setPaymentStatus(purchaseMaster.getPaymentStatus());
                    setApprovalStatus(purchaseMaster.getApprovalStatus());
                    setShippingFee(purchaseMaster.getShippingFee());
                    setAmountPaid(purchaseMaster.getAmountPaid());
                    setAmountDue(purchaseMaster.getAmountDue());
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
                .tax(getTax())
                .discount(getDiscount())
                .amountPaid(getAmountPaid())
                .shippingFee(getShippingFee())
                .purchaseStatus(getPurchaseStatus())
                .paymentStatus("")
                .notes(getNotes())
                .build();
        if (!PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
            purchaseMaster.setPurchaseDetails(PurchaseDetailViewModel.getPurchaseDetails());
        }
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
        log.severe(throwable.getMessage());
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
