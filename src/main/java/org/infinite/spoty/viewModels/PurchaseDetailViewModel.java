package org.infinite.spoty.viewModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.PurchaseDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.PurchaseDetail;
import org.infinite.spoty.database.models.PurchaseMaster;

public class PurchaseDetailViewModel {
    public static final ObservableList<PurchaseDetail> purchaseDetailList = FXCollections.observableArrayList();
    public static final ObservableList<PurchaseDetail> purchaseDetailTempList = FXCollections.observableArrayList();
    private static final StringProperty id = new SimpleStringProperty();
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

    public static String getId() {
        return id.get();
    }

    public static void setId(String id) {
        PurchaseDetailViewModel.id.set(id);
    }

    public static StringProperty idProperty() {
        return id;
    }

    public static PurchaseMaster getPurchase() {
        return purchase.get();
    }

    public static void setPurchase(PurchaseMaster purchase) {
        PurchaseDetailViewModel.purchase.set(purchase);
    }

    public static ObjectProperty<PurchaseMaster> purchaseProperty() {
        return purchase;
    }

    public static String getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(String quantity) {
        PurchaseDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getCost() {
        return cost.get();
    }

    public static void setCost(String cost) {
        PurchaseDetailViewModel.cost.set(cost);
    }

    public static StringProperty costProperty() {
        return cost;
    }

    public static String getNetTax() {
        return netTax.get();
    }

    public static void setNetTax(String netTax) {
        PurchaseDetailViewModel.netTax.set(netTax);
    }

    public static StringProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        PurchaseDetailViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        PurchaseDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getDiscount() {
        return discount.get();
    }

    public static void setDiscount(String discount) {
        PurchaseDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static String getDiscountType() {
        return discountType.get();
    }

    public static void setDiscountType(String discountType) {
        PurchaseDetailViewModel.discountType.set(discountType);
    }

    public static StringProperty discountTypeProperty() {
        return discountType;
    }

    public static ProductDetail getProduct() {
        return product.get();
    }

    public static void setProduct(ProductDetail product) {
        PurchaseDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductDetail> productProperty() {
        return product;
    }

    public static String getTotal() {
        return total.get();
    }

    public static void setTotal(String total) {
        PurchaseDetailViewModel.total.set(total);
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
        purchaseDetailTempList.add(purchaseDetail);
    }

    public static void resetProperties() {
        setId("");
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

    public static ObservableList<PurchaseDetail> getPurchaseDetails() {
        purchaseDetailList.clear();
        purchaseDetailList.addAll(PurchaseDetailDao.fetchPurchaseDetails());
        return purchaseDetailList;
    }

    public static void updatePurchaseDetail(int index) {
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
        purchaseDetailTempList.remove(index);
        purchaseDetailTempList.add(index, purchaseDetail);
        resetProperties();
    }

    public static void getItem(int purchaseDetailID) {
        PurchaseDetail purchaseDetail = PurchaseDetailDao.findPurchaseDetail(purchaseDetailID);
        setId(String.valueOf(purchaseDetail.getId()));
        setPurchase(purchaseDetail.getPurchase());
        setCost(String.valueOf(purchaseDetail.getCost()));
        setNetTax(String.valueOf(purchaseDetail.getNetTax()));
        setTaxType(purchaseDetail.getTaxtType());
        setDiscount(String.valueOf(purchaseDetail.getDiscount()));
        setDiscountType(purchaseDetail.getDiscountType());
        setProduct(purchaseDetail.getProduct());
        setSerial(purchaseDetail.getSerialNumber());
        setTotal(String.valueOf(purchaseDetail.getTotal()));
        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
        getPurchaseDetails();
    }

    public static void getItem(PurchaseDetail purchaseDetail, int index) {
        setId("index:" + index + ";");
        setPurchase(purchaseDetail.getPurchase());
        setCost(String.valueOf(purchaseDetail.getCost()));
        setNetTax(String.valueOf(purchaseDetail.getNetTax()));
        setTaxType(purchaseDetail.getTaxtType());
        setDiscount(String.valueOf(purchaseDetail.getDiscount()));
        setDiscountType(purchaseDetail.getDiscountType());
        setProduct(purchaseDetail.getProduct());
        setSerial(purchaseDetail.getSerialNumber());
        setTotal(String.valueOf(purchaseDetail.getTotal()));
        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
    }

    public static void updateItem(int purchaseDetailID) {
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
        PurchaseDetailDao.updatePurchaseDetail(purchaseDetail, purchaseDetailID);
        getPurchaseDetails();
    }

    public static void removePurchaseDetail(int index) {
        purchaseDetailTempList.remove(index);
    }
}