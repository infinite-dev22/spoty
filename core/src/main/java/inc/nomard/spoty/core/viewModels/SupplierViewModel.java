package inc.nomard.spoty.core.viewModels;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.*;
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
import lombok.extern.java.*;

@Log
public class SupplierViewModel {
    public static final ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
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
    private static final ListProperty<Supplier> suppliers = new SimpleListProperty<>(suppliersList);
    private static final SuppliersRepositoryImpl suppliersRepository = new SuppliersRepositoryImpl();
    private static LongProperty id;
    private static StringProperty name;
    private static StringProperty email;
    private static StringProperty phone;
    private static StringProperty city;
    private static StringProperty address;
    private static StringProperty taxNumber;
    private static StringProperty country;

    static {
        id = new SimpleLongProperty(0);
        name = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        phone = new SimpleStringProperty("");
        city = new SimpleStringProperty("");
        address = new SimpleStringProperty("");
        taxNumber = new SimpleStringProperty("");
        country = new SimpleStringProperty("");
    }

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        SupplierViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        SupplierViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        SupplierViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        SupplierViewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static String getCity() {
        return city.get();
    }

    public static void setCity(String city) {
        SupplierViewModel.city.set(city);
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static String getAddress() {
        return address.get();
    }

    public static void setAddress(String address) {
        SupplierViewModel.address.set(address);
    }

    public static StringProperty addressProperty() {
        return address;
    }

    public static String getTaxNumber() {
        return taxNumber.get();
    }

    public static void setTaxNumber(String taxNumber) {
        SupplierViewModel.taxNumber.set(taxNumber);
    }

    public static StringProperty taxNumberProperty() {
        return taxNumber;
    }

    public static String getCountry() {
        return country.get();
    }

    public static void setCountry(String country) {
        SupplierViewModel.country.set(country);
    }

    public static StringProperty countryProperty() {
        return country;
    }

    public static ObservableList<Supplier> getSuppliers() {
        return suppliers.get();
    }

    public static void setSuppliers(ObservableList<Supplier> suppliers) {
        SupplierViewModel.suppliers.set(suppliers);
    }

    public static ListProperty<Supplier> suppliersProperty() {
        return suppliers;
    }

    public static void resetProperties() {
        setId(0);
        setName("");
        setEmail("");
        setEmail("");
        setPhone("");
        setCity("");
        setAddress("");
        setTaxNumber("");
        setCountry("");
        id = null;
        name = null;
        email = null;
        phone = null;
        city = null;
        address = null;
        taxNumber = null;
        country = null;
    }

    public static void saveSupplier(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                    SpotyGotFunctional.MessageConsumer successMessage,
                                    SpotyGotFunctional.MessageConsumer errorMessage) {
        var supplier = Supplier.builder()
                .name(getName())
                .email(getEmail())
                .phone(getPhone())
                .city(getCity())
                .address(getAddress())
                .taxNumber(getTaxNumber())
                .country(getCountry())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = suppliersRepository.post(supplier);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Supplier created successfully");
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
            SpotyLogger.writeToFile(throwable, SupplierViewModel.class);
            return null;
        });
    }

    public static void getAllSuppliers(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                       SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = suppliersRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<Supplier>>() {
                    }.getType();
                    ArrayList<Supplier> supplierList = gson.fromJson(response.body(), listType);
                    suppliersList.clear();
                    suppliersList.addAll(supplierList);
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
            SpotyLogger.writeToFile(throwable, SupplierViewModel.class);
            return null;
        });
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = suppliersRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var supplier = gson.fromJson(response.body(), Supplier.class);
                    setId(supplier.getId());
                    setName(supplier.getName());
                    setEmail(supplier.getEmail());
                    setPhone(supplier.getPhone());
                    setCity(supplier.getCity());
                    setCountry(supplier.getCountry());
                    setAddress(supplier.getAddress());
                    setTaxNumber(supplier.getTaxNumber());
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
            SpotyLogger.writeToFile(throwable, SupplierViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = suppliersRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<Supplier>>() {
                    }.getType();
                    ArrayList<Supplier> supplierList = gson.fromJson(
                            response.body(), listType);
                    suppliersList.clear();
                    suppliersList.addAll(supplierList);
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
            SpotyLogger.writeToFile(throwable, SupplierViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var supplier = Supplier.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .phone(getPhone())
                .city(getCity())
                .address(getAddress())
                .taxNumber(getTaxNumber())
                .country(getCountry())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = suppliersRepository.put(supplier);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Supplier updated successfully");
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
            SpotyLogger.writeToFile(throwable, SupplierViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = suppliersRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Supplier deleted successfully");
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
            SpotyLogger.writeToFile(throwable, SupplierViewModel.class);
            return null;
        });
    }
}
