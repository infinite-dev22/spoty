package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.PurchaseDetailDao;
import org.infinite.spoty.database.models.*;

public class PurchaseDetailsViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final ObjectProperty<PurchaseMaster> purchase = new SimpleObjectProperty<>(null);
    private static final DoubleProperty cost = new SimpleDoubleProperty(0);
    private static final DoubleProperty netTax = new SimpleDoubleProperty(0);
    private static final StringProperty taxType = new SimpleStringProperty(null);
    private static final DoubleProperty discount = new SimpleDoubleProperty(0);
    private static final StringProperty discountType = new SimpleStringProperty(null);
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>(null);
    private static final StringProperty serial = new SimpleStringProperty(null);
    private static final DoubleProperty total = new SimpleDoubleProperty(0);
    private static final IntegerProperty quantity = new SimpleIntegerProperty(0);
    public static final ObservableList<PurchaseDetail> purchaseDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<PurchaseDetail> purchaseTempList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        PurchaseDetailsViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static PurchaseMaster getPurchase() {
        return purchase.get();
    }

    public static void setPurchase(PurchaseMaster purchase) {
        PurchaseDetailsViewModel.purchase.set(purchase);
    }

    public static ObjectProperty<PurchaseMaster> purchaseProperty() {
        return purchase;
    }

    public static int getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(int quantity) {
        PurchaseDetailsViewModel.quantity.set(quantity);
    }

    public static IntegerProperty quantityProperty() {
        return quantity;
    }

    public static double getCost() {
        return cost.get();
    }

    public static void setCost(double cost) {
        PurchaseDetailsViewModel.cost.set(cost);
    }

    public static DoubleProperty costProperty() {
        return cost;
    }

    public static double getNetTax() {
        return netTax.get();
    }

    public static void setNetTax(double netTax) {
        PurchaseDetailsViewModel.netTax.set(netTax);
    }

    public static DoubleProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        PurchaseDetailsViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        PurchaseDetailsViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static double getDiscount() {
        return discount.get();
    }

    public static void setDiscount(double discount) {
        PurchaseDetailsViewModel.discount.set(discount);
    }

    public static DoubleProperty discountProperty() {
        return discount;
    }

    public static String getDiscountType() {
        return discountType.get();
    }

    public static void setDiscountType(String discountType) {
        PurchaseDetailsViewModel.discountType.set(discountType);
    }

    public static StringProperty discountTypeProperty() {
        return discountType;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        PurchaseDetailsViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        PurchaseDetailsViewModel.total.set(total);
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static void addPurchaseDetail() {
        PurchaseDetail purchaseDetail = new PurchaseDetail(getPurchase(),
                getCost(),
                getNetTax(),
                getTaxType(),
                getDiscount(),
                getDiscountType(),
                getProduct(),
                getSerial(),
                getTotal(),
                getQuantity());
        purchaseTempList.add(purchaseDetail);
    }

    public static void resetProperties() {
        setId(0);
        setPurchase(null);
        setCost(0);
        setNetTax(0);
        setTaxType(null);
        setDiscount(0);
        setDiscountType(null);
        setSerial(null);
        setTotal(0);
        setQuantity(0);
    }

    public static void resetTempList() {
        purchaseDetailsList.clear();
    }

    public static ObservableList<PurchaseDetail> getPurchaseDetail() {
        purchaseDetailsList.clear();
        purchaseDetailsList.addAll(PurchaseDetailDao.fetchPurchaseDetails());
        return purchaseDetailsList;
    }
}