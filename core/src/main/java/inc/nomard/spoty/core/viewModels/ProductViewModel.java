package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.response.ResponseModel;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.ProductsRepositoryImpl;
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
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static inc.nomard.spoty.core.values.SharedResources.setTempId;

@Slf4j
public class ProductViewModel {
    public static final ObservableList<Product> productsList = FXCollections.observableArrayList();
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
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Brand> brand = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<ProductCategory> category = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<File> imageFile = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> unit = new SimpleObjectProperty<>(null);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty barcodeType = new SimpleStringProperty("");
    private static final StringProperty image = new SimpleStringProperty("");
    private static final LongProperty quantity = new SimpleLongProperty(0);
    private static final StringProperty cost = new SimpleStringProperty();
    private static final StringProperty price = new SimpleStringProperty();
    private static final ObjectProperty<Discount> discount = new SimpleObjectProperty<>();
    private static final ObjectProperty<Tax> tax = new SimpleObjectProperty<>();
    private static final StringProperty taxType = new SimpleStringProperty("");
    private static final StringProperty stockAlert = new SimpleStringProperty();
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final ProductsRepositoryImpl productsRepository = new ProductsRepositoryImpl();
    private static final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageNumber = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageSize = new SimpleIntegerProperty(50);

    public static Long getId() {
        return id.get();
    }

    public static void setId(long id) {
        ProductViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static UnitOfMeasure getUnit() {
        return unit.get();
    }

    public static void setUnit(UnitOfMeasure unit) {
        ProductViewModel.unit.set(unit);
    }

    public static ObjectProperty<UnitOfMeasure> unitProperty() {
        return unit;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ProductViewModel.name.set(name != null ? name : "");
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static long getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(long quantity) {
        ProductViewModel.quantity.set(quantity);
    }

    public static LongProperty quantityProperty() {
        return quantity;
    }

    public static double getCost() {
        return (Objects.equals(cost.get(), null) || cost.get().isEmpty())
                ? 0.0
                : Double.parseDouble(cost.get());
    }

    public static void setCost(double cost) {
        ProductViewModel.cost.set((cost == 0) ? "" : String.valueOf(cost));
    }

    public static StringProperty costProperty() {
        return cost;
    }

    public static double getPrice() {
        return (Objects.equals(price.get(), null) || price.get().isEmpty())
                ? 0.0
                : Double.parseDouble(price.get());
    }

    public static void setPrice(double price) {
        ProductViewModel.price.set((price == 0) ? "" : String.valueOf(price));
    }

    public static StringProperty priceProperty() {
        return price;
    }

    public static Discount getDiscount() {
        return discount.get();
    }

    public static void setDiscount(Discount discount) {
        ProductViewModel.discount.set(discount);
    }

    public static ObjectProperty<Discount> discountProperty() {
        return discount;
    }

    public static Tax getTax() {
        return tax.get();
    }

    public static void setTax(Tax tax) {
        ProductViewModel.tax.set(tax);
    }

    public static ObjectProperty<Tax> taxProperty() {
        return tax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        ProductViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static long getStockAlert() {
        return (Objects.equals(stockAlert.get(), null) || stockAlert.get().isEmpty())
                ? 0
                : Long.parseLong(stockAlert.get());
    }

    public static void setStockAlert(long stockAlert) {
        ProductViewModel.stockAlert.set((stockAlert == 0) ? "" : String.valueOf(stockAlert));
    }

    public static StringProperty stockAlertProperty() {
        return stockAlert;
    }

    public static String getSerialNumber() {
        return serial.get();
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static ObservableList<Product> getProducts() {
        return productsList;
    }

    public static void setProducts(ObservableList<Product> productsList) {
        ProductViewModel.productsList.setAll(productsList);
    }

    public static Brand getBrand() {
        return brand.get();
    }

    public static void setBrand(Brand brand) {
        ProductViewModel.brand.set(brand);
    }

    public static ObjectProperty<Brand> brandProperty() {
        return brand;
    }

    public static ProductCategory getCategory() {
        return category.get();
    }

    public static void setCategory(ProductCategory category) {
        ProductViewModel.category.set(category);
    }

    public static ObjectProperty<ProductCategory> categoryProperty() {
        return category;
    }

    public static String getBarcodeType() {
        return barcodeType.get();
    }

    public static void setBarcodeType(String barcodeType) {
        ProductViewModel.barcodeType.set(barcodeType);
    }

    public static StringProperty barcodeTypeProperty() {
        return barcodeType;
    }

    public static String getImage() {
        return image.get();
    }

    public static void setImage(String image) {
        ProductViewModel.image.set(image);
    }

    public static StringProperty imageProperty() {
        return image;
    }

    public static File getImageFile() {
        return imageFile.get();
    }

    public static void setImageFile(File image) {
        ProductViewModel.imageFile.set(image);
    }

    public static ObjectProperty<File> imageFileProperty() {
        return imageFile;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        ProductViewModel.serial.set(serial);
    }

    public static Integer getTotalPages() {
        return totalPages.get();
    }

    public static void setTotalPages(Integer totalPages) {
        ProductViewModel.totalPages.set(totalPages);
    }

    public static IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public static Integer getPageNumber() {
        return pageNumber.get();
    }

    public static void setPageNumber(Integer pageNumber) {
        ProductViewModel.pageNumber.set(pageNumber);
    }

    public static IntegerProperty pageNumberProperty() {
        return pageNumber;
    }

    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer pageSize) {
        ProductViewModel.pageSize.set(pageSize);
    }

    public static IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public static void saveProduct(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer successMessage,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        var product = Product.builder()
                .name(getName())
                .brand(getBrand())
                .category(getCategory())
                .unit(getUnit())
                .barcodeType(getBarcodeType())
                .salePrice(getPrice())
                .costPrice(getCost())
                .quantity(getQuantity())
                .tax(getTax())
                .discount(getDiscount())
                .stockAlert(getStockAlert())
                .serialNumber(getSerialNumber())
                .image(getImage())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = productsRepository.post(product, getImageFile());
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    successMessage.showMessage("Product created successfully");
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
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage(response.body()));
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
            SpotyLogger.writeToFile(throwable, ProductViewModel.class);
            return null;
        });
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0);
                    setTempId(-1);
                    setBrand(null);
                    setCategory(null);
                    setUnit(null);
                    setName("");
                    setBarcodeType("");
                    setImage("");
                    setQuantity(0);
                    setCost(0);
                    setPrice(0);
                    setTax(null);
                    setDiscount(null);
                    setTaxType("");
                    setStockAlert(0);
                    setSerial("");
                });
    }

    public static void getAllProducts(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer errorMessage, Integer pageNo, Integer pageSize) {
        productsRepository.fetchAll(pageNo, pageSize).thenAccept(response -> {
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<ResponseModel<Product>>() {
                    }.getType();
                    ResponseModel<Product> responseModel = gson.fromJson(response.body(), type);
                    setTotalPages(responseModel.getTotalPages());
                    setPageNumber(responseModel.getPageable().getPageNumber());
                    setPageSize(responseModel.getPageable().getPageSize());
                    ArrayList<Product> productList = responseModel.getContent();
                    productsList.clear();
                    productsList.addAll(productList);
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
                });
            } else if (response.statusCode() == 401) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, ProductViewModel.class);
            return null;
        });
    }

    public static void getAllProductsNonPaged(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                              SpotyGotFunctional.MessageConsumer errorMessage) {
        productsRepository.fetchAllNonPaged().thenAccept(response -> {
            if (response.statusCode() == 200) {
                Type type = new TypeToken<ArrayList<Product>>() {
                }.getType();
                ArrayList<Product> productList = gson.fromJson(response.body(), type);
                Platform.runLater(() -> {
                    productsList.clear();
                    productsList.addAll(productList);
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
                });
            } else if (response.statusCode() == 401) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, ProductViewModel.class);
            return null;
        });
    }

    public static void getProduct(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = productsRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var product = gson.fromJson(response.body(), Product.class);
                    setId(product.getId());
                    setName(product.getName());
                    setBrand(product.getBrand());
                    setCategory(product.getCategory());
                    setUnit(product.getUnit());
                    setBarcodeType(product.getBarcodeType());
                    setPrice(product.getSalePrice());
                    setCost(product.getCostPrice());
                    setQuantity(product.getQuantity());
                    setDiscount(product.getDiscount());
                    setTax(product.getTax());
                    setStockAlert(product.getStockAlert());
                    setSerial(product.getSerialNumber());
                    setImage(product.getImage());
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
            SpotyLogger.writeToFile(throwable, ProductViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = productsRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<Product>>() {
                    }.getType();
                    ArrayList<Product> productList = gson.fromJson(
                            response.body(), listType);
                    productsList.clear();
                    productsList.addAll(productList);
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
            SpotyLogger.writeToFile(throwable, ProductViewModel.class);
            return null;
        });
    }

    public static void updateProduct(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                     SpotyGotFunctional.MessageConsumer successMessage,
                                     SpotyGotFunctional.MessageConsumer errorMessage) {
        var product = Product.builder()
                .id(getId())
                .name(getName())
                .brand(getBrand())
                .category(getCategory())
                .unit(getUnit())
                .barcodeType(getBarcodeType())
                .salePrice(getPrice())
                .costPrice(getCost())
                .quantity(getQuantity())
                .tax(getTax())
                .discount(getDiscount())
                .stockAlert(getStockAlert())
                .serialNumber(getSerialNumber())
                .image(getImage())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = productsRepository.put(product, getImageFile());
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Product updated successfully");
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
            SpotyLogger.writeToFile(throwable, ProductViewModel.class);
            return null;
        });
    }

    public static void deleteProduct(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = productsRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Product deleted successfully");
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
            SpotyLogger.writeToFile(throwable, ProductViewModel.class);
            return null;
        });
    }
}
