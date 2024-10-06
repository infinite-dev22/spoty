package inc.nomard.spoty.core.viewModels.purchases;

import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseDetail;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.log4j.Log4j2;

import static inc.nomard.spoty.core.values.SharedResources.*;

@Log4j2
public class PurchaseDetailViewModel {
    private static final ListProperty<PurchaseDetail> purchaseDetails =
            new SimpleListProperty<>(FXCollections.observableArrayList());
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        PurchaseDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        PurchaseDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static ObservableList<PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails.get();
    }

    public static void setPurchaseDetails(ObservableList<PurchaseDetail> purchaseDetails) {
        PurchaseDetailViewModel.purchaseDetails.set(purchaseDetails);
    }

    public static ListProperty<PurchaseDetail> purchaseDetailsProperty() {
        return purchaseDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
    }

    public static void addPurchaseDetail() {
        var purchaseDetail = PurchaseDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .build();
        getPurchaseDetails().add(purchaseDetail);
    }

    public static void updatePurchaseDetail() {
        var purchaseDetail = getPurchaseDetails().get(Math.toIntExact(getTempId()));
        purchaseDetail.setProduct(getProduct());
        purchaseDetail.setQuantity(getQuantity());
        getPurchaseDetails().set(getTempId(), purchaseDetail);
    }

    public static void getPurchaseDetail(PurchaseDetail purchaseDetail) {
        setTempId(getPurchaseDetails().indexOf(purchaseDetail));
        setProduct(purchaseDetail.getProduct());
        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
    }

    public static void removePurchaseDetail(Long index, int tempIndex) {
        Platform.runLater(() -> getPurchaseDetails().remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
