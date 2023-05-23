package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ProductDetailDao;
import org.infinite.spoty.database.models.*;

public class ProductDetailViewModel {
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final ObjectProperty<ProductMaster> purchase = new SimpleObjectProperty<>(null);
    private static final ListProperty<Branch> branches = new SimpleListProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> unit = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> purchaseUnit = new SimpleObjectProperty<>(null);
    private static final StringProperty name = new SimpleStringProperty(null);
    private static final IntegerProperty quantity = new SimpleIntegerProperty(0);
    private static final DoubleProperty cost = new SimpleDoubleProperty(0);
    private static final DoubleProperty price = new SimpleDoubleProperty(0);
    private static final DoubleProperty netTax = new SimpleDoubleProperty(0);
    private static final StringProperty taxType = new SimpleStringProperty(null);
    private static final IntegerProperty stockAlert = new SimpleIntegerProperty(0);
    private static final StringProperty serial = new SimpleStringProperty(null);
    public static final ObservableList<ProductDetail> purchaseDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<ProductDetail> purchaseTempList = FXCollections.observableArrayList();

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        ProductDetailViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static ProductMaster getProduct() {
        return purchase.get();
    }

    public static void setProduct(ProductMaster purchase) {
        ProductDetailViewModel.purchase.set(purchase);
    }

    public static ObjectProperty<ProductMaster> purchaseProperty() {
        return purchase;
    }

    public static ObservableList<Branch> getBranches() {
        return branches.get();
    }

    public static void setBranches(ObservableList<Branch> branches) {
        ProductDetailViewModel.branches.set(branches);
    }

    public static ListProperty<Branch> branchesProperty() {
        return branches;
    }

    public static UnitOfMeasure getUnit() {
        return unit.get();
    }

    public static void setUnit(UnitOfMeasure unit) {
        ProductDetailViewModel.unit.set(unit);
    }

    public static ObjectProperty<UnitOfMeasure> unitProperty() {
        return unit;
    }

    public static UnitOfMeasure getSaleUnit() {
        return saleUnit.get();
    }

    public static void setSaleUnit(UnitOfMeasure saleUnit) {
        ProductDetailViewModel.saleUnit.set(saleUnit);
    }

    public static ObjectProperty<UnitOfMeasure> saleUnitProperty() {
        return saleUnit;
    }

    public static UnitOfMeasure getProductUnit() {
        return purchaseUnit.get();
    }

    public static void setProductUnit(UnitOfMeasure purchaseUnit) {
        ProductDetailViewModel.purchaseUnit.set(purchaseUnit);
    }

    public static ObjectProperty<UnitOfMeasure> purchaseUnitProperty() {
        return purchaseUnit;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ProductDetailViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static int getQuantity() {
        return quantity.get();
    }

    public static void setQuantity(int quantity) {
        ProductDetailViewModel.quantity.set(quantity);
    }

    public static IntegerProperty quantityProperty() {
        return quantity;
    }

    public static double getCost() {
        return cost.get();
    }

    public static void setCost(double cost) {
        ProductDetailViewModel.cost.set(cost);
    }

    public static DoubleProperty costProperty() {
        return cost;
    }

    public static double getPrice() {
        return price.get();
    }

    public static void setPrice(double price) {
        ProductDetailViewModel.price.set(price);
    }

    public static DoubleProperty priceProperty() {
        return price;
    }

    public static double getNetTax() {
        return netTax.get();
    }

    public static void setNetTax(double netTax) {
        ProductDetailViewModel.netTax.set(netTax);
    }

    public static DoubleProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        ProductDetailViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static int getStockAlert() {
        return stockAlert.get();
    }

    public static void setStockAlert(int stockAlert) {
        ProductDetailViewModel.stockAlert.set(stockAlert);
    }

    public static IntegerProperty stockAlertProperty() {
        return stockAlert;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        ProductDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static void addProductDetail() {
        ProductDetail purchaseDetail = new ProductDetail(getProduct(), getBranches(), getUnit(),getSaleUnit(),
                getProductUnit(), getName(), getQuantity(), getCost(), getPrice(), getNetTax(), getTaxType(),
                getStockAlert(), getSerial());
        purchaseTempList.add(purchaseDetail);
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setBranches(null);
        setUnit(null);
        setSaleUnit(null);
        setProductUnit(null);
        setName(null);
        setQuantity(0);
        setCost(0);
        setPrice(0);
        setNetTax(0);
        setTaxType(null);
        setStockAlert(0);
        setSerial(null);
    }

    public static void resetTempList() {
        purchaseDetailsList.clear();
    }

    public static ObservableList<ProductDetail> getProductDetail() {
        purchaseDetailsList.clear();
        purchaseDetailsList.addAll(ProductDetailDao.fetchProductDetails());
        return purchaseDetailsList;
    }
}