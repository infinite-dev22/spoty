package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.QuotationDetail;
import org.infinite.spoty.database.models.QuotationMaster;
import org.infinite.spoty.database.models.ProductDetail;

public class QuotationDetailViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<QuotationMaster> quotation = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty tax = new SimpleStringProperty();
    private static final StringProperty discount = new SimpleStringProperty();
    public static final ObservableList<QuotationDetail> quotationDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<QuotationDetail> quotationDetailsTempList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        QuotationDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        QuotationDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static QuotationMaster getQuotation() {
        return quotation.get();
    }

    public static void setQuotation(QuotationMaster quotation) {
        QuotationDetailViewModel.quotation.set(quotation);
    }

    public static ObjectProperty<QuotationMaster> quotationProperty() {
        return quotation;
    }

    public static String getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(String quantity) {
        QuotationDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getTax() {
        return tax.get();
    }

    public static void setTax(String tax) {
        QuotationDetailViewModel.tax.set(tax);
    }

    public static StringProperty taxProperty() {
        return tax;
    }

    public static String getDiscount() {
        return discount.get();
    }

    public static void setDiscount(String discount) {
        QuotationDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setTax("");
        setDiscount("");
        setQuantity("");
    }

    public static void addQuotationDetails() {
        QuotationDetail quotationDetail = new QuotationDetail(getProduct(),
                Double.parseDouble(getTax()),
                Double.parseDouble(getDiscount()),
                Integer.parseInt(getQuantity()));
        quotationDetailsTempList.add(quotationDetail);
        resetProperties();
        quotationDetailsTempList.forEach(System.out::println);
    }

    public static ObservableList<QuotationDetail> getQuotationDetails() {
        quotationDetailsList.clear();
        quotationDetailsList.addAll(QuotationDetailViewModel.getQuotationDetails());
        return quotationDetailsList;
    }
}
