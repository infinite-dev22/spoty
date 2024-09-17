package inc.nomard.spoty.core.viewModels.quotations;

import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.quotations.QuotationDetail;
import inc.nomard.spoty.network_bridge.repositories.implementations.QuotationsRepositoryImpl;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Objects;

import static inc.nomard.spoty.core.values.SharedResources.PENDING_DELETES;

@Log
public class QuotationDetailViewModel {
    // TODO: Add more fields according to DB design and necessity.
    @Getter
    public static final ObservableList<QuotationDetail> quotationDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<QuotationDetail> quotationDetails =
            new SimpleListProperty<>(quotationDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final IntegerProperty tempId = new SimpleIntegerProperty(-1);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final ObjectProperty<Discount> discount = new SimpleObjectProperty<>();
    private static final ObjectProperty<Tax> tax = new SimpleObjectProperty<>();
    private static final DoubleProperty subTotal = new SimpleDoubleProperty();
    private static final QuotationsRepositoryImpl quotationsRepository = new QuotationsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        QuotationDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Integer getTempId() {
        return tempId.get();
    }

    public static void setTempId(Integer tempId) {
        QuotationDetailViewModel.tempId.set(tempId);
    }

    public static IntegerProperty tempIdProperty() {
        return tempId;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        QuotationDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        QuotationDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static Discount getDiscount() {
        return discount.get();
    }

    public static void setDiscount(Discount discount) {
        QuotationDetailViewModel.discount.set(discount);
    }

    public static ObjectProperty<Discount> discountProperty() {
        return discount;
    }

    public static Tax getTax() {
        return tax.get();
    }

    public static void setTax(Tax tax) {
        QuotationDetailViewModel.tax.set(tax);
    }

    public static ObjectProperty<Tax> taxProperty() {
        return tax;
    }

    public static Double getSubTotal() {
        return subTotal.get();
    }

    public static void setSubTotal(Double subTotal) {
        QuotationDetailViewModel.subTotal.set(subTotal);
    }

    public static DoubleProperty subTotalProperty() {
        return subTotal;
    }

    public static ObservableList<QuotationDetail> getQuotationDetails() {
        return quotationDetails.get();
    }

    public static void setQuotationDetails(ObservableList<QuotationDetail> quotationDetails) {
        QuotationDetailViewModel.quotationDetails.set(quotationDetails);
    }

    public static ListProperty<QuotationDetail> quotationDetailsProperty() {
        return quotationDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
        setTax(null);
        setDiscount(null);
        setSubTotal(0.00);
    }

    public static void addQuotationDetails() {
        var subTotal = getSubTotal() * getQuantity();
        var quotationDetail = QuotationDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .discount(getDiscount())
                .tax(getTax())
                .subTotal(subTotal)
                .build();
        quotationDetailsList.add(quotationDetail);
    }

    public static void updateQuotationDetail() {
        QuotationDetail quotationDetail = quotationDetailsList.get(getTempId());
        if (!Objects.equals(getId(), 0L)) {
            quotationDetail.setId(getId());
        }
        quotationDetail.setProduct(getProduct());
        quotationDetail.setQuantity(getQuantity());
        quotationDetail.setSubTotal(getSubTotal());
        quotationDetailsList.set(getTempId(), quotationDetail);
    }

    public static void getQuotationDetail(QuotationDetail quotationDetail) {
        setTempId(getQuotationDetails().indexOf(quotationDetail));
        if (Objects.nonNull(quotationDetail.getId())) {
            setId(quotationDetail.getId());
        }
        setProduct(quotationDetail.getProduct());
        setTax(getTax());
        setDiscount(getDiscount());
        setQuantity(String.valueOf(quotationDetail.getQuantity()));
    }

    public static void removeQuotationDetail(Long index, int tempIndex) {
        Platform.runLater(() -> quotationDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
