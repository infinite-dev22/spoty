package inc.nomard.spoty.core.viewModels.returns.purchases;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.utils.adapters.*;
import java.time.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class PurchaseReturnDetailViewModel {
    @Getter
    public static final ObservableList<PurchaseReturnDetail> purchaseReturnDetailsList =
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
    private static final LongProperty id = new SimpleLongProperty();
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<PurchaseReturnMaster> purchaseReturn =
            new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty location = new SimpleStringProperty("");

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        PurchaseReturnDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        PurchaseReturnDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static PurchaseReturnMaster getPurchaseReturn() {
        return purchaseReturn.get();
    }

    public static void setPurchaseReturn(PurchaseReturnMaster purchaseReturn) {
        PurchaseReturnDetailViewModel.purchaseReturn.set(purchaseReturn);
    }

    public static ObjectProperty<PurchaseReturnMaster> purchaseReturnProperty() {
        return purchaseReturn;
    }

    public static long getQuantity() {
        return Long.parseLong(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        PurchaseReturnDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        PurchaseReturnDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        PurchaseReturnDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getLocation() {
        return location.get();
    }

    public static void setLocation(String location) {
        PurchaseReturnDetailViewModel.location.set(location);
    }

    public static StringProperty locationProperty() {
        return location;
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setLocation("");
    }

    public static void getPurchaseReturnDetails() throws Exception {
//        Dao<PurchaseReturnDetail, Long> purchaseReturnDetailDao =
//                DaoManager.createDao(connectionSource, PurchaseReturnDetail.class);
//
//        purchaseReturnDetailsList.clear();
//        purchaseReturnDetailsList.addAll(purchaseReturnDetailDao.queryForAll());
    }
}
