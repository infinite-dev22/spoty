package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.database.models.UnitOfMeasure;

public class SaleDetailViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final StringProperty ref = new SimpleStringProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final StringProperty serial = new SimpleStringProperty();
    private static final StringProperty price = new SimpleStringProperty();
    private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>();
    private static final StringProperty netTax = new SimpleStringProperty();
    private static final StringProperty taxType = new SimpleStringProperty();
    private static final StringProperty discount = new SimpleStringProperty();
    private static final StringProperty discountType = new SimpleStringProperty();
    private static final StringProperty total = new SimpleStringProperty();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final ObservableList<SaleDetail> saleDetailList = FXCollections.observableArrayList();
    public static final ObservableList<SaleDetail> saleDetailTempList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        SaleDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static String getRef() {
        return ref.get();
    }

    public static void setRef(String ref) {
        SaleDetailViewModel.ref.set(ref);
    }

    public static StringProperty refProperty() {
        return ref;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        SaleDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        SaleDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getPrice() {
        return price.get();
    }

    public static void setPrice(String price) {
        SaleDetailViewModel.price.set(price);
    }

    public static StringProperty priceProperty() {
        return price;
    }

    public static UnitOfMeasure getSaleUnit() {
        return saleUnit.get();
    }

    public static void setSaleUnit(UnitOfMeasure saleUnit) {
        SaleDetailViewModel.saleUnit.set(saleUnit);
    }

    public static ObjectProperty<UnitOfMeasure> saleUnitProperty() {
        return saleUnit;
    }

    public static String getNetTax() {
        return netTax.get();
    }

    public static void setNetTax(String netTax) {
        SaleDetailViewModel.netTax.set(netTax);
    }

    public static StringProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        SaleDetailViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static String getDiscount() {
        return discount.get();
    }

    public static void setDiscount(String discount) {
        SaleDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static String getDiscountType() {
        return discountType.get();
    }

    public static void setDiscountType(String discountType) {
        SaleDetailViewModel.discountType.set(discountType);
    }

    public static StringProperty discountTypeProperty() {
        return discountType;
    }

    public static String getTotal() {
        return total.get();
    }

    public static void setTotal(String total) {
        SaleDetailViewModel.total.set(total);
    }

    public static StringProperty totalProperty() {
        return total;
    }

    public static String getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(String quantity) {
        SaleDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setSerial("");
        setSaleUnit(null);
        setNetTax("");
        setTaxType("");
        setDiscount("null");
        setDiscountType("null");
    }

    public static void addSaleDetail() {
        SaleDetail saleDetail = new SaleDetail(getProduct(),
                getSerial(),
                Double.parseDouble(getNetTax()),
                getTaxType(),
                Double.parseDouble(getDiscount()),
                getDiscountType());
        saleDetailTempList.add(saleDetail);
        saleDetailTempList.forEach(System.out::println);
        resetProperties();
    }
}
