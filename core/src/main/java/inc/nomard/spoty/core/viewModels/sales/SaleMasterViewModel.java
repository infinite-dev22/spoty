package inc.nomard.spoty.core.viewModels.sales;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
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
public class SaleMasterViewModel {
    @Getter
    public static final ObservableList<SaleMaster> salesList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<SaleMaster> sales = new SimpleListProperty<>(salesList);
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
    private static final SalesRepositoryImpl salesRepository = new SalesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        SaleMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        SaleMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static String getSaleStatus() {
        return saleStatus.get();
    }

    public static void setSaleStatus(String saleStatus) {
        SaleMasterViewModel.saleStatus.set(saleStatus);
    }

    public static StringProperty saleStatusProperty() {
        return saleStatus;
    }

    public static String getPaymentStatus() {
        return paymentStatus.get();
    }

    public static void setPaymentStatus(String paymentStatus) {
        SaleMasterViewModel.paymentStatus.set(paymentStatus);
    }

    public static StringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    public static String getNotes() {
        return notes.get();
    }

    public static void setNotes(String notes) {
        SaleMasterViewModel.notes.set(notes);
    }

    public static StringProperty notesProperty() {
        return notes;
    }

    public static ObservableList<SaleMaster> getSales() {
        return sales.get();
    }

    public static void setSales(ObservableList<SaleMaster> sales) {
        SaleMasterViewModel.sales.set(sales);
    }

    public static ListProperty<SaleMaster> salesProperty() {
        return sales;
    }

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        SaleMasterViewModel.total.set(total);
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static double getAmountPaid() {
        return amountPaid.get();
    }

    public static void setAmountPaid(double amountPaid) {
        SaleMasterViewModel.amountPaid.set(amountPaid);
    }

    public static DoubleProperty amountPaidProperty() {
        return amountPaid;
    }

    public static Tax getTax() {
        return tax.get();
    }

    public static void setTax(Tax tax) {
        SaleMasterViewModel.tax.set(tax);
    }

    public static ObjectProperty<Tax> netTaxProperty() {
        return tax;
    }

    public static Discount getDiscount() {
        return discount.get();
    }

    public static void setDiscount(Discount discount) {
        SaleMasterViewModel.discount.set(discount);
    }

    public static ObjectProperty<Discount> discountProperty() {
        return discount;
    }

    public static double getSubTotal() {
        return subTotal.get();
    }

    public static void setSubTotal(double subTotal) {
        SaleMasterViewModel.subTotal.set(subTotal);
    }

    public static DoubleProperty subTotalProperty() {
        return subTotal;
    }

    public static double getAmountDue() {
        return amountDue.get();
    }

    public static void setAmountDue(double amountDue) {
        SaleMasterViewModel.amountDue.set(amountDue);
    }

    public static DoubleProperty amountDueProperty() {
        return amountDue;
    }

    public static double getChangeAmount() {
        return changeAmount.get();
    }

    public static void setChangeAmount(double changeAmount) {
        SaleMasterViewModel.changeAmount.set(changeAmount);
    }

    public static DoubleProperty changeAmountProperty() {
        return changeAmount;
    }

    public static double getShippingFee() {
        return shippingFee.get();
    }

    public static void setShippingFee(double shippingFee) {
        SaleMasterViewModel.shippingFee.set(shippingFee);
    }

    public static DoubleProperty shippingFeeProperty() {
        return shippingFee;
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

    public static void saveSaleMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer successMessage,
                                      SpotyGotFunctional.MessageConsumer errorMessage) {
        var sale = SaleMaster.builder()
                .customer(getCustomer())
                .total(getTotal())
                .amountPaid(getAmountPaid())
                .tax(getTax())
                .discount(getDiscount())
                .subTotal(0)
                .amountDue(0)
                .changeAmount(0)
                .shippingFee(0)
                .saleStatus(getSaleStatus())
                .paymentStatus(getPaymentStatus())
                .notes(getNotes())
                .build();
        if (!SaleDetailViewModel.saleDetailsList.isEmpty()) {
            sale.setSaleDetails(SaleDetailViewModel.saleDetailsList);
        }
        CompletableFuture<HttpResponse<String>> responseFuture = salesRepository.post(sale);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Sale created successfully");
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
            SpotyLogger.writeToFile(throwable, SaleMasterViewModel.class);
            return null;
        });
    }

    public static void getAllSaleMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        setDefaultCustomer();
        CompletableFuture<HttpResponse<String>> responseFuture = salesRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<SaleMaster>>() {
                    }.getType();
                    ArrayList<SaleMaster> saleList = gson.fromJson(response.body(), listType);
                    salesList.clear();
                    salesList.addAll(saleList);
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
            SpotyLogger.writeToFile(throwable, SaleMasterViewModel.class);
            return null;
        });
    }

    public static void getSaleMaster(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = salesRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            // Process the successful response
            if (Objects.nonNull(onSuccess)) {
                Platform.runLater(() -> {
                    var saleMaster = gson.fromJson(response.body(), SaleMaster.class);
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
                    setChangeAmount(saleMaster.getChangeAmount());
                    setShippingFee(saleMaster.getShippingFee());
                    SaleDetailViewModel.saleDetailsList.clear();
                    SaleDetailViewModel.saleDetailsList.addAll(saleMaster.getSaleDetails());
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
            SpotyLogger.writeToFile(throwable, SaleMasterViewModel.class);
            return null;
        });
    }

    public static void searchSaleMaster(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = salesRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<SaleMaster>>() {
                    }.getType();
                    ArrayList<SaleMaster> saleList = gson.fromJson(
                            response.body(), listType);
                    salesList.clear();
                    salesList.addAll(saleList);
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
            SpotyLogger.writeToFile(throwable, SaleMasterViewModel.class);
            return null;
        });
    }

    public static void updateSaleMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer successMessage,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        var sale = SaleMaster.builder()
                .id(getId())
                .customer(getCustomer())
                .total(getTotal())
                .amountPaid(getAmountPaid())
                .saleStatus(getSaleStatus())
                .paymentStatus(getPaymentStatus())
                .notes(getNotes())
                .build();
        if (!SaleDetailViewModel.getSaleDetailsList().isEmpty()) {
            sale.setSaleDetails(SaleDetailViewModel.getSaleDetailsList());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = salesRepository.put(sale);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Sale updated successfully");
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
            SpotyLogger.writeToFile(throwable, SaleMasterViewModel.class);
            return null;
        });
    }

    public static void deleteSaleMaster(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = salesRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Sale deleted successfully");
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
            SpotyLogger.writeToFile(throwable, SaleMasterViewModel.class);
            return null;
        });
    }

    public static void setDefaultCustomer() {
        var customer = new Customer();
        customer.setId(1L);
        customer.setName("Walk In Customer");
        setCustomer(customer);
    }
}
