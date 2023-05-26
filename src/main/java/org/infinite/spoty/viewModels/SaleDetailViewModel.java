package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.SaleDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.database.models.UnitOfMeasure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleDetailViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final StringProperty date = new SimpleStringProperty();
    private static final StringProperty ref = new SimpleStringProperty();
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>();
    private static final StringProperty serial = new SimpleStringProperty();
    private static final DoubleProperty price = new SimpleDoubleProperty();
    private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>();
    private static final DoubleProperty netTax = new SimpleDoubleProperty();
    private static final StringProperty taxType = new SimpleStringProperty();
    private static final DoubleProperty discount = new SimpleDoubleProperty();
    private static final StringProperty discountType = new SimpleStringProperty();
    private static final DoubleProperty total = new SimpleDoubleProperty();
    private static final IntegerProperty quantity = new SimpleIntegerProperty();
    private static final ObservableList<SaleDetail> saleDetailList = FXCollections.observableArrayList();
    private static final ObservableList<SaleDetail> saleDetailTempList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        SaleDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        Date formatedDate;
        try {
            formatedDate = new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return formatedDate;
    }

    public static void setDate(String date) {
        SaleDetailViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
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

    public static double getPrice() {
        return price.get();
    }

    public static void setPrice(double price) {
        SaleDetailViewModel.price.set(price);
    }

    public static DoubleProperty priceProperty() {
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

    public static double getNetTax() {
        return netTax.get();
    }

    public static void setNetTax(double netTax) {
        SaleDetailViewModel.netTax.set(netTax);
    }

    public static DoubleProperty netTaxProperty() {
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

    public static double getDiscount() {
        return discount.get();
    }

    public static void setDiscount(double discount) {
        SaleDetailViewModel.discount.set(discount);
    }

    public static DoubleProperty discountProperty() {
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

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        SaleDetailViewModel.total.set(total);
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static int getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(int quantity) {
        SaleDetailViewModel.quantity.set(quantity);
    }

    public static IntegerProperty quantityProperty() {
        return quantity;
    }

    public static void resetProperties() {
        setId(0);
        setDate(null);
        setRef(null);
        setProduct(null);
        setSerial(null);
        setPrice(0);
        setSaleUnit(null);
        setNetTax(0);
        setTaxType(null);
        setDiscount(0);
        setDiscountType(null);
        setTotal(0);
        setQuantity(0);
    }

    public static void addSaleDetail() {
        SaleDetail saleDetail = new SaleDetail(getDate(),
                getRef(),
                getProduct(),
                getSerial(),
                getPrice(),
                getSaleUnit(),
                getNetTax(),
                getTaxType(),
                getDiscount(),
                getDiscountType(),
                getTotal(),
                getQuantity());
        saleDetailTempList.add(saleDetail);
        resetProperties();
    }}
