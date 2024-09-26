package inc.nomard.spoty.core.viewModels.sales;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleDetail;
import inc.nomard.spoty.network_bridge.repositories.implementations.SalesRepositoryImpl;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.java.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static inc.nomard.spoty.core.values.SharedResources.setTempId;

@Log
public class SaleDetailViewModel {
    @Getter
    public static final ObservableList<SaleDetail> saleDetailsList =
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
    private static final ListProperty<SaleDetail> saleDetails =
            new SimpleListProperty<>(saleDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty ref = new SimpleStringProperty();
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final DoubleProperty subTotalPrice = new SimpleDoubleProperty();
    private static final DoubleProperty price = new SimpleDoubleProperty();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final SalesRepositoryImpl salesRepository = new SalesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        SaleDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getRef() {
        return ref.get();
    }

    public static void setRef(String ref) {
        SaleDetailViewModel.ref.set(ref);
    }

    public static StringProperty refProperty() {
        return ref;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        SaleDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static double getSubTotalPrice() {
        return subTotalPrice.get();
    }

    public static void setSubTotalPrice(double price) {
        SaleDetailViewModel.subTotalPrice.set(price);
    }

    public static DoubleProperty subTotalPriceProperty() {
        return subTotalPrice;
    }

    public static double getPrice() {
        return price.get();
    }

    public static void setPrice(double price) {
        SaleDetailViewModel.price.set(price);
    }

    public static DoubleProperty totalProperty() {
        return price;
    }

    public static int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(Long quantity) {
        SaleDetailViewModel.quantity.set((quantity < 1) ? "" : String.valueOf(quantity));
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static ObservableList<SaleDetail> getSaleDetails() {
        return saleDetails.get();
    }

    public static void setSaleDetails(ObservableList<SaleDetail> saleDetails) {
        SaleDetailViewModel.saleDetails.set(saleDetails);
    }

    public static ListProperty<SaleDetail> saleDetailsProperty() {
        return saleDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity(0L);
        setPrice(0);
        setSubTotalPrice(0);
        saleDetailsList.clear();
    }

    public static void addSaleDetail() {
        var saleDetail =
                SaleDetail.builder()
                        .product(getProduct())
                        .quantity(getQuantity())
                        .unitPrice(getSubTotalPrice())
                        .build();
        saleDetailsList.add(saleDetail);
    }

    public static void updateCartSale(Long index) {
        var saleDetail = getSaleDetails().get(Math.toIntExact(index));
        saleDetail.setProduct(getProduct());
        saleDetail.setQuantity(getQuantity());
        saleDetail.setUnitPrice(getSubTotalPrice());
        getSaleDetails().set(Math.toIntExact(index), saleDetail);
    }

    public static void getCartSale(SaleDetail saleDetail) {
        setProduct(saleDetail.getProduct());
        setQuantity((long) saleDetail.getQuantity());
        setSubTotalPrice(saleDetail.getUnitPrice());
    }

    public static void getSaleDetail(SaleDetail saleDetail) {
        setProduct(saleDetail.getProduct());
        setQuantity((long) saleDetail.getQuantity());
        setSubTotalPrice(saleDetail.getUnitPrice());
    }
}
