package inc.nomard.spoty.core.viewModels.returns.sales;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.returns.purchases.PurchaseReturnMasterViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleDetailViewModel;
import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.response.ResponseModel;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.SaleReturnMaster;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.SaleReturnsRepositoryImpl;
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
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

@Log
public class SaleReturnMasterViewModel {
    @Getter
    public static final ObservableList<SaleReturnMaster> saleReturnsList =
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
    private static final ListProperty<SaleReturnMaster> sales = new SimpleListProperty<>(saleReturnsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final StringProperty saleStatus = new SimpleStringProperty("");
    private static final DoubleProperty total = new SimpleDoubleProperty();
    private static final DoubleProperty amountPaid = new SimpleDoubleProperty();
    private static final ObjectProperty<Tax> tax = new SimpleObjectProperty<>();
    private static final ObjectProperty<Discount> discount = new SimpleObjectProperty<>();
    private static final DoubleProperty subTotal = new SimpleDoubleProperty();
    private static final DoubleProperty amountDue = new SimpleDoubleProperty();
    private static final DoubleProperty changeAmount = new SimpleDoubleProperty();
    private static final DoubleProperty shippingFee = new SimpleDoubleProperty();
    private static final StringProperty paymentStatus = new SimpleStringProperty("");
    private static final StringProperty notes = new SimpleStringProperty("");
    private static final SaleReturnsRepositoryImpl saleReturnsRepository = new SaleReturnsRepositoryImpl();
    private static final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageNumber = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageSize = new SimpleIntegerProperty(50);

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        SaleReturnMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        SaleReturnMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static String getSaleStatus() {
        return saleStatus.get();
    }

    public static void setSaleStatus(String saleStatus) {
        SaleReturnMasterViewModel.saleStatus.set(saleStatus);
    }

    public static StringProperty saleStatusProperty() {
        return saleStatus;
    }

    public static String getPaymentStatus() {
        return paymentStatus.get();
    }

    public static void setPaymentStatus(String paymentStatus) {
        SaleReturnMasterViewModel.paymentStatus.set(paymentStatus);
    }

    public static StringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    public static String getNotes() {
        return notes.get();
    }

    public static void setNotes(String notes) {
        SaleReturnMasterViewModel.notes.set(notes);
    }

    public static StringProperty notesProperty() {
        return notes;
    }

    public static ObservableList<SaleReturnMaster> getSales() {
        return sales.get();
    }

    public static void setSales(ObservableList<SaleReturnMaster> sales) {
        SaleReturnMasterViewModel.sales.set(sales);
    }

    public static ListProperty<SaleReturnMaster> salesProperty() {
        return sales;
    }

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        SaleReturnMasterViewModel.total.set(total);
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static double getAmountPaid() {
        return amountPaid.get();
    }

    public static void setAmountPaid(double amountPaid) {
        SaleReturnMasterViewModel.amountPaid.set(amountPaid);
    }

    public static DoubleProperty amountPaidProperty() {
        return amountPaid;
    }

    public static Tax getTax() {
        return tax.get();
    }

    public static void setTax(Tax tax) {
        SaleReturnMasterViewModel.tax.set(tax);
    }

    public static ObjectProperty<Tax> netTaxProperty() {
        return tax;
    }

    public static Discount getDiscount() {
        return discount.get();
    }

    public static void setDiscount(Discount discount) {
        SaleReturnMasterViewModel.discount.set(discount);
    }

    public static ObjectProperty<Discount> discountProperty() {
        return discount;
    }

    public static double getSubTotal() {
        return subTotal.get();
    }

    public static void setSubTotal(double subTotal) {
        SaleReturnMasterViewModel.subTotal.set(subTotal);
    }

    public static DoubleProperty subTotalProperty() {
        return subTotal;
    }

    public static double getAmountDue() {
        return amountDue.get();
    }

    public static void setAmountDue(double amountDue) {
        SaleReturnMasterViewModel.amountDue.set(amountDue);
    }

    public static DoubleProperty amountDueProperty() {
        return amountDue;
    }

    public static double getChangeAmount() {
        return changeAmount.get();
    }

    public static void setChangeAmount(double changeAmount) {
        SaleReturnMasterViewModel.changeAmount.set(changeAmount);
    }

    public static DoubleProperty changeAmountProperty() {
        return changeAmount;
    }

    public static double getShippingFee() {
        return shippingFee.get();
    }

    public static void setShippingFee(double shippingFee) {
        SaleReturnMasterViewModel.shippingFee.set(shippingFee);
    }

    public static DoubleProperty shippingFeeProperty() {
        return shippingFee;
    }

    public static Integer getTotalPages() {
        return totalPages.get();
    }

    public static void setTotalPages(Integer totalPages) {
        SaleReturnMasterViewModel.totalPages.set(totalPages);
    }

    public static IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public static Integer getPageNumber() {
        return pageNumber.get();
    }

    public static void setPageNumber(Integer pageNumber) {
        SaleReturnMasterViewModel.pageNumber.set(pageNumber);
    }

    public static IntegerProperty pageNumberProperty() {
        return pageNumber;
    }

    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer pageSize) {
        SaleReturnMasterViewModel.pageSize.set(pageSize);
    }

    public static IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0L);
                    setCustomer(null);
                    setSaleStatus("");
                    setPaymentStatus("");
                    setNotes("");
                    setTotal(0);
                    setAmountPaid(0);
                    setTax(null);
                    setDiscount(null);
                    setSubTotal(0);
                    setAmountDue(0);
                    setChangeAmount(0);
                    setShippingFee(0);
                    PENDING_DELETES.clear();
                    SaleDetailViewModel.resetProperties();
                    setDefaultCustomer();
                });
    }

    public static void saveSaleReturnMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer successMessage,
                                            SpotyGotFunctional.MessageConsumer errorMessage) {
        var sale = SaleReturnMaster.builder()
                .customer(getCustomer())
                .total(getTotal())
                .amountPaid(getAmountPaid())
                .tax(getTax())
                .discount(getDiscount())
                .subTotal(0)
                .amountDue(0)
                .shippingFee(0)
                .saleStatus(getSaleStatus())
                .paymentStatus(getPaymentStatus())
                .notes(getNotes())
                .build();
        if (!SaleDetailViewModel.saleDetailsList.isEmpty()) {
            sale.setSaleReturnDetails(SaleDetailViewModel.saleDetailsList);
        }
        CompletableFuture<HttpResponse<String>> responseFuture = saleReturnsRepository.post(sale);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Sale created successfully");
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void getSaleReturnMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer errorMessage, Integer pageNo, Integer pageSize) {
        setDefaultCustomer();
        CompletableFuture<HttpResponse<String>> responseFuture = saleReturnsRepository.fetchAll(pageNo, pageSize);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type type = new TypeToken<ResponseModel<SaleReturnMaster>>() {
                    }.getType();
                    ResponseModel<SaleReturnMaster> responseModel = gson.fromJson(response.body(), type);
                    setTotalPages(responseModel.getTotalPages());
                    setPageNumber(responseModel.getPageable().getPageNumber());
                    setPageSize(responseModel.getPageable().getPageSize());
                    ArrayList<SaleReturnMaster> saleList = responseModel.getContent();
                    saleReturnsList.clear();
                    saleReturnsList.addAll(saleList);
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void getSaleReturnMaster(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = saleReturnsRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            // Process the successful response
            if (Objects.nonNull(onSuccess)) {
                Platform.runLater(() -> {
                    var saleMaster = gson.fromJson(response.body(), SaleReturnMaster.class);
                    setId(saleMaster.getId());
                    setCustomer(saleMaster.getCustomer());
                    setNotes(saleMaster.getNotes());
                    setSaleStatus(saleMaster.getSaleStatus());
                    setPaymentStatus(saleMaster.getPaymentStatus());
                    setTotal(saleMaster.getTotal());
                    setAmountPaid(saleMaster.getAmountPaid());
                    setTax(saleMaster.getTax());
                    setDiscount(saleMaster.getDiscount());
                    setSubTotal(saleMaster.getSubTotal());
                    setAmountDue(saleMaster.getAmountDue());
                    setShippingFee(saleMaster.getShippingFee());
                    SaleDetailViewModel.saleDetailsList.clear();
                    SaleDetailViewModel.saleDetailsList.addAll(saleMaster.getSaleReturnDetails());
                    onSuccess.run();
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void searchSaleReturnMaster(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = saleReturnsRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<SaleReturnMaster>>() {
                    }.getType();
                    ArrayList<SaleReturnMaster> saleList = gson.fromJson(
                            response.body(), listType);
                    saleReturnsList.clear();
                    saleReturnsList.addAll(saleList);
                    onSuccess.run();
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void updateSaleReturnMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                              SpotyGotFunctional.MessageConsumer successMessage,
                                              SpotyGotFunctional.MessageConsumer errorMessage) {
        var sale = SaleReturnMaster.builder()
                .id(getId())
                .customer(getCustomer())
                .total(getTotal())
                .amountPaid(getAmountPaid())
                .saleStatus(getSaleStatus())
                .paymentStatus(getPaymentStatus())
                .notes(getNotes())
                .build();
        if (!SaleDetailViewModel.getSaleDetailsList().isEmpty()) {
            sale.setSaleReturnDetails(SaleDetailViewModel.getSaleDetailsList());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = saleReturnsRepository.put(sale);
        responseFuture.thenAccept(response -> handleResponse(response, onSuccess, successMessage, errorMessage)).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void deleteSaleReturnMaster(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = saleReturnsRepository.delete(findModel);
        responseFuture.thenAccept(response -> handleResponse(response, onSuccess, successMessage, errorMessage)).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void setDefaultCustomer() {
        var customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Walk-In");
        customer.setLastName("Customer");
        setCustomer(customer);
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
        log.info("STATUS: " + response.statusCode() + ", BODY: " + response.body());
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
        log.severe(throwable.getMessage());
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
