package inc.nomard.spoty.core.viewModels.stock_ins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.StockInDetail;
import inc.nomard.spoty.network_bridge.repositories.implementations.StockInsRepositoryImpl;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

import static inc.nomard.spoty.core.values.SharedResources.*;

@Slf4j
public class StockInDetailViewModel {
    @Getter
    public static final ObservableList<StockInDetail> stockInDetailsList =
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
    private static final ListProperty<StockInDetail> stockInDetails =
            new SimpleListProperty<>(stockInDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StockInsRepositoryImpl stockInsRepository = new StockInsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        StockInDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        StockInDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        StockInDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        StockInDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static ObservableList<StockInDetail> getStockInDetails() {
        return stockInDetails.get();
    }

    public static void setStockInDetails(ObservableList<StockInDetail> stockInDetails) {
        StockInDetailViewModel.stockInDetails.set(stockInDetails);
    }

    public static ListProperty<StockInDetail> stockInDetailsProperty() {
        return stockInDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
        setDescription("");
    }

    public static void addStockInDetails() {
        var stockInDetail = StockInDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .description(getDescription())
                .build();
        stockInDetailsList.add(stockInDetail);
    }

    public static void updateStockInDetail() {
        var stockInDetail = stockInDetailsList.get(getTempId());
        stockInDetail.setProduct(getProduct());
        stockInDetail.setQuantity(getQuantity());
        stockInDetail.setDescription(getDescription());
        stockInDetailsList.set(getTempId(), stockInDetail);
    }

    public static void getStockInDetail(StockInDetail stockInDetail) {
        setTempId(getStockInDetails().indexOf(stockInDetail));
        if (Objects.nonNull(stockInDetail.getId())) {
            setId(stockInDetail.getId());
        }
        setProduct(stockInDetail.getProduct());
        setQuantity(String.valueOf(stockInDetail.getQuantity()));
        setDescription(stockInDetail.getDescription());
    }

    public static void removeStockInDetail(Long index, int tempIndex) {
        Platform.runLater(() -> stockInDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
