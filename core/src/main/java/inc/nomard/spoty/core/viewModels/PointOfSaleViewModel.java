package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Slf4j
public class PointOfSaleViewModel {
    public static final LongProperty itemQuantity = new SimpleLongProperty(1);
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
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<Product> customer = new SimpleObjectProperty<>();
    private static final ObjectProperty<Product> branch = new SimpleObjectProperty<>();
    private static final DoubleProperty subTotal = new SimpleDoubleProperty();
    private static final DoubleProperty total = new SimpleDoubleProperty();
    private static final DoubleProperty price = new SimpleDoubleProperty();
    private static final DoubleProperty tax = new SimpleDoubleProperty();
    private static final DoubleProperty discount = new SimpleDoubleProperty();
    private static final ObservableList<Product> posCartList = FXCollections.observableArrayList();

    public static Long getItemQuantity() {
        return itemQuantity.get();
    }

    public static void setItemQuantity(long itemQuantity) {
        PointOfSaleViewModel.itemQuantity.set(itemQuantity);
    }

    public static DoubleProperty discountProperty() {
        return discount;
    }

    public static DoubleProperty priceProperty() {
        return price;
    }

    public static DoubleProperty subTotalProperty() {
        return subTotal;
    }

    public static DoubleProperty taxProperty() {
        return tax;
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static LongProperty itemQuantityProperty() {
        return itemQuantity;
    }

    public static ObjectProperty<Product> branchProperty() {
        return branch;
    }

    public static ObjectProperty<Product> customerProperty() {
        return customer;
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static ObservableList<Product> getPosCartList() {
        return posCartList;
    }

    public static Product getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Product customer) {
        PointOfSaleViewModel.customer.set(customer);
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        PointOfSaleViewModel.product.set(product);
    }

    public static Product getBranch() {
        return branch.get();
    }

    public static void setBranch(Product branch) {
        PointOfSaleViewModel.branch.set(branch);
    }

    public static double getDiscount() {
        return discount.get();
    }

    public static void setDiscount(double discount) {
        PointOfSaleViewModel.discount.set(discount);
    }

    public static double getPrice() {
        return price.get();
    }

    public static void setPrice(double price) {
        PointOfSaleViewModel.price.set(price);
    }

    public static double getSubTotal() {
        return subTotal.get();
    }

    public static void setSubTotal(double subTotal) {
        PointOfSaleViewModel.subTotal.set(subTotal);
    }

    public static double getTax() {
        return tax.get();
    }

    public static void setTax(double tax) {
        PointOfSaleViewModel.tax.set(tax);
    }

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        PointOfSaleViewModel.total.set(total);
    }
}
