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

package org.infinite.spoty.viewModels.old;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.purchases.PurchaseDetail;
import org.infinite.spoty.data_source.dtos.purchases.PurchaseMaster;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;
import static org.infinite.spoty.values.SharedResources.setTempId;

public class PurchaseDetailViewModel {
    public static final ObservableList<PurchaseDetail> purchaseDetailList =
            FXCollections.observableArrayList();
    private static final ListProperty<PurchaseDetail> purchaseDetails =
            new SimpleListProperty<>(purchaseDetailList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<PurchaseMaster> purchase = new SimpleObjectProperty<>(null);
    private static final StringProperty cost = new SimpleStringProperty("");
    private static final StringProperty netTax = new SimpleStringProperty("");
    private static final StringProperty taxType = new SimpleStringProperty("");
    private static final StringProperty discount = new SimpleStringProperty("");
    private static final StringProperty discountType = new SimpleStringProperty("");
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>(null);
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty price = new SimpleStringProperty("");
    private static final StringProperty totalPrice = new SimpleStringProperty("");
    private static final StringProperty quantity = new SimpleStringProperty("");

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        PurchaseDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
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

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        PurchaseDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static String getTotalPrice() {
        return totalPrice.get();
    }

    public static void setTotalPrice(String totalPrice) {
        PurchaseDetailViewModel.totalPrice.set(totalPrice);
    }

    public static StringProperty totalPriceProperty() {
        return totalPrice;
    }

    public static String getPrice() {
        return price.get();
    }

    public static void setPrice(String price) {
        PurchaseDetailViewModel.price.set(price);
    }

    public static StringProperty priceProperty() {
        return price;
    }

    public static StringProperty totalProperty() {
        return totalPrice;
    }

    public static ObservableList<PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails.get();
    }

    public static void setPurchaseDetails(ObservableList<PurchaseDetail> purchaseDetails) {
        PurchaseDetailViewModel.purchaseDetails.set(purchaseDetails);
    }

    public static ListProperty<PurchaseDetail> purchaseDetailsProperty() {
        return purchaseDetails;
    }

    public static void addPurchaseDetail() {
//        PurchaseDetail purchaseDetail =
//                new PurchaseDetail(
//                        getPurchase(),
//                        Double.parseDouble(getCost()),
//                        getProduct(),
//                        Long.parseLong(getQuantity()));
//
//        Platform.runLater(() -> purchaseDetailList.add(purchaseDetail));

        resetProperties();
    }

    public static void savePurchaseDetails() throws Exception {
//        Dao<PurchaseDetail, Long> purchaseDetailDao =
//                DaoManager.createDao(connectionSource, PurchaseDetail.class);
//
//        purchaseDetailDao.create(purchaseDetailList);

        purchaseDetailList.clear();
    }

    public static void resetProperties() {
        setId(0);
        setTempId(-1);
        setPurchase(null);
        setProduct(null);
        setCost("");
        setNetTax("");
        setTaxType(null);
        setDiscount("");
        setDiscountType(null);
        setSerial(null);
        setTotalPrice("");
        setQuantity("");
    }

    public static void getAllPurchaseDetails() throws Exception {
//        Dao<PurchaseDetail, Long> purchaseDetailDao =
//                DaoManager.createDao(connectionSource, PurchaseDetail.class);
//
//        purchaseDetailList.clear();
//        purchaseDetailList.addAll(purchaseDetailDao.queryForAll());
    }

    public static void updatePurchaseDetail(long index) throws Exception {
//        Dao<PurchaseDetail, Long> purchaseDetailDao =
//                DaoManager.createDao(connectionSource, PurchaseDetail.class);
//
//        PurchaseDetail purchaseDetail = purchaseDetailDao.queryForId(index);
//        purchaseDetail.setPurchase(getPurchase());
//        purchaseDetail.setCost(Double.parseDouble(getCost()));
//        purchaseDetail.setNetTax(Double.parseDouble(getNetTax()));
//        purchaseDetail.setTaxType(purchaseDetail.getTaxType());
//        purchaseDetail.setDiscount(Double.parseDouble(getDiscount()));
//        purchaseDetail.setDiscountType(purchaseDetail.getDiscountType());
//        purchaseDetail.setProduct(getProduct());
//        purchaseDetail.setSerialNumber(getSerial());
//        purchaseDetail.setTotalPrice(Double.parseDouble(getTotalPrice()));
//        purchaseDetail.setQuantity(Long.parseLong(getQuantity()));
//
//        Platform.runLater(
//                () -> {
//                    purchaseDetailList.remove(getTempId());
//                    purchaseDetailList.add(getTempId(), purchaseDetail);
//                });

        resetProperties();
    }

    public static void getItem(long index, int tempIndex) throws Exception {
//        Dao<PurchaseDetail, Long> purchaseDetailDao =
//                DaoManager.createDao(connectionSource, PurchaseDetail.class);
//
//        PurchaseDetail purchaseDetail = purchaseDetailDao.queryForId(index);
//
//        setTempId(tempIndex);
//        setId(purchaseDetail.getId());
//        setPurchase(purchaseDetail.getPurchase());
//        setCost(String.valueOf(purchaseDetail.getCost()));
//        setNetTax(String.valueOf(purchaseDetail.getNetTax()));
//        setTaxType(purchaseDetail.getTaxType());
//        setDiscount(String.valueOf(purchaseDetail.getDiscount()));
//        setDiscountType(purchaseDetail.getDiscountType());
//        setProduct(purchaseDetail.getProduct());
//        setSerial(purchaseDetail.getSerialNumber());
//        setTotalPrice(String.valueOf(purchaseDetail.getTotalPrice()));
//        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
    }

    public static void updateItem(long index) throws Exception {
//        Dao<PurchaseDetail, Long> purchaseDetailDao =
//                DaoManager.createDao(connectionSource, PurchaseDetail.class);
//
//        PurchaseDetail purchaseDetail = purchaseDetailDao.queryForId(index);
//
//        setId(purchaseDetail.getId());
//        setPurchase(purchaseDetail.getPurchase());
//        setCost(String.valueOf(purchaseDetail.getCost()));
//        setNetTax(String.valueOf(purchaseDetail.getNetTax()));
//        setTaxType(purchaseDetail.getTaxType());
//        setDiscount(String.valueOf(purchaseDetail.getDiscount()));
//        setDiscountType(purchaseDetail.getDiscountType());
//        setProduct(purchaseDetail.getProduct());
//        setSerial(purchaseDetail.getSerialNumber());
//        setTotalPrice(String.valueOf(purchaseDetail.getTotalPrice()));
//        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
//
//        purchaseDetailDao.update(purchaseDetail);

        getAllPurchaseDetails();
    }

    public static void updatePurchaseDetails() throws Exception {
//        Dao<PurchaseDetail, Long> purchaseDetailDao =
//                DaoManager.createDao(connectionSource, PurchaseDetail.class);
//
//        purchaseDetailList.forEach(
//                purchaseDetail -> {
//                    try {
//                        purchaseDetailDao.update(purchaseDetail);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, PurchaseDetailViewModel.class);
//                    }
//                });

        getAllPurchaseDetails();
    }

    public static void removePurchaseDetail(long index, int tempIndex) {
        Platform.runLater(() -> purchaseDetailList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deletePurchaseDetails(@NotNull LinkedList<Long> indexes) {
//        indexes.forEach(
//                index -> {
//                    try {
//                        SQLiteConnection connection = SQLiteConnection.getInstance();
//                        ConnectionSource connectionSource = connection.getConnection();
//
//                        Dao<PurchaseDetail, Long> purchaseDetailDao =
//                                DaoManager.createDao(connectionSource, PurchaseDetail.class);
//
//                        purchaseDetailDao.deleteById(index);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, PurchaseDetailViewModel.class);
//                    }
//                });
    }

    public static ObservableList<PurchaseDetail> getPurchaseDetailList() {
        return purchaseDetailList;
    }
}
