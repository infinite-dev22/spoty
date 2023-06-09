package org.infinite.spoty.viewModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.QuotationDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.QuotationDetail;
import org.infinite.spoty.database.models.QuotationMaster;

public class QuotationDetailViewModel {
    // TODO: Add more fields according to DB design and necessity.
    public static final ObservableList<QuotationDetail> quotationDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<QuotationDetail> quotationDetailsTempList = FXCollections.observableArrayList();
    private static final StringProperty id = new SimpleStringProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<QuotationMaster> quotation = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty tax = new SimpleStringProperty();
    private static final StringProperty discount = new SimpleStringProperty();

    public static int getId() {
        return Integer.parseInt(id.get());
    }

    public static void setId(String id) {
        QuotationDetailViewModel.id.set(id);
    }

    public static StringProperty idProperty() {
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

    public static int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        QuotationDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static double getTax() {
        return Double.parseDouble(tax.get());
    }

    public static void setTax(String tax) {
        QuotationDetailViewModel.tax.set(tax);
    }

    public static StringProperty taxProperty() {
        return tax;
    }

    public static double getDiscount() {
        return Double.parseDouble(discount.get());
    }

    public static void setDiscount(String discount) {
        QuotationDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static void resetProperties() {
        setId("");
        setProduct(null);
        setTax("");
        setDiscount("");
        setQuantity("");
    }

    public static void addQuotationDetails() {
        QuotationDetail quotationDetail = new QuotationDetail(getProduct(), getTax(), getDiscount(), getQuantity());
        quotationDetailsTempList.add(quotationDetail);
        resetProperties();
        quotationDetailsTempList.forEach(System.out::println);
    }

    public static void updateQuotationDetail() {
        QuotationDetail quotationDetail = new QuotationDetail(getProduct(),
                getTax(),
                getDiscount(),
                getQuantity());
        quotationDetail.setId(getId());
        quotationDetail.setQuotation(getQuotation());
        QuotationDetailDao.updateQuotationDetail(quotationDetail, getId());
        quotationDetailsTempList.addAll(getQuotationDetails());
        resetProperties();
    }

    public static ObservableList<QuotationDetail> getQuotationDetails() {
        quotationDetailsList.clear();
        quotationDetailsList.addAll(QuotationDetailDao.fetchQuotationDetails());
        return quotationDetailsList;
    }

    public static void getItem(int quotationDetailID) {
        QuotationDetail quotationDetail = QuotationDetailDao.findQuotationDetail(quotationDetailID);
        setId(String.valueOf(quotationDetail.getId()));
        setProduct(quotationDetail.getProduct());
        setTax(String.valueOf(quotationDetail.getNetTax()));
        setDiscount(String.valueOf(quotationDetail.getDiscount()));
        setQuantity(String.valueOf(quotationDetail.getQuantity()));
        setQuotation(quotationDetail.getQuotation());
        getQuotationDetails();
    }

    public static void updateItem(int quotationDetailID) {
        QuotationDetail quotationDetail = new QuotationDetail(getProduct(), getTax(), getDiscount(), getQuantity());
        QuotationDetailDao.updateQuotationDetail(quotationDetail, quotationDetailID);
        getQuotationDetails();
    }

    public static void removeQuotationDetail(int index) {
        quotationDetailsTempList.remove(index);
    }
}
