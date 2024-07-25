package inc.nomard.spoty.core.viewModels.transfers;

import com.google.gson.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.transfers.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.adapters.*;
import java.time.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class TransferDetailViewModel {
    public static final ObservableList<TransferDetail> transferDetailsList =
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
    private static final ListProperty<TransferDetail> transferDetails =
            new SimpleListProperty<>(transferDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final TransfersRepositoryImpl transfersRepository = new TransfersRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        TransferDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        TransferDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        TransferDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static ObservableList<TransferDetail> getTransferDetails() {
        return transferDetails.get();
    }

    public static void setTransferDetails(ObservableList<TransferDetail> transferDetails) {
        TransferDetailViewModel.transferDetails.set(transferDetails);
    }

    public static ListProperty<TransferDetail> transferDetailsProperty() {
        return transferDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
    }

    public static void addTransferDetails() {
        var transferDetail = TransferDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .build();
        transferDetailsList.add(transferDetail);
    }

    public static void updateTransferDetail() {
        var transferDetail = TransferDetail.builder()
                .id(getId())
                .product(getProduct())
                .quantity(getQuantity())
                .build();
        transferDetailsList.set(getTempId(), transferDetail);
    }

    public static void getTransferDetail(TransferDetail transferDetail) {
        setId(transferDetail.getId());
        setProduct(transferDetail.getProduct());
        setQuantity(String.valueOf(transferDetail.getQuantity()));
    }

    public static void removeTransferDetail(Long index, int tempIndex) {
        transferDetailsList.remove(tempIndex);
        PENDING_DELETES.add(index);
    }
}
