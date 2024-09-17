package inc.nomard.spoty.core.viewModels.returns.purchases;

import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseDetail;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import static inc.nomard.spoty.core.values.SharedResources.*;

@Log
public class PurchaseReturnDetailViewModel {
    private static final ListProperty<PurchaseDetail> purchaseReturnDetails =
            new SimpleListProperty<>(FXCollections.observableArrayList());
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseReturnDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        PurchaseReturnDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
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

    public static ObservableList<PurchaseDetail> getPurchaseReturnDetails() {
        return purchaseReturnDetails.get();
    }

    public static void setPurchaseReturnDetails(ObservableList<PurchaseDetail> purchaseReturnDetails) {
        PurchaseReturnDetailViewModel.purchaseReturnDetails.set(purchaseReturnDetails);
    }

    public static ListProperty<PurchaseDetail> purchaseReturnDetailsProperty() {
        return purchaseReturnDetails;
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
        getPurchaseReturnDetails().add(purchaseDetail);
    }

    public static void updatePurchaseDetail() {
        var purchaseDetail = getPurchaseReturnDetails().get(Math.toIntExact(getTempId()));
        purchaseDetail.setProduct(getProduct());
        purchaseDetail.setQuantity(getQuantity());
        getPurchaseReturnDetails().set(getTempId(), purchaseDetail);
    }

    public static void getPurchaseDetail(PurchaseDetail purchaseDetail) {
        setTempId(getPurchaseReturnDetails().indexOf(purchaseDetail));
        setProduct(purchaseDetail.getProduct());
        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
    }

    public static void removePurchaseDetail(Long index, int tempIndex) {
        Platform.runLater(() -> getPurchaseReturnDetails().remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
