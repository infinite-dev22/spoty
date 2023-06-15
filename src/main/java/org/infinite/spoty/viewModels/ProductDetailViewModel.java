/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ProductDetailDao;
import org.infinite.spoty.database.dao.ProductDetailDao;
import org.infinite.spoty.database.models.*;

public class ProductDetailViewModel {
    private static final StringProperty id = new SimpleStringProperty("");
    private static final ObjectProperty<ProductMaster> product = new SimpleObjectProperty<>(null);
    private static final ListProperty<Branch> branches = new SimpleListProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> unit = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<UnitOfMeasure> productUnit = new SimpleObjectProperty<>(null);
    private static final StringProperty name = new SimpleStringProperty(null);
    private static final IntegerProperty quantity = new SimpleIntegerProperty(0);
    private static final DoubleProperty cost = new SimpleDoubleProperty(0);
    private static final DoubleProperty price = new SimpleDoubleProperty(0);
    private static final DoubleProperty netTax = new SimpleDoubleProperty(0);
    private static final StringProperty taxType = new SimpleStringProperty(null);
    private static final IntegerProperty stockAlert = new SimpleIntegerProperty(0);
    private static final StringProperty serial = new SimpleStringProperty(null);
    public static final ObservableList<ProductDetail> productDetailsList = FXCollections.observableArrayList();
    public static final ObservableList<ProductDetail> productDetailTempList = FXCollections.observableArrayList();

    public static String getId() {
        return id.get();
    }

    public static void setId(String id) {
        ProductDetailViewModel.id.set(id);
    }

    public static StringProperty idProperty() {
        return id;
    }

    public static ProductMaster getProduct() {
        return product.get();
    }

    public static void setProduct(ProductMaster product) {
        ProductDetailViewModel.product.set(product);
    }

    public static ObjectProperty<ProductMaster> productProperty() {
        return product;
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
        return productUnit.get();
    }

    public static void setProductUnit(UnitOfMeasure productUnit) {
        ProductDetailViewModel.productUnit.set(productUnit);
    }

    public static ObjectProperty<UnitOfMeasure> productUnitProperty() {
        return productUnit;
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
        ProductDetail productDetail = new ProductDetail(getProduct(), getBranches(), getUnit(),getSaleUnit(),
                getProductUnit(), getName(), getQuantity(), getCost(), getPrice(), getNetTax(), getTaxType(),
                getStockAlert(), getSerial());
        productDetailTempList.add(productDetail);
    }

    public static void resetProperties() {
        setId("");
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
        productDetailsList.clear();
    }

    public static ObservableList<ProductDetail> getProductDetails() {
        productDetailsList.clear();
        productDetailsList.addAll(ProductDetailDao.fetchProductDetails());
        return productDetailsList;
    }

    public static void updateProductDetail(int index) {
        ProductDetail productDetail = new ProductDetail(getProduct(), getBranches(), getUnit(),getSaleUnit(),
                getProductUnit(), getName(), getQuantity(), getCost(), getPrice(), getNetTax(), getTaxType(),
                getStockAlert(), getSerial());
        productDetailTempList.remove(index);
        productDetailTempList.add(index, productDetail);
        resetProperties();
    }

    public static void getItem(int productDetailID) {
        ProductDetail productDetail = ProductDetailDao.findProductDetail(productDetailID);
        setId(String.valueOf(productDetail.getId()));
        setProduct(productDetail.getProduct());
        setBranches(FXCollections.observableArrayList(productDetail.getBranch()));
        setUnit(productDetail.getUnit());
        setSaleUnit(productDetail.getSaleUnit());
        setProductUnit(productDetail.getSaleUnit());
        setName(productDetail.getName());
        setQuantity(productDetail.getQuantity());
        setCost(productDetail.getCost());
        setPrice(productDetail.getPrice());
        setNetTax(productDetail.getNetTax());
        setTaxType(productDetail.getTaxType());
        setStockAlert(productDetail.getStockAlert());
        setSerial(productDetail.getSerialNumber());
        getProductDetails();
    }

    public static void getItem(ProductDetail productDetail, int index) {
        setId("index:" + index + ";");
        setProduct(productDetail.getProduct());
        setBranches(FXCollections.observableArrayList(productDetail.getBranch()));
        setUnit(productDetail.getUnit());
        setSaleUnit(productDetail.getSaleUnit());
        setProductUnit(productDetail.getSaleUnit());
        setName(productDetail.getName());
        setQuantity(productDetail.getQuantity());
        setCost(productDetail.getCost());
        setPrice(productDetail.getPrice());
        setNetTax(productDetail.getNetTax());
        setTaxType(productDetail.getTaxType());
        setStockAlert(productDetail.getStockAlert());
        setSerial(productDetail.getSerialNumber());
    }

    public static void updateItem(int productDetailID) {
        ProductDetail productDetail = new ProductDetail(getProduct(), getBranches(), getUnit(),getSaleUnit(),
                getProductUnit(), getName(), getQuantity(), getCost(), getPrice(), getNetTax(), getTaxType(),
                getStockAlert(), getSerial());
        ProductDetailDao.updateProductDetail(productDetail, productDetailID);
        getProductDetails();
    }

    public static void removeProductDetail(int index) {
        productDetailTempList.remove(index);
    }
}