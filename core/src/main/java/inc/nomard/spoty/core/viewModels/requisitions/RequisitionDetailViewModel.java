package inc.nomard.spoty.core.viewModels.requisitions;

import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.requisitions.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class RequisitionDetailViewModel {
    @Getter
    public static final ObservableList<RequisitionDetail> requisitionDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<RequisitionDetail> requisitionDetails =
            new SimpleListProperty<>(requisitionDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>(null);
    private static final StringProperty quantity = new SimpleStringProperty("");

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        RequisitionDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        RequisitionDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        RequisitionDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static ObservableList<RequisitionDetail> getRequisitionDetails() {
        return requisitionDetails.get();
    }

    public static void setRequisitionDetails(ObservableList<RequisitionDetail> requisitionDetails) {
        RequisitionDetailViewModel.requisitionDetails.set(requisitionDetails);
    }

    public static ListProperty<RequisitionDetail> requisitionDetailsProperty() {
        return requisitionDetails;
    }

    public static void addRequisitionDetail() {
        var requisitionDetail = RequisitionDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .build();
        requisitionDetailsList.add(requisitionDetail);
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
    }

    public static void updateRequisitionDetail(Long index) {
        RequisitionDetail requisitionDetail = requisitionDetailsList.get(Math.toIntExact(index));
        requisitionDetail.setProduct(getProduct());
        requisitionDetail.setQuantity(getQuantity());
        requisitionDetailsList.set(getTempId(), requisitionDetail);
    }

    public static void getRequisitionDetail(Long index) {
        var requisitionDetail = requisitionDetailsList.get(Math.toIntExact(index));
        setTempId(getRequisitionDetails().indexOf(requisitionDetail));
        setProduct(requisitionDetail.getProduct());
        setQuantity(String.valueOf(requisitionDetail.getQuantity()));
    }

    public static void removeRequisitionDetail(Long index, int tempIndex) {
        Platform.runLater(() -> requisitionDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
