package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.PurchaseDetailDao;
import org.infinite.spoty.database.models.*;

public class PurchaseDetailsViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty();
    private static final ObjectProperty<PurchaseMaster> purchase = new SimpleObjectProperty<>(null);
    private static final StringProperty cost = new SimpleStringProperty("");
    private static final StringProperty netTax = new SimpleStringProperty("");
    private static final StringProperty taxType = new SimpleStringProperty("");
    private static final StringProperty discount = new SimpleStringProperty("");
    private static final StringProperty discountType = new SimpleStringProperty("");
    private static final ObjectProperty<ProductDetail> product = new SimpleObjectProperty<>(null);
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty total = new SimpleStringProperty("");
    private static final StringProperty quantity = new SimpleStringProperty("");
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

    public static String getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(String quantity) {
        PurchaseDetailsViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getCost() {
        return cost.get();
    }

    public static void setCost(String cost) {
        PurchaseDetailsViewModel.cost.set(cost);
    }

    public static StringProperty costProperty() {
        return cost;
    }

    public static String getNetTax() {
        return netTax.get();
    }

    public static void setNetTax(String netTax) {
        PurchaseDetailsViewModel.netTax.set(netTax);
    }

    public static StringProperty netTaxProperty() {
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

    public static String getDiscount() {
        return discount.get();
    }

    public static void setDiscount(String discount) {
        PurchaseDetailsViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
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

    public static String getTotal() {
        return total.get();
    }

    public static void setTotal(String total) {
        PurchaseDetailsViewModel.total.set(total);
    }

    public static StringProperty totalProperty() {
        return total;
    }

    public static void addPurchaseDetail() {
        setCost("0");
        setTotal("0");
        PurchaseDetail purchaseDetail = new PurchaseDetail(getPurchase(),
                Double.parseDouble(getCost()),
                Double.parseDouble(getNetTax()),
                getTaxType(),
                Double.parseDouble(getDiscount()),
                getDiscountType(),
                getProduct(),
                getSerial(),
                Double.parseDouble(getTotal()),
                Integer.parseInt(getQuantity()));
        purchaseTempList.add(purchaseDetail);
    }

    public static ObservableList<PurchaseDetail> getTempList() {
        purchaseTempList.forEach(System.out::println);
        return purchaseTempList;
    }

    public static void resetProperties() {
        setId(0);
        setPurchase(null);
        setCost("");
        setNetTax("");
        setTaxType(null);
        setDiscount("");
        setDiscountType(null);
        setSerial(null);
        setTotal("");
        setQuantity("");
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