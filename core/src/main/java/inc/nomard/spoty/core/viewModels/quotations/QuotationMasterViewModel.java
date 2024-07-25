package inc.nomard.spoty.core.viewModels.quotations;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
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
public class QuotationMasterViewModel {
    @Getter
    public static final ObservableList<QuotationMaster> quotationsList =
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
    private static final ListProperty<QuotationMaster> quotations =
            new SimpleListProperty<>(quotationsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty taxRate = new SimpleStringProperty("");
    private static final StringProperty discount = new SimpleStringProperty("");
    private static final StringProperty shippingFee = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final QuotationsRepositoryImpl quotationRepository = new QuotationsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        QuotationMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        QuotationMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        QuotationMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        QuotationMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        QuotationMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static String getTaxRate() {
        return (taxRate.get().isEmpty() && taxRate.get().isBlank()) ? "0" : taxRate.get();
    }

    public static void setTaxRate(String taxRate) {
        QuotationMasterViewModel.taxRate.set(taxRate);
    }

    public static StringProperty taxRateProperty() {
        return taxRate;
    }

    public static String getDiscount() {
        return (discount.get().isEmpty() && discount.get().isBlank()) ? "0" : discount.get();
    }

    public static void setDiscount(String discount) {
        QuotationMasterViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static String getShippingFee() {
        return (shippingFee.get().isEmpty() && shippingFee.get().isBlank()) ? "0" : shippingFee.get();
    }

    public static void setShippingFee(String shippingFee) {
        QuotationMasterViewModel.shippingFee.set(shippingFee);
    }

    public static StringProperty shippingFeeProperty() {
        return shippingFee;
    }

    public static ObservableList<QuotationMaster> getQuotations() {
        return quotations.get();
    }

    public static void setQuotations(ObservableList<QuotationMaster> quotations) {
        QuotationMasterViewModel.quotations.set(quotations);
    }

    public static ListProperty<QuotationMaster> quotationsProperty() {
        return quotations;
    }

    public static void resetProperties() {
        setId(0L);
        setCustomer(null);
        setBranch(null);
        setTaxRate("");
        setDiscount("");
        setShippingFee("");
        setNote("");
        QuotationDetailViewModel.quotationDetailsList.clear();
        PENDING_DELETES.clear();
    }

    public static void saveQuotationMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer successMessage,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        var quotationMaster = QuotationMaster.builder()
                .customer(getCustomer())
                .notes(getNote())
                .status(getStatus())
                .discount(Double.parseDouble(getDiscount()))
                .shippingFee(Double.parseDouble(getShippingFee()))
                .build();
        if (!QuotationDetailViewModel.quotationDetailsList.isEmpty()) {
            quotationMaster.setQuotationDetails(
                    QuotationDetailViewModel.getQuotationDetailsList());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.post(quotationMaster);
        responseFuture.thenAccept(response -> {
            Platform.runLater(() -> {
                // Handle successful response
                if (response.statusCode() == 201) {
                    // Process the successful response
                    onSuccess.run();
                    successMessage.showMessage("Quotation created successfully");
                } else if (response.statusCode() == 401) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Access denied");
                    }
                } else if (response.statusCode() == 404) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Resource not found");
                    }
                } else if (response.statusCode() == 500) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An error occurred");
                    }
                }
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                // Handle exceptions during the request (e.g., network issues)
                if (Connectivity.isConnectedToInternet()) {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An in app error occurred");
                    }
                } else {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("No Internet Connection");
                    }
                }
            });
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void getAllQuotationMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                              SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            Platform.runLater(() -> {
                // Handle successful response
                if (response.statusCode() == 200) {
                    // Process the successful response
                    Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
                    }.getType();
                    ArrayList<QuotationMaster> quotationMasterList = gson.fromJson(response.body(), listType);
                    quotationsList.clear();
                    quotationsList.addAll(quotationMasterList);
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
                } else if (response.statusCode() == 401) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Access denied");
                    }
                } else if (response.statusCode() == 404) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Resource not found");
                    }
                } else if (response.statusCode() == 500) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An error occurred");
                    }
                }
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                // Handle exceptions during the request (e.g., network issues)
                if (Connectivity.isConnectedToInternet()) {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An in app error occurred");
                    }
                } else {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("No Internet Connection");
                    }
                }
            });
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void getQuotationMaster(Long index,
                                          SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                          SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            Platform.runLater(() -> {
                // Handle successful response
                if (response.statusCode() == 200) {
                    // Process the successful response
                    var quotationMaster = gson.fromJson(response.body(), QuotationMaster.class);
                    setId(quotationMaster.getId());
                    setNote(quotationMaster.getNotes());
                    setTaxRate(getTaxRate());
                    setDiscount(getDiscount());
                    setShippingFee(getShippingFee());
                    QuotationDetailViewModel.quotationDetailsList.clear();
                    QuotationDetailViewModel.quotationDetailsList.addAll(quotationMaster.getQuotationDetails());
                    onSuccess.run();
                } else if (response.statusCode() == 401) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Access denied");
                    }
                } else if (response.statusCode() == 404) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Resource not found");
                    }
                } else if (response.statusCode() == 500) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An error occurred");
                    }
                }
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                // Handle exceptions during the request (e.g., network issues)
                if (Connectivity.isConnectedToInternet()) {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An in app error occurred");
                    }
                } else {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("No Internet Connection");
                    }
                }
            });
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            Platform.runLater(() -> {
                // Handle successful response
                if (response.statusCode() == 200) {
                    // Process the successful response
                    Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
                    }.getType();
                    ArrayList<QuotationMaster> quotationMasterList = gson.fromJson(
                            response.body(), listType);
                    quotationsList.clear();
                    quotationsList.addAll(quotationMasterList);
                    onSuccess.run();
                } else if (response.statusCode() == 401) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Access denied");
                    }
                } else if (response.statusCode() == 404) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Resource not found");
                    }
                } else if (response.statusCode() == 500) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An error occurred");
                    }
                }
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                // Handle exceptions during the request (e.g., network issues)
                if (Connectivity.isConnectedToInternet()) {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An in app error occurred");
                    }
                } else {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("No Internet Connection");
                    }
                }
            });
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var quotationMaster = QuotationMaster.builder()
                .id(getId())
                .customer(getCustomer())
                .status(getStatus())
                .discount(Double.parseDouble(getDiscount()))
                .notes(getNote())
                .shippingFee(Double.parseDouble(getShippingFee()))
                .build();
        if (!QuotationDetailViewModel.getQuotationDetailsList().isEmpty()) {
            quotationMaster.setQuotationDetails(QuotationDetailViewModel.getQuotationDetailsList());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.put(quotationMaster);
        responseFuture.thenAccept(response -> {
            Platform.runLater(() -> {
                // Handle successful response
                if (response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 204) {
                    // Process the successful response
                    onSuccess.run();
                    successMessage.showMessage("Quotation updated successfully");
                } else if (response.statusCode() == 401) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Access denied");
                    }
                } else if (response.statusCode() == 404) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Resource not found");
                    }
                } else if (response.statusCode() == 500) {
                    // Handle non-200 status codes
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An error occurred");
                    }
                }
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                // Handle exceptions during the request (e.g., network issues)
                if (Connectivity.isConnectedToInternet()) {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An in app error occurred");
                    }
                } else {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("No Internet Connection");
                    }
                }
            });
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {

        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.delete(findModel);

        responseFuture.thenAccept(response -> {
            // Logging response status for debugging
            System.out.println("Response status code: " + response.statusCode());

            Platform.runLater(() -> {
                if (response.statusCode() == 200 || response.statusCode() == 204) {
                    // Process the successful response
                    System.out.println("Success response received");
                    onSuccess.run();
                    successMessage.showMessage("Quotation deleted successfully");
                } else if (response.statusCode() == 401) {
                    // Handle non-200 status codes
                    System.out.println("Access denied response received");
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Access denied");
                    }
                } else if (response.statusCode() == 404) {
                    // Handle non-200 status codes
                    System.out.println("Resource not found response received");
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("Resource not found");
                    }
                } else if (response.statusCode() == 500) {
                    // Handle non-200 status codes
                    System.out.println("Error response received");
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An error occurred");
                    }
                }
            });
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            throwable.printStackTrace();
            Platform.runLater(() -> {
                if (Connectivity.isConnectedToInternet()) {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("An in-app error occurred");
                    }
                } else {
                    if (Objects.nonNull(errorMessage)) {
                        errorMessage.showMessage("No Internet Connection");
                    }
                }
            });
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }
}
