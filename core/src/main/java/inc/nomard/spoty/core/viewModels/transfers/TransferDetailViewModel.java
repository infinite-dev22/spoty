package inc.nomard.spoty.core.viewModels.transfers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferDetail;
import inc.nomard.spoty.network_bridge.repositories.implementations.TransfersRepositoryImpl;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static inc.nomard.spoty.core.values.SharedResources.*;

@Log4j2
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
